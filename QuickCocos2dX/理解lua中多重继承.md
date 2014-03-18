今天看到quick-cocos2dx群里有人在问如何实现多重继承，想想自己好像还真没用过lua中的多重继承，确切的说没用过。但如果真用到多重继承的时候，我会想想是不是非得用多重继承，总觉得多重继承有点“重”。好了废话不说，查阅手册，研究了下。看来在Lua中实现多重继承还是挺简单的。


## 多重继承例子

```
local function search(k, tables)
	for i, v in ipairs(tables) do
		if v[k] then
			return v[k]
		end
	end
	return nil
end

-- 这里实现多重继承，参数arg为多重父类表
function createClassFrom(...)
	-- c为返回值的目标表，为实现多重继承的子类表
	local c = {}	
	local parents = {...}
	setmetatable(c, {__index = function(t, k)
		return search(k, parents)
	end})

	function c:new(o)
		o = o or {}
		setmetatable(o, {__index = c})
		return o
	end

	return c
end

-- 人 吃饭
Human = {name = "human"}
function Human:eat()
	print("human eat")
end

-- 程序员 写代码
Programmer = {name = "coder"}
function Programmer:doProgramming()
	print("do coding")
end

-- 女程序员 继承 人和程序员
-- 性别女
FemaleProgrammer = createClassFrom(Human, Programmer)
local femaleCoder = FemaleProgrammer:new({sex = "female"})
femaleCoder:eat() -- human eat
femaleCoder:doProgramming() -- do coding
```
上面代码难点在于理解lua中`__index`的用法。我们在lua中实现继承的时候，会用到这个`__index`。我们再次看看这个`__index`究竟是怎么回事。元表的`__index`可以是一个表，也可以是一个函数。

## 1. __index是个表的情况

```
local Test = { group = "quick" }
function Test:new(o)
	o = o or {}
	setmetatable(o, {__index = Test})
	return o
end

function Test:getName()
	return self.name
end

function Test:setName(name)
	self.name = name
end

local a = Test:new({name = "Just a test"})
print(a:getName()) -- Just a test
print(a.group) -- quick
```
当表a调用自身所没有的方法( getName() )或者属性(group)的时候, lua会通过getmetatable(a)得到a的元表{__index = Test}, 而该元表的`__index`是个表Test，则lua会在这个表Test中看看是否有缺少的域方法("getName")以及属性(group)，如果找到了则会调用表Test的方法或者属性。

## 2. __index 是函数的情况

```
local Test = { }
Test.prototype = { group = "quick", 
				   qq = "284148017", 
				   company = "chukong", 
				   getName = function() return "Just a test" end}

function Test:new(o)
	o = o or {}
	setmetatable(o, {__index = function(table, key)
		return Test.prototype[key]
	end})
	return o
end

local a = Test:new()
print(a:getName()) -- Just a test
print(a.qq) -- 284148017
print(a.company) -- chukong
```
当表a调用自身所没有的方法(getName)或者属性(qq/company)的时候, lua会通过getmetatable(a)得到a的元表
`__index = function(table, key)  return Test.prototype[key] end `, 而该元表的`__index`是个函数，该函数的`实参`依次为正在调用方法、属性的表a 以及 表a中缺失的方法名或属性(键值key)，lua会将这两个实参传入进去并调用__index指向的函数。

例如：

-  `a:getName()`时，就会调用a的元表的__index方法，将自身a以及"getName"键名依次传入进去，返回了`Test.prototype["getName"]`该函数，lua会自动调用该函数，所以`a:getName()`会返回`Just a test`。
-  `a.qq`时，就会调用a的元表的__index方法，将自身a以及"qq"键名依次传入进去，返回了`Test.prototype["qq"]`该函数，lua会自动调用该函数，所以`a:qq`会返回`284148017`。

## 相关阅读

-  关于lua的单个继承，可以查看以前这篇[详解QuickCocos2dX继承机制](http://childhood.logdown.com/posts/169509/detailed-quickcocos2dx-inheritance-mechanism)

## 最后
如果代码有问题或者其他疑问，欢迎一起探讨学习
Happy Coding:)
