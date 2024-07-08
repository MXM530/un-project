#include <linux/init.h>
#include <linux/module.h>
#include <linux/interrupt.h>
#include <linux/delay.h>

MODULE_LICENSE("GPL");

//下面的语句首先定义两个参数，然后利用宏module_param宏来接受参数
static int irq;
static char *devname;
module_param(irq, int, 0644);
module_param(devname, charp, 0644);

static struct tasklet_struct my_tasklet;
//在驱动程序中声明tastlet对象

//中断底半部处理函数
static void tasklet_handler(unsigned long data)//定义tasklet 工作函数
{
    static int count = 0;
    printk(KERN_INFO "=== In tasklet_handler, count: %d\n", ++count);
//KERN_INFO符号是一个字符串，可以用来对进入内核回环缓冲区的信息进行过滤

}
/*中断处理函数，第一个参数是处理程序响应的中断号（打印日志信息显示），
第二个参数是是一个通用指针，他在与中断处理程序注册时传递给request_iqr时产生的dev一致
*/
static irqreturn_t irq_handler(int irq, void *dev_id)
{
    printk(KERN_INFO "=== In irq_handler\n");
    tasklet_schedule(&my_tasklet);//在中断处理函数中启动中断下半部
    return IRQ_HANDLED;//返回两个特殊值：当不是在注册处理函数期间产生源，返回IRQ_NONE，反之返回IRQ_HANDLED
}
//动态初始化tasklet
static int __init tasklet_interrupt_init(void)
{
    int req_ret;
    printk(KERN_INFO "=== Module starts...\n");
    tasklet_init(&my_tasklet, tasklet_handler, 0);    //初始化tasklet
    req_ret = request_irq(irq, irq_handler, IRQF_SHARED, devname, &my_tasklet);//注册中断处理函数
/*IRQF_SHARED表明该中断是共享的，一个中断线对应多个外设中断源，在终端处理时需要执行额外的识别工作*/

    printk(KERN_INFO "=== req_ret is %d\n", req_ret);
    if (!req_ret)
    {
        printk(KERN_INFO "=== %s request IRQ: %d success...\n", devname, irq);
    }
    return 0;
}

static void __exit tasklet_interrupt_exit(void)
{
    free_irq(irq, &my_tasklet);
/*驱动程序通过函数 free_irq()，向操作系统注销一个中断处理函数：
void free_irq(unsigned int irq, void *dev_id);
参数说明:
irq: 硬件中断号;
dev_id: 中断程序的唯一标识;*/
    printk(KERN_INFO "=== Module exits...\n");
    printk(KERN_INFO "=== %s request IRQ: %d leaving success...\n", devname, irq);
}

module_init(tasklet_interrupt_init);
module_exit(tasklet_interrupt_exit);
