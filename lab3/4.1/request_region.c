#include <linux/module.h>
#include <asm/io.h>
#include <linux/ioport.h>

MODULE_LICENSE("GPL");

struct resource * myregion;

static int __init mem_module_init(void)
{
	printk("Start request region!\n");

	myregion = request_region(22222, 10,"ve");//申请IO资源

	if (myregion != NULL){
		printk("it's ok for %lld .",myregion->start);//获得基地址

	}else{
		printk("Failed to request region!\n");//失败
	}

	return 0;
}

static void __exit mem_module_exit(void)
{
	release_region(22222, 10);
	printk("Exit request_region!\n");//归还申请的IO资源
}

module_init(mem_module_init);
module_exit(mem_module_exit);
