首先放上Collection类的子孙后代族谱图以作参考，，虽然这一篇用不上

**JDK1.8**

![image](http://wiki.jikexueyuan.com/project/java-enhancement/images/20-1.png)

## 先放结论
- ArrayList底层由数组实现，可以无限的往里添加元素（自动增长）
- 当你遇到访问元素比插入或者是删除元素更加频繁的时候，你应该使用ArrayList，ArrayList 擅长于随机访问。
- 在ArrayList中增加或者是删除元素，要调用System.arraycopy这种效率很低的操作，如果遇到了需要频繁插入或者是删除的时候，你可以选择其他的Java集合，比如LinkedList。
- ArrayList 是非同步的。

## 从构造函数说起
ArrayList有三个构造函数

```
private static final int DEFAULT_CAPACITY = 10;
private static final Object[] EMPTY_ELEMENTDATA = {};

// 传入多少就创建多大容量的ArrayList
public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
}
//默认空数组    
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}

//传入一个collection，创建一个包含该collection的ArrayList
public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
}

```

## add()函数

add() 也有两个重载方法
```
//直接添加进去
 public boolean add(E e) {
        //确保内部容量足够，不够自动增大0.5倍
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;//这里就是ArrayList之所以是线程不安全的原因
        return true;
}
//在指定位置添加
public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //这个函数就是ArrayList进行增删操作之所以效率低的原因(remove函数中同理)
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
}

private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

//需要增长时，复制一个1.5倍大的数组
private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        //旧数组的对象被复制到了新的数组中，并且现有的数组指向新的数组。
        elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```
简单走一遍上面的代码可以看出，如果每次添加一个元素到ArrayList中，当ArrayList满了时候，会复制出一个1.5倍原来大小的新数组，旧数组的对象被复制到了新的数组中，并且现有的数组指向新的数组，也就是说，**如果我们预先知道需要多大容量的数组的话，可以直接用带参数的构造函数，这样就可以减少数组增大时候多次复制消耗的资源。**

# interator接口  留坑待填

	
```
// iterate via "iterator loop"
System.out.println("\n==> Iterator Example...");
Iterator<String> crunchifyIterator = crunchifyList.iterator();
while (crunchifyIterator.hasNext()) {
	System.out.println(crunchifyIterator.next());
}
```

		
## 其它常用函数
- public int indexOf(Object o)
- public boolean isEmpty() 
- public boolean add(E e)
- public void add(int index, E element)
- public int size()
- public boolean contains(Object o) 
- public int lastIndexOf(Object o)//后面开始数，求索引
- public Object[] toArray()//
- public E get(int index)
- public E set(int index, E element)
- public E remove(int index)
- public boolean remove(Object o)
- public void clear()
- public boolean addAll(Collection<? extends E> c)
- public boolean addAll(int index, Collection<? extends E> c)
-  protected void removeRange(int fromIndex, int toIndex)
- public boolean removeAll(Collection<?> c)
- public boolean removeIf(Predicate<? super E> filter)
- public void sort(Comparator<? super E> c)

## 关于ArrayList的面试题

**1. 当传递ArrayList到某个方法中，或者某个方法返回ArrayList，什么时候要考虑安全隐患？如何修复安全违规这个问题呢？**

 当array被当做参数传递到某个方法中，如果array在没有被复制的情况下直接被分配给了成员变量，那么就可能发生这种情况，即**当原始的数组被调用的方法改变的时候，传递到这个方法中的数组也会改变。**下面的这段代码展示的就是安全违规以及如何修复这个问题。

 ArrayList被直接赋给成员变量——安全隐患：
    
```
public void setMyArray(String[] array){
        this.myArray = array;
    
    }
```

 修复这个安全隐患：


```
String[] myArray;
public void setMyArray(String[] newMyArray){
    if(newMyArray == null){
      this.myArray = new String[0];
    }else {
      this.myArray = Arrays.copyOf(newMyArray,newMyArray.length);
    }
}
```

**2. 在索引中ArrayList的增加或者删除某个对象的运行过程？效率很低吗？解释一下为什么？**

很低，因为增删操作中主要用到了下面这个函数：

```
public static native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);
```

src:源数组；

srcPos:源数组要复制的起始位置；

dest:目的数组；	

destPos:目的数组放置的起始位置；

length:复制的长度。

注意：src and dest都必须是同类型或者可以进行转换类型的数组．有趣的是这个函数可以实现自己到自己复制，比如：


```
int[] fun ={0,1,2,3,4,5,6}; 
System.arraycopy(fun,0,fun,3,3);
```

则结果为：{0,1,2,0,1,2,6};

实现过程是这样的，先生成一个长度为length的临时数组,将fun数组中srcPos 
到srcPos+length-1之间的数据拷贝到临时数组中，再执行System.arraycopy(临时数组,0,fun,3,3)

**3. 如何复制某个ArrayList到另一个ArrayList中去？写出你的代码？**

下面就是把某个ArrayList复制到另一个ArrayList中去的几种技术：

使用clone()方法，比如

```
ArrayList newArray = oldArray.clone();
```


使用ArrayList构造方法，比如：

```
ArrayList myObject = new ArrayList(myTempObject);
```


使用Collection的copy方法。

注意1和2是浅拷贝(shallow copy)。






































