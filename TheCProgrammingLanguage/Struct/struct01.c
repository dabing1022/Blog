#include <stdio.h>
#include <stdlib.h>


struct point
{
    int x;
    int y;
};

struct point makePoint(int x, int y)
{
    struct point temp;
    
    temp.x = x;
    temp.y = y;
    
    return temp;
}

#define min(a, b) ((a) < (b) ? (a) : (b))
#define max(a, b) ((a) > (b) ? (a) : (b))


int main()
{
    printf("---------Struct Test---------\n");

    struct point p1 = makePoint(100, 200);
    printf("p1.x :%d, p1.y:%d \n", p1.x, p1.y);

    //struct ptr
    struct point *pp;
    pp = &p1;
    printf("pp x:%d, y:%d \n", (*pp).x, (*pp).y);
    printf("pp2 x:%d, y:%d \n", pp->x, pp->y);
    
    return 0;
}


