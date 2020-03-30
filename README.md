# ProxyServer
计网课程项目-代理服务器-带有web cache功能
--------------------------------------
学弟学妹们使用请点个星星

### 1	开发环境<br>
开发语言：JAVA<br>
开发工具：myeclipse2014<br>
浏览器：Chrome<br>

### 2	系统设计<br>
#### 2.1 系统类图<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/1.jpg)<br>
图1 主要类图<br>

#### 2.2 系统架构<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/2.png)<br>
图2 系统架构图<br>
#### 2.3 系统流程<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/3.png) <br>
图3 系统流程图<br>
#### 2.4	Web Cache设计<br>
 ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/4.png)<br>
1.缓存命中<br>
① 客户端请求某个Web数据，会先送至代理服务器中，代理服务器本身会监听80号端口接收用户请求<br>
② 当代理服务器收到用户请求之后，会将这个请求送达至代理进程中<br>
③ 进程创建一个子线程去处理这个请求，子线程拆除用户请求报文中的应用层首部，TCP首部，IP首部等，从而获取到请求报文中的URL<br>
④ 对URL进行hash计算，然后和代理服务器中hash表中的缓存键进行比对，若一致则缓存命中<br>
⑤ 寻找其中的Last-Modified:字段，发送If-Modified-Since：time请求首部给后端服务器，来询问是否数据未修改。如果服务器返回304 Not Modified，则在对应的值所指向的内存或硬盘空间上找到对应的内容数据返回响应给客户端，否则代理服务器会自行封装成请求报文，把自己当做http的客户端，向上游服务器发起请求，若内容存在，上游服务器会构建成响应报文，返回给代理服务器。当代理服务器收到响应之后，会检查该对象是否可以缓存，如若可以，会对URL进行hash之后生成一个键，存放到对应的hash表中，在相应的内存或磁盘空间上存储对应的内容数据，再返回响应给客户端<br>


2.缓存未命中<br>
① 客户端请求某个Web数据，会先送至代理服务器中，代理服务器本身会监听80号端口接收用户请求<br>
② 当代理服务器收到用户请求之后，会将这个请求送达至代理进程中<br>
③ 进程拆除用户请求报文中的应用层首部，TCP首部，IP首部等，从而获取到请求报文中的URL<br>
④ 对URL进行hash计算，然后和代理服务器中hash表中的缓存键进行比对，不一致则缓存未命中<br>
⑤ 代理服务器会自行封装成请求报文，把自己当做http的客户端，向上游服务器发起请求<br>
⑥ 若内容存在，上游服务器会构建成响应报文，返回给代理服务器。<br>
⑦ 当代理服务器收到响应之后，会检查该对象是否可以缓存，如若可以，会对URL进行hash之后生成一个键，存放到对应的hash表中<br>
⑧ 在相应的内存或磁盘空间上存储对应的内容数据<br>
⑨ 当操作完成之后，会将数据构建成相应报文，然后响应给客户端<br>



### 3 实现截图<br>
1.开启代理服务器<br>
 ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/5.png)<br>
2.访问网页<br>
 ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/6.png)<br>
3.查看缓存<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/7.png)<br> 
 ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/8.png)<br>
  ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/9.png)<br>
  ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/10.png)<br>
### 4 使用说明<br>
1.	打开Chrome浏览器设置，再点显示高级设置，点击更改代理服务器设置<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/11.png)<br>
2.	点击局域网设置，勾选代理服务器，输入本机IP：127.0.0.1，和代理服务器端口号：8888，点击确定<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/12.png)<br>
3.系统需要预先装有JAVA环境才可运行，双击文件夹内的start.bat，即可运行本程序<br>
 ![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/13.png)<br>
使用时需要指定缓存的存储目录，输入t可以指定默认目录，默认目录是同一文件夹下新建的tmp文件夹<br>
![](https://github.com/EccentricBox/ProxyServer/blob/master/PNG/14.png)<br>

### 爱心打赏
![](https://github.com/BoxFighter/ProxyServer/blob/master/money.png?raw=true)<br>
