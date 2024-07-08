package symbol;

/**
 * 产生式概述表
 */
public class ProductionInfo {
    // 产生式序号
    private int indexId;
    // 头部非终结符
    private String headName;
    // 产生式体中文法符的个数
    private int bodySize;

    private static int idNum = 0;

    public ProductionInfo(String headName, int bodySize) {
        this.indexId = idNum++;
        this.headName = headName;
        this.bodySize = bodySize;
    }

    @Override
    public String toString() {
        return "\n产生式概述表 {" +
                "产生式序号: " + indexId +
                ", 产生式头部非终结符: '" + headName + '\'' +
                ", 产生式体文法符个数: " + bodySize +
                '}';
    }
}
