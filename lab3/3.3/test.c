#include "emalloc.h"
#include <stdio.h>

int main() {
    int i, j;
    int ***arrays = emalloc(50 * sizeof(int **));
//����һ������Ϊ50���洢����Ϊָ��int�Ķ���ָ����ڴ�

    for (i = 0; i < 50; i++) {
        arrays[i] = emalloc(50 * sizeof(int *));//�����ڴ�
        for (j = 0; j < 50; j++) {
            arrays[i][j] = emalloc(sizeof(int));
            *arrays[i][j] = i * j;
            printf("arrays[%d][%d] is OK.\n", i, j);
        }
    }

    return 0;
}
