#include "emalloc.h"
#include <stdlib.h>
#include <stdio.h>

#define MEMORY_POOL_SIZE (256 * 1024 * 1024)//内存池初始为256MB

static char memory_pool[MEMORY_POOL_SIZE];//内存池申请内存空间时，先到内存池中查找合适的内存块

void *emalloc(size_t size) {
    static char *free_pointer = memory_pool;//指向内存池的首地址
    char *result = NULL;

    if (size <= 0) {//分配失败
        return NULL;
    }

    if (free_pointer + size > memory_pool + MEMORY_POOL_SIZE) {//申请的内存空间大于内存块大小
        printf("Out of memory!\n");
        return NULL;
    }

    result = free_pointer;
    free_pointer += size;//分配size大小的内存空间

    return result;
}

void efree(void *ptr) {//释放内存块
    (void)ptr; // In this simple example, we do not implement the free operation.
}
