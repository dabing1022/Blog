#include <stdio.h>

int main()
{
    char board[3][3] = {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };

    printf("address of board       : %p\n", board);
    printf("address of board[0][0] : %p\n", &board[0][0]);
    printf("Value of board[0]      : %p\n", board[0]);
    return 0;
}
