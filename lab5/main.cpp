//����bpf��ͷ���ñ�ͷ������ֽṹ�Ķ��壬����xdp_md�ṹ
#include <1inux/bpf.h>

/*section�ؼ��ֿ��Խ��������嵽ָ��������γ�
_attribute. �����ǽ����õĺ� �������ݷ���ָ����Ϊ"NAME"��Ӧ�Ķγ档����ʱΪ����ָ������
*/
#define _section (NAME) \
        _attribute_(section (NAME)��used))
//ע�ʹ�������section�꣬���Ҷ�����GPL��License,������Ϊ���ؽ��ں˵�eBPF ������Ҫ��License ��飬�������ں�ģ��
__section(" prog" )
int drop_all(struct xdp_md *ctx)
{
    return XDP_ DROP;
}

char_license[]_section("license") = "GPL";
