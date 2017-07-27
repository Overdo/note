# Java Annotation

注解，也就是元数据，即一种描述数据的数据

Java Annotation是JDK5.0引入的一种注释机制。

先上一张来自网络的思维导图

![img](http://images.cnitblog.com/blog/400827/201409/161325133151441.jpg)

## 一、Annotations 基础

格式：

```
@Override
void mySuperMethod() { ... }
```

注解内可以包含元素，如

```
@Author(
   name = "Benjamin Franklin",
   date = "3/27/2003"
)
class MyClass() { ... }
```

或者

```
@SuppressWarnings(value = "unchecked")
void myMethod() { ... }
```

如果是只有一个value值的元素，那“value=”可以省略，如：

```
@SuppressWarnings("unchecked")
void myMethod() { ... }
```

如果注解没有元素，那么括号都可以省略，也可以一次生命多个注解，如：

```
@Author(name = "Jane Doe")
@EBook
class MyClass { ... }
```

jdk1.8还支持重复注解：

```
@Author(name = "Jane Doe")
@Author(name = "John Smith")
class MyClass { ... }
```

## 二、Annotation组成部分

java annotation 的组成中，有3个非常重要的主干类。它们分别是：

**(01) Annotation.java**

```
package java.lang.annotation;
public interface Annotation {

    boolean equals(Object obj);

    int hashCode();

    String toString();

    Class<? extends Annotation> annotationType();
}

```

**(02) ElementType.java**

```
package java.lang.annotation;

public enum ElementType {
    TYPE,               /* 类、接口（包括注释类型）或枚举声明  */

    FIELD,              /* 字段声明（包括枚举常量）  */

    METHOD,             /* 方法声明  */

    PARAMETER,          /* 参数声明  */

    CONSTRUCTOR,        /* 构造方法声明  */

    LOCAL_VARIABLE,     /* 局部变量声明  */

    ANNOTATION_TYPE,    /* 注释类型声明  */

    PACKAGE             /* 包声明  */
}
```

**(03) RetentionPolicy.java**

```
package java.lang.annotation;
public enum RetentionPolicy {
    SOURCE,            /* Annotation信息仅存在于编译器处理期间，编译器处理完之后就没有该Annotation信息了  */

    CLASS,             /* 编译器将Annotation存储于类对应的.class文件中。默认行为  */

    RUNTIME            /* 编译器将Annotation存储于class文件中，并且可由JVM读入 */
}

```

说明：
(01) Annotation 就是个接口。
​     “每1个Annotation” 都与 “1个RetentionPolicy”关联，并且与 “1～n个ElementType”关联。可以通俗的理解为：每1个Annotation对象，都会有唯一的RetentionPolicy属性；至于ElementType属性，则有1~n个。

(02) ElementType 是Enum枚举类型，它用来指定Annotation的类型。
​     “每1个Annotation” 都与 “1～n个ElementType”关联。当Annotation与某个ElementType关联时，就意味着：Annotation有了某种用途。
​     例如，若一个Annotation对象是METHOD类型，则该Annotation只能用来修饰方法。

(03) RetentionPolicy 是Enum枚举类型，它用来指定Annotation的策略。通俗点说，就是不同RetentionPolicy类型的Annotation的作用域不同。
​     “每1个Annotation” 都与 “1个RetentionPolicy”关联。
​     a) 若Annotation的类型为 SOURCE，则意味着：Annotation仅存在于编译器处理期间，编译器处理完之后，该Annotation就没用了。
​     例如，“ @Override ”标志就是一个Annotation。当它修饰一个方法的时候，就意味着该方法覆盖父类的方法；并且在编译期间会进行语法检查！编译器处理完后，“@Override”就没有任何作用了。
​     b) 若Annotation的类型为 CLASS，则意味着：编译器将Annotation存储于类对应的.class文件中，它是Annotation的默认行为。
​     c) 若Annotation的类型为 RUNTIME，则意味着：编译器将Annotation存储于class文件中，并且可由JVM读入。

这时，只需要记住“每1个Annotation” 都与 “1个RetentionPolicy”关联，并且与 “1～n个ElementType”关联。学完后面的内容之后，再回头看这些内容，会更容易理解。

## 三、java 常用的Annotation

| 标注                | 说明                                       |
| :---------------- | :--------------------------------------- |
| @Deprecated       | @Deprecated 所标注内容，不再被建议使用。               |
| @Override         | @Override 只能标注方法，表示该方法覆盖父类中的方法。          |
| @Documented       | @Documented 所标注内容，可以出现在javadoc中。         |
| @Inherited        | @Inherited只能被用来标注“Annotation类型”，它所标注的Annotation具有继承性。 |
| @Retention        | @Retention只能被用来标注“Annotation类型”，而且它被用来指定Annotation的RetentionPolicy属性。 |
| @Target           | @Target只能被用来标注“Annotation类型”，而且它被用来指定Annotation的ElementType属性。 |
| @SuppressWarnings | @SuppressWarnings 所标注内容产生的警告，编译器会对这些警告保持静默。 |

由于“@Deprecated和@Override”类似，“@Documented, @Inherited, @Retention, @Target”类似；下面，我们只对@Deprecated, @Inherited, @SuppressWarnings 这3个Annotation进行说明。

###  3.1@Deprecated

@Deprecated 的定义如下：

```
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Deprecated {
}

```

说明：
(01) @interface -- 它的用来修饰Deprecated，意味着Deprecated实现了java.lang.annotation.Annotation接口；即Deprecated就是一个注解。

(02) @Documented -- 它的作用是说明该注解能出现在javadoc中。

(03) @Retention(RetentionPolicy.RUNTIME) -- 它的作用是指定Deprecated的策略是RetentionPolicy.RUNTIME。这就意味着，编译器会将Deprecated的信息保留在.class文件中，并且能被虚拟机读取。

(04) @Deprecated 所标注内容，不再被建议使用。
​     例如，若某个方法被 @Deprecated 标注，则该方法不再被建议使用。如果有开发人员试图使用或重写被@Deprecated标示的方法，编译器会给相应的提示信息。示例如下:

### 3.2@Inherited

@Inherited 的定义如下：

```
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Inherited {
}

```

说明：
(01) @interface -- 它的用来修饰Inherited，意味着Inherited实现了java.lang.annotation.Annotation接口；即Inherited就是一个注解。

(02) @Documented -- 它的作用是说明该注解能出现在javadoc中。

(03) @Retention(RetentionPolicy.RUNTIME) -- 它的作用是指定Inherited的策略是RetentionPolicy.RUNTIME。这就意味着，编译器会将Inherited的信息保留在.class文件中，并且能被虚拟机读取。

(04) @Target(ElementType.ANNOTATION_TYPE) -- 它的作用是指定Inherited的类型是ANNOTATION_TYPE。这就意味着，@Inherited只能被用来标注“Annotation类型”。

(05) @Inherited 的含义是，它所标注的Annotation将具有继承性。
​     假设，我们定义了某个Annotaion，它的名称是MyAnnotation，并且MyAnnotation被标注为@Inherited。现在，某个类Base使用了MyAnnotation，则Base具有了“具有了注解MyAnnotation”；现在，Sub继承了Base，由于MyAnnotation是@Inherited的(具有继承性)，所以，Sub也“具有了注解MyAnnotation”。

@Inherited的使用示例
源码如下(InheritableSon.java)：

### 3.3@SuppressWarnings

@SuppressWarnings 的定义如下：

```
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    String[] value();
}

```

说明：
(01) @interface -- 它的用来修饰SuppressWarnings，意味着SuppressWarnings实现了java.lang.annotation.Annotation接口；即SuppressWarnings就是一个注解。

(02) @Retention(RetentionPolicy.SOURCE) -- 它的作用是指定SuppressWarnings的策略是RetentionPolicy.SOURCE。这就意味着，SuppressWarnings信息仅存在于编译器处理期间，编译器处理完之后SuppressWarnings就没有作用了。

(03) @Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE}) -- 它的作用是指定SuppressWarnings的类型同时包括TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE。

> TYPE意味着，它能标注“类、接口（包括注释类型）或枚举声明”。
> FIELD意味着，它能标注“字段声明”。
> METHOD意味着，它能标注“方法”。
> PARAMETER意味着，它能标注“参数”。
> CONSTRUCTOR意味着，它能标注“构造方法”。
> LOCAL_VARIABLE意味着，它能标注“局部变量”。

(04) String[] value(); 意味着，SuppressWarnings能指定参数

(05) SuppressWarnings 的作用是，让编译器对“它所标注的内容”的某些警告保持静默。例如，"@SuppressWarnings(value={"deprecation", "unchecked"})" 表示对“它所标注的内容”中的 “SuppressWarnings不再建议使用警告”和“未检查的转换时的警告”保持沉默。

补充：SuppressWarnings 常用的关键字的表格

| 关键字         | 说明                                       |
| ----------- | ---------------------------------------- |
| deprecation | 使用了不赞成使用的类或方法时的警告                        |
| unchecked   | 执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型。 |
| fallthrough | 当 Switch 程序块直接通往下一种情况而没有 Break 时的警告。     |
| path        | 在类路径、源文件路径等中有不存在的路径时的警告。                 |
| serial      | 当在可序列化的类上缺少 serialVersionUID 定义时的警告。     |
| finally     | 任何 finally 子句不能正常完成时的警告。                 |
| all         | 关于以上所有情况的警告。                             |



## 四、Annotation 的作用

Annotation 是一个辅助类，它在Junit、Struts、Spring等工具框架中被广泛使用。

我们在编程中经常会使用到的Annotation作用有：

### 1 编译检查

Annotation具有“让编译器进行编译检查的作用”。

例如，@SuppressWarnings, @Deprecated和@Override都具有编译检查作用。

(01) 关于@SuppressWarnings和@Deprecated，已经在“第二部分”中详细介绍过了。这里就不再举例说明了。

(02) 若某个方法被 @Override的 标注，则意味着该方法会覆盖父类中的同名方法。如果有方法被@Override标示，但父类中却没有“被@Override标注”的同名方法，则编译器会报错。

### 2 在反射中使用Annotation

在反射的Class, Method, Field等函数中，有许多于Annotation相关的接口。
这也意味着，我们可以在反射中解析并使用Annotation。

### 3 根据Annotation生成帮助文档

通过给Annotation注解加上@Documented标签，能使该Annotation标签出现在javadoc中。

### 4 能够帮忙查看查看代码

通过@Override, @Deprecated等，我们能很方便的了解程序的大致结构。
另外，我们也可以通过自定义Annotation来实现一些功能。



## 五、annotation的解析

上面简单介绍了注解的定义，组成，常用注解，用途，接下来就是annotation信息的获取和使用处理了。

我们通常使用Java反射机制从一个类中解析注解，请记住，**注解保持性策略应该是RUNTIME**，否则它的信息在运行期无效，我们也不能从中获取任何数据。

具体代码留坑待填























































































































