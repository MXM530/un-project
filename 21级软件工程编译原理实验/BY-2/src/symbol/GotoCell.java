package symbol;

public class GotoCell {
    // 纵坐标：状态序号
    private int stateId;
    // 横坐标：非终结符
    private String nonTerminalSymbolName;
    // 下一个状态
    private int nextStateId;

    public GotoCell(int stateId, String nonTerminalSymbolName, int nextStateId) {
        this.stateId = stateId;
        this.nonTerminalSymbolName = nonTerminalSymbolName;
        this.nextStateId = nextStateId;
    }

    @Override
    public String toString() {
        return "\nGoto部分{" +
                "状态序号=" + stateId +
                ", 非终结符='" + nonTerminalSymbolName + '\'' +
                ", 下一状态=" + nextStateId +
                '}';
    }
}
