#include <stdio.h>
#include <stdlib.h>
#include <math.h>
typedef unsigned long long int ULL;
#define MB_256 (ULL)(1024*1024*256)
#define KB_4 (ULL) (1024*4)
#define MB_1 (ULL) (1024*1024)
#define MB_4 (ULL) (1024*1024*4)

typedef struct Node* Nodeptr;
/* 0     32MB      64MB       128MB     256MB*/
/*   4KB      1MB         4MB      Others*/

void test(){
	printf("Good here!\n");
}



128M 64M 32M 16M 8M 4M 2M 1M 512KB 256KB 128KB 64KB 32KB 16KB 8KB 4KB 2KB 1KB 
2^20 * 2^7

/* for Area 4KB/Area 1MB/Area 4MB use first fit and normal algorithm */
/* for Area others use first fit and Buddy algorithm */

void* Head;
void* Head_4KB;
void* Head_1MB;
void* Head_4MB;
void* Head_others;

struct Node{
	int size;
	void* addr;
	struct Node* Next;
};

struct bit{
	int bitpart;
	Nodeptr bithead;
};

bit BuddyList[28];

Nodeptr LL_4KB;
Nodeptr LL_1MB;
Nodeptr LL_4MB;
Nodeptr LL_others;

void Init(){
	Head = malloc(1024*1024*256);
	Head_4KB = Head;
	Head_1MB = Head_4KB + 32*MB_1;
	Head_4MB = Head_4KB + 64*MB_1;
	Head_others = Head_4KB + 128*MB_1;
	LL_4KB = (Nodeptr)malloc(sizeof(struct Node));
	LL_1MB = (Nodeptr)malloc(sizeof(struct Node));
	LL_4MB = (Nodeptr)malloc(sizeof(struct Node));
	LL_others = (Nodeptr)malloc(sizeof(struct Node));
	LL_4KB->addr = Head_4KB;
	LL_4KB->size = 32*MB_1;
	LL_1MB->addr = Head_1MB;
	LL_1MB->size = 32*MB_1;
	LL_4MB->addr = Head_4MB;
	LL_4MB->size = 64*MB_1;
	LL_others->addr = Head_others;
	LL_others->size = 128*MB_1;
	BuddyList[27].bithead = LL_others; 
}

void* Buddy_malloc(){



}

void Buddy_free(){


}


void* emallochelp(int size,Nodeptr head){
	if (head -> size == size){
		Nodeptr tmp = head;
		head = head -> Next;
		void* addr = tmp->addr;
		free(tmp);
		return addr;
	}
	else if (head -> size < size ){
		printf("No enough space\n");
		return NULL;
	}
	head->addr += size;
	head->size -= size;
	return head->addr - size;
}

void Buddy(int size){
	int initial_size = 128*MB_1;
	while ( size < initial_size ){
		initial_size /= 2;
	}
	
}

void* emalloc(int size){
	if (size == KB_4 && LL_4KB->size != 0 ){
		emallochelp(KB_4,LL_4KB);
	}
	else if (size == MB_1 && LL_1MB->size != 0){
		emallochelp(MB_1,LL_1MB);
	}
	else if (size == MB_4 && LL_4MB->size != 0){
		emallochelp(MB_4,LL_4MB);
	}
	else{
		Nodeptr tmp = LL_others;
		while ( tmp ){
			if ( tmp->size >= size ){
				tmp->size -= size;
				tmp->addr += size;
				return tmp->addr - size;
			}
			tmp = tmp->Next;
		}
		printf("Fail to Malloc : No enough space\n");
		return NULL;
	}
	
}

Nodeptr efreehelp(void* addr,Nodeptr head, ULL size){

		Nodeptr tmp = head;
		if ( addr < tmp->addr ){
			if ( (ULL)(tmp->addr - addr) == size ){
				tmp->addr -= size;
				tmp->size += size;
			}
			else{
				Nodeptr n = (Nodeptr)(malloc (sizeof(struct Node)));
				n->size = size;
				n->addr = addr;
				n->Next = head;
				head = n;
			}
			return head;
		}
		do{
			if ( addr < tmp->Next->addr ){
				if ( addr - tmp->addr == tmp->size && tmp->Next->addr - addr == size){
					Nodeptr tmp2 = tmp -> Next;
					tmp -> size += size + tmp -> Next -> size;
					tmp -> Next = tmp -> Next -> Next;
					free(tmp2);
				}
				else if ( addr - tmp->addr == tmp->size ){
					tmp -> size += size;
				}
				else if ( tmp -> Next -> addr - addr == size ){
					tmp -> Next -> addr = addr;
					tmp -> Next -> size += size;
				}
				else{
					Nodeptr n = (Nodeptr)(malloc (sizeof(struct Node)));
					n -> size = size;
					n -> addr = addr;
					n -> Next = tmp->Next;
					tmp->Next = n;
				}
				return head;
			}
			tmp = tmp->Next;
		}while (tmp->Next);
		if ( addr - tmp->addr == tmp->size){
			tmp->size += size;
		}
		else{
			Nodeptr n = (Nodeptr)(malloc (sizeof(struct Node)));
			n->size = size;
			n->addr = addr;
			n->Next = NULL;
			tmp->Next = n;
		}
		return head;


}

void efree(void* addr){
	if ( addr >= Head_4KB && addr < Head_1MB ){
		LL_4KB = efreehelp(addr,LL_4KB,KB_4);
		return;
	}
	else if ( addr < Head_4MB ){
		LL_1MB = efreehelp(addr,LL_1MB,MB_1);
		return;
	}
	else if ( addr < Head_others ){
		LL_4MB = efreehelp(addr,LL_4MB,MB_4);
		return;
	}
	else if ( addr < Head_4KB + MB_256 ){
		LL_others = efreehelp(addr,LL_others,0);
		return;
	}
	else{
		printf("Invalid Free Operation\n");
	}

}

void helpshow(Nodeptr head){
	int i = 1;
	Nodeptr tmp = head;
	while(tmp){
		printf("Node%d: size:%d     \taddr:%p\n",i,tmp->size/1024,tmp->addr);
		tmp = tmp -> Next;
		i++;
	}
	printf("------------------------------------------------------\n\n");
}
void showLinkLists(){
	printf("4KB:\n");
	helpshow(LL_4KB);
	printf("1MB:\n");
	helpshow(LL_1MB);
	printf("4MB:\n");
	helpshow(LL_4MB);
	printf("others:\n");
	helpshow(LL_others);
}

void exithelp(Nodeptr head){
	Nodeptr tmp;
	while(head){
		tmp = head;
		head = head->Next;
		free(tmp);
	}
}
void Exit(){
	exithelp(LL_4KB);
	exithelp(LL_1MB);
	exithelp(LL_4MB);
	exithelp(LL_others);
	free(Head);
}

}

int main(){
	Init();
	/* Operations below */
	showLinkLists();
	int *p1 = (int *)emalloc(KB_4);
	int *p2 = (int *)emalloc(KB_4);
	int *p3 = (int *)emalloc(KB_4);
	showLinkLists();
	efree(p1);
	showLinkLists();
	/* Operations above */
	Exit();
	return 0;
}
