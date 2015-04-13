#include <stdio.h>

int test(double d)
{
	return d + 2;
}

int test1(double d)
{
	return d + 1;
}

int test2(double d)
{
	return d + 2;
}

int test3(double d)
{
	return d + 3;
}

int test4(double d)
{
	return d + 4;
}

int main()
{
	int a = test(30);
	printf("a is %d\n", a);

	int (*test_p)(double);
	test_p = test;
	int b = test_p(100);
	printf("b is %d\n", b);


	int (*func_table[])(double) = {
		test1,
		test2,
		test3,
		test4
	};

	int a1 = func_table[0](1);
	int a2 = func_table[1](1);
	int a3 = func_table[2](1);
	int a4 = func_table[3](1);
	printf("a1: %d, a2: %d, a3: %d, a4: %d\n", a1, a2, a3, a4);
}