package symbol;

import java.util.ArrayList;

public class ItemSet {
    //LR(0)项集
    // 状态序号
    private int stateId;
    // LR0 项目表
    private ArrayList<LR0Item> pItemTable;
    private static int idNumber = 0;
    public ItemSet() {
        stateId = idNumber++;
        pItemTable = new ArrayList<>();
    }

    public ItemSet(int stateId) {
        this.stateId = stateId;
        pItemTable = new ArrayList<>();
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
    public void setStateId() {
        this.stateId = idNumber++;
    }

    public ArrayList<LR0Item> getpItemTable() {
        return pItemTable;
    }
    public void addItem(LR0Item item) {
        pItemTable.add(item);
    }
    public Boolean containsItem(Production production,int dot) {
        for (LR0Item item:pItemTable) {
            if(item.getProduction() == production && item.getDotPosition() == dot) {
                return  true;
            }
        }
        return false;
    }

    public Boolean isSameItemSet(ItemSet itemSet) {
        if(pItemTable.size() !=itemSet.getpItemTable().size()) return false;

        //项目数量相同的情况下 判断元素是否相同
        for(int i = 0; i < pItemTable.size();i++) {
            if(!pItemTable.get(i).equals(itemSet.getpItemTable().get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nItemSet{\n" +
                "状态序号:" + stateId +
                ",\nLR0项目表=" + pItemTable + "\n" +
                '}';
    }
}
