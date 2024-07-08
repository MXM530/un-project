#include <linux/init.h>
#include <linux/module.h>
#include <linux/vmalloc.h>

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Your Name");

static char *vmalloc_mem1;
static char *vmalloc_mem2;
static char *vmalloc_mem3;

static int __init my_vmalloc_init(void)
{
    printk(KERN_INFO "Start vmalloc!\n");
    vmalloc_mem1 = vmalloc(8192);
    printk(KERN_INFO "vmalloc_mem1 addr = %px\n", vmalloc_mem1);
    
    vmalloc_mem2 = vmalloc(1024 * 1024);
    printk(KERN_INFO "vmalloc_mem2 addr = %px\n", vmalloc_mem2);

    vmalloc_mem3 = vmalloc(64 * 1024 * 1024);
    printk(KERN_INFO "vmalloc_mem3 addr = %px\n", vmalloc_mem3);

    return 0;
}

static void __exit vmalloc_exit(void)
{
    vfree(vmalloc_mem1);
    vfree(vmalloc_mem2);
    vfree(vmalloc_mem3);
    printk(KERN_INFO "Exit vmalloc!\n");
}

module_init(my_vmalloc_init);
module_exit(vmalloc_exit);
