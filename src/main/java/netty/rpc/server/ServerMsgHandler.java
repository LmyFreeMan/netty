package netty.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import netty.rpc.pojo.User;

@Log4j2
public class ServerMsgHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("读进来的信息为"+msg);
        User user= (User) msg;
        ctx.channel().writeAndFlush(user);
    }
}

