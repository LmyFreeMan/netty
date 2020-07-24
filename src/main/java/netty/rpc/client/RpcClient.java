package netty.rpc.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.office.DiscardServer;
import netty.rpc.server.ServerMsgHandler;

/**
 * Discards any incoming data.
 */
public class RpcClient {
    private int port;
    public RpcClient(int port) {
        this.port = port;
    }
    public void run() throws Exception {
        //bossGroup是专门处理连接的(ServerSocketChannel)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // (1)
        try {
            //启动的引导类，可以让程序员配置信息
            Bootstrap b = new Bootstrap(); // (2)
            //设置线程组
            b.group(bossGroup)
                    //设置io模型，使用的是java的NIO模型
                    .channel(NioServerSocketChannel.class) // (3)
                    //配置channel-PPline当中的handler（编解码器）
                    .handler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("ObjectEncoder",new ObjectEncoder());
                            ch.pipeline().addLast("ObjectDecoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingResolver(null)));
                            ch.pipeline().addLast("clientHandler", (ChannelHandler) new ClientMsgHandler());
                        }
                    });
            // Bind and start to accept incoming connections.
            ChannelFuture f = b.connect("127.0.0.1",8080).sync(); // (7)
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }
}