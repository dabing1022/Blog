这里我提供2个解决办法

##1.新增函数绑定

-  在`CCDrawNode.h`文件中添加头文件`#include "cocoa/CCPointArray.h"`
-  在`CCDrawNode.h`文件中添加函数声明：

（函数A）

```
void drawPolygon(CCPointArray *verts, const ccColor4F &fillColor, float borderWidth, const ccColor4F &borderColor);
````

这里将

（函数B）

```
void drawPolygon(CCPoint *verts, unsigned int count, const ccColor4F &fillColor, float borderWidth, const ccColor4F &borderColor);
```

函数的C风格CCPoint数组verts，换成了CCPointArray类型数组，同时省去了多边形顶点个数count。

- 在`CCDrawNode.cpp`中添加我们新增函数的实现：

```
void CCDrawNode::drawPolygon(CCPointArray *verts, const ccColor4F &fillColor, float borderWidth, const ccColor4F &borderColor)
{
	//用CCPointArray迂回了下
    CCPoint* points = verts->fetchPoints();
    //得到顶点个数
    unsigned int count = verts->count();
    //调用原有函数
    drawPolygon(points, count, fillColor, borderWidth, borderColor);
}
```

-  更新tolua文件

在`cocos2d/quick-cocos2d-x/lib/luabinding/cocos2dx/draw_nodes/CCDrawNode.tolua`文件中，在其中添加函数声明

`void drawPolygon(CCPointArray *verts, ccColor4F fillColor, float borderWidth, ccColor4F borderColor);`，添加结束后回到`cocos2d/quick-cocos2d-x/lib/luabinding`目录，运行build脚本，完毕后`cocos2d/quick-cocos2d-x/lib/cocos2d-x/scripting/lua/cocos2dx_support/LuaCocos2d.cpp`这个超大文件会得到更新，里面已经为我们添加了新增函数的绑定。

-  重新编译player，让player认识我们新增的函数。打开quick项目下player目录，重新编译player，新编译的player便可以正常识别这个函数了。

下面是测试代码：

```
    local node = CCDrawNode:create()
    local pointarr1 = CCPointArray:create(4)
    pointarr1:add(ccp(100, 100))
    pointarr1:add(ccp(200, 100))
    pointarr1:add(ccp(200, 200))
    pointarr1:add(ccp(100, 200))
    node:drawPolygon(pointarr1, ccc4f(1.0, 1.0, 0, 0.5), 4, ccc4f(0.1, 1, 0.1, 1) )
    self:addChild(node)
```
效果图：

![](https://dl.dropboxusercontent.com/u/76275795/BlogPictures/20140217/drawPolygon.png)

##2. 曲线救国

```
    local node = CCDrawNode:create()
    local pointarr1 = CCPointArray:create(4)
    pointarr1:add(ccp(100, 100))
    pointarr1:add(ccp(200, 100))
    pointarr1:add(ccp(200, 200))
    pointarr1:add(ccp(100, 200))
    node:drawPolygon(pointarr1:fetchPoints(), 4, ccc4f(1.0, 1.0, 0, 0.5), 4, ccc4f(0.1, 1, 0.1, 1) )
    self:addChild(node)
```

通过CCPointArray的fetchPoints得到c风格CCPoint数组，顶点个数为4

截图和上面一样