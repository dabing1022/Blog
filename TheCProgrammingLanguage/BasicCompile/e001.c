// const pointer
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
    return 0;
}
