#include <stdio.h>
#include <stdlib.h>

// 斐波那契数列
#define NUM 13
int main()
{
    int i;
    long fib[NUM] = {1, 1};

    for(i = 2; i < NUM; i++) {
        fib[i] = fib[i - 1] + fib[i - 2];
    }

    for(i = 0; i < NUM; i++) {
        printf("%ld\n", fib[i]);
    }

    return 0;
}
