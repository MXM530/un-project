#include <stdio.h>
#include <stdlib.h>
#include <string.h>
typedef struct node
{
    char name[20];    /*���̵�����*/
    int prio;     /*���̵����ȼ�*/
    int round;     /*����CPU��ʱ��Ƭ*/
    int cputime;    /*CPUִ��ʱ��*/
    int needtime;    /*����ִ������Ҫ��ʱ��*/
    char state;     /*���̵�״̬��W--����̬��R--ִ��̬��F--���̬*/
    int count;     /*��¼ִ�еĴ���*/
    struct node *next;   /*����ָ��*/
}PCB;
/*�����������У��������У�ִ�ж��к���ɶ���*/
PCB *ready=NULL;
PCB*run=NULL;
PCB*finish=NULL;
int num;

void GetFirst()  /*ȡ�õ�һ���������нڵ�*/
{
    run = ready;
    if(ready!=NULL)
    {
        run ->state = 'R';
        ready = ready ->next;
        run ->next = NULL;
    }
}

void Output()    /*���������Ϣ*/
{
      PCB *p;
      /*p = ready;*/
      printf("----��������----\n");
      printf("������\t���ȼ�\tʱ��Ƭ\tcpuʱ��\tneedʱ��\t����״̬\t������\n");
       p = ready;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
      printf("\n");
       printf("----��ɶ���----\n");
       printf("������\t���ȼ�\tʱ��Ƭ\tcpuʱ��\tneedʱ��\t����״̬\t������\n");
      p = finish;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
        printf("\n");
       printf("----���ж���----\n");
       printf("������\t���ȼ�\tʱ��Ƭ\tcpuʱ��\tneedʱ��\t����״̬\t������\n");
      p = run;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }

}
void InsertPrio(PCB *in) /*�������ȼ����У��涨������ԽС�����ȼ�Խ��*/
{
      PCB *fst,*nxt;
      fst = nxt = ready;

      if(ready == NULL)  /*�������Ϊ�գ���Ϊ��һ��Ԫ��*/
      {
        in->next = ready;
        ready = in;
      }
      else     /*�鵽���ʵ�λ�ý��в���*/
      {
        if(in ->prio >= fst ->prio)  /*�ȵ�һ����Ҫ��(���ڵ���)������뵽��ͷ*/
        {
          in->next = ready;
          ready = in;
        }
        else
        {
          while(fst->next != NULL)  /*�ƶ�ָ����ҵ�һ������С��Ԫ�ص�λ�ý��в���*/
          {
            nxt = fst;
            fst = fst->next;
          }

          if(fst ->next == NULL) /*�Ѿ���������β���������ȼ�����С��������뵽��β����*/
          {
            in ->next = fst ->next;
            fst ->next = in;
          }
          else     /*���뵽������*/
          {
            nxt = in;
            in ->next = fst;
          }
        }
      }
    }
void InsertWait(PCB *in)  /*�����̲��뵽��������β��*/
{
      PCB *fst;
      fst = ready;

      if(ready == NULL)
      {
        in->next = ready;
        ready = in;
      }
      else
      {
        while(fst->next != NULL)
        {
          fst = fst->next;
        }
        in ->next = fst ->next;
        fst ->next = in;
      }
    }
void InsertFinish(PCB *in)  /*�����̲��뵽��ɶ���β��*/
{
      PCB *fst;
      fst = finish;

      if(finish == NULL)
      {
        in->next = finish;
        finish = in;
      }
      else
      {
        while(fst->next != NULL)
        {
          fst = fst->next;
        }
        in ->next = fst ->next;
        fst ->next = in;
      }
}

void TimeCreate() /*ʱ��Ƭ���뺯��*/
{
      PCB *tmp;
      int i;
      printf("����������ֺͽ���ʱ��Ƭ����ʱ�䣺\n");
      for(i = 0;i < num; i++)
      {
        if((tmp = (PCB *)malloc(sizeof(PCB)))==NULL)
        {
          perror("malloc");
          exit(1);
        }
        scanf("%s",tmp->name);
        getchar();
        scanf("%d",&(tmp->needtime));

        tmp ->cputime = 0;
        tmp ->state ='W';
        tmp ->prio = 0;
        tmp ->round = 2;  /*����ÿ�������������ʱ��Ƭ��2*/
        tmp ->count = 0;
        InsertWait(tmp);
      }
}
void RoundRun()    /*ʱ��Ƭ��ת�����㷨*/
{

      int flag = 1;
      GetFirst();
      while(run != NULL)
      {
        Output();
        while(flag)
        {
          run->count++;
          run->cputime++;
          run->needtime--;
          if(run->needtime == 0) /*����ִ�����*/
          {
            run ->state = 'F';
            InsertFinish(run);
            flag = 0;
          }
          else if(run->count == run->round)/*ʱ��Ƭ����*/
          {
            run->state = 'W';
            run->count = 0;   /*���������㣬Ϊ�´���׼��*/
            InsertWait(run);
            flag = 0;
          }
        }
        flag = 1;
        GetFirst();
      }
}


int main(void)
{
    char chose;
    printf("������Ҫ�����Ľ�����Ŀ:\n");
    scanf("%d",&num);
    getchar();
    TimeCreate();
    RoundRun();

    Output();
    return 0;
}
