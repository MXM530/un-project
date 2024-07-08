/*
����1

#include <stdio.h>
#include <stdlib.h>


typedef struct node
{
    char name[10];     //���̱�ʶ��
    int prio;          //����������
    int round;         //����ʱ��Ƭ��תʱ��Ƭ
    int cputime;       //����ռ��CPUʱ��
    int needtime;      //���̵���ɻ���Ҫ��ʱ��
    int count;         //������
    char state;        //���̵�״̬
    struct node *next; //��ָ��
} PCB, *Process;
/*
//��ʼ��PCB
Process initPCB(int time)
{
    Process process = (Process)malloc(sizeof(PCB));
    printf("��������̵ı�ʶ�������ȼ�����������ʱ��\n");
    scanf("%s %d", process->name,   process->prio,&(process->needtime));
    process->cputime = 0;
    process->round = time;
    process->state = 'R';
    process->count = 0;
    process->next = NULL;
    return process;
}
// ��ʼ�����̶���
Process initProcessQueue()
{
    Process process = (Process)malloc(sizeof(PCB));
    process->next = NULL;
    return process;
}
//��Ӳ������������ȼ��Ĵ�С�����,�����ڲ�������
void insertProcess(Process p, Process ready1)
{
    Process ready = ready1;

    //�����������Ϊ��ֱ�Ӳ���
    if (!(ready->next))
    {
        ready->next = p;
    }
    else
    {
        //��������,������,temp������¼�Ƿ�����β
        int temp = 0;
        while (ready->next)
        {

            if (p->prio > ready->next->prio)
            {
                p->next = ready->next;
                ready->next = p;
                temp = 1;
                break;
            }
            else
            {
                ready = ready->next;
            }
        }
        //�����е����ȼ���С�������β
        if (!temp)
        {
            ready->next = p;
        }
    }
}

//���Ӳ���������Ԫ��㿪ʼ����
Process deleteProcess(Process p)
{
    if (!p->next)
    {
        return p;
    }
    Process q = p->next;
    p->next = q->next;
    q->next = NULL;
    return q;
}

//��̬���ȼ����ӣ��Զ������������ȼ���̬����
void addPriority(Process p, int priority)
{
    Process q = p;
    while (q->next)
    {
        q->next->prio -= priority;
        q = q->next;
    }
}
//��ӡ����
void Output(Process p)
{

    printf("%-10s", p->name);
    printf("%-10d", p->round);
    printf("%-10d", p->cputime);
    printf("%-10d", p->needtime);
    printf("%-10d", p->prio);
    printf("%-10c", p->state);
    printf("%-10d", p->count);
    printf("\n");
}
//��ӡ����
void Print(Process p)
{
    if (!p->next)
    {
        printf("����Ϊ��\n");
        return;
    }
    printf("���̺�   ʱ��Ƭ   cpuʱ��  ����ʱ��   ������    ״̬    ִ�д���\n");
    while (p->next)
    {
        Output(p->next);
        p = p->next;
    }
}

void runProcess(Process process)
{
    //���н��̣���Ӧ�Ĳ����ı�
    process->state = 'r';
    process->needtime -= process->round;
    process->cputime += process->round;
    process->prio -= 1; //���еĽ������ȼ���5
    process->count += 1;
}

int main()
{
    int round; //ʱ��Ƭ
    printf("������ʱ��Ƭ�Ĵ�С\n");
    scanf("%d", &round);
    //�������������У���ɣ�3������
    Process run = initProcessQueue();
    Process ready = initProcessQueue();
    Process finish = initProcessQueue();
    //�������̣�����ʼ���������������
    printf("��������Ҫ�����Ľ�������\n");
    int count; //������
    scanf("%d", &count);
    for (int i = 0; i < count; i++)
    {
        Process process = initPCB(round);
        insertProcess(process, ready);
    }
    //����״̬��ֱ����������Ϊ�գ���ֹͣ
    while (ready->next)
    {
        if (!run->next)
        {
            Process q = deleteProcess(ready); //�������г���
            insertProcess(q, run);            //�������ж���
            runProcess(q);
            addPriority(ready, 1);  //�������������ȼ���2
            q = deleteProcess(run); //�����ж��г���
            if (q->needtime <= 0)   //�ж�����ʱ���Ƿ�С��0
            {

                q->cputime += q->needtime;
                q->needtime = 0;
                q->state = 'F';
                insertProcess(q, finish); //С�ڵ��ڼ�����ɶ��У����ı���Ӧ�Ĳ���
            }
            else
            {
                q->state = 'R';
                insertProcess(q, ready); //���ڼ��������������
            }
        }
        //ÿִ��һ�δ�ӡÿ�����У��۲�ִ�����
        printf("************************************************************************");
        printf("\n");
        printf("��ɶ��У�\n");
        Print(finish);
        printf("\n");
        printf("�������У�\n");
        Print(ready);
        printf("\n");
        printf("���ж��У�\n");
        Print(run);
        printf("\n");
    }

    return 0;
}
*/

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
PCB *ready=NULL,*run=NULL,*finish=NULL; /*�����������У��������У�ִ�ж��к���ɶ���*/
int num;
void GetFirst()   /*�Ӿ�������ȡ�õ�һ���ڵ�*/
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
      printf("������\t���ȼ�\tʱ��Ƭ\tcpuʱ��\tneedʱ��\t����״̬\t������\n");
      printf("----��������----\n");
       p = ready;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
       printf("----��ɶ���----\n");
      p = finish;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
       printf("----���ж���----\n");
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
void InsertTime(PCB *in)  /*�����̲��뵽��������β��*/
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
void PrioCreate() /*���ȼ��������뺯��*/
{
      PCB *tmp;
      int i;

      printf("����������ֺͽ�������ʱ�䣺\n");
      for(i = 0;i < num; i++)
      {
        if((tmp = (PCB *)malloc(sizeof(PCB)))==NULL)
        {
          perror("malloc");
          exit(1);
        }
        scanf("%s",tmp->name);
        getchar();    /*���ջس�����*/
        scanf("%d",&(tmp->needtime));
        tmp ->cputime = 0;
        tmp ->state ='W';
        tmp ->prio = 50 - tmp->needtime;  /*���������ȼ�����Ҫ��ʱ��Խ�࣬���ȼ�Խ��*/
        tmp ->count = 0;
        InsertPrio(tmp);      /*�������ȼ��Ӹߵ��ͣ����뵽��������*/
      }
}
 void Priority()   /*�������ȼ����ȣ�ÿ��ִ��һ��ʱ��Ƭ*/
{
      int flag = 1;
      GetFirst();
      while(run != NULL)  /*���������в�Ϊ��ʱ������Ƚ�����ִ�ж���ִ��*/
      {
        Output();  /*���ÿ�ε��ȹ����и����ڵ��״̬*/
        while(flag)
        {
          run->prio -= 3; /*���ȼ���ȥ��,����Ϊ0�����ȼ�����*/
          run->cputime++; /*CPUʱ��Ƭ��һ*/
          run->needtime--;/*����ִ����ɵ�ʣ��ʱ���һ*/
          if(run->needtime == 0)/*�������ִ����ϣ�������״̬��ΪF��������뵽��ɶ���*/
          {
            run ->state = 'F';
            run->count++; /*����ִ�еĴ�����һ*/
            InsertFinish(run);
            flag = 0;
          }
          else   /*������״̬��ΪW�����������*/
          {
            run->state = 'W';
            run->count++; /*����ִ�еĴ�����һ*/
            InsertTime(run);
            flag = 0;
          }
        }
        flag = 1;
        GetFirst();    /*����ȡ�������ж�ͷ���̽���ִ�ж���*/
      }
}

int main(void)
{
      char chose;
      printf("������Ҫ�����Ľ�����Ŀ:\n");
      scanf("%d",&num);
      getchar();

      PrioCreate();
      Priority();

      Output();
      return 0;

}

