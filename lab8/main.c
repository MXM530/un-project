/*
代码1

#include <stdio.h>
#include <stdlib.h>


typedef struct node
{
    char name[10];     //进程标识符
    int prio;          //进程优先数
    int round;         //进程时间片轮转时间片
    int cputime;       //进程占用CPU时间
    int needtime;      //进程到完成还需要的时间
    int count;         //计数器
    char state;        //进程的状态
    struct node *next; //链指针
} PCB, *Process;
/*
//初始化PCB
Process initPCB(int time)
{
    Process process = (Process)malloc(sizeof(PCB));
    printf("请输入进程的标识符、优先级、运行所需时间\n");
    scanf("%s %d", process->name,   process->prio,&(process->needtime));
    process->cputime = 0;
    process->round = time;
    process->state = 'R';
    process->count = 0;
    process->next = NULL;
    return process;
}
// 初始化进程队列
Process initProcessQueue()
{
    Process process = (Process)malloc(sizeof(PCB));
    process->next = NULL;
    return process;
}
//入队操作，按照优先级的大小来入队,类似于插入排序
void insertProcess(Process p, Process ready1)
{
    Process ready = ready1;

    //如果就绪队列为空直接插入
    if (!(ready->next))
    {
        ready->next = p;
    }
    else
    {
        //遍历链表,并插入,temp用来记录是否插入队尾
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
        //比所有的优先级都小，插入队尾
        if (!temp)
        {
            ready->next = p;
        }
    }
}

//出队操作，从首元结点开始出队
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

//动态优先级增加，对队列中所有优先级动态增加
void addPriority(Process p, int priority)
{
    Process q = p;
    while (q->next)
    {
        q->next->prio -= priority;
        q = q->next;
    }
}
//打印函数
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
//打印函数
void Print(Process p)
{
    if (!p->next)
    {
        printf("队列为空\n");
        return;
    }
    printf("进程号   时间片   cpu时间  所需时间   优先数    状态    执行次数\n");
    while (p->next)
    {
        Output(p->next);
        p = p->next;
    }
}

void runProcess(Process process)
{
    //运行进程，相应的参数改变
    process->state = 'r';
    process->needtime -= process->round;
    process->cputime += process->round;
    process->prio -= 1; //运行的进程优先级减5
    process->count += 1;
}

int main()
{
    int round; //时间片
    printf("请输入时间片的大小\n");
    scanf("%d", &round);
    //创建就绪，运行，完成，3个队列
    Process run = initProcessQueue();
    Process ready = initProcessQueue();
    Process finish = initProcessQueue();
    //创建进程，并初始化，加入就绪队列
    printf("请输入你要创建的进程数量\n");
    int count; //进程数
    scanf("%d", &count);
    for (int i = 0; i < count; i++)
    {
        Process process = initPCB(round);
        insertProcess(process, ready);
    }
    //运行状态，直到就绪队列为空，才停止
    while (ready->next)
    {
        if (!run->next)
        {
            Process q = deleteProcess(ready); //就绪队列出队
            insertProcess(q, run);            //加入运行队列
            runProcess(q);
            addPriority(ready, 1);  //就绪队列中优先级加2
            q = deleteProcess(run); //从运行队列出队
            if (q->needtime <= 0)   //判断运行时间是否小于0
            {

                q->cputime += q->needtime;
                q->needtime = 0;
                q->state = 'F';
                insertProcess(q, finish); //小于等于加入完成队列，并改变相应的参数
            }
            else
            {
                q->state = 'R';
                insertProcess(q, ready); //大于继续加入就绪队列
            }
        }
        //每执行一次打印每个队列，观察执行情况
        printf("************************************************************************");
        printf("\n");
        printf("完成队列：\n");
        Print(finish);
        printf("\n");
        printf("就绪队列：\n");
        Print(ready);
        printf("\n");
        printf("运行队列：\n");
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
    char name[20];    /*进程的名字*/
    int prio;     /*进程的优先级*/
    int round;     /*分配CPU的时间片*/
    int cputime;    /*CPU执行时间*/
    int needtime;    /*进程执行所需要的时间*/
    char state;     /*进程的状态，W--就绪态，R--执行态，F--完成态*/
    int count;     /*记录执行的次数*/
    struct node *next;   /*链表指针*/
}PCB;
PCB *ready=NULL,*run=NULL,*finish=NULL; /*定义三个队列，就绪队列，执行队列和完成队列*/
int num;
void GetFirst()   /*从就绪队列取得第一个节点*/
{
    run = ready;
    if(ready!=NULL)
    {
        run ->state = 'R';
        ready = ready ->next;
        run ->next = NULL;
    }
}

void Output()    /*输出队列信息*/
{
      PCB *p;
      /*p = ready;*/
      printf("进程名\t优先级\t时间片\tcpu时间\tneed时间\t进程状态\t计数器\n");
      printf("----就绪队列----\n");
       p = ready;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
       printf("----完成队列----\n");
      p = finish;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }
       printf("----运行队列----\n");
      p = run;
      while(p!=NULL)
      {
        printf("%s\t%d\t%d\t%d\t%d\t\t%c\t\t%d\n",p->name,p->prio,p->round,p->cputime,p->needtime,p->state,p->count);
        p = p->next;
      }

}
void InsertPrio(PCB *in) /*创建优先级队列，规定优先数越小，优先级越低*/
{
      PCB *fst,*nxt;
      fst = nxt = ready;

      if(ready == NULL)  /*如果队列为空，则为第一个元素*/
      {
        in->next = ready;
        ready = in;
      }
      else     /*查到合适的位置进行插入*/
      {
        if(in ->prio >= fst ->prio)  /*比第一个还要大(大于等于)，则插入到队头*/
        {
          in->next = ready;
          ready = in;
        }
        else
        {
          while(fst->next != NULL)  /*移动指针查找第一个别它小的元素的位置进行插入*/
          {
            nxt = fst;
            fst = fst->next;
          }

          if(fst ->next == NULL) /*已经搜索到队尾，则其优先级数最小，将其插入到队尾即可*/
          {
            in ->next = fst ->next;
            fst ->next = in;
          }
          else     /*插入到队列中*/
          {
            nxt = in;
            in ->next = fst;
          }
        }
      }
}
void InsertTime(PCB *in)  /*将进程插入到就绪队列尾部*/
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
void InsertFinish(PCB *in)  /*将进程插入到完成队列尾部*/
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
void PrioCreate() /*优先级调度输入函数*/
{
      PCB *tmp;
      int i;

      printf("输入进程名字和进程所需时间：\n");
      for(i = 0;i < num; i++)
      {
        if((tmp = (PCB *)malloc(sizeof(PCB)))==NULL)
        {
          perror("malloc");
          exit(1);
        }
        scanf("%s",tmp->name);
        getchar();    /*吸收回车符号*/
        scanf("%d",&(tmp->needtime));
        tmp ->cputime = 0;
        tmp ->state ='W';
        tmp ->prio = 50 - tmp->needtime;  /*设置其优先级，需要的时间越多，优先级越低*/
        tmp ->count = 0;
        InsertPrio(tmp);      /*按照优先级从高到低，插入到就绪队列*/
      }
}
 void Priority()   /*按照优先级调度，每次执行一个时间片*/
{
      int flag = 1;
      GetFirst();
      while(run != NULL)  /*当就绪队列不为空时，则调度进程如执行队列执行*/
      {
        Output();  /*输出每次调度过程中各个节点的状态*/
        while(flag)
        {
          run->prio -= 3; /*优先级减去三,若设为0则优先级不变*/
          run->cputime++; /*CPU时间片加一*/
          run->needtime--;/*进程执行完成的剩余时间减一*/
          if(run->needtime == 0)/*如果进程执行完毕，将进程状态置为F，将其插入到完成队列*/
          {
            run ->state = 'F';
            run->count++; /*进程执行的次数加一*/
            InsertFinish(run);
            flag = 0;
          }
          else   /*将进程状态置为W，入就绪队列*/
          {
            run->state = 'W';
            run->count++; /*进程执行的次数加一*/
            InsertTime(run);
            flag = 0;
          }
        }
        flag = 1;
        GetFirst();    /*继续取就绪队列队头进程进入执行队列*/
      }
}

int main(void)
{
      char chose;
      printf("请输入要创建的进程数目:\n");
      scanf("%d",&num);
      getchar();

      PrioCreate();
      Priority();

      Output();
      return 0;

}

