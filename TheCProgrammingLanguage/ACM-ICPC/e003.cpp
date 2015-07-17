#include <iostream>

using namespace std;


#define MAXSIZE 10
typedef struct
{
	int r[MAXSIZE+1];
	int length;
}SqList;

void swap(SqList *L, int i, int j)
{
	int temp = L->r[i];
	L->r[i] = L->r[j];
	L->r[j] = temp;
}

void quickSort(SqList *L)
{
	qSort(L, 1, L->length);
}

void qSort(SqList *L, int low, int high)
{
	int pivot;
	if (low < high)
	{
		pivot = partition(L, low, high);
		qSort(L, low, pivot - 1);
		qSort(L, pivot + 1, high);
	}
}

int partition(SqList *L, int low, int high)
{
	int pivotKey;
	pivotKey = L->r[low];
	while (low < high) {
		while (low < high && L->r[high] >= pivotKey) {
			high--;
		}
		swap(L, low, high);
		while (low < high && L->r[low] <= pivotKey) {
			low++;
		}
		swap(L, low, high);
	}

	return low;
}

int main()
{
	return 0;
}
