# 命令行工具(Command line tools)

Scrapy是通过 `scrapy` 命令行工具进行控制的。 这里我们称之为 “Scrapy tool” 以用来和子命令进行区分。 对于子命令，我们称为 “command” 或者 “Scrapy commands”。

Scrapy tool 针对不同的目的提供了多个命令，每个命令支持不同的参数和选项。

## 默认的Scrapy项目结构

在开始对命令行工具以及子命令的探索前，让我们首先了解一下Scrapy的项目的目录结构。

虽然可以被修改，但所有的Scrapy项目默认有类似于下边的文件结构:

```
scrapy.cfg
myproject/
    __init__.py
    items.py
    pipelines.py
    settings.py
    spiders/
        __init__.py
        spider1.py
        spider2.py
        ...
```

`scrapy.cfg` 存放的目录被认为是 *项目的根目录* 。该文件中包含python模块名的字段定义了项目的设置。例如:

```
[settings]
default = myproject.settings

```

## 使用 `scrapy` 工具

您可以以无参数的方式启动Scrapy工具。该命令将会给出一些使用帮助以及可用的命令:

```
Scrapy X.Y - no active project

Usage:
  scrapy <command> [options] [args]

Available commands:
  crawl         Run a spider
  fetch         Fetch a URL using the Scrapy downloader
[...]

```

如果您在Scrapy项目中运行，当前激活的项目将会显示在输出的第一行。上面的输出就是响应的例子。如果您在一个项目中运行命令将会得到类似的输出:

```
Scrapy X.Y - project: myproject

Usage:
  scrapy <command> [options] [args]

[...]

```

### 创建项目

一般来说，使用 `scrapy` 工具的第一件事就是创建您的Scrapy项目:

```
scrapy startproject myproject

```

该命令将会在 `myproject` 目录中创建一个Scrapy项目。

接下来，进入到项目目录中:

```
cd myproject

```

这时候您就可以使用 `scrapy` 命令来管理和控制您的项目了。

### 控制项目

您可以在您的项目中使用 `scrapy` 工具来对其进行控制和管理。

比如，创建一个新的spider:

```
scrapy genspider mydomain mydomain.com

```

有些Scrapy命令(比如 [`crawl`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-crawl))要求必须在Scrapy项目中运行。 您可以通过下边的 [commands reference](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#topics-commands-ref) 来了解哪些命令需要在项目中运行，哪些不用。

另外要注意，有些命令在项目里运行时的效果有些许区别。 以fetch命令为例，如果被爬取的url与某个特定spider相关联， 则该命令将会使用spider的动作(spider-overridden behaviours)。 (比如spider指定的 `user_agent`)。 该表现是有意而为之的。一般来说， `fetch` 命令就是用来测试检查spider是如何下载页面。

## 可用的工具命令(tool commands)

该章节提供了可用的内置命令的列表。每个命令都提供了描述以及一些使用例子。您总是可以通过运行命令来获取关于每个命令的详细内容:

```
scrapy <command> -h

```

您也可以查看所有可用的命令:

```
scrapy -h

```

Scrapy提供了两种类型的命令。一种必须在Scrapy项目中运行(针对项目(Project-specific)的命令)，另外一种则不需要(全局命令)。全局命令在项目中运行时的表现可能会与在非项目中运行有些许差别(因为可能会使用项目的设定)。

全局命令:

- [`startproject`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-startproject)
- [`settings`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-settings)
- [`runspider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-runspider)
- [`shell`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-shell)
- [`fetch`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-fetch)
- [`view`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-view)
- [`version`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-version)

项目(Project-only)命令:

- [`crawl`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-crawl)
- [`check`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-check)
- [`list`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-list)
- [`edit`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-edit)
- [`parse`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-parse)
- [`genspider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-genspider)
- [`deploy`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-deploy)
- [`bench`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-bench)

### startproject

- 语法: `scrapy startproject <project_name>`
- 是否需要项目: *no*

在 `project_name` 文件夹下创建一个名为 `project_name` 的Scrapy项目。

例子:

```
$ scrapy startproject myproject

```

### genspider

- 语法: `scrapy genspider [-t template] <name> <domain>`
- 是否需要项目: *yes*

在当前项目中创建spider。

这仅仅是创建spider的一种快捷方法。该方法可以使用提前定义好的模板来生成spider。您也可以自己创建spider的源码文件。

例子:

```
$ scrapy genspider -l
Available templates:
  basic
  crawl
  csvfeed
  xmlfeed

$ scrapy genspider -d basic
import scrapy

class $classname(scrapy.Spider):
    name = "$name"
    allowed_domains = ["$domain"]
    start_urls = (
        'http://www.$domain/',
        )

    def parse(self, response):
        pass

$ scrapy genspider -t basic example example.com
Created spider 'example' using template 'basic' in module:
  mybot.spiders.example

```

### crawl

- 语法: `scrapy crawl <spider>`
- 是否需要项目: *yes*

使用spider进行爬取。

例子:

```
$ scrapy crawl myspider
[ ... myspider starts crawling ... ]

```

### check

- 语法: `scrapy check [-l] <spider>`
- 是否需要项目: *yes*

运行contract检查。

例子:

```
$ scrapy check -l
first_spider
  * parse
  * parse_item
second_spider
  * parse
  * parse_item

$ scrapy check
[FAILED] first_spider:parse_item
>>> 'RetailPricex' field is missing

[FAILED] first_spider:parse
>>> Returned 92 requests, expected 0..4

```

### list

- 语法: `scrapy list`
- 是否需要项目: *yes*

列出当前项目中所有可用的spider。每行输出一个spider。

使用例子:

```
$ scrapy list
spider1
spider2

```

### edit

- 语法: `scrapy edit <spider>`
- 是否需要项目: *yes*

使用 [`EDITOR`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/settings.html#std:setting-EDITOR) 中设定的编辑器编辑给定的spider

该命令仅仅是提供一个快捷方式。开发者可以自由选择其他工具或者IDE来编写调试spider。

例子:

```
$ scrapy edit spider1

```

### fetch

- 语法: `scrapy fetch <url>`
- 是否需要项目: *no*

使用Scrapy下载器(downloader)下载给定的URL，并将获取到的内容送到标准输出。

该命令以spider下载页面的方式获取页面。例如，如果spider有 `USER_AGENT` 属性修改了 User Agent，该命令将会使用该属性。

因此，您可以使用该命令来查看spider如何获取某个特定页面。

该命令如果非项目中运行则会使用默认Scrapy downloader设定。

例子:

```
$ scrapy fetch --nolog http://www.example.com/some/page.html
[ ... html content here ... ]

$ scrapy fetch --nolog --headers http://www.example.com/
{'Accept-Ranges': ['bytes'],
 'Age': ['1263   '],
 'Connection': ['close     '],
 'Content-Length': ['596'],
 'Content-Type': ['text/html; charset=UTF-8'],
 'Date': ['Wed, 18 Aug 2010 23:59:46 GMT'],
 'Etag': ['"573c1-254-48c9c87349680"'],
 'Last-Modified': ['Fri, 30 Jul 2010 15:30:18 GMT'],
 'Server': ['Apache/2.2.3 (CentOS)']}

```

### view

- 语法: `scrapy view <url>`
- 是否需要项目: *no*

在浏览器中打开给定的URL，并以Scrapy spider获取到的形式展现。 有些时候spider获取到的页面和普通用户看到的并不相同。 因此该命令可以用来检查spider所获取到的页面，并确认这是您所期望的。

例子:

```
$ scrapy view http://www.example.com/some/page.html
[ ... browser starts ... ]

```

### shell

- 语法: `scrapy shell [url]`
- 是否需要项目: *no*

以给定的URL(如果给出)或者空(没有给出URL)启动Scrapy shell。 查看 [Scrapy终端(Scrapy shell)](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/shell.html#topics-shell) 获取更多信息。

例子:

```
$ scrapy shell http://www.example.com/some/page.html
[ ... scrapy shell starts ... ]

```

### parse

- 语法: `scrapy parse <url> [options]`
- 是否需要项目: *yes*

获取给定的URL并使用相应的spider分析处理。如果您提供 `--callback` 选项，则使用spider的该方法处理，否则使用 `parse` 。

支持的选项:

- `--spider=SPIDER`: 跳过自动检测spider并强制使用特定的spider
- `--a NAME=VALUE`: 设置spider的参数(可能被重复)
- `--callback` or `-c`: spider中用于解析返回(response)的回调函数
- `--pipelines`: 在pipeline中处理item
- `--rules` or `-r`: 使用 [`CrawlSpider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.CrawlSpider) 规则来发现用来解析返回(response)的回调函数
- `--noitems`: 不显示爬取到的item
- `--nolinks`: 不显示提取到的链接
- `--nocolour`: 避免使用pygments对输出着色
- `--depth` or `-d`: 指定跟进链接请求的层次数(默认: 1)
- `--verbose` or `-v`: 显示每个请求的详细信息

例子:

```
$ scrapy parse http://www.example.com/ -c parse_item
[ ... scrapy log lines crawling example.com spider ... ]

>>> STATUS DEPTH LEVEL 1 <<<
# Scraped Items  ------------------------------------------------------------
[{'name': u'Example item',
 'category': u'Furniture',
 'length': u'12 cm'}]

# Requests  -----------------------------------------------------------------
[]

```

### settings

- 语法: `scrapy settings [options]`
- 是否需要项目: *no*

获取Scrapy的设定

在项目中运行时，该命令将会输出项目的设定值，否则输出Scrapy默认设定。

例子:

```
$ scrapy settings --get BOT_NAME
scrapybot
$ scrapy settings --get DOWNLOAD_DELAY
0

```

### runspider

- 语法: `scrapy runspider <spider_file.py>`
- 是否需要项目: *no*

在未创建项目的情况下，运行一个编写在Python文件中的spider。

例子:

```
$ scrapy runspider myspider.py
[ ... spider starts crawling ... ]

```

### version

- 语法: `scrapy version [-v]`
- 是否需要项目: *no*

输出Scrapy版本。配合 `-v` 运行时，该命令同时输出Python, Twisted以及平台的信息，方便bug提交。

### deploy

0.11 新版功能.

- 语法: `scrapy deploy [ <target:project> | -l <target> | -L ]`
- 是否需要项目: *yes*

将项目部署到Scrapyd服务。查看 [部署您的项目](http://scrapyd.readthedocs.org/en/latest/deploy.html) 。

### bench

0.17 新版功能.

- 语法: `scrapy bench`
- 是否需要项目: *no*

运行benchmark测试。 [Benchmarking](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/benchmarking.html#benchmarking) 。

## 自定义项目命令

您也可以通过 [`COMMANDS_MODULE`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:setting-COMMANDS_MODULE) 来添加您自己的项目命令。您可以以 [scrapy/commands](https://github.com/scrapy/scrapy/blob/master/scrapy/commands) 中Scrapy commands为例来了解如何实现您的命令。

### COMMANDS_MODULE

Default: `''` (empty string)

用于查找添加自定义Scrapy命令的模块。

例子:

```
COMMANDS_MODULE = 'mybot.commands'
```



# Items

爬取的主要目标就是从非结构性的数据源提取结构性数据，例如网页。 Scrapy提供 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 类来满足这样的需求。

[`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 对象是种简单的容器，保存了爬取到得数据。 其提供了 [类似于词典(dictionary-like)](http://docs.python.org/library/stdtypes.html#dict) 的API以及用于声明可用字段的简单语法。

## 声明Item

Item使用简单的class定义语法以及 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象来声明。例如:

```
import scrapy

class Product(scrapy.Item):
    name = scrapy.Field()
    price = scrapy.Field()
    stock = scrapy.Field()
    last_updated = scrapy.Field(serializer=str)

```

注解

熟悉 [Django](http://www.djangoproject.com/) 的朋友一定会注意到Scrapy Item定义方式与 [Django Models](http://docs.djangoproject.com/en/dev/topics/db/models/) 很类似, 不过没有那么多不同的字段类型(Field type)，更为简单。

## Item字段(Item Fields)

[`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象指明了每个字段的元数据(metadata)。例如下面例子中 `last_updated` 中指明了该字段的序列化函数。

您可以为每个字段指明任何类型的元数据。 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象对接受的值没有任何限制。也正是因为这个原因，文档也无法提供所有可用的元数据的键(key)参考列表。 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象中保存的每个键可以由多个组件使用，并且只有这些组件知道这个键的存在。您可以根据自己的需求，定义使用其他的 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 键。 设置 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象的主要目的就是在一个地方定义好所有的元数据。 一般来说，那些依赖某个字段的组件肯定使用了特定的键(key)。您必须查看组件相关的文档，查看其用了哪些元数据键(metadata key)。

需要注意的是，用来声明item的 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象并没有被赋值为class的属性。 不过您可以通过 [`Item.fields`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item.fields) 属性进行访问。

以上就是所有您需要知道的如何声明item的内容了。

## 与Item配合

接下来以 [下边声明](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#topics-items-declaring) 的 `Product` item来演示一些item的操作。您会发现API和 [dict API](http://docs.python.org/library/stdtypes.html#dict) 非常相似。

### 创建item

```
>>> product = Product(name='Desktop PC', price=1000)
>>> print product
Product(name='Desktop PC', price=1000)

```

### 获取字段的值

```
>>> product['name']
Desktop PC
>>> product.get('name')
Desktop PC

>>> product['price']
1000

>>> product['last_updated']
Traceback (most recent call last):
    ...
KeyError: 'last_updated'

>>> product.get('last_updated', 'not set')
not set

>>> product['lala'] # getting unknown field
Traceback (most recent call last):
    ...
KeyError: 'lala'

>>> product.get('lala', 'unknown field')
'unknown field'

>>> 'name' in product  # is name field populated?
True

>>> 'last_updated' in product  # is last_updated populated?
False

>>> 'last_updated' in product.fields  # is last_updated a declared field?
True

>>> 'lala' in product.fields  # is lala a declared field?
False

```

### 设置字段的值

```
>>> product['last_updated'] = 'today'
>>> product['last_updated']
today

>>> product['lala'] = 'test' # setting unknown field
Traceback (most recent call last):
    ...
KeyError: 'Product does not support field: lala'

```

### 获取所有获取到的值

您可以使用 [dict API](http://docs.python.org/library/stdtypes.html#dict) 来获取所有的值:

```
>>> product.keys()
['price', 'name']

>>> product.items()
[('price', 1000), ('name', 'Desktop PC')]

```

### 其他任务

复制item:

```
>>> product2 = Product(product)
>>> print product2
Product(name='Desktop PC', price=1000)

>>> product3 = product2.copy()
>>> print product3
Product(name='Desktop PC', price=1000)

```

根据item创建字典(dict):

```
>>> dict(product) # create a dict from all populated values
{'price': 1000, 'name': 'Desktop PC'}

```

根据字典(dict)创建item:

```
>>> Product({'name': 'Laptop PC', 'price': 1500})
Product(price=1500, name='Laptop PC')

>>> Product({'name': 'Laptop PC', 'lala': 1500}) # warning: unknown field in dict
Traceback (most recent call last):
    ...
KeyError: 'Product does not support field: lala'

```

## 扩展Item

您可以通过继承原始的Item来扩展item(添加更多的字段或者修改某些字段的元数据)。

例如:

```
class DiscountedProduct(Product):
    discount_percent = scrapy.Field(serializer=str)
    discount_expiration_date = scrapy.Field()

```

您也可以通过使用原字段的元数据,添加新的值或修改原来的值来扩展字段的元数据:

```
class SpecificProduct(Product):
    name = scrapy.Field(Product.fields['name'], serializer=my_serializer)

```

这段代码在保留所有原来的元数据值的情况下添加(或者覆盖)了 `name` 字段的 `serializer` 。

## Item对象

- *class*`scrapy.item.``Item`([*arg*])

  返回一个根据给定的参数可选初始化的item。Item复制了标准的 [dict API](http://docs.python.org/library/stdtypes.html#dict) 。包括初始化函数也相同。Item唯一额外添加的属性是:`fields`一个包含了item所有声明的字段的字典，而不仅仅是获取到的字段。该字典的key是字段(field)的名字，值是 [Item声明](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#topics-items-declaring) 中使用到的 [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 对象。

## 字段(Field)对象

- *class*`scrapy.item.``Field`([*arg*])

  [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field) 仅仅是内置的 [dict](http://docs.python.org/library/stdtypes.html#dict) 类的一个别名，并没有提供额外的方法或者属性。换句话说， [`Field`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Field)对象完完全全就是Python字典(dict)。被用来基于类属性(class attribute)的方法来支持 [item声明语法](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#topics-items-declaring) 。



# Spiders

Spider类定义了如何爬取某个(或某些)网站。包括了爬取的动作(例如:是否跟进链接)以及如何从网页的内容中提取结构化数据(爬取item)。 换句话说，Spider就是您定义爬取的动作及分析某个网页(或者是有些网页)的地方。

对spider来说，爬取的循环类似下文:

1. 以初始的URL初始化Request，并设置回调函数。 当该request下载完毕并返回时，将生成response，并作为参数传给该回调函数。

   spider中初始的request是通过调用 [`start_requests()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.start_requests) 来获取的。 [`start_requests()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.start_requests) 读取 [`start_urls`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.start_urls) 中的URL， 并以 [`parse`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.parse) 为回调函数生成 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 。

2. 在回调函数内分析返回的(网页)内容，返回 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 对象或者 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 或者一个包括二者的可迭代容器。 返回的Request对象之后会经过Scrapy处理，下载相应的内容，并调用设置的callback函数(函数可相同)。

3. 在回调函数内，您可以使用 [选择器(Selectors)](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/selectors.html#topics-selectors) (您也可以使用BeautifulSoup, lxml 或者您想用的任何解析器) 来分析网页内容，并根据分析的数据生成item。

4. 最后，由spider返回的item将被存到数据库(由某些 [Item Pipeline](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/item-pipeline.html#topics-item-pipeline) 处理)或使用 [Feed exports](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/feed-exports.html#topics-feed-exports) 存入到文件中。

虽然该循环对任何类型的spider都(多少)适用，但Scrapy仍然为了不同的需求提供了多种默认spider。 之后将讨论这些spider。

## Spider参数

Spider可以通过接受参数来修改其功能。 spider参数一般用来定义初始URL或者指定限制爬取网站的部分。 您也可以使用其来配置spider的任何功能。

在运行 [`crawl`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/commands.html#std:command-crawl) 时添加 `-a` 可以传递Spider参数:

```
scrapy crawl myspider -a category=electronics

```

Spider在构造器(constructor)中获取参数:

```
import scrapy

class MySpider(Spider):
    name = 'myspider'

    def __init__(self, category=None, *args, **kwargs):
        super(MySpider, self).__init__(*args, **kwargs)
        self.start_urls = ['http://www.example.com/categories/%s' % category]
        # ...

```

Spider参数也可以通过Scrapyd的 `schedule.json` API来传递。 参见 [Scrapyd documentation](http://scrapyd.readthedocs.org/).

## 内置Spider参考手册

Scrapy提供多种方便的通用spider供您继承使用。 这些spider为一些常用的爬取情况提供方便的特性， 例如根据某些规则跟进某个网站的所有链接、根据 [Sitemaps](http://www.sitemaps.org/) 来进行爬取，或者分析XML/CSV源。

下面spider的示例中，我们假定您有个项目在 `myproject.items` 模块中声明了 `TestItem`:

```
import scrapy

class TestItem(scrapy.Item):
    id = scrapy.Field()
    name = scrapy.Field()
    description = scrapy.Field()

```

### Spider

- *class*`scrapy.spider.``Spider`

  Spider是最简单的spider。每个其他的spider必须继承自该类(包括Scrapy自带的其他spider以及您自己编写的spider)。 Spider并没有提供什么特殊的功能。 其仅仅请求给定的 `start_urls`/`start_requests` ，并根据返回的结果(resulting responses)调用spider的 `parse` 方法。`name`定义spider名字的字符串(string)。spider的名字定义了Scrapy如何定位(并初始化)spider，所以其必须是唯一的。 不过您可以生成多个相同的spider实例(instance)，这没有任何限制。 name是spider最重要的属性，而且是必须的。如果该spider爬取单个网站(single domain)，一个常见的做法是以该网站(domain)(加或不加 [后缀](http://en.wikipedia.org/wiki/Top-level_domain) )来命名spider。 例如，如果spider爬取 `mywebsite.com` ，该spider通常会被命名为 `mywebsite` 。`allowed_domains`可选。包含了spider允许爬取的域名(domain)列表(list)。 当 [`OffsiteMiddleware`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spider-middleware.html#scrapy.contrib.spidermiddleware.offsite.OffsiteMiddleware) 启用时， 域名不在列表中的URL不会被跟进。`start_urls`URL列表。当没有制定特定的URL时，spider将从该列表中开始进行爬取。 因此，第一个被获取到的页面的URL将是该列表之一。 后续的URL将会从获取到的数据中提取。`start_requests`()该方法必须返回一个可迭代对象(iterable)。该对象包含了spider用于爬取的第一个Request。当spider启动爬取并且未制定URL时，该方法被调用。 当指定了URL时，[`make_requests_from_url()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.make_requests_from_url) 将被调用来创建Request对象。 该方法仅仅会被Scrapy调用一次，因此您可以将其实现为生成器。该方法的默认实现是使用 [`start_urls`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.start_urls) 的url生成Request。如果您想要修改最初爬取某个网站的Request对象，您可以重写(override)该方法。 例如，如果您需要在启动时以POST登录某个网站，你可以这么写:`def start_requests(self):    return [scrapy.FormRequest("http://www.example.com/login",                               formdata={'user': 'john', 'pass': 'secret'},                               callback=self.logged_in)]def logged_in(self, response):    # here you would extract links to follow and return Requests for    # each of them, with another callback    pass``make_requests_from_url`(*url*)该方法接受一个URL并返回用于爬取的 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 对象。 该方法在初始化request时被 [`start_requests()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.start_requests) 调用，也被用于转化url为request。默认未被复写(overridden)的情况下，该方法返回的Request对象中， [`parse()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.parse) 作为回调函数，dont_filter参数也被设置为开启。 (详情参见 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request)).`parse`(*response*)当response没有指定回调函数时，该方法是Scrapy处理下载的response的默认方法。`parse` 负责处理response并返回处理的数据以及(/或)跟进的URL。 [`Spider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider) 对其他的Request的回调函数也有相同的要求。该方法及其他的Request回调函数必须返回一个包含 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 及(或) [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 的可迭代的对象。参数:**response** ([`Response`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Response)) – 用于分析的response`log`(*message*[, *level*, *component*])使用 [`scrapy.log.msg()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/logging.html#scrapy.log.msg) 方法记录(log)message。 log中自动带上该spider的 [`name`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.spider.Spider.name) 属性。 更多数据请参见 [Logging](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/logging.html#topics-logging) 。`closed`(*reason*)当spider关闭时，该函数被调用。 该方法提供了一个替代调用signals.connect()来监听 [`spider_closed`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/signals.html#std:signal-spider_closed) 信号的快捷方式。

#### Spider样例

让我们来看一个例子:

```
import scrapy

class MySpider(scrapy.Spider):
    name = 'example.com'
    allowed_domains = ['example.com']
    start_urls = [
        'http://www.example.com/1.html',
        'http://www.example.com/2.html',
        'http://www.example.com/3.html',
    ]

    def parse(self, response):
        self.log('A response from %s just arrived!' % response.url)

```

另一个在单个回调函数中返回多个Request以及Item的例子:

```
import scrapy
from myproject.items import MyItem

class MySpider(scrapy.Spider):
    name = 'example.com'
    allowed_domains = ['example.com']
    start_urls = [
        'http://www.example.com/1.html',
        'http://www.example.com/2.html',
        'http://www.example.com/3.html',
    ]

    def parse(self, response):
        sel = scrapy.Selector(response)
        for h3 in response.xpath('//h3').extract():
            yield MyItem(title=h3)

        for url in response.xpath('//a/@href').extract():
            yield scrapy.Request(url, callback=self.parse)

```

### CrawlSpider

- *class*`scrapy.contrib.spiders.``CrawlSpider`

  爬取一般网站常用的spider。其定义了一些规则(rule)来提供跟进link的方便的机制。 也许该spider并不是完全适合您的特定网站或项目，但其对很多情况都使用。 因此您可以以其为起点，根据需求修改部分方法。当然您也可以实现自己的spider。除了从Spider继承过来的(您必须提供的)属性外，其提供了一个新的属性:`rules`一个包含一个(或多个) [`Rule`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.Rule) 对象的集合(list)。 每个 [`Rule`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.Rule) 对爬取网站的动作定义了特定表现。 Rule对象在下边会介绍。 如果多个rule匹配了相同的链接，则根据他们在本属性中被定义的顺序，第一个会被使用。该spider也提供了一个可复写(overrideable)的方法:`parse_start_url`(*response*)当start_url的请求返回时，该方法被调用。 该方法分析最初的返回值并必须返回一个 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item)对象或者 一个 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 对象或者 一个可迭代的包含二者对象。

#### 爬取规则(Crawling rules)

- *class*`scrapy.contrib.spiders.``Rule`(*link_extractor*, *callback=None*, *cb_kwargs=None*, *follow=None*, *process_links=None*, *process_request=None*)

  `link_extractor` 是一个 [Link Extractor](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/link-extractors.html#topics-link-extractors) 对象。 其定义了如何从爬取到的页面提取链接。`callback` 是一个callable或string(该spider中同名的函数将会被调用)。 从link_extractor中每获取到链接时将会调用该函数。该回调函数接受一个response作为其第一个参数， 并返回一个包含 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 以及(或) [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 对象(或者这两者的子类)的列表(list)。警告当编写爬虫规则时，请避免使用 `parse` 作为回调函数。 由于 [`CrawlSpider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.CrawlSpider) 使用 `parse` 方法来实现其逻辑，如果 您覆盖了 `parse` 方法，crawl spider 将会运行失败。`cb_kwargs` 包含传递给回调函数的参数(keyword argument)的字典。`follow` 是一个布尔(boolean)值，指定了根据该规则从response提取的链接是否需要跟进。 如果 `callback` 为None， `follow` 默认设置为 `True` ，否则默认为 `False` 。`process_links` 是一个callable或string(该spider中同名的函数将会被调用)。 从link_extractor中获取到链接列表时将会调用该函数。该方法主要用来过滤。`process_request` 是一个callable或string(该spider中同名的函数将会被调用)。 该规则提取到每个request时都会调用该函数。该函数必须返回一个request或者None。 (用来过滤request)

#### CrawlSpider样例

接下来给出配合rule使用CrawlSpider的例子:

```
import scrapy
from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors import LinkExtractor

class MySpider(CrawlSpider):
    name = 'example.com'
    allowed_domains = ['example.com']
    start_urls = ['http://www.example.com']

    rules = (
        # 提取匹配 'category.php' (但不匹配 'subsection.php') 的链接并跟进链接(没有callback意味着follow默认为True)
        Rule(LinkExtractor(allow=('category\.php', ), deny=('subsection\.php', ))),

        # 提取匹配 'item.php' 的链接并使用spider的parse_item方法进行分析
        Rule(LinkExtractor(allow=('item\.php', )), callback='parse_item'),
    )

    def parse_item(self, response):
        self.log('Hi, this is an item page! %s' % response.url)

        item = scrapy.Item()
        item['id'] = response.xpath('//td[@id="item_id"]/text()').re(r'ID: (\d+)')
        item['name'] = response.xpath('//td[@id="item_name"]/text()').extract()
        item['description'] = response.xpath('//td[@id="item_description"]/text()').extract()
        return item

```

该spider将从example.com的首页开始爬取，获取category以及item的链接并对后者使用 `parse_item`方法。 当item获得返回(response)时，将使用XPath处理HTML并生成一些数据填入 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 中。

### XMLFeedSpider

- *class*`scrapy.contrib.spiders.``XMLFeedSpider`

  XMLFeedSpider被设计用于通过迭代各个节点来分析XML源(XML feed)。 迭代器可以从 `iternodes` ， `xml` ， `html` 选择。 鉴于 `xml` 以及 `html` 迭代器需要先读取所有DOM再分析而引起的性能问题， 一般还是推荐使用 `iternodes` 。 不过使用 `html` 作为迭代器能有效应对错误的XML。您必须定义下列类属性来设置迭代器以及标签名(tag name):`iterator`用于确定使用哪个迭代器的string。可选项有:`'iternodes'` - 一个高性能的基于正则表达式的迭代器`'html'` - 使用 [`Selector`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/selectors.html#scrapy.selector.Selector) 的迭代器。 需要注意的是该迭代器使用DOM进行分析，其需要将所有的DOM载入内存， 当数据量大的时候会产生问题。`'xml'` - 使用 [`Selector`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/selectors.html#scrapy.selector.Selector) 的迭代器。 需要注意的是该迭代器使用DOM进行分析，其需要将所有的DOM载入内存， 当数据量大的时候会产生问题。默认值为 `iternodes` 。`itertag`一个包含开始迭代的节点名的string。例如:`itertag = 'product'``namespaces`一个由 `(prefix, url)` 元组(tuple)所组成的list。 其定义了在该文档中会被spider处理的可用的namespace。 `prefix` 及 `uri` 会被自动调用 [`register_namespace()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/selectors.html#scrapy.selector.Selector.register_namespace) 生成namespace。您可以通过在 [`itertag`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.XMLFeedSpider.itertag) 属性中制定节点的namespace。例如:`class YourSpider(XMLFeedSpider):    namespaces = [('n', 'http://www.sitemaps.org/schemas/sitemap/0.9')]    itertag = 'n:url'    # ...`除了这些新的属性之外，该spider也有以下可以覆盖(overrideable)的方法:`adapt_response`(*response*)该方法在spider分析response前被调用。您可以在response被分析之前使用该函数来修改内容(body)。 该方法接受一个response并返回一个response(可以相同也可以不同)。`parse_node`(*response*, *selector*)当节点符合提供的标签名时(`itertag`)该方法被调用。 接收到的response以及相应的 [`Selector`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/selectors.html#scrapy.selector.Selector) 作为参数传递给该方法。 该方法返回一个 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 对象或者 [`Request`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/request-response.html#scrapy.http.Request) 对象 或者一个包含二者的可迭代对象(iterable)。`process_results`(*response*, *results*)当spider返回结果(item或request)时该方法被调用。 设定该方法的目的是在结果返回给框架核心(framework core)之前做最后的处理， 例如设定item的ID。其接受一个结果的列表(list of results)及对应的response。 其结果必须返回一个结果的列表(list of results)(包含Item或者Request对象)。

#### XMLFeedSpider例子

该spider十分易用。下边是其中一个例子:

```
from scrapy import log
from scrapy.contrib.spiders import XMLFeedSpider
from myproject.items import TestItem

class MySpider(XMLFeedSpider):
    name = 'example.com'
    allowed_domains = ['example.com']
    start_urls = ['http://www.example.com/feed.xml']
    iterator = 'iternodes' # This is actually unnecessary, since it's the default value
    itertag = 'item'

    def parse_node(self, response, node):
        log.msg('Hi, this is a <%s> node!: %s' % (self.itertag, ''.join(node.extract())))

        item = TestItem()
        item['id'] = node.xpath('@id').extract()
        item['name'] = node.xpath('name').extract()
        item['description'] = node.xpath('description').extract()
        return item

```

简单来说，我们在这里创建了一个spider，从给定的 `start_urls` 中下载feed， 并迭代feed中每个 `item` 标签，输出，并在 [`Item`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/items.html#scrapy.item.Item) 中存储有些随机数据。

### CSVFeedSpider

- *class*`scrapy.contrib.spiders.``CSVFeedSpider`

  该spider除了其按行遍历而不是节点之外其他和XMLFeedSpider十分类似。 而其在每次迭代时调用的是 [`parse_row()`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.CSVFeedSpider.parse_row) 。`delimiter`在CSV文件中用于区分字段的分隔符。类型为string。 默认为 `','` (逗号)。`headers`在CSV文件中包含的用来提取字段的行的列表。参考下边的例子。`parse_row`(*response*, *row*)该方法接收一个response对象及一个以提供或检测出来的header为键的字典(代表每行)。 该spider中，您也可以覆盖 `adapt_response` 及 `process_results` 方法来进行预处理(pre-processing)及后(post-processing)处理。

#### CSVFeedSpider例子

下面的例子和之前的例子很像，但使用了 [`CSVFeedSpider`](http://scrapy-chs.readthedocs.io/zh_CN/0.24/topics/spiders.html#scrapy.contrib.spiders.CSVFeedSpider):

```
from scrapy import log
from scrapy.contrib.spiders import CSVFeedSpider
from myproject.items import TestItem

class MySpider(CSVFeedSpider):
    name = 'example.com'
    allowed_domains = ['example.com']
    start_urls = ['http://www.example.com/feed.csv']
    delimiter = ';'
    headers = ['id', 'name', 'description']

    def parse_row(self, response, row):
        log.msg('Hi, this is a row!: %r' % row)

        item = TestItem()
        item['id'] = row['id']
        item['name'] = row['name']
        item['description'] = row['description']
        return item

```

### SitemapSpider

- *class*`scrapy.contrib.spiders.``SitemapSpider`

  SitemapSpider使您爬取网站时可以通过 [Sitemaps](http://www.sitemaps.org/) 来发现爬取的URL。其支持嵌套的sitemap，并能从 [robots.txt](http://www.robotstxt.org/) 中获取sitemap的url。`sitemap_urls`包含您要爬取的url的sitemap的url列表(list)。 您也可以指定为一个 [robots.txt](http://www.robotstxt.org/) ，spider会从中分析并提取url。`sitemap_rules`一个包含 `(regex, callback)` 元组的列表(list):`regex` 是一个用于匹配从sitemap提供的url的正则表达式。 `regex` 可以是一个字符串或者编译的正则对象(compiled regex object)。callback指定了匹配正则表达式的url的处理函数。 `callback` 可以是一个字符串(spider中方法的名字)或者是callable。例如:`sitemap_rules = [('/product/', 'parse_product')]`规则按顺序进行匹配，之后第一个匹配才会被应用。如果您忽略该属性，sitemap中发现的所有url将会被 `parse` 函数处理。`sitemap_follow`一个用于匹配要跟进的sitemap的正则表达式的列表(list)。其仅仅被应用在 使用 Sitemap index files 来指向其他sitemap文件的站点。默认情况下所有的sitemap都会被跟进。`sitemap_alternate_links`指定当一个 `url` 有可选的链接时，是否跟进。 有些非英文网站会在一个 `url` 块内提供其他语言的网站链接。例如:`<url>    <loc>http://example.com/</loc>    <xhtml:link rel="alternate" hreflang="de" href="http://example.com/de"/></url>`当 `sitemap_alternate_links` 设置时，两个URL都会被获取。 当 `sitemap_alternate_links` 关闭时，只有 `http://example.com/` 会被获取。默认 `sitemap_alternate_links` 关闭。

#### SitemapSpider样例

简单的例子: 使用 `parse` 处理通过sitemap发现的所有url:

```
from scrapy.contrib.spiders import SitemapSpider

class MySpider(SitemapSpider):
    sitemap_urls = ['http://www.example.com/sitemap.xml']

    def parse(self, response):
        pass # ... scrape item here ...

```

用特定的函数处理某些url，其他的使用另外的callback:

```
from scrapy.contrib.spiders import SitemapSpider

class MySpider(SitemapSpider):
    sitemap_urls = ['http://www.example.com/sitemap.xml']
    sitemap_rules = [
        ('/product/', 'parse_product'),
        ('/category/', 'parse_category'),
    ]

    def parse_product(self, response):
        pass # ... scrape product ...

    def parse_category(self, response):
        pass # ... scrape category ...

```

跟进 [robots.txt](http://www.robotstxt.org/) 文件定义的sitemap并只跟进包含有 `..sitemap_shop` 的url:

```
from scrapy.contrib.spiders import SitemapSpider

class MySpider(SitemapSpider):
    sitemap_urls = ['http://www.example.com/robots.txt']
    sitemap_rules = [
        ('/shop/', 'parse_shop'),
    ]
    sitemap_follow = ['/sitemap_shops']

    def parse_shop(self, response):
        pass # ... scrape shop here ...

```

在SitemapSpider中使用其他url:

```
from scrapy.contrib.spiders import SitemapSpider

class MySpider(SitemapSpider):
    sitemap_urls = ['http://www.example.com/robots.txt']
    sitemap_rules = [
        ('/shop/', 'parse_shop'),
    ]

    other_urls = ['http://www.example.com/about']

    def start_requests(self):
        requests = list(super(MySpider, self).start_requests())
        requests += [scrapy.Request(x, self.parse_other) for x in self.other_urls]
        return requests

    def parse_shop(self, response):
        pass # ... scrape shop here ...

    def parse_other(self, response):
        pass # ... scrape other here ...
```















































































































