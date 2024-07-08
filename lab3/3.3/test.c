#include "emalloc.h"
#include <stdio.h>

int main() {
    int i, j;
    int ***arrays = emalloc(50 * sizeof(int **));
//开辟一个长度为50，存储类型为指向int的二级指针的内存

    for (i = 0; i < 50; i++) {
        arrays[i] = emalloc(50 * sizeof(int *));//分配内存
        for (j = 0; j < 50; j++) {
            arrays[i][j] = emalloc(sizeof(int));
            *arrays[i][j] = i * j;
            printf("arrays[%d][%d] is OK.\n", i, j);
        }
    }

    return 0;
}
