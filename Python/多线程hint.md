

- 当我们使用线程的时候，能使用线程的局部变量，就尽量不要用全局变量，因为使用全局变量涉及同步的问题，可以使用threading.local()对象，如

  `ThreadLocalHelper = threading.local()`

  此时在不同线程操作ThreadLocalHelper ，互不影响

- 如何线程间通信

  - 使用标准库中的Queue.Queue，它是一个线程安全的队列

- 线程间的事件通知，可以使用标准库中的Threading.Event:

  - 等待事件一端调用wait，等待事件（暂停）
  - 通知事件一端调用set，通知事件（继续）

- 如何使用线程池























































































