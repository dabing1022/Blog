#include <stdio.h>

static int i;

void fun1 (void)
{
    static int j = 0;
    j++;
    printf("j: %d", j);
    printf("\n");
}


void fun2 (void)
{
    i = 0;
    i++;
    printf("i: %d", i);
    printf("\n");
}


int main()
{
    for(int k = 0; k < 10; k++)
    {
        fun1();
        fun2();
    }

    printf("---------------------\n");
    
    int *p = NULL;
    printf("sizeof p :%lu", sizeof(p));
    printf("\n");
    printf("sizeof *p :%lu", sizeof(*p));
    printf("\n");


    int b[2] = {1, 2};
    printf("sizeof b :%lu", sizeof(b));
    printf("\n");

    printf("---%.13f ", 10000000000.00 + 0.00000000001);
    printf("\n");
}
