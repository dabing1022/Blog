// const pointer
#include <iostream>
using namespace std;

int main()
{
    int count = 1;
    // pointer const
    int *const pcount = &count;

    int item = 34;
    // this is wrong.
    //pcount = &item;

    int item2 = 25;
    const int *const pitem = &item;
    // this is right, we can change item2 value by assign it
    item2 = 26;
    // but this is wrong
    //*pitem = 26;

    cout << "sizeof(double) = " << sizeof(double) << endl;
    cout << "sizeof(char) = " << sizeof(char) << endl;

    int x;
    int i = sizeof x;
    cout << "sizeof x = " << i << endl;

    int y[10];
    int *p = y;
    cout << sizeof(y) / sizeof(*y) << endl;
    cout << sizeof(p) / sizeof(*p) << endl;

    // struct
    struct  {
        char c;
        int i;
    }structInstance;

    cout << "sizeof structInstance: " << sizeof structInstance << endl;


    return 0;
}
