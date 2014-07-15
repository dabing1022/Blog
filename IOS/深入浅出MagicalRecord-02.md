我们接着上一篇 [深入浅出MagicalRecord-01](http://childhood.logdown.com/posts/208957/easy-magicalrecord-01)。

这一节我们一起粗略的了解下 CoreData 中的一些核心概念以及 MagicalRecord 的入门准备。只有对 CoreData 理解深入了，才能更轻松的使用 MagicalRecord。

# <i class="icon-heart"/> 1. CoreData 的核心概念

先上两幅关键的概念图

![概念图1](http://childhoodgamedev.qiniudn.com/ios_CoreData1.jpeg)

![概念图2](http://childhoodgamedev.qiniudn.com/ios_CoreData2.jpeg)

### <i class="icon-comment"/> (1)NSManagedObjectModel 托管对象模型（MOM）
![托管对象的数据模型](http://childhoodgamedev.qiniudn.com/ios_NSManagedObjectModel.png)
![MOM](http://childhoodgamedev.qiniudn.com/ios_managed_object_model_2x.png)

这个MOM由实体描述对象，即`NSEntityDescription`实例的集合组成，实体描述对象介绍见下面第7条。

作用：添加实体的属性，建立属性之间的关系

### <i class="icon-comment"/> (2)NSManagedObjectContext 托管对象上下文（MOC）
![托管对象上下文](http://childhoodgamedev.qiniudn.com/ios_CoreData5.png)

在概念图2中，托管对象上下文（MOC）通过持久化存储协调器（PSC）从持久化存储（NSPersistentStore）中获取对象时，这些对象会形成一个临时副本在MOC中形成一个对象集合，该对象集合包含了对象以及对象彼此之间的一些关系。我们可以对这些副本进行修改，然后进行保存，然后MOC会通过 PSC 对 NSPersistentStore 进行操作，持久化存储就会发生变化。

CoreData中的`NSManagedObjectModel 托管对象的数据模型（MOM）`，通过 MOC 进行注册。MOC有插入、删除以及更新数据模型等操作，并提供了撤销和重做的支持。

作用：插入数据，更新数据，删除数据

### <i class="icon-comment"/> (3)NSPersistentStoreCoordinator 持久化存储协调器（PSC）
![持久化存储协调器](http://childhoodgamedev.qiniudn.com/ios_CoreData6.png)
![持久化堆栈](http://childhoodgamedev.qiniudn.com/ios_advanced_persistence_stack_2x.png)

在应用程序和外部数据存储的对象之间提供访问通道的框架对象集合统称为持久化堆栈（persistence stack）。在堆栈顶部的是托管对象上下文（MOC），在堆栈底部的是持久化对象存储（persistent object stores）。在托管对象上下文和持久化对象存储之间便是持久化存储协调器（PSC）。应用程序通过类NSPersistentStoreCoordinator的实例访问持久化对象存储。

持久化存储协调器为一或多个托管对象上下文提供一个访问接口，使其下层的多个持久化存储可以表现为单一一个聚合存储。一个托管对象上下文可以基于持久化存储协调器下的所有数据存储来创建一个对象图。持久化存储协调器只能与一个托管对象模型（MOM）相关联。

### <i class="icon-comment"/> (4)NSManagedObject 托管对象（MO）
![托管对象数据记录](http://childhoodgamedev.qiniudn.com/ios_CoreData4.png)

托管对象必须继承自`NSManagedObject`或者`NSManagedObject`的子类。NSManagedObject能够表述任何实体。它使用一个私有的内部存储，以维护其属性，并实现托管对象所需的所有基本行为。托管对象有一个指向实体描述的引用。`NSEntityDescription 实体描述`表述了实体的元数据，包括实体的名称，实体的属性和实体之间的关系。

### <i class="icon-comment"/> (5)Controller 控制器

概念图1中绿色的 Array Controller，Object Controller，Tree Controller 这些控制器，一般都是通过 control+drag 将 Managed Object Context 绑定到它们，这样我们就可以在 nib 中可视化地操作数据

### <i class="icon-comment"/> (6)NSFetchRequest 获取数据请求

使用托管对象上下文来检索数据时，会创建一个获取请求（fetch request）。类似Sql查询语句的功能。

### <i class="icon-comment"/> (7)NSEntityDescription 实体描述
![实体描述](http://childhoodgamedev.qiniudn.com/ios_Entity.png)

实体描述对象提供了一个实体的元数据，包括实体名（Name），类名（ClassName），属性（Properties）以及实体属性与其他实体的一些关系（Relationships）等。

### <i class="icon-comment"/> (8).xcdatamodeld
![](http://childhoodgamedev.qiniudn.com/ios_CoreData3.png)
![](http://childhoodgamedev.qiniudn.com/ios_xcdatamodeld.png)

里面是.xcdatamodeld文件，用数据模型编辑器编辑，编译后为.momd或.mom文件。

我们可以选中我们的应用程序（路径类似为`/Users/Childhood/Library/Application Support/iPhone Simulator/7.1/Applications/005D926F-5763-4305-97FE-AE55FE7281A4`），右键显示包内容，我们看到是这样的。

![](http://childhoodgamedev.qiniudn.com/ios_xcdatamodeld2.png)

我们着重理解下他们之间的协同工作关系。

这里有一个简单的demo<i class="icon-github"/>[CoreDataDemo](https://github.com/dabing1022/CoreDataDemo)。

```objective-c CoreDataDemoSnippet https://gist.github.com/dabing1022/0ce84c7d2882b487f10f  <i class="icon-github-alt"/>Gist地址
#pragma mark - CoreDataAbout
 
- (void)saveContext
{
    NSError* error = nil;
    NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil) {
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
            NSLog(@"Unresolved error :%@, %@", error, [error userInfo]);
            abort();
        }else{
            NSLog(@"save success!");
        }
    }
}
 
- (NSURL*)applicationDocDir
{
    NSURL* url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask]lastObject];
    NSLog(@"%@", [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0]);
    
    return url;
}
 
// 后缀为.xcdatamodeld的包，里面是.xcdatamodel文件，用数据模型编辑器编辑
// 编译后为.momd或.mom文件
- (NSManagedObjectModel*)managedObjectModel
{
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    
    NSURL* modelURL = [[NSBundle mainBundle] URLForResource:@"Journal" withExtension:@"momd"];
    NSLog(@"model url: %@", [modelURL path]);
    self.managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    
    return self.managedObjectModel;
}
 
- (NSManagedObjectContext*)managedObjectContext
{
    if (_managedObjectContext != nil) {
        return _managedObjectContext;
    }
    
    NSPersistentStoreCoordinator* coordinator = [self persistentStoreCoordinator];
    
    if (coordinator != nil) {
        self.managedObjectContext = [[NSManagedObjectContext alloc]init];
        [self.managedObjectContext setPersistentStoreCoordinator:coordinator];
    }
    
    return self.managedObjectContext;
}
 
- (NSPersistentStoreCoordinator*)persistentStoreCoordinator
{
    if (_persistentStoreCoordinator != nil) {
        return _persistentStoreCoordinator;
    }
    
    NSURL* storeUrl = [[self applicationDocDir] URLByAppendingPathComponent:@"Journal.sqlite"];
    
    NSError* error = nil;
    self.persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    
    if (![self.persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeUrl options:nil error:&error]) {
        NSLog(@"Error: %@, %@", error, [error userInfo]);
    }
    
    return self.persistentStoreCoordinator;
}
```
结合图示和代码，我们来看下他们的运作机制（下面参考[罗朝辉（飘飘白云）[Cocoa]深入浅出 Cocoa 之 Core Data（1）- 框架详解](http://blog.csdn.net/kesalin/article/details/6739319))

-  应用程序先创建或读取模型文件（后缀为xcdatamodeld）生成 NSManagedObjectModel 对象。
-  生成 NSManagedObjectContext 和 NSPersistentStoreCoordinator 对象，MOC会设置自身的持久化存储协调器(PSC)，通过PSC来对数据文件进行读写。
-  NSPersistentStoreCoordinator 负责从数据文件(xml，sqlite，二进制文件等)中读取数据生成 Managed Object，或保存 Managed Object 写入数据文件。
-  NSManagedObjectContext 参与对数据进行各种操作的整个过程，它可以持有多个 Managed Object。我们通过它来监测 Managed Object。监测数据对象有两个作用：支持 undo/redo 以及数据绑定。
-  Array Controller，Object Controller，Tree Controller 这些控制器一般与 NSManagedObjectContext 关联，因此我们可以通过它们在 nib 中可视化地操作数据对象

# <i class="icon-heart"/> 2. MagicalRecord的入门准备

如上篇提到的，在工程的PCH预编译头文件中导入`CoreData+MagicalRecord.h`文件。因为该头文件包括了所有需要的MagicalRecord头文件。

在我们的app delegate中，或者在`awakeFromNib`中都可以，我们可以使用下列的方法来设置CoreData堆栈。

```objective-c setup系列方法
+ (void) setupCoreDataStack;
+ (void) setupAutoMigratingCoreDataStack;
+ (void) setupCoreDataStackWithInMemoryStore;
+ (void) setupCoreDataStackWithStoreNamed:(NSString *)storeName;
+ (void) setupCoreDataStackWithAutoMigratingSqliteStoreNamed:(NSString *)storeName;
```
通过调用上面的方法，我们就可以实例化一块CoreData堆栈，并且为该实例提供 getter 和 setter 方法。

需要注意的一点是，当我们的编译器在 DEBUG 模式下（DEBUG的flag为1），如果改变了定义的数据模型而没有创建新的数据模型，那么 MagicalRecord 则会删除老的存储并且会自动创建一份新的，不用在每次改变的时候进行卸载/重新安装。

在我们的app退出时，我们可以使用下面这个方法来做清理工作。

```objective-c cleanUp
[MagicalRecord cleanUp];
```

# <i class="icon-book"/> 3. 参考学习

-  [MagicalRecord的官方资料](https://github.com/magicalpanda/MagicalRecord/blob/develop/Docs/GettingStarted.md)
-  [CoreDataClassOverview](http://cocoadevcentral.com/articles/000086.php)
-  [iphone数据存储之 -- Core Data的使用](http://www.cnblogs.com/xiaodao/archive/2012/10/08/2715477.html)
-  [[Cocoa]深入浅出 Cocoa 之 Core Data（1）- 框架详解](http://blog.csdn.net/kesalin/article/details/6739319)
