package com.arbonkeep.netty.demo2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author LBX
 * @Date 2021/5/10 20:48
 * @Description
 */
public class TestClientHandler extends SimpleChannelInboundHandler<String > {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "来自服务端的数据：" + msg);
        ctx.writeAndFlush("发给服务端的数据：" + System.currentTimeMillis());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
