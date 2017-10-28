#### 如何在列表,字典,集合中根据条件筛选数据？

```
## 过滤掉列表[3,39，-1,10,20，-2，...]中的负数
## 筛选出字典{'LiLei':79,'Jim':88,'Lucy':92 ...}中高于90的项
## 筛选出集合{77,89,32,20 ...}中能被3整除的元素
```

通用做法：迭代

更好的解决方案：

- 列表(比迭代快上一倍左右)
  - filter函数 ：filter(lamda x : x >= 0 , data)
  - 列表解析  ：[x for x in data if x >= 0]
- 字典
  - 字典解析
  - d = {x : random.randint(60,101) for x in range(1,21) }
  - {k:v for k,v in d.iteritems() if v > 90}
- 集合
  - 集合解析
  - s = set(data)
  - {x for x in s if x % 3 == 0}

---------------

#### 如何为元组中的每个元素命名，提高程序可读性？

例如用元组表示一个学生信息：student= ('Jim',16,'male','jim9769@gmail.com')

访问时，我们只能用索引（index）来访问,大量的索引将会降低程序可读性

解决方案：

- 方案一   定义类似于其他语言的美剧类型，也就是定义一系列数值常量
- 方案二   使用标准库中collection.namedtuple代替内置tuple

例

Student = nametuple ( 'Student' , [ 'name' , 'age' , 'sex' , email ] )

s = Student( ' Jim ' , 16 , ' male ' , email = ' Jim142124@gmail.com ' )

s.name









#### tips

- timeit模块--准确测量某段代码的运行时间

  - ```
    timeit.timeit('[x for x in range(100) if x%2==0]',number=100)
    ```