#include <stdio.h>
#include <stdlib.h>

// 静态变量：从程序启动奥运行结束为止持续存在的变量。静态变量总是在虚拟地址空间中占有固定的区域。
// 静态变量包括：全局变量，文件内static变量，局部static变量

int global_variable;
static int file_static_variable;

void func1()
{
	int func1_variable;
	static int func1_static_variable;

	printf("&func1_variable..%p\n", &func1_variable);
	printf("&func1_static_variable..%p\n", &func1_static_variable);
}

void func2()
{
	int func2_variable;
	printf("&func2_variable..%p\n", &func2_variable);
}

int main()
{
	int *p;

	printf("&func1..%p\n", func1);
	printf("&func2..%p\n", func2);

	printf("string literal..%p\n", "abc");

	printf("&global_variable..%p\n", &global_variable);
	printf("&file_static_variable..%p\n", &file_static_variable);

	func1();
	func2();

	p = (int *)malloc(sizeof(int));
	printf("malloc address..%p\n", p);

	return 0;
}