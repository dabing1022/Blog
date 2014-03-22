# Effective C++
=====================================

> 改善程序与设计的55个具体做法
> 
> 55 Specific Ways to Improve Your Programs and Designs

这几天在阅读这本书，确实是本很棒的书，想起以前自己栽的坑，后悔没有早一点阅读。先摘抄了下这55条做法。

1. 视 C++ 为一个语言联邦
2. 尽量以 const, enum, inline 替换 #define
3. 尽可能使用 const
4. 确定对象被使用前已被
5. 了解 C++ 默默编写并调用哪些函数
6. 若不想使用编译器自动生成的函数，就该明确拒绝
7. 为多态基类声明 virtual 析构函数
8. 别让异常逃离析构函数
9. 绝不在构造和析构过程中调用 virtual 函数
10. 令 operator＝ 返回一个 reference to *this
11. 在 operator＝ 中处理“自我赋值”
12. 复制对象时勿忘每一个成分
13. 以对象管理资源
14. 在资源管理类中小心 coping 行为
15. 在资源管理类中提供对原始资源的访问
16. 成对使用 new 和 delete 时要采取相同形式
17. 以独立语句将 newed 对象置入智能指针
18. 让接口容易被正确使用，不易被误用
19. 设计 class 犹如设计 type
20. 宁以 pass-by-reference-to-const 替换 pass-by-value
21. 必须返回对象时，别妄想返回其 reference
22. 将成员变量声明为 private
23. 宁以 non-member、non-friend 替换 member 函数
24. 若所有参数皆需类型转换，请为此采用 non-member 函数
25. 考虑写出一个不抛异常的 swap 函数
26. 尽可能延后变量定义式的出现时间
27. 尽量少做转型动作
28. 避免返回 handles 指向对象内部成分
29. 为“异常安全”而努力是值得的
30. 透彻了解 inlining 的里里外外
31. 将文件间的编译依存关系降至最低
32. 确定你的 public 继承塑模出 is-a 关系
33. 避免遮掩继承而来的名称
34. 区分接口继承和实现继承
35. 考虑 virtura 函数以外的其他选择
36. 绝不重新定义继承而来的 not-virtual 函数
37. 绝不重新定义继承而来的缺省参数值
38. 通过复合塑模出 has-a 或 “根据某物实现出”
39. 明智而审慎地使用 private 继承
40. 明智而审慎地使用多重继承
41. 了解隐式接口和编译期多态
42. 了解 typename 的双重意义
43. 学习处理模板化基类内的名称
44. 将与参数无关的代码抽离 templates
45. 运用成员函数模板接受所有兼容类型
46. 需要类型转换时请为模板定义非成员函数
47. 请使用 traits classes 表现类型信息
48. 认识 template 元编程
49. 了解 new-handler 的行为
50. 了解 new 和 delete 的合理替换时机
51. 编写 new 和 delete 时需固守常规
52. 写了 placement new 也要写 placement delete
53. 不要轻忽编译器的警告
54. 让自己熟悉包括 TR1 在内的标准程序库
55. 让自己熟悉 Boost