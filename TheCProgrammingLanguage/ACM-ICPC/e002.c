#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// 进制转换
void convert(char *s, int n, int b)
{
    char bit[] = {"0123456789ABCDEF"};
    int len;
    if (n == 0) {
        strcpy(s, "");
        return;
    }

    convert(s, n / b, b);
    len = strlen(s);
    s[len] = bit[n % b];
    s[len+1] = '\0';
}

// 数制转换
int main()
{
    char s[80];
    int base, old;
    printf("请输入10进制数：");
    scanf("%d", &old);

    printf("请输入转换的进制：");
    scanf("%d", &base);

    convert(s, old, base);
    printf("10进制数%d的%d进制转换结果为%s\n", old, base,s);

    getchar();

    return 0;
}
