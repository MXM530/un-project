obj-m:=tasklet_interrupt.o
#表明有一个模块要从目标文件tasklet_interrupt.o 建立，在从目标文件建 立后结果模块命名为tasklet_interrupt.ko,#:=表示覆盖之前的赋值

KERNELDIR?=/lib/modules/$(shell uname -r)/build  
#KERNELDIR是保存1inux源码路径, shell uname -r 取得当前内核的版本号,#?=表示如果之前没有被赋值过就赋予等号后的值
PWD:=$(shell pwd)
#PWD是指定编译后的模块存放的位置，she1l pwd取得当前工作路径

all:载入全部的模块；
	$(MAKE) -C $(KERNELDIR) M=$(PWD) modules
#-C选项:此选项指定内核源码的位置， KERNELDIR指定内核代码目录，M=$(PWD):程序会自动到你所指定的dir目录中查找模块源码，将其编译，生成Ko文件
#=是最基本的赋值，就是把右值赋给左值

install:
	insmod tasklet_interrupt.ko irq=31 devname="tasklet_dev"  
#功能说明：向内核载入模块 install loadable kernel module
语法：insmod [模块文件][符号名称 = 符号值]

uninstall:
	rmmod tasklet_interrupt  
#卸载内核模块

clean: #clean是Makefile的入口， 其目的是为了清除所有生成的文件
	rm -rf *.o *~ core .depend .*.cmd *.ko *.mod.c .tmp_versions modules.order Module.symvers

##default, clean这- 类的是makefile中的伪目标， 伪目标并不是一个真正的编译目标，它代表着-系列你想要执行的命令集合
.PHONY: all install uninstall clean
