import java.util.ArrayList;
import symbol.CharSet;
import symbol.State;

public class Problem1 {

    /**
     * 字符集表的定义
     * 创建一个新的字符集对象，返回值为字符集id。
     * 创建字符集，表现为往字符集表中添加新的行。
     * 当一个字符集包含多个段时，便会在字符集表中有多行，一行记录一段。
     */
    static private ArrayList<CharSet> pCharSetTable = new ArrayList<>();

    /**
     * 字符集的运算范围
     */
    public static int range(char fromChar, char toChar) {
        CharSet c = new CharSet(fromChar, toChar);  //indexId = 0;segment=0;
        pCharSetTable.add(c);
        return c.getIndexId();
    }

    /**
     * char | 运算
     */
    public static int union(char c1, char c2) {
        CharSet charSet1 = new CharSet(c1,c2);  //indexId=0,segment=0;
        pCharSetTable.add(charSet1);
        //判断c1 | c2 是否是新的字符集
        if(c1 != c2) {
           CharSet charSet2 = new CharSet(c1,c2,charSet1.getIndexId()); //index=0,segment=1;
           pCharSetTable.add(charSet2);
        }
        return charSet1.getIndexId();
    }

    /**
     * 字符集 | 字符
     */
    public static int union(int charSetId,char c) {
        //判断字符集表是否已存在charsetid对应的字符集
        boolean flag = false;
        int newId = 0;
        for(CharSet charSet:pCharSetTable) {
            if(charSet.getIndexId() == charSetId) {
                if(flag) {
                    //新建一个段
                    CharSet charSet1 = new CharSet(charSet.getFromChar(),charSet.getToChar(),newId);
                    pCharSetTable.add(charSet1);
                } else {
                    // 新建字符集（newId）
                    CharSet charSet1 = new CharSet(charSet.getFromChar(),charSet.getToChar());
                    newId = charSet1.getIndexId();
                    pCharSetTable.add(charSet1);
                    flag = true;
                }
            }
        }

        //新建一个段
        CharSet newSegment = new CharSet(c,c,newId);
        pCharSetTable.add(newSegment);
        return  newId;
    }

    /**
     * int union(int charSetId1,int charSetId2)；
     * 字符集与字符集的并运算
     */

    public static int union(int charSetId1,int charSetId2) {
        boolean flag = false;
        int newId = 0;
        ArrayList<CharSet> newList = new ArrayList<>();
        //字符集1
        for (CharSet charSet: pCharSetTable) {
            if (charSet.getIndexId() == charSetId1) {
                if(flag) {
                    //新建段
                    CharSet newcharSet = new CharSet(charSet.getFromChar(), charSet.getToChar(),newId);
                    newList.add(newcharSet);
                } else {
                    //新建字符集
                    CharSet newCharSet = new CharSet(charSet.getFromChar(),charSet.getToChar());
                    newId = newCharSet.getIndexId();//获取新的id
                    newList.add(newCharSet);
                    flag = true;
                }
            }
        }
        //字符集2
        for (CharSet charSet: pCharSetTable) {
            if (charSet.getIndexId() == charSetId2) {
                //新建段
                CharSet newcharSet = new CharSet(charSet.getFromChar(), charSet.getToChar(),newId);
                newList.add(newcharSet);
            }
        }

        for (CharSet charSet:newList) {
            pCharSetTable.add(charSet);
        }
        return newId;
    }


    /**
     * int difference(int charSetId, char c)； // 字符集与字符之间的差运算
     * 判断字符c是否在字符集中 在：分两段（from-(c-1),(c+1)-to)
     */
    public static int difference(int charSetId, char c) {
        boolean flag = false;
        int newId = 0;
        for (CharSet charSet : pCharSetTable) {
            if(charSet.getIndexId() == charSetId) {
                //包含字符c的情况 分段
                if(charSet.getFromChar() < c && charSet.getToChar() > c) {
                    if(flag) {
                        //新建一个段
                        CharSet newcharSet = new CharSet(charSet.getFromChar(), (char)(c-1),newId);
                        pCharSetTable.add(newcharSet);

                    } else {
                        //新建字符集
                        CharSet newcharSet = new CharSet(charSet.getFromChar(), (char)(c-1));
                        newId = newcharSet.getIndexId();
                        pCharSetTable.add(newcharSet);
                        flag = true;
                    }
                    CharSet newcharset = new CharSet((char)(c+1),charSet.getToChar(),newId);
                    pCharSetTable.add(newcharset);
                }
                else if(charSet.getFromChar() ==  c) {
                    if(flag) {
                        //新建一个段
                        CharSet newcharSet = new CharSet((char)(c+1),charSet.getToChar(),newId);
                        pCharSetTable.add(newcharSet);

                    } else {
                        //新建字符集
                        CharSet newcharSet = new CharSet((char)(c+1),charSet.getToChar());
                        newId = newcharSet.getIndexId();
                        pCharSetTable.add(newcharSet);
                        flag = true;
                    }

                }
                else if (charSet.getToChar() == c) {
                    if(flag) {
                        //新建一个段
                        CharSet newcharSet = new CharSet(charSet.getFromChar(),(char)(c-1),newId);
                        pCharSetTable.add(newcharSet);

                    } else {
                        //新建字符集
                        CharSet newcharSet = new CharSet(charSet.getFromChar(), (char)(c-1));
                        newId = newcharSet.getIndexId();
                        pCharSetTable.add(newcharSet);
                        flag = true;
                    }
                }
                else {
                    if(flag) {
                        //新建一个段
                        CharSet newcharSet = new CharSet(charSet.getToChar(), charSet.getToChar(),newId);
                        pCharSetTable.add(newcharSet);

                    } else {
                        //新建字符集
                        CharSet newcharSet = new CharSet(charSet.getFromChar(), charSet.getToChar());
                        newId = newcharSet.getIndexId();
                        pCharSetTable.add(newcharSet);
                        flag = true;
                    }
                }

            }
        }
        return newId;
    }


}
