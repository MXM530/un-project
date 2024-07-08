CC=gcc
CFLAGS=-Wall -Wextra -Werror -std=c11

all:test

test:test.o emalloc.o
	$(CC) $(CFLAGS) test.o emalloc.o -o test

test.o:test.c emalloc.h
	$(CC) $(CFLAGS) -c test.c

emalloc.o:emalloc.c emalloc.h
	$(CC) $(CFLAGS) -c emalloc.c

clean:
	rm -f test *.o
