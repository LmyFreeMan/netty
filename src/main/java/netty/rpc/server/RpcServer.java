package netty.rpc.server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.office.DiscardServer;

/**
 * Discards any incoming data.
 */
public class RpcServer {
    private int port;
    public RpcServer(int port) {
        this.port = port;
    }
    public void run() throws Exception {
        //bossGroup是专门处理连接的(ServerSocketChannel)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // (1)
        //workerGroup是专门处理io事件的，一般设置为256-512(SocketChannel)
        EventLoopGroup workerGroup = new NioEventLoopGroup(253);
        try {
            //启动的引导类，可以让程序员配置信息
            ServerBootstrap b = new ServerBootstrap(); // (2)
            //设置线程组
            b.group(bossGroup, workerGroup)
                    //设置io模型，使用的是java的NIO模型
                    .channel(NioServerSocketChannel.class) // (3)
                    //配置channel-PPline当中的handler（编解码器）
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast("ObjectEncoder",new ObjectEncoder());
                                ch.pipeline().addLast("ObjectDecoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingResolver(null)));
                                ch.pipeline().addLast("handler",new ServerMsgHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }
}