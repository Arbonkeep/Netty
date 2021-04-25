package com.arbonkeep.netty.demo1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author LBX
 * @Date 2021/4/24 18:57
 * @Description
 */
public class TestServerInicialzer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        //将处理器添加到管道
        pipeline.addLast("testServerHandler", new TestHttpServerHandler());
    }
}
