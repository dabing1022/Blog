# 详解Cocos2DX中Chipmunk碰撞过滤
==================================
![](https://rb7uxa.blu.livefilestore.com/y2p6DgToKusj5GSVy2rO9pIIUBEA3_FxKXSy2Gi8YDHpyOAc7awsLZ9dTCjx8J0XBNV0rvKObBbmQXuuRjvtzAmiMN0PSXLien88UXCUtuIlSo/collisionFilterTitle.png?psid=1)

这节让我们来一起探讨下Chipmunk对碰撞过滤（collision filtering）的处理。碰撞过滤，顾名思义，就是要筛选出发生碰撞的一些刚体，将不会发生碰撞的刚体过滤出去，从而在后续回调中对碰撞进行处理。比如《AngryBird》里面，小鸟和箱子碰撞后，小鸟羽毛飞散、死亡，箱子爆破等的处理。

很多人更熟悉Box2D，为了更好的理解碰撞过滤，让我们先看瞅瞅Box2D是怎么实现碰撞过滤的，然后过渡到Chipmunk。

## 1.Box2D 碰撞过滤实现机制

在Box2D中，通过标志位和掩码的设计来实现碰撞过滤。其中有两个标志位和一个组别索引，分别是

-  categoryBits 类别标志位
-  maskBits 掩码标志位
-  groupIndex 组别索引

这三个属性在碰撞过滤机制中扮演着重要的角色。

### 过滤规则

-  如果两个形状材质的组别索引相同为0，使用类别和掩码计算规则来确定是否碰撞
-  如果两个形状材质的组别索引相同为正数，则直接确定为碰撞
-  如果两个形状材质的组别索引相同为负数，则直接确定为不碰撞
-  如果两个形状材质的组别索引不相同，使用类别和掩码计算规则来确定是否碰撞

额外的一些规则

-  静态刚体的形状永远不会与其他静态刚体的形状发生碰撞
-  同一刚体上的形状永远不会发生碰撞
-  可以选择性的启用或者禁止被关节约束的刚体形状之间的碰撞

> 注：组别索引的过滤筛选要比类别和掩码标志位过滤筛选具有更高的优先级。

```
player1ShapeDef.filter.groupIndex = 1
player2ShapeDef.filter.groupIndex = 1
player3ShapeDef.filter.groupIndex = 2
player4ShapeDef.filter.groupIndex = -3
player5ShapeDef.filter.groupIndex = -3
player6ShapeDef.filter.groupIndex = 0
player7ShapeDef.filter.groupIndex = 0
```

根据上面的规则，我们知道

-  player1与player2碰撞
-  player4与player5不碰撞
-  player1与player3，player3与player4，player5与player7等等这些组别索引不同的形状材质，则要进一步根据类别和掩码计算来确定是否碰撞，后面我们马上会看到。
-  player6与player7组别索引相同为0，也要进一步根据类别和掩码计算来确定是否碰撞

### 类别标志位与掩码标志位的计算

Box2D支持16个类别，我们对于任何一种形状材质都可以设定类别标志位。通常我们可以用一个16进制来表示一个类别标志位，一共16位。比如`0x0004`，展开其实就是`0x0000 0000 0000 0100`。

举个例子：

```
playerShapeDef.filter.categoryBits  = 0x0001
playerShapeDef.filter.maskBits      = 0x0002
monsterShapeDef.filter.categoryBits = 0x0002
monsterShapeDef.filter.maskBits     = 0x0001
```
计算规则：

-  让`材质形状A的类别标志位`与`材质形状B的掩码标志位`进行"按位与"运算得到结果r1
-  让`材质形状B的类别标志位`与`材质形状A的掩码标志位`进行"按位与"运算得到结果r2
-  r1与r2进行“逻辑与”，如果为true，则形状材质A与形状材质B则碰撞，false则不碰撞

我们根据上述规则得出结论，player与player之间不会碰撞，monster与monster之间也不会碰撞，但player与monster之间会发生碰撞。

## 2. Chipmunk2D 碰撞过滤实现

在Chipmunk中，一个shape具有`group`和`layer`的属性，一起来看下在`cpSpaceStep.c`中的一个检测函数`queryReject`，即查询否定拒绝。

```
static inline cpBool
queryReject(cpShape *a, cpShape *b)
{
	return (
		// BBoxes must overlap
		!cpBBIntersects(a->bb, b->bb)
		// Don't collide shapes attached to the same body.
		|| a->body == b->body
		// Don't collide objects in the same non-zero group
		|| (a->group && a->group == b->group)
		// Don't collide objects that don't share at least on layer.
		|| !(a->layers & b->layers)
		// Don't collide infinite mass objects
		|| (a->body->m == INFINITY && b->body->m == INFINITY)
	);
}
```

根据上面的一些否定情况，我们总结出过滤规则：

-  形状a与形状b的轴对齐包围盒如果没有发生碰撞，则不可能碰撞
-  如果形状a和形状b同属于同一个刚体，则不会碰撞
-  如果形状a和形状b在相同的非0组，则不会碰撞，同在0组，或者不相等则考虑碰撞
-  如果形状a的层和形状b的层的按位与运算为0，即意味着不在一个“位面”上，则不会碰撞
-  如果形状a和b从属的刚体的质量无限大，则不可能碰撞

这个是Chipmunk2D里面的碰撞机制，看起来和Box2D不太一样，啊哈？Cocos2dX对物理引擎进行了封装，碰撞过滤的实现和这里的方式却有所不同。封装的碰撞过滤接近了Box2D碰撞过滤的思路。让我们再来看下。

CCPhysicsShape/CCPhysicsBody类里有三个重要的属性，分别是

-  categoryBitmask 

类别掩码，该掩码定义了刚体形状属于的类别。Chipmunk支持32种类别。通过对刚体或刚体形状设定categoryBitmask与contactTestBitmask，将两者按位与运算，我们便可以指定游戏中的哪些刚体之间可以有相互作用，并在相互作用后并进行后续的通知。（该通知直接影响到preSolve、postSolve、seperate等回调是否被调用）

默认值为`0xFFFFFFFF`。

>注意：相互作用并不等于就会产生碰撞反应，如传感器(sensor)就是一例。

-  contactTestBitmask

接触测试掩码，该掩码定义了哪些类别的刚体可以与本刚体（或刚体形状）产生相互作用。在物理空间中，每个刚体的类别掩码（categoryBitmask）会和其他刚体的接触测试掩码（contactTestBitmask）进行按位与运算，如果结果为非0值，便会产生一个`PhysicsContact`对象，并作为参数传入到physics world的代理方法内。为了性能考虑，我们只会设定我们关注的相互作用的的掩码。

默认值为`0x00000000`。

-  collisionBitmask

碰撞掩码，该掩码定义了哪些类别的刚体可以与本刚体（或刚体形状）发生碰撞。当刚体彼此接触的时候，可能会发生碰撞反应。此时该刚体的碰撞掩码（collisionBitmask）会与另外一个刚体的类别（categoryBitmask）进行按位与运算，如果结果为非0值，该刚体就会受到碰撞影响。每个刚体都可以选择是否要受到碰撞影响。例如，你可以通过设定碰撞掩码来避免碰撞计算带来的刚体速度的改变。

默认值为`0xFFFFFFFF`。

另外值得一提的是，封装后的CCPhysicsShape和CCPhysicsBody的group属性和Chipmunk2D的group对过滤规则的影响不一样！！！这里要注意下。上面总结的第三条是Chimunk2D的group的过滤规则，但在Cocos2DX封装之下的group，却采取了和Box2D一样的group过滤规则，即

-  如果两个形状材质的组别索引相同为正数，则直接确定为碰撞
-  如果两个形状材质的组别索引相同为负数，则直接确定为不碰撞

组别索引的过滤筛选要比掩码过滤筛选具有更高的优先级。 之前我以为这是官方的一个bug，提过一个Issule给官方团队，见这里[https://github.com/cocos2d/cocos2d-x/pull/6148](https://github.com/cocos2d/cocos2d-x/pull/6148)。官方解释的原因是对物理引擎的封装要隐藏掉具体的使用哪个引擎的细节，而更关心的是友好的api，性能和功能性，另外一方面是对于有SpriteKit开发经验的开发者要更友好点。解释可以接受，但感觉怪怪的，这里的封装建构在Chipmunk2D之上，但group的过滤却是Box2D的规则。换个角度想，如果不叫group，或许更好接受点。

关于在Cocos2DX v3.x里面如何理解Chipmunk2D的碰撞过滤，可以参考这个简单的[demo](https://github.com/ChipmunkCommunityCN/RockChipmunk2D/blob/master/Classes/ContactFilterScene.cpp)

思考：为什么ball1与ball2不碰撞，box1与ball1、ball2不碰撞，box2与ball1、ball2碰撞？改变他们的group会怎么样？对他们的一些掩码重新赋值会怎么样？朋友们可以尝试着设定不同的掩码来观察，方便理解其中的规则。

欢迎朋友们关注这个基础概念demo的项目，在学习过程的测试demo可以提交个pull request过来，一起来丰富这个项目。

![](https://rb7uxa.blu.livefilestore.com/y2p5DekgFBQqdfVSsMkYO3JtoE1Cb9kwN0sLFTdT2QM_qNIFG3yMbXK5ThS_GcKOuFBEii48E63kA8zb2qvaVqf3ojmNBnq3sjgTZNqb079EWs/list.png?psid=1)

![](https://rb7uxa.bl3302.livefilestore.com/y2p_b_T9alUasHoJnyonV3cJns7Nhx9-rAvGhDm9BegDO78KDYDgRt97CE0bzjA1ED0LiH4ijoQAjMLm_aVVYGye-NQ_pkrIl1BnyKqKY0qPm8/collisionFilter.png?psid=1)

## 参考

- [Box2D中文手册](https://github.com/ChipmunkCommunityCN/ChipmunkDocsCN/wiki/Box2D%E4%B8%AD%E6%96%87%E6%96%87%E6%A1%A3)
- [Chipmunk2D中文手册](https://github.com/ChipmunkCommunityCN/ChipmunkDocsCN/blob/master/Chipmunk2D%E4%B8%AD%E6%96%87%E6%89%8B%E5%86%8C.md)
- [Box2D碰撞过滤](http://ohcoder.com/blog/2012/11/30/collision-filtering/)
- [quick-cocos2d-x物理引擎之chipmunk](http://my.oschina.net/lonewolf/blog/173593)


