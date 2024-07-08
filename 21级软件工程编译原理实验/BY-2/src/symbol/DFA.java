package symbol;

import java.util.ArrayList;

public class DFA {
    // 开始项集
    private ItemSet startupItemSet;
    // 变迁边表
    private ArrayList<TransitionEdge> pEdgeTable;

    public DFA(ItemSet startupItemSet) {
        this.startupItemSet = startupItemSet;
        pEdgeTable = new ArrayList<>();
    }
    //添加边
    public void addEdges(ArrayList<TransitionEdge> edges) {
        pEdgeTable.addAll(edges);
    }

    //查找下一个状态
    public ItemSet findNextSet(ItemSet from, GrammarSymbol symbol) {
        for (TransitionEdge edge: pEdgeTable) {
            if (edge.getFromItemSet() == from && edge.getDriverSymbol() == symbol) {
                return edge.getToItemSet();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DFA {\n" +
                "ItemSet-Id= " + startupItemSet.getStateId() +
                ", \n变迁边表=" + pEdgeTable +
                '}';
    }
}
