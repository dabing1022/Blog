![](https://rb7uxa.blu.livefilestore.com/y2pmYoXb1Na_cvhS0eo0GkXePFOt0KDnK0n2o1vuXwt1_7J36L_yEkW3Z-gRlCSepn5Ov0e77WXEhpua8PBdKCSU0SGt3RHadjVZS7SZk6uyWk/cocos2dx_box2d_chipmunk.png?psid=1)

# 走进COCOS2DX的物理世界
---------------------------

**Box2D作者**

![](https://rb7uxa.blu.livefilestore.com/y2pdfpxZvF93tXEN2V-fHJle1_cZ7pOBtSdSDepYiskW92ukBAubHmycfTS0SaS9siYQRE_fZmPQ_QGtJiwzNUKCK3lfrYtb5Z486GJBHDJSLQ/authorOfBox2D.png?psid=1)

**Chipmunk2D作者**

![](https://rb7uxa.blu.livefilestore.com/y2pu7NzTKMVphWG-B_p8iZ1S1uOR3DkX9K2vH1JZntm6oMKyGqD16vaoJehdmSAFKkKx5ybWg20xyyxSzK8gyikRSiL14Tq009LdUyl1qK6iTk/authorOfChipmunk.png?psid=1)


关于物理引擎，概括性的介绍，朋友们可以看下[物理引擎-百度百科](http://baike.baidu.com/view/721450.htm)和[物理引擎分类-维基百科](http://zh.wikipedia.org/wiki/Category:%E7%89%A9%E7%90%86%E5%BC%95%E6%93%8E)。Box2D和Chipmunk2D这两个物理引擎精确的说是刚体物理仿真库，这里的物理就是刚体动力学。而我们在谈到这物理引擎的时候，经常会听到刚体（Rigid body）。那什么是刚体？刚体就是在任何力的作用下，体积和形状都不发生改变的物体。动力学又是什么？动力学是计算刚体在受力作用下随时间移动并相互作用的一个过程。Box2D和Chipmunk2D两个引擎的建构理论基础当然基于此。

作为物理引擎来讲，两个物理引擎在一些基本概念上都是一致的，有的只是说法不同而已。如Box2D中将物理世界称之为`world`，而Chipmunk则称之为`space`，其实描述的都是碰撞/物理世界，即`physics world`。一些概念如`shape`-形状,`body`-刚体,`contact`-接触,`joint`-关节等都一样。

在Cocos2dX中，包括了这两个物理引擎库。开发者可以自由的选择使用哪个物理引擎。但我想不少开发者都在纠结使用哪一个。1000个读者就有1000个哈姆雷特，每个人看法不同，最后的决定也不同。那两大引擎究竟大的区别在哪？这里有几个链接相信大家看了后会有自己的判断。

-  [StackOverflow上的一个讨论](http://www.cocos2d-iphone.org/forums/topic/current-state-of-box2d-vs-chipmunk-debate/)
-  [Kobold2D作者Steffen Itterheim的看法](http://www.kobold2d.com/pages/viewpage.action?pageId=918433)
-  [TIGForums上Chipmunk作者本尊的讨论](http://forums.tigsource.com/index.php?topic=9318.15)
-  [Cocos2d-iPhone论坛上作者本尊以及Birkemose（现Cocos2d-iPhone主要维护人）的讨论](http://www.cocos2d-iphone.org/forums/topic/current-state-of-box2d-vs-chipmunk-debate/)

### 1. 哪个更容易学习？

这个取决于你。50%的开发者认为Box2D比Chipmunk2D要容易，而50%的却认为Chipmunk更容易。我想这是在国外的情况。在国内，Box2D的资料相对Chipmunk更多点，如果对两个引擎都是新接触的话，很明显国内开发者的博客、文档资料、论坛甚至包括一些box2d的游戏源码会让你学起box2d更为容易一些。我以前做as开发的时候用了一下box2d，但只是皮毛，那时候学起来就觉得丰富的文档资料、前人的博客经验分享对学习有着莫大的帮助。最近在学习Chipmunk的时候，谷歌+百度，发现国内这方面资料确实不多。发起Chipmunk中文官方文档的翻译也是我逼不得已的一个举动，一方面想填补国内该引擎中文官方文档的空白，一方面也是方便自己学习。最近文档刚翻译完，这里非常感谢泰然组以及folk进行斧正错误的一些朋友们。文档可见[Chipmunk2D中文手册-github](https://github.com/iTyran/ChipmunkDocsCN/blob/master/Chipmunk2D.md)。顺便提一下，[Chipmunk中文交流社区](https://github.com/ChipmunkCommunityCN)欢迎朋友们加入进来，无论是对项目的folk还是关注，提Issue，这都是你学习Chipmunk最好的方式。这里将会发展成国内最好的一个Chipmunk2D资料分享以及学习平台。以后千万不要告诉妈妈说，Chipmunk文档资料太少了，不好学。

### 2. 性能比较呢？

这里有数据说话

-  [Chipmunk 6b 与Box2D 2.12 性能比较](https://docs.google.com/spreadsheet/ccc?key=0ApnIlzE8tqk6dG1fQWl4NFNrNFhlTmZ3ZlVUdFJFdUE&hl=en_US#gid=0)
-  [Chipmunk 5.3.4 与Box2D 2.12 性能比较](https://docs.google.com/spreadsheet/ccc?authkey=COOb4o8N&key=0ApnIlzE8tqk6dC1XeUdNUjVHMTdmeFNiejRCSjZUT0E&hl=en_US&authkey=COOb4o8N#gid=0)

在很多测试中Chipmunk的性能要明显优于Box2D。对于对性能不做太大要求的游戏，我们完全可以忽略这点差异。

### 3. 还有其他差异吗？

有。Box2D支持“bullet”，也就是我们说的高速移动物体（形象地称为“子弹”）。高速移动的物体在一瞬间可能会错误的穿越过一些物体（被称为“隧道效应”），box2d通过连续碰撞检测来防止这种情况发生。而Chipmunk没有支持这个特性。

另外最显著也是我们常常谈论的，这俩引擎的关节有些不一样。是的。他们有的作用原理是一样的，只是命名不同，如box2d中的旋转关节(RevoluteJoint)就好比Chipmunk中的枢轴关节(PivotJoint)。而有些如box2d中的摩擦力关节(FrictionJoint)，滑轮关节(PulleyJoint)等Chipmunk中并没有，而Chipmunk中的一些阻尼弹簧(DampedSpring)，简单马达(SimpleMotor)等Box2d中并没有。但不用担心，通过关节的一些合理组合，两个物理引擎基本上都可以模拟出彼此的任何关节。

还有很多其他差异，读者可以看上面讨论的4个链接。这里就不详细介绍了。


### 3. Box2D是用C++写的，更面向对象，Chipmunk是c写的，面向对象使用起来不方便

这个就不用再纠结了。下面我们来看看Cocos2dX关于物理引擎部分都为我们做了什么。

Cocos2dX对Chipmunk进行了一些封装，封装工作做了下面几个工作。

1.针对Chipmunk设计良好的c接口进行了一些封装，在cocos2dx v3.0beta2版本中封装的物理库目录如下

![](https://rb7uxa.blu.livefilestore.com/y2p5fT1FaSYGZoOQ_8K616nCTm1byMN3ujegHQduLieavoaWQOOYxigyZGahx8yqoWDcQmYJuwCSLLOT6sA88lLiIBOCF7Dc19abM2WNcmk2Ug/contents.png?psid=1)

-  物理世界 CCPhysicsWorld.h
-  物理世界信息 CCPhysicsWorldInfo_chipmunk.h

-  刚体 CCPhysicsBody.h
-  刚体信息 CCPhysicsBodyInfo_chipmunk.h

-  形状 CCPhysicsShape.h
-  形状信息 CCPhysicsShapeInfo_chipmunk.h
  
-  约束关节 CCPhysicsJoint.h
-  约束关节信息 CCPhysicsJointInfo_chipmunk.h

-  接触 CCPhysicsContact.h
-  接触信息 CCPhysicsContactInfo_chipmunk.h

-  帮助类 CCPhysicsHelper_chipmunk.h, 包括了一些常用的静态转换函数, 主要指的是cocos2dx中的运算类型和Chipmunk的运算类型的一些互转，如`cpv2point`,`cpv2size`,`cpfloat2float`,`rect2cpbb`等以及反向转换函数。


2.在`extensions/physics-nodes`文件夹下，还存在着两个类。分别是`CCPhysicsSprite.h`和`CCPhysicsDebugNode.h`。

`CCPhysicsSprite.h`物理精灵类：通过设置预处理宏`CC_ENABLE_CHIPMUNK_INTEGRATION`(开启Chipmunk迭代)或者`CC_ENABLE_BOX2D_INTEGRATION`（开启Box2D迭代）来启用相应的引擎。

![](https://rb7uxa.blu.livefilestore.com/y2pmffLifQedWluidjbZniXVr-aqSDnuouv89QqeDiseIfc8aXApb_og5JAuvD7HdcUq_pxoET8pJEs63Zhl_GBmReQNbwWxLYE4p6SIWbC2UQ/PreprocessorMacros.png?psid=1)

如果尝试启用两个，则会抛出`"Either Chipmunk or Box2d should be enabled, but not both at the same time"`错误提示。开发者启用哪个引擎，则应该调用该引擎对应的此类成员函数，如果尝试调用另外一个未启用引擎对应的成员函数，则会抛出断言，“兄弟，别乱调！” 。

`CCPhysicsDebugNode.h`调试节点类：这个类继承于`DrawNode`，用来调试用。将`space`空间传入，这个类会遍历出空间中所有的形状（包括圆形、线段、多边形等）、约束关节（销关节、滑动关节、枢轴关节等等）进行绘制，并配合不同的颜色来标识。详细信息可查看该类。

3.不要以为只有前两条就完了，前面的封装是封装好了，但引擎的数据模拟和图形渲染是独立开的，那最后一步当然是对两者进行绑定。也就意味着将刚体和我们的node节点进行绑定，熟悉box2d的都知道，`bodyDef.userData = someDrawNode`, 刚体定义的用户数据指针会指向我们的可视精灵，当模拟中发生刚体旋转、缩放、位移时，要同步的对可视精灵进行旋转、缩放、位移。这样我们就会看到一个仿真的物理世界。

那Cocos2dX如何做的？

首先在`ccConfig.h`中有个宏开关，

```
/** Use physics integration API */
#ifndef CC_USE_PHYSICS
#define CC_USE_PHYSICS 1
#endif
```
如果我们需要用到物理引擎，设置为1，不需要的话，设置为0。

其次在`CCNode`,`CCSprite`以及`CCScene`中都是以`CC_USE_PHYSICS`启用为前提做了下面一些工作。
-  `CCNode`类：

成员属性

```
#if CC_USE_PHYSICS
    PhysicsBody* _physicsBody;        ///< the physicsBody the node have
#endif
```

成员方法

``` 
#if CC_USE_PHYSICS
    /**
     *   set the PhysicsBody that let the sprite effect with physics
     */
    void setPhysicsBody(PhysicsBody* body);

    /**
     *   get the PhysicsBody the sprite have
     */
    PhysicsBody* getPhysicsBody() const;

    /**
     *   update rotation and position from physics body
     */
    virtual bool updatePhysicsTransform();

#endif
```

设置自身的`PhysicsBody`成员属性，同时还包括位置、角度等同步的一些操作

-  `CCSprite`类：和`CCNode`类似，多了一个dirty设置，跟图形绘制相关。
-  `CCScene`类：

```
#if CC_USE_PHYSICS
public:
    virtual void addChild(Node* child, int zOrder, int tag) override;
    virtual void update(float delta) override;
    inline PhysicsWorld* getPhysicsWorld() { return _physicsWorld; }
    static Scene *createWithPhysics();
protected:
    bool initWithPhysics();
    void addChildToPhysicsWorld(Node* child);

    PhysicsWorld* _physicsWorld;
#endif 
```

平常我们通过`Scene::create()`来创建一个普通的场景，而创建一个物理世界的场景只需`Scene::createWithPhysics()`就可以了，非常容易不是么？这里虚函数`addChild`的重写，会递归将该node以及子node的刚体都加入到空间（物理世界）中。

总的来说，Cocos2dX 3.0版本对物理引擎做的工作使得我们开发物理效果类游戏更方便，追根溯源，对Chipmunk的封装起到了关键作用。后面我会慢慢更新一些对Chipmunk的基本概念的理解、如何使用的一些文章以及demo演示。

>题外话：当[QuickCocos2DX](https://github.com/dualface/quick-cocos2d-x)同步到3.0时，物理引擎部分也会相应导出绑定到lua，加之廖大团队对之进一步的封装完善，你会发现到时用起lua写物理游戏，岂是一个爽字了的。有朋友指出在quick-cocos2d-x中没有box2d，确实cocos2dx官方团队一直没对Box2D做luabinding，不过论坛有个朋友做了这样的工作，自己没测试过，链接地址：[http://www.cocos2d-x.org/forums/11/topics/3181?r=40339](http://www.cocos2d-x.org/forums/11/topics/3181?r=40339)

欢迎大家一起交流。Happy coding! :)


---------------------------
### BOX2D相关资料（保持更新）

-  [Box2dの资料整理与收集](http://bbs.ityran.com/thread-358-1-1.html)

### Chipmunk2D相关资料（保持更新）

-  [Chipmunk官网](http://chipmunk-physics.net/‎)
-  [Chipmunk中文官方文档](https://github.com/iTyran/ChipmunkDocsCN/blob/master/Chipmunk2D.md)
-  [Chipmunk中文交流社区](https://github.com/ChipmunkCommunityCN)