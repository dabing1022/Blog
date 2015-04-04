#include <iostream>

using namespace std;


#define MAXSIZE 10
typedef struct
{
	int r[MAXSIZE];
	int length;
}SqList;

void swap(SqList *L, int i, int j) {
	int temp = L->r[i];
	L->r[i] = L->r[j];
	L->r[j] = temp;
}

void bubbleSort0(SqList* L)
{
	int i, j;
	for (i = 0; i < L->length; i++) {
		for (j = i + 1; j <= L->length; j++) {
			if (L->r[i] > L->r[j]) {
				swap(L, i, j);
			}
		}
	}
}

int main() {
	SqList* L0;
	L0->r[0] = 10;
	L0->r[1] = 5;
	L0->length = 2;
	bubbleSort0(L0);
	for (int i = 0; i < L0->length; i++) {
		cout << L0->r[i] << " ";
	}
	
	return 0;
}

