obj-m:=workqueue_test.o

KERNELDIR?= /lib/modules/$(shell uname -r)/build
PWD:=$(shell pwd)

all:
	$(MAKE) -C $(KERNELDIR) M=$(PWD) modules

install:
	insmod workqueue_test.ko times=5

uninstall:
	rmmod workqueue_test

clean:
	rm -rf *.o *~ core .depend .*.cmd *.ko *.mod.c .tmp_versions modules.order Module.symvers

.PHONY: all install uninstall clean
