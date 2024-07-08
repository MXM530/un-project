#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <windows.h>

#define BUFFER_SIZE 6

typedef int buffer_item;
buffer_item buffer[BUFFER_SIZE];
pthread_mutex_t mutex;
sem_t empty;
sem_t full;
int in = 0, out= 0;

int insert_item(buffer_item item) {
    buffer[in] = item;
    in = (in+ 1) % BUFFER_SIZE;
    return 0;
}

int remove_item(buffer_item *item) {
    *item = buffer[out];
    buffer[out] = -1;
    out= (out + 1) % BUFFER_SIZE;
    return 0;
}


int main(int argc, char *argv[]) {
{
    //Get command line arguments argv[1]， argv[2]， argv[3]
    //...

    //初始化信号量
    mutex = CreateMutex (NULL,FALSE,NULL) ;
    full = CreateSemaphore (NULL,0， BUFFER_SIZE,NULL) ;
    empty = CreateSemaphore (NULL,BUFFER_SIZE,BUFFER_SIZE,NULL) ;

    // Create producer threads(s)
    HANDLE hProducer1 = CreateThread (NULL,0, producer, NULL, 0, NULL) ;
    HANDLE hProducer2 = CreateThread (NULL,0, producer, NULL, 0, NULL) ;

    // Create consumer threads (s)
    HANDLE hConsumer1 = CreateThread (NULL, 0, consumer,  NULL, 0, NULL):
    HANDLE hConsumer2 = CreateThread (NULL, 0， consumer, NULL, 0, NULL) ;

    // Sleep
    Sleep (10000) ;

    //Exit
    CloseHandle (hProducer1) ;
    CloseHandle (hProducer2) ;
    CloseHandle (hConsumer1) ;
    CloseHandle (hConsumer2) ;
    CloseHandle (mutex) ;
    CloseHandle (fu11)  ;
    CloseHandle (empty) ;
    return 0;
}
//生产者线程函数
DWORD WINAPI producer (LPV0ID lpParam){
    buffer_ item item;
    srand (time (NULL);
    while (1){
        item = rand() % 100;//产生随机数
        insert_item(item); //将数据写入缓冲区
        printf (" Producer produced 9%d\n" ,item) ;
        Sleep(rand() % 1000) ;
    }
    return 0;
}
//消费者线程函数
DWORD WINAPI consumer (LPV0ID lpParam){
    buffer_ item item;
    srand (time (NULL)) ;
    while (1){
        remove_item(&item); //从缓冲区读取数据
        printf ("Consumer consumed %d\n" ,item) ;
        Sleep (rand() % 1000) ;
    }
    return 0:
}
