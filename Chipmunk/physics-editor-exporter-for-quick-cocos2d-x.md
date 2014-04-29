PhysicsEditorExporter for QuickCocos2dx 使用说明
==================================================

## 前言
> 写这篇文章的时候，我用的是quick-cocos2d-x 2.2.1rc版本，最近quick将要升级2.2.3，但不影响使用。

在quick-cocos2d-x引擎目录的tool文件夹内，有个physics_editor_exporter，这东西是干嘛用的呢？

用过[PhysicsEditor](http://www.codeandweb.com/physicseditor/)的人可能清楚，这个软件可以对图形进行顶点数据编辑，然后选择目标引擎导出数据（下图序号第三位置处）。

![](https://rb7uxa.bl3302.livefilestore.com/y2pyZLRAI74Mfua9j6kQ1u4_7cld8WRnVuslvSoD0cstPdthc17X4O-tAOp7D2SE4np0cgY48QALVJh9AIc8XP2DTifIBn4JPwHTK6GY_XHs_8/features.jpg?psid=1)

![](https://rb7uxa.bl3302.livefilestore.com/y2ptjZjXlirnuy0JrgMzcI1sjyaUW8Tue7mEPdO8obaozF_QstyCN_Cs8ZlPk6Btf8PvRsDe6mBHWtNGqjDr8mrPBeVYUIZ0vD9NwFwClJXrrk/pixelguy-juggling.png?psid=1)

目前默认支持的引擎都有Cocos2D，Corona，Sparrow，LibGDX，AndEngine，Moai，Box2d/Nape-flash等。我们可以自定义导出数据格式，来让软件导出。而Physics_editor_export for quick-cocos2d-x就是在Corona模板（该引擎采用lua语言进行开发，导出的数据为.lua格式）的基础上进行修改而成。

## 使用方法

打开PhysicsEditor的安装目录，找到Resources/exporters，发现里面正是各个引擎的数据导出模板，我们将`quick-cocos2d-x/tool/physics_editor_exporter`目录下的`quick_chipmunk`文件夹拷贝进该目录，然后重启PhysicsEditor，就可以看到导出引擎数据列表中已经有了quick-cocos2d-x chipmunk这么一项。

关于如何使用PhysicsEditor这里不做介绍了，google下，关于如何使用的文章蛮多。

编辑好我们的图片顶点，并且设置合适的参数（包括弹性、摩擦力、质量等参数）后，我们选择Publish As选择目录导出我们的数据。（选择quick-cocos2d-x chipmunk，默认会保存为.lua）

读者可以打开保存的.lua进行查看，感兴趣的可以查看Physics_editor_export的模板与导出的.lua之间是如果对应解析导出的。这项工作由软件完成。

廖大的这个版本有点小问题，我进行了一些修改并附带了一个sample。

1.  模板中的将要导出的fixture字段修改为更适合Chipmunk引擎的shape字段
2.  修正了多边形定点按照scaleFactor计算的bug

[![](https://dl.dropboxusercontent.com/u/76275795/gitHub-download-button.png)](https://github.com/ChipmunkCommunityCN/quick-x_physics_editor_exporter)

Screenshot：

![](https://rb7uxa.blu.livefilestore.com/y2pe_RHleIUc3beH1WSyxwE7WkNPn7c2VVcSDTwmKoVV5nd4XGuGyDR8eVJRO0L1_SwuqEJYmvfaY8HS7qjML59Lw-xc5YydTz6YkSWSnzRQLw/quick-pe-exporter.png?psid=1)

### sample说明

-  res/fruits.pes为PhysicsEditor工程文件
-  res/fruitsPhysicsData.lua为导出的数据文件
-  西瓜、草莓、菠萝、葡萄我在PE设置了碰撞类型依次为1、2、3、4，并在MainScene.lua中对类型1、2的碰撞进行了监听，如果发生碰撞，会将他们移除掉。

## 额外的说明

1.  多边形顶点数据必须是按照顺时针
2.  Chipmunk不支持凹多边形，只支持凸多边形

> 以上两点都跟物理引擎其中涉及到的算法相关，这里不做深究。

这两点不用担心，PhysicsEditor都为我们做好了工作，当我们编辑的图形顶点形成的是凹多边形时，它会将之计算拆分为若干的凸多边形组合，导出的数据完全满足上面提到的两条。朋友们如果看导出的.lua文件会发现，polygons里面如果有若干数组的话，正是凹多边形拆分而来的凸多边形数据。

## 最后

如果有问题，欢迎留言，HappyCoding:)