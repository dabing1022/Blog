#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

struct student
{
    char *name;
    int score;
}stu, *pstu;

int checkSystem();
int main()
{
    pstu = (struct student *)malloc(sizeof(struct student));
    assert(NULL != pstu);
    pstu->score = 99;
    printf("student score: %d\n", pstu->score);
    free(pstu);

    printf("checkSystem: %d\n", checkSystem());
    return 0;
}


/* 如果当前系统为大端模式这个函数返回 0;如果为小端模式,函数返回 1。 */
int checkSystem()
{
    union check {
        int i;
        char ch;
    } c;
    c.i = 1;
    return (c.ch == 1);
}
