#ifndef EMALLOC_H
#define EMALLOC_H

#include <stddef.h>

void *emalloc(size_t size);
void efree(void *ptr);

#endif
