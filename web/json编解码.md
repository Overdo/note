



### 模块 json

- 编码**：把一个**Python对象**编码转换成Json字符串   json.dumps() 

  - data1 = {'b':789,'c':456,'a':123}

    d1 = json.dumps(data1,sort_keys``=``True``,indent``=``4``)

    print d1

    输出：

    { 
    ​    "a": 123, 
    ​    "b": 789, 
    ​    "c": 456 
    }

- **解码**：把Json格式字符串解码转换成**Python对象**   json.loads()

  - data1 = {'b':789,'c':456,'a':123}

    d1 = json.loads(data1,sort_key=True)

    d1

    输出

    {"a": 123, "b": 789, "c": 456} 	



### json.loads和json.load的区别

- loads针对内存对象，即将Python内置数据序列化为字串

  如使用json.dumps序列化的对象d_json=json.dumps({'a':1, 'b':2})，在这里d_json是一个字串'{"b": 2, "a": 1}'

  d=json.loads(d_json)  #{ b": 2, "a": 1}，使用load重新反序列化为dict

- load针对[文件句柄](https://www.baidu.com/s?wd=%E6%96%87%E4%BB%B6%E5%8F%A5%E6%9F%84&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1dBnhDLnj6dmyDzPvD4uymL0ZwV5Hcvrjm3rH6sPfKWUMw85HfYnjn4nH6sgvPsT6KdThsqpZwYTjCEQLGCpyw9Uz4Bmy-bIi4WUvYETgN-TLwGUv3ErjTdnHbYrHD4PHDvPjRdPjRz)

  如本地有一个json文件a.json则可以d=json.load(open('a.json'))

  相应的，dump就是将内置类型序列化为json对象后写入文件