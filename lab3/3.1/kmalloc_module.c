#include <linux/init.h>
#include <linux/module.h>
#include <linux/slab.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Your Name");

static char *kmalloc_mem1;
static char *kmalloc_mem2;

static int __init kmalloc_init(void)
{
    printk(KERN_INFO "Start kmalloc!\n");
    kmalloc_mem1 = kmalloc(1024, GFP_KERNEL);//1kb 
    printk(KERN_INFO "kmalloc_mem1 addr = %px\n", kmalloc_mem1);
    
    kmalloc_mem2 = kmalloc(8192, GFP_KERNEL);//8kb 
    printk(KERN_INFO "kmalloc_mem2 addr = %px\n", kmalloc_mem2);

    return 0;
}

static void __exit kmalloc_exit(void)
{
    kfree(kmalloc_mem1);
    kfree(kmalloc_mem2);
    printk(KERN_INFO "Exit kmalloc!\n");
}

module_init(kmalloc_init);
module_exit(kmalloc_exit);
