
//客户端发送一次请求

handlerAdded: handler被添加到channel的pipeline
channelRegistered: channel注册到NioEventLoop
channelActive: channel准备就绪
channelRead: channel中有可读的数据
channelReadComplete: channel读数据完成
channelReadComplete: channel读数据完成



客户端发送多次请求
channelReadComplete: channel读数据完成
channelReadComplete: channel读数据完成

channelReadComplete: channel读数据完成
channelReadComplete: channel读数据完成

channelReadComplete: channel读数据完成
channelReadComplete: channel读数据完成