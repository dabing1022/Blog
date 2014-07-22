我们接着上一篇 [深入浅出MagicalRecord-03](http://childhood.logdown.com/posts/211016/easy-magicalrecord-03)。

这节我们来一起学习下 MagicalRecord 对数据的存储，内容主要来自于 [MagicalRecord的github资料](https://github.com/magicalpanda/MagicalRecord/blob/develop/Docs/Saving.md)。

# <i class="icon-save"/> 存储的时机

一般情况下，我们应该在数据发生变化时就进行存储操作。有些应用选择在退出的时候存储，然而在大多数情况下这是不必要的。__*事实上，如果你只是当应用退出的时候进行存储，你有可能会丢失数据！*__如果你的应用崩溃了呢？用户会丢失他们改变的数据，这是很糟糕的体验，应该极力去避免出现这种情况。

如果你发现存储比较耗时，有下面几点你可以考虑下：

1.__利用后台线程存储__

MagicalRecord 提供了一个简洁的API来操作后台线程对实体改变的存储。例如：
```js
[MagicalRecord saveWithBlock:^(NSManagedObjectContext *localContext) {
    //在这里做存储工作，该闭包代码块的工作将会在后台线程运行
} completion:^(BOOL success, NSError *error) {
    [application endBackgroundTask:bgTask];
    bgTask = UIBackgroundTaskInvalid;
}];
```
2.__将存储任务拆分成细小的存储__

类似导入大量数据这样的任务应该被拆分成多个小模块。一次性存储多少量的数据并没有统一的标准，所以你需要使用 Apple's Instruments 的来测试下你的应用的性能。

# <i class="icon-save"/> 处理长时存储

## <i class="icon-mobile-phone"/> ios平台

当退出 ios 应用时，有机会来整理和存储数据到磁盘上。如果你知道存储操作要持续一会，那么最好的方法就是请求应用延期退出。如下：
```js
UIApplication *application = [UIApplication sharedApplication];

__block UIBackgroundTaskIdentifier bgTask = [application beginBackgroundTaskWithExpirationHandler:^{
    [application endBackgroundTask:bgTask];
    bgTask = UIBackgroundTaskInvalid;
}];

[MagicalRecord saveWithBlock:^(NSManagedObjectContext *localContext) {
    //这里做存储操作
} completion:^(BOOL success, NSError *error) {
    [application endBackgroundTask:bgTask];
    bgTask = UIBackgroundTaskInvalid;
}];
```
请确保认真阅读了 [read the documentation for beginBackgroundTaskWithExpirationHandler](https://developer.apple.com/library/iOS/documentation/UIKit/Reference/UIApplication_Class/Reference/Reference.html#//apple_ref/occ/instm/UIApplication/beginBackgroundTaskWithExpirationHandler:)，因为不适当或者不必要的延长应用程序的生命周期可能会在审核的时候遭到拒绝。

## <i class="icon-desktop"/> OSX平台

在 OS X Mavericks (10.9) 以及后面的版本中，App Nap 可以使得应用在后台的时候可以有效的被终止退出。如果你知道存储操作要持续一会，那么最好的方法就是暂时禁用自动终止和突然终止功能（前提是你的应用支持这些功能）：
```js
NSProcessInfo *processInfo = [NSProcessInfo processInfo];

[processInfo disableSuddenTermination];
[processInfo disableAutomaticTermination:@"Application is currently saving to persistent store"];

[MagicalRecord saveWithBlock:^(NSManagedObjectContext *localContext) {
    //这里做存储操作
} completion:^(BOOL success, NSError *error) {
    [processInfo enableSuddenTermination];
    [processInfo enableAutomaticTermination:@"Application has finished saving to the persistent store"];
}];
```
和 ios 实现一样，在实现之前确保阅读 [read the documentation on NSProcessInfo](https://developer.apple.com/library/mac/documentation/cocoa/reference/foundation/Classes/NSProcessInfo_Class/Reference/Reference.html)来避免被拒绝。

# <i class="icon-bell-alt"/> 变化
在 MagicalRecord 2.2 中，存储API更加一致并遵循CoreData的命名模式。在这个版本中，已经加入了自动化测试来确保将来更新时，存储工作（新的API和废弃的API）能够继续工作。

`MR_save`被暂时恢复到当前线程的同步运行和存储到_持久存储_(persistent store)的原始状态。然而，__`MR_save`方法被标记为“将被废弃”(deprecated)，将会在下个大版本 MagicalRecord 3.0 中移除掉。__你应该使用`MR_saveToPersistentStoreAndWait`同功能函数来替代它。

## a)新的方法
新增加了下面几个方法：
### NSManagedObjectContext+MagicalSaves
```js
- (void) MR_saveOnlySelfWithCompletion:(MRSaveCompletionHandler)completion;
- (void) MR_saveToPersistentStoreWithCompletion:(MRSaveCompletionHandler)completion;
- (void) MR_saveOnlySelfAndWait;
- (void) MR_saveToPersistentStoreAndWait;
- (void) MR_saveWithOptions:(MRSaveContextOptions)mask completion:(MRSaveCompletionHandler)completion;
```
### MagicalRecord+Actions
```js
+ (void) saveWithBlock:(void(^)(NSManagedObjectContext *localContext))block;
+ (void) saveWithBlock:(void(^)(NSManagedObjectContext *localContext))block completion:(MRSaveCompletionHandler)completion;
+ (void) saveWithBlockAndWait:(void(^)(NSManagedObjectContext *localContext))block;
+ (void) saveUsingCurrentThreadContextWithBlock:(void (^)(NSManagedObjectContext *localContext))block completion:(MRSaveCompletionHandler)completion;
+ (void) saveUsingCurrentThreadContextWithBlockAndWait:(void (^)(NSManagedObjectContext *localContext))block;
```

## b)标记为废弃的函数

下面这些函数被标记为“废弃的”，将会在 MagicalRecord 3.0 版本移除掉，推荐使用替代的函数。
### NSManagedObjectContext+MagicalSaves
```js
- (void) MR_save;
- (void) MR_saveWithErrorCallback:(void(^)(NSError *error))errorCallback;
- (void) MR_saveInBackgroundCompletion:(void (^)(void))completion;
- (void) MR_saveInBackgroundErrorHandler:(void (^)(NSError *error))errorCallback;
- (void) MR_saveInBackgroundErrorHandler:(void (^)(NSError *error))errorCallback completion:(void (^)(void))completion;
- (void) MR_saveNestedContexts;
- (void) MR_saveNestedContextsErrorHandler:(void (^)(NSError *error))errorCallback;
- (void) MR_saveNestedContextsErrorHandler:(void (^)(NSError *error))errorCallback completion:(void (^)(void))completion;
```
### MagicalRecord+Actions
```js
+ (void) saveWithBlock:(void(^)(NSManagedObjectContext *localContext))block;
+ (void) saveInBackgroundWithBlock:(void(^)(NSManagedObjectContext *localContext))block;
+ (void) saveInBackgroundWithBlock:(void(^)(NSManagedObjectContext *localContext))block completion:(void(^)(void))completion;
+ (void) saveInBackgroundUsingCurrentContextWithBlock:(void (^)(NSManagedObjectContext *localContext))block completion:(void (^)(void))completion errorHandler:(void (^)(NSError *error))errorHandler;
```