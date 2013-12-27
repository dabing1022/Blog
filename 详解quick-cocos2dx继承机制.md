#详解Quick-Cocos2dX继承机制

**Author: ChildhoodAndy**

**BLOG: [childhood.logdown.com](http://childhood.logdown.com)**

**EMAIL：dabing1022@gmail.com**

------------------------------------------------------------------

这篇文章让我们来理解下QuickCocos2dx的继承机制，关于继承这个概念，是OO思想里提出来的。在C++中，我们就不说了; 在lua中，是用table配合元表metatable来实现面向对象的。下面分两部分来理解Quick中的继承机制。

##第一部分：LUA的继承

###充电站：元表
-  lua中的元表和js的原型非常相似，熟悉js的朋友应该发现了
-  在lua中，每一个表都有TA的元表metatable，lua默认创建一个不带元表的新表：

```
t = {}
print(getmetatable(t)) --nil
```
-  设置元表

```
mt = {name = "quick"}
t = {}
setmetatable(t, mt)
assert(getmetatalbe(t) == mt)
```

![LuaInherit lua继承](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/luaInherit.png)

图示代码：

```
--====================Person======================
local Person = {}
Person.attack = 5

function Person:new(o)
	o = o or {}
	setmetatable(o, self)
	self.__index = self
	return o
end

function Person:setAttack(attack)
	self.attack = attack
end

function Person:getAttack()
	return self.attack
end


--====================Hero======================
local Hero = Person:new()
Hero.name = ""
Hero.skill = ""


--====================hero1,hero2======================
local hero1 = Hero:new({name = "金刚狼"})
hero1.name = "金刚狼"
hero1.skill = "甩开爪子切牛排"

local hero2 = Hero:new({name = "超人"})
hero2.skill = "内裤外穿走T台"


----====================================================
function printKeys(name, t)
	print("======================" .. name)
	for k, v in pairs(t) do
		print(k)
	end
end


printKeys("Person", Person)
printKeys("Person.__index", Person.__index)

printKeys("Hero", Hero)
printKeys("Hero.__index", Hero.__index)
printKeys("getmetatable(Hero).__index", getmetatable(Hero).__index)

printKeys("hero1", hero1)

printKeys("hero2", hero2)
```

结合log我们分析下：

```
======================Person
setAttack
__index
getAttack
new
attack
======================Person.__index
setAttack
__index
getAttack
new
attack
======================Hero
skill
name
__index
======================Hero.__index
skill
name
__index
======================getmetatable(Hero).__index
setAttack
__index
getAttack
new
attack
======================hero1
name
skill
======================hero2
name
skill
[Finished in 0.0s]
```

###转回正题

假如当我们调用`hero1:setAttack(500)`的时候，在hero1中是找不到`setAttack`方法的，这时候：

1.  lua会通过`getmetatable(hero1)`得到`hero1`的元表并到元表的`__index域`中去查找，箭头走向：**3---》2**
2.  但仍然没找到，得到Hero元表并继续在其`__index域`中寻找，箭头走向：**1---》0**，这时候寻找到`setAttack`方法并且调用，由于`setAttack`方法，`hero1`会增加字段`attack`。



##第二部分：QUICK的继承

quick的继承实现要考虑到对C++对象的继承和对lua对象的继承。对lua对象的继承我们第一部分已经用元表机制说明。当我们在quick中用class新建类时，始终要清醒的明白，我们新建的类其实就是返回一个lua表（cls）.

继承的核心代码见`framework/functions.lua`中`class(classname, super)`函数。

为了方便理解，上个图先：

![quick-x class](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20131224/quick-x%E7%BB%A7%E6%89%BF.png)


函数class(classname, super)有两个参数：

-  参数1：classname，见名知意，类名
-  参数2：super
    -  1.super的类型:`superType = type(super)`
    -  2.superType可以为`function`, `table`，当不为这两种类型的时候我们将之置为`nil`
    -  3.superType为`function`的时候，表示从C++对象继承，走图示2    
    -  4.superType为`table`的时候，还要看其`__ctype`值，1表示继承自C++对象，走图1；2表示继承自lua表对象，走图3
    -  5.superType为`nil`的时候，从lua表继承，走图4
    
samples/coinFlip项目是个绝佳的例子，我们可以结合该范例进行理解。这里我就不赘述了。

------------------------
>你我是朋友，各拿一个苹果彼此交换，交换后仍然是各有一个苹果；倘若你有一个思想，我也有一种思想，而朋友间交流思想，那我们每个人就有两种思想了。 ——爱尔兰剧作家 萧伯纳 