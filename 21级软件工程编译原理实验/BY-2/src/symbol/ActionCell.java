package symbol;

public class ActionCell {
//    LR(1)语法分析表包含ACTION和GOTO两个部分
    //纵坐标：状态序号
    private int stateId;
    // 横坐标：终结符
    private String terminalSymbolName;
    // Action 类别
    /**
     * 取值有三种：‘r’和‘s’，以及‘a’。‘r’是规约，‘s’是移入, ‘a’是接受。
     * 当Action类别为规约时，id的取值为产生式id。当Action类别为移入时，id的取值为下一状态id。
     */
    private ActionCategory type ;
    // Action 的 id
    private int id;
    public ActionCell(int stateId, String terminalSymbolName, ActionCategory type, int id) {
        this.stateId = stateId;
        this.terminalSymbolName = terminalSymbolName;
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return "\nAction部分 {" +
                "状态序号:" + stateId +
                ", 终结符:'" + terminalSymbolName + '\'' +
                ", 操作类型/ID:" + type + id +
                '}';
    }
}
