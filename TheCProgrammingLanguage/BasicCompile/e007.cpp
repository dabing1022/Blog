#include <iostream>

using namespace std;

// 二叉排序树

typedef struct BiTNode
{
	int data;
	struct BiTNode *lchild, *rchild;
}BiTNode, *BiTree;


// f: T's father
// p: search result position
bool searchBST(BiTree T, int key, BiTree f, BiTree *p)
{
	if (!T)
	{
		*p = f;
		return false;
	}
	else if (key == T->data)
	{
		*p = T;
		return true;
	}
	else if (key < T->data)
	{
		return searchBST(T->lchild, key, T, p);
	}
	else 
	{
		return searchBST(T->rchild, key, T, p);
	}
}

bool insertBST(BiTree *T, int key)
{
	BiTree p, s;
	if (!searchBST(*T, key, NULL, &p))
	{
		s = (BiTree)malloc(sizeof(BiTNode));
		s->data = key;
		s->lchild = s->rchild = NULL;
		if (!p)
		{
			*T = s;
		}
		else if (key < p->data)
		{
			p->lchild = s;
		}
		else
		{
			p->rchild = s;
		}
		return true;
	}
	else 
	{
		return false;
	}
}

int main()
{
	int i = 0;
	int a[10] = {62, 88, 58, 47, 35, 33, 20, 100, 28, 66};
	BiTree t = NULL;
	for (i = 0; i < 10; i++)
	{
		insertBST(&t, a[i]);
	}

	BiTree p;
	if (searchBST(t, 62, NULL, &p))
	{
		cout << "find 20" << endl;
	}
	else 
	{
		cout << "not found 20" << endl;
	}

	return 0;
}