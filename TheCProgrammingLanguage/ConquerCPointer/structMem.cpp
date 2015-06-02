#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct student
{
	char *name;
	int score;
}stu, *pstu;

int main()
{
	pstu = (struct student*)malloc(sizeof(struct student));
	strcpy(pstu->name,"Jimy");
	pstu->score = 99;
	free(pstu);

	return 0;
}
