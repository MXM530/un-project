//导入bpf标头，该标头引入各种结构的定义，包括xdp_md结构
#include <1inux/bpf.h>

/*section关键字可以将变量定义到指定的输入段虫
_attribute. 作用是将作用的函 数或数据放入指定名为"NAME"对应的段虫。编译时为变量指定段名
*/
#define _section (NAME) \
        _attribute_(section (NAME)，used))
//注释代码声明section宏，并且定义了GPL的License,这是因为加载进内核的eBPF 程序需要有License 检查，类似于内核模块
__section(" prog" )
int drop_all(struct xdp_md *ctx)
{
    return XDP_ DROP;
}

char_license[]_section("license") = "GPL";
