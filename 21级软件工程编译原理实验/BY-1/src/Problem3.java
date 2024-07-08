import java.util.ArrayList;
import symbol.*;

public class Problem3 {
    /**
     * 状态转移函数:一个状态集合可以通过条件跳转的下一状态集合
     * @param graph
     * @param stateId
     * @param driveId
     * @return
     */
    public static ArrayList<Integer> move(Graph graph, int stateId, int driveId) {
        ArrayList<Edge> edgeList = graph.getpEdgeTable();//边的集合
        ArrayList<Integer> stateList = new ArrayList<>();//状态集合
        for(Edge edge:edgeList) {
            if(edge.getFromState() == stateId && edge.getDriverId() == driveId) {
                int nextState = edge.getNextState();//下一状态
                stateList.add(nextState);
            }
        }
        return stateList;
    }

    /**
     * 求状态转移函数的ε闭包
     * @param graph
     * @param stateArrayList
     * @return
     */
    public static ArrayList<Integer> closure(Graph graph, ArrayList<Integer> stateArrayList)  {
        ArrayList<Edge> edgeList = graph.getpEdgeTable();//边的集合
        ArrayList<Integer> currentStateList = new ArrayList<>();//当前状态集合
        ArrayList<Integer> resultStateList = new ArrayList<>();//闭包运算结果->状态集合
        ArrayList<Integer> nextStateList = new ArrayList<>();//next状态集合
        //初始化
        resultStateList = stateArrayList;
        currentStateList = stateArrayList;

        while(!currentStateList.isEmpty()) {
            for (Edge edge:edgeList) {
                //查找能进行空变迁的状态集合
                if(currentStateList.contains(edge.getFromState()) && edge.getType() == DriverType.NULL) {
                    if(!nextStateList.contains(edge.getNextState())) {
                        //保存空变迁指向的next状态
                        nextStateList.add(edge.getNextState());
                    }
                }
            }

            //去除重复搜索的状态
            for(Integer stateId:resultStateList) {
                if(nextStateList.contains(stateId)) {
                    nextStateList.remove(stateId);
                }
            }
            //去除后为next为空则闭包搜索结束
            if(nextStateList.isEmpty()){
                break;
            } else {
                //以next为当前状态继续搜索
                currentStateList = nextStateList;
                resultStateList.addAll(nextStateList);
            }
        }
        return resultStateList;
    }

    /**
     * 合并前两个功能
     * @param graph
     * @param currentStateArrayList
     * @param driveId
     * @return
     */
    public static ArrayList<Integer> dTran(Graph graph, ArrayList<Integer> currentStateArrayList, int driveId) {
        ArrayList<Integer> nextStateList = new ArrayList<>();//下一状态集合
        for(Integer stateId:currentStateArrayList) {
            //先求转移状态集合
            ArrayList<Integer> stateList = move(graph,stateId,driveId);
            nextStateList.addAll(stateList);
        }
        //再求闭包运算结果
        return closure(graph,nextStateList);
    }

    /**
     * NFA转换成DFA
     */
    public static Graph NFAToDFA(Graph pNFA) {
        Graph graph = new Graph();//新建DFA图
        //首先穷举NFA开始状态集合的出边，驱动字符，以及下一状态集合
        ArrayList<Integer> currentStateList = new ArrayList<>();
        currentStateList.add(pNFA.getStart().getStateId());//当前从初始状态集合开始
        currentStateList = closure(pNFA,currentStateList); //闭包结果集合

        //存储所有状态集合
        ArrayList<ArrayList<Integer>> allStateList = new ArrayList<>();
        allStateList.add(currentStateList);//存储初始状态集合
        //存储DFA驱动边集合
        ArrayList<Edge> newEdgeList = new ArrayList<>();
        //存储DFA状态集合
        ArrayList<State> newStateList = new ArrayList<>();
        //存储DFA终止状态集合
        ArrayList<State> newEndState = new ArrayList<>();
        //DFA的初始状态
        State newStart = new State(0,StateType.UNMATCH,LexemeCategory.EMPTY);
        newStateList.add(newStart);
        graph.setStart(newStart);

        int currentStateId = 0;//当前状态Id
        ArrayList<Edge> edgeList = pNFA.getpEdgeTable();
        while(currentStateId < allStateList.size()) {
            currentStateList = allStateList.get(currentStateId);//当前状态集合
            //存储DFA驱动Id
            ArrayList<Integer> newDriverIdList = new ArrayList<>();
            //遍历NFA图
            for(Edge edge:edgeList) {
                if(currentStateList.contains(edge.getFromState()) && edge.getType() != DriverType.NULL && !newDriverIdList.contains(edge.getDriverId())) {
                    //调用DTranh函数 得出每一转换的下一状态集合
                    ArrayList<Integer> nextStateList = dTran(pNFA,currentStateList,edge.getDriverId());
                    newDriverIdList.add(edge.getDriverId());//添加驱动
                    //判断下一状态集合与前面已知的状态集合（即开始状态集合）是否相同
                    if(!allStateList.contains(nextStateList)) {
                        //不相同则保存
                        allStateList.add(nextStateList);
                        //添加xin边
                        Edge newEdge = new Edge(currentStateId,newStateList.size(),edge.getDriverId(),edge.getType());
                        newEdgeList.add(newEdge);
                        //添加新的状态，判断是否终止，有无出边 设置DFA状态的type
                        StateType type = StateType.UNMATCH;
                        LexemeCategory category = LexemeCategory.EMPTY;
                        //遍历NFA
                        for(State state1 : pNFA.getpStateTable()) {
                            //DFA状态集合包含NFA终止状态，则状态的type=match
                            if(nextStateList.contains(state1.getStateId()) && state1.getType() == StateType.MATCH) {
                                type = StateType.MATCH;
                            }
                            //DFA状态集合中包含NFA中属性不为空的状态，则属性也为该状态的属性
                            if(state1.getCategory() != LexemeCategory.EMPTY) {
                                category = state1.getCategory();
                            }
                        }
                        State state2 = new State(newStateList.size(),type,category);//新建DFA状态
                        //判断是否为终止状态
                        if(type == StateType.MATCH) {
                            newEndState.add(state2);
                        }
                        //DFA添加状态
                        newStateList.add(state2);
                    } else {
                        //如果是已有的状态集合,得到集合，添加边
                        int stateNUm = allStateList.indexOf(nextStateList);
                        Edge newEdge = new Edge(currentStateId,stateNUm,edge.getDriverId(),edge.getType());
                        newEdgeList.add(newEdge);
                    }
                }
            }
            currentStateId++;
        }
        graph.setNumOfStates(currentStateId);
        graph.setpEndStateTable(newEndState);
        graph.setpEdgeTable(newEdgeList);
        return graph;
    }
}
