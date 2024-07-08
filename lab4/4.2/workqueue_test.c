#include <linux/init.h>
#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/workqueue.h>
#include <linux/slab.h>
#include <linux/time.h>
#include <linux/rtc.h>
#include <linux/delay.h>

MODULE_LICENSE("GPL");

static struct workqueue_struct *wq;
static struct delayed_work dwork;
static struct work_struct immediate_task;
static int times=5;
module_param(times,int,0644);

static void print_date(struct work_struct *work)
{
    ktime_t tv;
    struct rtc_time tmm;
    tv=ktime_get_real();
    tmm=rtc_ktime_to_tm(tv);
    printk("%d :\n",5-times);
    printk("%04d-%02d-%02d %02d:%02d:%02d\n",
           tmm.tm_year + 1900, tmm.tm_mon + 1, tmm.tm_mday,
           tmm.tm_hour+8, tmm.tm_min, tmm.tm_sec);

}
static void print_date_de(struct work_struct *work)
{
    ktime_t tv;
    struct rtc_time tmm;
    tv=ktime_get_real();
    tmm=rtc_ktime_to_tm(tv);
    printk("this is a delay work %04d-%02d-%02d %02d:%02d:%02d\n",
           tmm.tm_year + 1900, tmm.tm_mon + 1, tmm.tm_mday,
           tmm.tm_hour+8, tmm.tm_min, tmm.tm_sec);
}

static int __init my_module_init(void)
{
    wq = create_singlethread_workqueue("my_wq"); //创建工作队列
    INIT_WORK(&immediate_task, print_date);
    queue_work(wq,&immediate_task);
    INIT_DELAYED_WORK(&dwork, print_date_de);
    queue_delayed_work(wq, &dwork,msecs_to_jiffies(10000));
    while(times>1){
        msleep(5000);
        queue_work(wq, &immediate_task);
        times=times-1;
    }
    return 0;

}

static void __exit my_module_exit(void)
{
    cancel_delayed_work(&dwork);
    flush_workqueue(wq);
    destroy_workqueue(wq);
    //最后销毁工作队列
    printk("unloading OK\n");
}

module_init(my_module_init);
module_exit(my_module_exit);
