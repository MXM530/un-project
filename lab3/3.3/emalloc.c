#include "emalloc.h"
#include <stdlib.h>
#include <stdio.h>

#define MEMORY_POOL_SIZE (256 * 1024 * 1024)//�ڴ�س�ʼΪ256MB

static char memory_pool[MEMORY_POOL_SIZE];//�ڴ�������ڴ�ռ�ʱ���ȵ��ڴ���в��Һ��ʵ��ڴ��

void *emalloc(size_t size) {
    static char *free_pointer = memory_pool;//ָ���ڴ�ص��׵�ַ
    char *result = NULL;

    if (size <= 0) {//����ʧ��
        return NULL;
    }

    if (free_pointer + size > memory_pool + MEMORY_POOL_SIZE) {//������ڴ�ռ�����ڴ���С
        printf("Out of memory!\n");
        return NULL;
    }

    result = free_pointer;
    free_pointer += size;//����size��С���ڴ�ռ�

    return result;
}

void efree(void *ptr) {//�ͷ��ڴ��
    (void)ptr; // In this simple example, we do not implement the free operation.
}
