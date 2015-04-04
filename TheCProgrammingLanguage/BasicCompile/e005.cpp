#include <iostream>
#include <string>

using namespace std;

int bitcount(unsigned x)
{
	int b;
	for(b = 0; x != 0; x >>= 1) {
		if (x & 01) {
			b++;
		}
	}

	return b;
}

int main()
{
	for (int i = 0; i < 10; ++i) {
		// cout << ++i << endl;
		cout << i++ << endl;
	}

	int n = 10;
	n = n & 0177;
	cout << "n = " << n << endl;

	int testNumber = 100;
	cout << "testNumber bit 1 count: " << bitcount(testNumber) << endl;

	char s[]="This is string for test";
    char d[20];
    strcpy(d,s);
    printf("%s",d);

    return 0;
}