#include <linux/module.h>  
#include <linux/init.h>  
#include <linux/kernel.h>  
      
#include <linux/fs.h>         // for basic filesystem  
#include <linux/proc_fs.h>    // for the proc filesystem  
#include <linux/seq_file.h>   // for sequence files  
#include <linux/slab.h>       // for kzalloc, kfree  
#include <linux/uaccess.h>    // for copy_from_user  
#define BUF_SIZE 10 
// global var  
static char *str = NULL;  
static int curr_len = 0;
static struct proc_dir_entry *parent;

// seq_operations -> show  
static int hello_show(struct seq_file *m, void *v)  
{  
    seq_printf(m, "This is proc message from module4\n");  
    seq_printf(m, "str is %s\n", str);  
      
    return 0; 
}  
      
// file_operations -> write  
static ssize_t hello_write(struct file *file, const char __user *buffer, size_t count, loff_t *f_pos)  
{  
    //分配临时缓冲区
    int i;
    if(curr_len > 0) curr_len -= 1;
    int tmp_len = curr_len;
    int copy_len = 0;
    char *dst = kzalloc((BUF_SIZE+1), GFP_KERNEL);
    char *tmp = kzalloc((BUF_SIZE+1), GFP_KERNEL);
    for (i = 0; i < curr_len; i++) {
        dst[i] = str[i];
    }

    if(count + curr_len > BUF_SIZE) {
        copy_len = BUF_SIZE - curr_len;
        curr_len = BUF_SIZE;
    } else {
        copy_len = count;
        curr_len += count;
    }
    
    printk(KERN_INFO "current len is %d", curr_len); 
    if (copy_from_user(tmp, buffer, copy_len+1)) {  
        return -EFAULT;  
    }  
    
    for (i = tmp_len; i < curr_len; i++) {
        dst[i] = tmp[i-tmp_len];
    }
    str = dst;
    return count;  
}  
      
// seq_operations -> open  
static int hello_open(struct inode *inode, struct file *file)  
{  
    return single_open(file, hello_show, NULL);  
}  
      
static const struct file_operations hello_fops =   
{  
    .owner      = THIS_MODULE,  
    .open       = hello_open,  
    .read       = seq_read,  
    .write      = hello_write,  
    .llseek     = seq_lseek,  
    .release    = single_release,  
};  
      
// module init  
static int __init hello_init(void)  
{  
    struct proc_dir_entry* hello_file;  
    parent = proc_mkdir("hello_dir",NULL);
    hello_file = proc_create("hello", 0, parent, &hello_fops);  
    if (NULL == hello_file)  
    {  
        return -ENOMEM;  
    }  
      
    return 0;  
}  
      
// module exit  
static void __exit hello_exit(void)  
{  
    remove_proc_entry("hello", NULL); 
    remove_proc_entry("hello_dir", NULL); 
    kfree(str);  
}  
      
module_init(hello_init);  
module_exit(hello_exit);   
MODULE_LICENSE("GPL");  

