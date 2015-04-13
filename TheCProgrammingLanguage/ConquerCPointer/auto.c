#include <stdio.h>

void func(int a, int b)
{
	int c, d;
	printf("func: &a..%p &b..%p\n", &a, &b);
	printf("func: &c..%p &d..%p\n", &c, &d);	
}

int main()
{
	int a, b;
	printf("main: &a..%p &b..%p\n", &a, &b);
	func(1, 2);

	return 0;
}