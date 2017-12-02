​	WSGI就像一种语言（比如汉语）一样，是一种接口的规范，定义了web服务器和应用程序之间交互的规则/标准，使得使用Python写的Web应用程序可以和Web服务器对接起来。

​	WSGI相当于是Web服务器和Python应用程序之间的桥梁。主要有两个作用：

- 让Web服务器知道如何调用Python应用程序，并且把用户的请求告诉应用程序。
- 让Python应用程序知道用户的具体请求是什么，以及如何返回结果给Web服务器。

### WSGI中的角色

在WSGI（规范）中定义了两个角色，Web服务器端称为**server**或者**gateway**，应用程序端称为**application**或者**framework**（因为WSGI的应用程序端的规范一般都是由具体的框架来实现的）。我们下面统一使用server和application这两个术语。

server端会先收到用户的请求，然后会根据规范的要求调用application端，如下图所示：

![img](https://segmentfault.com/img/bVm2AW)

调用的结果会被封装成HTTP响应后再发送给客户端。

### Server如何调用application？

首先，每个application的入口只有一个，也就是所有的客户端请求都从一个入口进入到应用程序中

接下来server端需要知道去哪里找application的入口。这个需要在server端中指定一个Python模块（Python应用中的一个文件），并且这个模块中需要包含一个名称为application的可调用对象（类或者函数），这个application对象就是这个应用程序的唯一入口了。WSGI还定义了application对象的形式：

```python
def simple_app(environ, start_response):
      pass
```

可以看到，上面代码中的`environ`和`start_response`就是server端调用**application**对象时传递的两个参数。

举个栗子：

假设我们的应用程序的**入口文件**是`/var/www/index.py`，那么我们就需要在server端配置好这个路径（如何配置取决于server端的实现），然后在`index.py`中的代码如下所示：

使用标准库（这个只是demo）

```python
import wsgiref

application = wsgiref.simple_server.demo_app
```

使用web.py框架

```python
import web

urls = (
    '/.*', 'hello',
)

class hello(object):
    def GET(self):
        return "Hello, world."

application = web.application(urls, globals()).wsgifunc()
```

你可以看到，文件中都需要有一个**application**对象，server端会找到这个文件，然后调用这个对象。所以支持WSGI的Python框架最终都会有这么一个application对象，不过框架的使用者不需要关心这个application对象内部是如何工作的，只需要关心路由定义、请求处理等具体的业务逻辑。

因为application对象是唯一的入口，所以不管客户端请求的路径和数据是什么，server都是调用这个application对象，具体的客户端请求的处理有application对象完成。

### application对象需要做什么

上面已经提到了，application对象需要是一个可调用对象，而且其定义需要满足如下形式：

```
def simple_app(environ, start_response):
      pass
```

当server按照WSGI的规范调用了application之后，application就可以开始处理客户端的请求了，处理请求之后，application对象需要返回处理结果给server端。处理请求和返回结果这两个事情，都和server调用application对象时传递的两个参数有关。

#### environ参数

environ参数是一个Python的字典，里面存放了所有和客户端相关的信息，这样application对象就能知道客户端请求的资源是什么，请求中带了什么数据等。environ字典包含了一些CGI规范要求的数据，以及WSGI规范新增的数据，还可能包含一些操作系统的环境变量以及Web服务器相关的环境变量。我们来看一些environ中常用的成员：

首先是CGI规范中要求的变量：

- **REQUEST_METHOD**： 请求方法，是个字符串，'GET', 'POST'等
- **SCRIPT_NAME**： HTTP请求的path中的用于查找到application对象的部分，比如Web服务器可以根据path的一部分来决定请求由哪个virtual host处理
- **PATH_INFO**： HTTP请求的path中剩余的部分，也就是application要处理的部分
- **QUERY_STRING**： HTTP请求中的查询字符串，URL中**?**后面的内容
- **CONTENT_TYPE**： HTTP headers中的content-type内容
- **CONTENT_LENGTH**： HTTP headers中的content-length内容
- **SERVER_NAME**和**SERVER_PORT**： 服务器名和端口，这两个值和前面的SCRIPT_NAME, PATH_INFO拼起来可以得到完整的URL路径
- **SERVER_PROTOCOL**： HTTP协议版本，HTTP/1.0或者HTTP/1.1
- **HTTP_**： 和HTTP请求中的headers对应。

WSGI规范中还要求environ包含下列成员：

- **wsgi.version**：表示WSGI版本，一个元组(1, 0)，表示版本1.0
- **wsgi.url_scheme**：http或者https
- **wsgi.input**：一个类文件的输入流，application可以通过这个获取HTTP request body
- **wsgi.errors**：一个输出流，当应用程序出错时，可以将错误信息写入这里
- **wsgi.multithread**：当application对象可能被多个线程同时调用时，这个值需要为True
- **wsgi.multiprocess**：当application对象可能被多个进程同时调用时，这个值需要为True
- **wsgi.run_once**：当server期望application对象在进程的生命周期内只被调用一次时，该值为True

上面列出的这些内容已经包括了客户端请求的所有数据，足够application对象处理客户端请求了。

tips: CGI也是一种接口协议，只不过WSGI只适用于Python

#### start_resposne参数

start_response是一个可调用对象，接收两个必选参数和一个可选参数：

- **status**: 一个字符串，表示HTTP响应状态字符串
- **response_headers**: 一个列表，包含有如下形式的元组：(header_name, header_value)，用来表示HTTP响应的headers
- **exc_info**（可选）: 用于出错时，server需要返回给浏览器的信息

当application对象根据environ参数的内容执行完业务逻辑后，就需要返回结果给server端。我们知道HTTP的响应需要包含status，headers和body，所以在application对象将body作为返回值return之前，需要先调用`start_response()`，将status和headers的内容返回给server，这同时也是告诉server，application对象要开始返回body了。

#### application对象的返回值

application对象的返回值用于为HTTP响应提供body，如果没有body，那么可以返回None。如果有body的化，那么需要返回一个可迭代的对象。server端通过遍历这个可迭代对象可以获得body的全部内容。

#### application demo

PEP-3333中有一个application的实现demo，我把它再简化之后如下：

```
def simple_app(environ, start_response):
      status = '200 OK'
      response_headers = [('Content-type', 'text/plain')]
      start_response(status, response_headers)
      return ['hello, world']
```

可以将这段代码和前面的说明对照起来理解。

### WSGI中间件

**WSGI Middleware**（中间件）也是WSGI规范的一部分。上一章我们已经说明了WSGI的两个角色：server和application。那么middleware是一种运行在server和application中间的应用（一般都是Python应用）。middleware同时具备server和application角色，对于server来说，它是一个application；对于application来说，它是一个server。middleware并不修改server端和application端的规范，只是同时实现了这两个角色的功能而已。

我们可以通过下图来说明middleware是如何工作的：

![img](https://segmentfault.com/img/bVm2I5)

上图中最上面的三个彩色框表示角色，中间的白色框表示操作，操作的发生顺序按照1 ~ 5进行了排序，我们直接对着上图来说明middleware是如何工作的：

1. Server收到客户端的HTTP请求后，生成了`environ_s`，并且已经定义了`start_response_s`。
2. Server调用Middleware的application对象，传递的参数是`environ_s`和`start_response_s`。
3. Middleware会根据`environ`执行业务逻辑，生成`environ_m`，并且已经定义了`start_response_m`。
4. Middleware决定调用Application的application对象，传递参数是`environ_m`和`start_response_m`。Application的application对象处理完成后，会调用`start_response_m`并且返回结果给Middleware，存放在`result_m`中。
5. Middleware处理`result_m`，然后生成`result_s`，接着调用`start_response_s`，并返回结果`result_s`给Server端。Server端获取到result_s后就可以发送结果给客户端了。

从上面的流程可以看出middleware应用的几个特点：

1. Server认为middleware是一个application。
2. Application认为middleware是一个server。
3. Middleware可以有多层。

因为Middleware能过处理所有经过的request和response，所以要做什么都可以，没有限制。比如可以检查request是否有非法内容，检查response是否有非法内容，为request加上特定的HTTP header等，这些都是可以的。

### WSGI的实现和部署

要使用WSGI，需要分别实现server角色和application角色。

Application端的实现一般是由Python的各种框架来实现的，比如Django, web.py等，一般开发者不需要关心WSGI的实现，框架会会提供接口让开发者获取HTTP请求的内容以及发送HTTP响应。

Server端的实现会比较复杂一点，这个主要是因为软件架构的原因。一般常用的Web服务器，如Apache和nginx，都不会内置WSGI的支持，而是通过扩展来完成。比如Apache服务器，会通过扩展模块mod_wsgi来支持WSGI。Apache和mod_wsgi之间通过程序内部接口传递信息，mod_wsgi会实现WSGI的server端、进程管理以及对application的调用。Nginx上一般是用proxy的方式，用nginx的协议将请求封装好，发送给应用服务器，比如uWSGI，应用服务器会实现WSGI的服务端、进程管理以及对application的调用。







































































































