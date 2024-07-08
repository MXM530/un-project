package symbol;

/**
    字符进行范围运算('a'~'z')、并运算(a|b)和差运算(-) 都返回一个新的字符集对象
 */
public class CharSet {
    /** 字符集id*/
    private final int indexId;
    /**字符集中的段id（一个字符集可以有多个段*/
    private final int segmentId;
    /** 段的起始字符from */
    private final char fromChar;
    /** 段的结束字符to */
    private final char toChar;

    private static int indexIdNum = 0;
    private static int segmentIdNum = 0;

    /**
     * 创建一个新的字符集 (初始化)
     */
    public CharSet(char fromChar,char toChar) {
        this.indexId = indexIdNum++;
        this.segmentId = segmentIdNum++;
        this.fromChar = fromChar;
        this.toChar = toChar;
    }

    /**
     * 创建字符集的段（初始化）
     */
    public CharSet(char fromChar,char toChar,int indexId) {
        this.indexId = indexId;
        this.segmentId = segmentIdNum++;
        this.fromChar = fromChar;
        this.toChar = toChar;
    }

    /**
     * 获取数据
     */

    public int getIndexId() {
        return indexId;
    }
    public int getSegmentId() {
        return segmentId;
    }

    public char getFromChar() {
        return fromChar;
    }

    public char getToChar() {
        return toChar;
    }
    public int getIndexIdNum() {
        return indexIdNum;
    }



}
