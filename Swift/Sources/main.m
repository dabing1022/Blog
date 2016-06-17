int main(int argc, char * argv[]) {
    void(^myBlock)(void) = ^{
        int i = 2;
    };
    myBlock();
    
    return 0;
}
