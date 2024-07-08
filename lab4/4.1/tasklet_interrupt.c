#include <linux/init.h>
#include <linux/module.h>
#include <linux/interrupt.h>
#include <linux/delay.h>

MODULE_LICENSE("GPL");

//�����������ȶ�������������Ȼ�����ú�module_param�������ܲ���
static int irq;
static char *devname;
module_param(irq, int, 0644);
module_param(devname, charp, 0644);

static struct tasklet_struct my_tasklet;
//����������������tastlet����

//�жϵװ벿������
static void tasklet_handler(unsigned long data)//����tasklet ��������
{
    static int count = 0;
    printk(KERN_INFO "=== In tasklet_handler, count: %d\n", ++count);
//KERN_INFO������һ���ַ��������������Խ����ں˻ػ�����������Ϣ���й���

}
/*�жϴ���������һ�������Ǵ��������Ӧ���жϺţ���ӡ��־��Ϣ��ʾ����
�ڶ�����������һ��ͨ��ָ�룬�������жϴ������ע��ʱ���ݸ�request_iqrʱ������devһ��
*/
static irqreturn_t irq_handler(int irq, void *dev_id)
{
    printk(KERN_INFO "=== In irq_handler\n");
    tasklet_schedule(&my_tasklet);//���жϴ������������ж��°벿
    return IRQ_HANDLED;//������������ֵ����������ע�ᴦ�����ڼ����Դ������IRQ_NONE����֮����IRQ_HANDLED
}
//��̬��ʼ��tasklet
static int __init tasklet_interrupt_init(void)
{
    int req_ret;
    printk(KERN_INFO "=== Module starts...\n");
    tasklet_init(&my_tasklet, tasklet_handler, 0);    //��ʼ��tasklet
    req_ret = request_irq(irq, irq_handler, IRQF_SHARED, devname, &my_tasklet);//ע���жϴ�����
/*IRQF_SHARED�������ж��ǹ���ģ�һ���ж��߶�Ӧ��������ж�Դ�����ն˴���ʱ��Ҫִ�ж����ʶ����*/

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
/*��������ͨ������ free_irq()�������ϵͳע��һ���жϴ�������
void free_irq(unsigned int irq, void *dev_id);
����˵��:
irq: Ӳ���жϺ�;
dev_id: �жϳ����Ψһ��ʶ;*/
    printk(KERN_INFO "=== Module exits...\n");
    printk(KERN_INFO "=== %s request IRQ: %d leaving success...\n", devname, irq);
}

module_init(tasklet_interrupt_init);
module_exit(tasklet_interrupt_exit);
