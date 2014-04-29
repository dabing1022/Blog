探讨Chipmunk2D碰撞查询
=======================

先借用《游戏引擎架构》一书中的一段话，来解释下物理引擎中的碰撞查询是用来干啥的。

> 碰撞检测的另一任务是回答有关游戏世界中碰撞体积的假想问题。例如：
> 1）若从玩家武器的的某方向射出子弹，若能击中目标，那目标是什么？  2）汽车从A点移动至B点是否会碰到任何障碍物？ 3）找出玩家在给定半径范围内的所有敌人对象。 一般而言，这些操作称为碰撞查询（collision query）。最常用的查询类型是碰撞投射（collision cast），或简称作投射（cast）。投射用于判断，若放置某假想物体于碰撞世界，并沿光线或线段移动，是否会碰到世界中的物体。投射与正常的碰撞检测操作有别，因为投射的实体并不真正存在于碰撞世界，它完全不会影响世界中的其他物体。这就是为什么我们称，碰撞投射是回答关于世界中碰撞体的假想问题。

在阅读本文前，朋友们可以先瞅下官方文档中对**碰撞查询**的一些相关说明。

【Chipmunk2D中文手册】 [猛击这里](https://github.com/iTyran/ChipmunkDocsCN/blob/master/Chipmunk2D.md)

# 1.最近点查询

最近点查询允许你检查离给定点一定距离内是否存在着形状，找到形状上离给定点最近的点或者找到离给定点最近的形状。

![](https://ttp86a.bl3302.livefilestore.com/y2p45f5qQM5hNZfrpNIFvCaFQUM2l7bnhizZ9bWYlENXToNT_67cXEd1-BHeyYrjTk2e8Z5nWEopEDXQvKa7RB3kTJs8dk9fPIkRHNA2kXVgUo/nearest2.png?psid=1)

（截图来自[Chipmunk](https://github.com/slembcke/Chipmunk2D)DEMO中的Segment Query）

![](https://ttp86a.bl3301.livefilestore.com/y2p290Qm4U33xtzbqcLOBCN0e2uMsDC5t8FC6vPbquFi5l0vFGaGiji4IePfzqS9Z3eqRKkMoTxbvjo21KWTkY3nJCIH-KqgGFwBBJwp_fX4os/any.png?psid=1)

（截图来自cocos2dx3.0 demo中的RayCast  模式：any（任意））

![](https://ttp86a.bl3302.livefilestore.com/y2p72DAUfj7UIc_7_wzIlX-SlvS622vYnecGhZ_9tkwHeqjPJ26BrjiShyj2bUvIkGKMUTnF_RGMK_1JgZGG29f1iEbhXlZs_fQFbq8Y9SFndQ/nearest.png?psid=1)

（截图来自cocos2dx3.0 demo中的RayCast  模式：nearest（最近点））

![](https://ttp86a.bl3302.livefilestore.com/y2p-2B9pPt6yFoIH9eyFuv_CsSQVzq1EHdHt2rNmCEOXZdEOME6YkhJxk_ljRui5miNzv9ddnY7uAbMBc9n54mpcQ9xO5ZoGtUPkoC5fpKGSlo/multiple.png?psid=1)

（截图来自cocos2dx3.0 demo中的RayCast  模式：multiple（多个点））