#include <stdio.h>


int hailstone(int n)
{
	int length = 1;
	while(1 < n)
	{
		if(n % 2 == 1){
			n = 3*n + 1;
		}else{
			n /= 2;
		}
		length ++;
	}
	return length;
}

int main(int argc, char *argv[]) {
	int length = hailstone(42);
	printf("length %d", length);
}