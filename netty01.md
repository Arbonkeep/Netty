# Netty

## Netty的执行流程与组件分析
<br>

### 简单的HelloWorld程序

1. 创建一个测试的TestServer

```java

/**
 * @Author LBX
 * @Date 2021/4/24 18:55
 * @Description
 */
public class TestServer  {
    public static void main(String[] args) {
        //创建事件循环组
        EventLoopGroup bossGroup = new NioEventLoopGroup();//接收连接，交给workerGroup4处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInicialzer());  
                    //TestServerInicialzer的作用是在Chanel被注册好之后，会自动创建，执行里面代码

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
```

2. 创建初始化器

```java

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

```

3. 创建一个正真处理请求的处理器

```java

/**
 * @Author LBX
 * @Date 2021/4/24 19:06
 * @Description
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端发送的请求
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
       if (msg instanceof  HttpRequest) {
            //  构造返回内容
            ByteBuf content = Unpooled.copiedBuffer("HelloWorld", CharsetUtil.UTF_8);
            // 创建响应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            // 设置头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());//通过读取文本字节来获取长度

            //响应客户端，使用writeAndFlush才是正真的返回给客户端，使用write  则会在缓冲区中
            ctx.writeAndFlush(response);
        }
    }

}

```