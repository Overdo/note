#### 视图和模块

- 在Django中，网页的页面和其他内容都是由视图来传递的（视图对WEB请求进行回应）。每个**视图都是由一个简单的Python函数(或者是基于类的视图的方法)表示的**。Django通过检查请求的URL（准确地说，是URL里域名之后的那部分）来选择使用哪个视图。

- url()函数具有四个参数：两个必需的`regex`和 `view`，以及两个可选的`kwargs`和`name`。

  - url()参数：regex

    ​	请注意，这些正则表达式不会检索URL中GET和POST的参数以及域名。 例如，对于`http://www.example.com/myapp/`请求，URLconf 将查找`myapp/`。对于`http://www.example.com/myapp/?page=3`请求，URLconf 也将查找`myapp/`。

  - url()参数：view

    ​	当Django找到一个匹配的正则表达式时，它就会调用view参数指定的视图函数，并将HttpRequest对象作为第一个参数，从正则表达式中“捕获”的其他值作为其他参数，传入到该视图函数中。如果正则表达式使用简单的捕获方式，值将作为位置参数传递； 如果使用命名的捕获方式，值将作为关键字参数传递。

  - url()参数：kwargs

    ​	任何关键字参数都可以以字典形式传递给目标视图。 我们在这个教程里不用Django的这个功能。

  - url()参数：name

    ​	命名你的URL。 这样就可以在Django的其它地方尤其是模板中，通过名称来明确地引用这个URL。 这个强大的特性可以使你仅仅修改一个文件就可以改变全局的URL模式。

- 每个视图函数只负责处理两件事中的一件：返回一个包含所请求页面内容的 [`HttpResponse`](http://python.usyiyi.cn/documents/django_182/ref/request-response.html#django.http.HttpResponse)对象，或抛出一个诸如[`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404)异常。

- [`TEMPLATES`](http://python.usyiyi.cn/documents/django_182/ref/settings.html#std:setting-TEMPLATES)设置描述了Django将如何加载并渲染模板。默认的设置文件settings.py配置了一个`DjangoTemplates`后端，其中将[`APP_DIRS`](http://python.usyiyi.cn/documents/django_182/ref/settings.html#std:setting-TEMPLATES-APP_DIRS)选项设置为`True`。按照惯例，`DjangoTemplates`在 INSTALLED_APPS所包含的每个应用的目录下查找名为"templates"子目录。怎么使用模板？

  - ```
    from django.http import HttpResponse
    from django.template import RequestContext, loader

    from .models import Question
    ```


            def index(request):
                latest_question_list = Question.objects.order_by('-pub_date')[:5]
                template = loader.get_template('polls/index.html')
                context = RequestContext(request, {
                    'latest_question_list': latest_question_list,
                })
                return HttpResponse(template.render(context))
            ```



##### 快捷方式：render()

​	常见的习惯是载入一个模板、填充一个context 然后返回一个含有模板渲染结果的[`HttpResponse`](http://python.usyiyi.cn/documents/django_182/ref/request-response.html#django.http.HttpResponse)对象。Django为此提供一个快捷方式。 下面是重写后的`index()`视图：

​	polls/views.py

```python
from django.shortcuts import render
from .models import Question

def index(request):
    latest_question_list = Question.objects.order_by('-pub_date')[:5]
    context = {'latest_question_list': latest_question_list}
    return render(request, 'polls/index.html', context)
```

注意，一旦我们在所有的视图上都应用这个快捷函数，我们将不再需要导入 [`loader`](http://python.usyiyi.cn/documents/django_182/topics/templates.html#module-django.template.loader)、[`RequestContext`](http://python.usyiyi.cn/documents/django_182/ref/templates/api.html#django.template.RequestContext)和[`HttpResponse`](http://python.usyiyi.cn/documents/django_182/ref/request-response.html#django.http.HttpResponse) （如果你没有改变先前的`detail`、`results`和 `vote`方法，你将需要在导入中保留`HttpResponse`）。

[`render()`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#django.shortcuts.render)函数将请求对象作为它的第一个参数，模板的名字作为它的第二个参数，一个字典作为它可选的第三个参数。 它返回一个[`HttpResponse`](http://python.usyiyi.cn/documents/django_182/ref/request-response.html#django.http.HttpResponse)对象，含有用给定的context 渲染后的模板。



#### 引发一个404错误

现在，让我们处理Question 详细页面的视图 —— 显示Question内容的页面： 下面是该视图：

polls/views.py

```
from django.http import Http404
from django.shortcuts import render

from .models import Question
# ...
def detail(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        raise Http404("Question does not exist")
    return render(request, 'polls/detail.html', {'question': question})

```

这里有一个新概念：如果没有找到所请求ID的Question，这个视图引发一个[`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404)异常。



#### 快捷方式：get_object_or_404()

一种常见的习惯是使用[`get()`](http://python.usyiyi.cn/documents/django_182/ref/models/querysets.html#django.db.models.query.QuerySet.get)并在对象不存在时引发[`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404)。Django为此提供一个快捷方式。 下面是重写后的`detail()`视图：

polls/views.py

```
from django.shortcuts import get_object_or_404, render

from .models import Question
# ...
def detail(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    return render(request, 'polls/detail.html', {'question': question})

```

[`get_object_or_404()`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#django.shortcuts.get_object_or_404) 函数将一个Django模型作为它的第一个参数，任意数量的关键字参数作为它的第二个参数，它会将这些关键字参数传递给模型管理器中的[`get()`](http://python.usyiyi.cn/documents/django_182/ref/models/querysets.html#django.db.models.query.QuerySet.get) 函数。如果对象不存在，它就引发一个 [`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404)异常。

理念

为什么我们要使用一个辅助函数[`get_object_or_404()`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#django.shortcuts.get_object_or_404)而不是在更高层自动捕获[`ObjectDoesNotExist`](http://python.usyiyi.cn/documents/django_182/ref/exceptions.html#django.core.exceptions.ObjectDoesNotExist)异常，或者让模型的API 引发 [`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404) 而不是[`ObjectDoesNotExist`](http://python.usyiyi.cn/documents/django_182/ref/exceptions.html#django.core.exceptions.ObjectDoesNotExist)？

因为那样做将会使模型层与视图层耦合在一起。 Django最重要的一个设计目标就是保持松耦合。 一些可控的耦合将会在[`django.shortcuts`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#module-django.shortcuts) 模块中介绍。 

还有一个[`get_list_or_404()`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#django.shortcuts.get_list_or_404)函数，它的工作方式类似[`get_object_or_404()`](http://python.usyiyi.cn/documents/django_182/topics/http/shortcuts.html#django.shortcuts.get_object_or_404)  —— 差别在于它使用[`filter()`](http://python.usyiyi.cn/documents/django_182/ref/models/querysets.html#django.db.models.query.QuerySet.filter)而不是[`get()`](http://python.usyiyi.cn/documents/django_182/ref/models/querysets.html#django.db.models.query.QuerySet.get)。如果列表为空则引发[`Http404`](http://python.usyiyi.cn/documents/django_182/topics/http/views.html#django.http.Http404)。



### 自动化测试

- 每个模型或视图应具有一个单独的`TestClass`
- 为你想测试的每一种情况建立一个单独的测试方法
- 测试方法的名字可以描述它们的功能