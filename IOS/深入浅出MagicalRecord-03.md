我们接着上一篇 [深入浅出MagicalRecord-02](http://childhood.logdown.com/posts/209933/easy-magicalrecord-02)。

这节我们来一起学习下MagicalRecord对数据的_增删改查_，内容主要来自于 [MagicalRecord的github资料](https://github.com/magicalpanda/MagicalRecord/blob/develop/Docs/Fetching.md)。

# 1. 增-创建实体

-  创建实体
```objective-c 
Person *myPerson = [Person MR_createEntity];
```
-  指定创建的上下文中创建实体
```objective-c 
Person *myPerson = [Person MR_createInContext:otherContext];
```

# 2. 删-删除实体

-  删除一个实体
```objective-c 
[myPerson MR_deleteEntity];
```

-  删除特定上下文中的实体
```objective-c 
[myPerson MR_deleteInContext:otherContext];
```

-  删除所有实体
```objective-c 
[Person MR_truncateAll];
```

-  删除特定上下文中的所有实体
```objective-c 
[Person MR_truncateAllInContext:otherContext];
```
# 3. 改-修改实体

```objective-c
Person *person = ...;
person.lastname = "xxx";
```
# 4. 查-查询实体

查询的结果通常会返回一个`NSArray`结果。

## a) 基本查询

-  从持久化存储（PersistantStore）中查询出所有的Person实体
```objective-c 
NSArray *people = [Person MR_findAll];
```

-  查询出所有的Person实体并按照 lastName 升序（ascending）排列
```objective-c 
NSArray *peopleSorted = [Person MR_findAllSortedBy:@"lastName" ascending:YES];
```

-  查询出所有的Person实体并按照 lastName 和 firstName 升序（ascending）排列
```objective-c 
NSArray *peopleSorted = [Person MR_findAllSortedBy:@"lastName,firstName" ascending:YES];
```

-  查询出所有的Person实体并按照 lastName 降序，firstName 升序（ascending）排列
```objective-c 
NSArray *peopleSorted = [Person MR_findAllSortedBy:@"lastName:NO,firstName" ascending:YES];
//或者
NSArray *peopleSorted = [Person MR_findAllSortedBy:@"lastName,firstName:YES" ascending:NO];
```

-  查询出所有的Person实体 firstName 为 Forrest 的实体
```objective-c 
Person *person = [Person MR_findFirstByAttribute:@"firstName" withValue:@"Forrest"];
```

## b) 高级查询

使用`NSPredicate`来实现高级查询。

```objective-c 
NSPredicate *peopleFilter = [NSPredicate predicateWithFormat:@"department IN %@", @[dept1, dept2]];
NSArray *people = [Person MR_findAllWithPredicate:peopleFilter];
```
## c)返回 NSFetchRequest

```objective-c
NSPredicate *peopleFilter = [NSPredicate predicateWithFormat:@"department IN %@", departments];
NSFetchRequest *people = [Person MR_requestAllWithPredicate:peopleFilter];
```
## d)自定义 NSFetchRequest

```objective-c
NSPredicate *peopleFilter = [NSPredicate predicateWithFormat:@"department IN %@", departments];

NSFetchRequest *peopleRequest = [Person MR_requestAllWithPredicate:peopleFilter];
[peopleRequest setReturnsDistinctResults:NO];
[peopleRequest setReturnPropertiesNamed:@[@"firstName", @"lastName"]];

NSArray *people = [Person MR_executeFetchRequest:peopleRequest];
```
## e)查询实体的个数

-  返回的是 NSNumber 类型
```objective-c  
NSNumber *count = [Person MR_numberOfEntities];
```

-  基于NSPredicate查询条件过滤后的实体个数
```objective-c 
NSNumber *count = [Person MR_numberOfEntitiesWithPredicate:...];
```
-  返回的是 NSUInteger 类型

```objective-c 
+ (NSUInteger) MR_countOfEntities;
+ (NSUInteger) MR_countOfEntitiesWithContext:(NSManagedObjectContext *)context;
+ (NSUInteger) MR_countOfEntitiesWithPredicate:(NSPredicate *)searchFilter;
+ (NSUInteger) MR_countOfEntitiesWithPredicate:(NSPredicate *)searchFilter inContext:(NSManagedObjectContext *)
```

## f)合计操作

```objective-c
NSInteger totalFat = [[CTFoodDiaryEntry MR_aggregateOperation:@"sum:" onAttribute:@"fatCalories" withPredicate:predicate] integerValue];

NSInteger fattest  = [[CTFoodDiaryEntry MR_aggregateOperation:@"max:" onAttribute:@"fatCalories" withPredicate:predicate] integerValue];

NSArray *caloriesByMonth = [CTFoodDiaryEntry MR_aggregateOperation:@"sum:" onAttribute:@"fatCalories" withPredicate:predicate groupBy:@"month"];
```

## g)从指定上下文中查询

```objective-c
NSArray *peopleFromAnotherContext = [Person MR_findAllInContext:someOtherContext];

Person *personFromContext = [Person MR_findFirstByAttribute:@"lastName" withValue:@"Gump" inContext:someOtherContext];

NSUInteger count = [Person MR_numberOfEntitiesWithContext:someOtherContext];
```
<center>下一篇：[深入浅出MagicalRecord-04](http://childhood.logdown.com/posts/211076/easy-magicalrecord-04)</center>