首先纠正下定柏兄对那个表的写法。

```
{["a"] = 1, ["b"] = 2}
```

或者

```
{a = 1, b = 2}
```

-------------------------

对数组表和哈希表两者而言，C/C++与lua交互仅仅是对堆栈的操作不同。

举例说明：

test1.lua内容如下：

```
test1Table = {1,2}
```

test2.lua内容如下：

```
test2Table = {a = 1, b = 2}  -- {["a"] = 1, ["b"] = 2}
```

下面针对test2.lua，描述下C/C++层与lua层的交互，如果有不对，还恳朋友们指出，千万不要让我带着错误的想法一直错下去。

首先我们理解下lua的堆栈索引，我画了一张图来示意：

![stackIndex](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/stackIndex.png)

关于堆栈的索引的两种方式，我们通常会结合着使用。使用正数索引的时候，我们可以很方便的用1来索引到栈底，用-1索引的时候，很容易索引到栈顶。

再次上图，结合图下代码理解：

![C/C++ interactive with lua](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/C%26Lua.png)


```
	lua_State* L = lua_open();
	luaopen_base(L);
	
	luaL_dofile(L, "test2.lua");
	
	//图示step1，取得test2Table对象放在栈顶
	lua_getglobal(L, "test2Table");    
	
	//图示step2，将C/C++字符串"a"放到栈顶，test2Table对象成为栈底
	lua_pushstring(L, "a");
	
	//图示step3，从当前-2位置（栈底）的test2Table对象中寻找“a”对应的值(1)，然后将栈顶的key也就是"a"出栈，再将1压栈
	lua_gettable(L, -2);
	
	//取得栈顶值1，赋值
	int val = lua_tonumber(L, -1);
```

了解到这个交互过程后，我相信关于

>怎么将lua的key-value的hash table解析成c++的map结构

心里就有谱了 :)
