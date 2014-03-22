
这一节让我们来理解下Chipmunk2D中的销关节约束。

首先看文档中的一些解释

```
cpPinJoint *cpPinJointAlloc(void)
cpPinJoint *cpPinJointInit(cpPinJoint *joint, cpBody *a, cpBody *b, cpVect anchr1, cpVect anchr2)
cpConstraint *cpPinJointNew(cpBody *a, cpBody *b, cpVect anchr1, cpVect anchr2)
```
a和b是被连接的两个刚体，anchr1和anchr2是这两个刚体的锚点。当关节被创建的时候距离便被确定，如果你想要设定一个特定的距离，使用setter函数来重新设定该值。

getter/setter函数

-  cpVect cpPinJointGetAnchr1(const cpConstraint *constraint)
-  void cpPinJointSetAnchr1(cpConstraint *constraint, cpVect value)
-  cpVect cpPinJointGetAnchr2(const cpConstraint *constraint)
-  void cpPinJointSetAnchr2(cpConstraint *constraint, cpVect value)
-  cpFloat cpPinJointGetDist(const cpConstraint *constraint)
-  void cpPinJointSetDist(cpConstraint *constraint, cpFloat value)

在Cocos2DX中，销关节被封装成了`PhysicsJointDistance`，我们先来看看该类头文件。

PhysicsJointDistance类

```
/** Set the fixed distance with two bodies */
class PhysicsJointDistance : public PhysicsJoint
{
public:
    static PhysicsJointDistance* construct(PhysicsBody* a, PhysicsBody* b, const Point& anchr1, const Point& anchr2);
    
    float getDistance() const;
    void setDistance(float distance);
    
protected:
    bool init(PhysicsBody* a, PhysicsBody* b, const Point& anchr1, const Point& anchr2);
    
protected:
    PhysicsJointDistance() {}
    virtual ~PhysicsJointDistance() {}
};
```
`PhysicsJointDistance`这个名字和销关节的工作机制还是很贴切的。(在Box2D中也有距离关节，b2DistanceJoint)。当关节被创建的时候，刚体a和刚体b的锚点距离就被定了下来。假如后面我们要对该距离进行修改，可以通过`setDistance()`方法来设定距离值。当设定的值不等于刚体之前的锚点间距时，我们会发现画面上刚体锚点的间距会发生突变，那是Chipmunk在按照你新设定的间距在修正。

> 1.注意：`anchr1`和`anchr2`针对的是刚体a和刚体b的局部坐标系。
> 
> 2.注意：`PhysicsJointPin`可不是销关节，其实是枢轴关节(PivotJoint)!`PhysicsJointDistance`才是我们这里说的销关节。

为了更好的理解销关节的工作机制，我做了几个演示来说明下:

![](https://rb7uxa.blu.livefilestore.com/y2pmjvT9TUJS0T37jdaFa8lqGTzQvCxn6bAHGwxLKrZFfy6jAz_nqreG-QvXpZ8bZ6adWmom4NklMLgUq9BHtxgJH8pqfunw_Go1VoQU-vBO1I/PinJoint.gif?psid=1)

图示1

![](https://rb7uxa.blu.livefilestore.com/y2p4ReuQG_Xq8PI2WhPJcHym7T457VFmcpaHR1ZAFPIpWQaUJiDHjGAIQkSgH6l_N52PTC4QDu0bBN_4P3EGx4J1TpDcbq4y98MrEnRwUXwyYM/PinJoint2.gif?psid=1)

图示2（多个销关节对绳索的模拟）

![](https://rb7uxa.blu.livefilestore.com/y2pSDxonSj2EyPuKrZZUu-d3G-kkPDf92zteyTxqVa4De8ZDZ_lUkWlDj1gOFbkghzIltZmvVKHN_Zm1xEXt6eRr70i0Y8PRxuLr2qlpFetDVE/screenshot4.png?psid=1)

图示3

![](https://eapnaq.blu.livefilestore.com/y2ptAfFxY0_Xo4e_z2l1ifxGF6nz6yGstJjWY1ywOFspUE4NJ-RQyvH_NmNzTpAjocz3Q74GMBxhWiW5Hu2WlyFWp3tLjF6in3FuCDjI6PtonM/github1.png?psid=1)
[图示3 Github地址](https://github.com/ChipmunkCommunityCN/RockChipmunkWithCocos2DX/blob/master/Classes/JointsDemo/PinJointScene.cpp)

如果朋友们有什么疑问，欢迎留言讨论(支持非注册用户哦）