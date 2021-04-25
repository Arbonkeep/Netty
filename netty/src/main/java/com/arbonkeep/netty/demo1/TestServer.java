package com.arbonkeep.netty.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author LBX
 * @Date 2021/4/24 17:55
 * @Description
 */
public class TestServer {
    public static void main(String[] args) throws Exception {
        //事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInicialzer());
            //绑定端口，进行同步
            ChannelFuture channelFuture = serverBootstrap.bind(1001).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            //关闭资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }



}
