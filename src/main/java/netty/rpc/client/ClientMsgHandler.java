package netty.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;
import netty.rpc.pojo.User;

import java.util.Date;

@Log4j2
public class ClientMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User user=new User();
        user.setAge(18);
        user.setBirthday(new Date());
        user.setName("Jack");
        ctx.channel().writeAndFlush(user);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       log.debug(msg.toString());
    }
}
