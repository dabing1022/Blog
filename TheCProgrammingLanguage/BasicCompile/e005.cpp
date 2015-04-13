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

char* customStrcpy(char *strDest, const char *strSrc)
{
	// assert((strDest != NULL) && (strSrc != NULL));
	char *address = strDest;
	while((*strDest++ = *strSrc++) != '\0');
	return address;
}

int strLen(const char *str)
{
	// assert(str != NULL);
	int len;
	while(*str++ != '\0')
	{
		len++;
	}
	return len;
}

void getMemory1(char *p)
{
	p = (char *)malloc(100);
}

void test1()
{
	char *str = NULL;
	getMemory1(str);
	// str will still be NULL
	customStrcpy(str, "hello world");
	printf("%s\n", str);
}

char* getMemory2()
{
	char p[] = "hello world"; 
	return p; 
}

void test2()
{
	char *str = NULL;
	str = getMemory2();
	printf("%s\n", str);
}

void getMemory3(char **p, int num)
{
	*p = (char *)malloc( num );
	if (*p == NULL) {
		// do something if failure
	}
}

void test3()
{
	char *str = NULL;
	getMemory3(&str, 100 );
	strcpy(str, "test3-hello world" ); 
	printf("%s\n", str);
}

void test4()
{ 
	char *str = (char *)malloc(100);
	strcpy(str, "test4-hello world");
	printf("%s\n", str);
	free(str);
	str = NULL;
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

	char s[]="This is string";
    char d[20];
    strcpy(d,s);
    printf("%s\n",d);

    // test1();
    test2();
    test3();
    test4();

    return 0;
}