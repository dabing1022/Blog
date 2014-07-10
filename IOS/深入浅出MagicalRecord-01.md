>《深入浅出MagicalRecord》我准备做成一个系列，记录我从0开始学习这个框架的所有心得、记录。希望能和大家一起探讨交流。

============================
# <i class="icon-pencil"></i> 1. CoreData与MagicalRecord
在 ios 开发中，我们会使用CoreData来进行数据持久化。但是在使用CoreData进行存取等操作时，代码量相对较多。而 MagicalRecord 正是为方便操作 CoreData 而生。

MagicalRecord 的三个目标：

-  简化 CoreData 相关代码
-  清晰、简单、单行获取数据
-  当需要优化请求的时候，仍然允许修改 NSFetchRequest

###<i class="icon-github icon-2x"></i>[MagicalRecord-Github地址](https://github.com/magicalpanda/MagicalRecord)

============================
# <i class="icon-cogs"></i> 2. 安装
### <i class="icon-pushpin"></i> 方法1：

1.从github上下载MagicalRecord源码 <i class="icon-github"></i>[MagicalRecord-Github地址](https://github.com/magicalpanda/MagicalRecord）。

2.将`MagicalRecord`文件夹拖放并添加到Xcode项目中。

3.添加 CoreData.framework。

![](http://childhoodgamedev.qiniudn.com/ios_QQ20140710-1.png)

4.在项目的预编译头文件中（PCH）中导入`CoreData+MagicalRecord.h`。或者在将要使用的类中单独导入。

5.开始编码。

### <i class="icon-pushpin"></i> 方法2：

1.如果已经安装了CocoaPods（不清楚CocoaPods使用的朋友们可以看唐巧的这篇[用CocoaPods做iOS程序的依赖管理](http://blog.devtang.com/blog/2014/05/25/use-cocoapod-to-manage-ios-lib-dependency/)）。

在终端中输入`pod search MagicalRecord`，结果如下：

![](http://childhoodgamedev.qiniudn.com/ios_QQ20140710-2.png)

复制`pod 'MagicalRecord', '~> 2.2'`到项目中的Podfile并保存文件。命令行cd到工程根目录下，并执行`pod install`。

2.以下步骤同方法1的3、4、5步骤。

> 注意：
> 如果你在使用 MagicalRecord 方法的时候不想带前缀MR_（比如用 findAll 代替 MR_findAll），只需在PCH文件中，在`CoreData+MagicalRecord.h`之前增加`#defin MR_SHORTHAND`即可。

============================
# <i class="icon-umbrella"></i> 3. 环境需求

-  iOS SDK 6.x 及以上
-  OS X SDK 10.8 及以上
-  使用Xcode5来运行github仓库中包含的测试Tests，但MagicalRecord库建立在Xcode4上。