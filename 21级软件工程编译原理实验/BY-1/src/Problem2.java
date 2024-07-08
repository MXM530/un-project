import java.util.ArrayList;
import symbol.*;


public class Problem2 {
    /**
     * 5个函数则都是有关NFA的组合，分别对应5种正则运算，创建一个新的NFA作为返回值。
     *构造一个最简单的NFA(只有两个状态）
     */
    public static Graph generateBasicNFA(DriverType driverType,int driverId, LexemeCategory category) {
        //NFA
        Graph pNFA = new Graph();
        State pState1 = new State(0,StateType.UNMATCH,LexemeCategory.EMPTY);
        State pState2 = new State(1,StateType.MATCH,category);
        Edge edge = new Edge(pState1.getStateId(),pState2.getStateId(),driverId,driverType);
        //加入NFA中
        // 加入NFA中
        pNFA.addState(pState1);
        pNFA.setStart(pState1);
        pNFA.addState(pState2);
        pNFA.addEdge(edge);
        return pNFA;
    }

    /**
     * Graph * union(Graph *pNFA1, Graph *pNFA2)；
     * 并运算
     */
    public static Graph union(Graph pNFA1, Graph pNFA2) {
        Graph graph = new Graph();
        //等价改造两个NFA(根据初始状态的入边、结束状态的出边合理添加ε边)
        pNFA1.transForm();
        pNFA2.transForm();
        State start = pNFA1.getStart();//NFA1 初始状态
        graph.setStart(start);
        //状态数量
        int stateNum1 = pNFA1.getNumOfStates();
        int stateNum2 = pNFA2.getNumOfStates();
        graph.setNumOfStates(stateNum1+stateNum2-2);//合并的状态数
        //重编号NFA2
        pNFA2.reNumber(stateNum1-2);
        graph.addToTable(pNFA1);
        graph.addToTable(pNFA2);
        //合并终止状态
        graph.mergeEndState(pNFA1,stateNum1+stateNum2-3);
        //合并初始状态
        graph.mergeStart(pNFA2); //pNFA1初始状态->pFA2初始状态
        return graph;
    }
    /**
     * Graph * product(Graph *pNFA1, Graph *pNFA2);
     * 连接运算 添加状态防止倒灌
     */
    public static Graph product(Graph pNFA1, Graph pNFA2){
        Graph graph = new Graph();
        //获得PNFA1初始状态
        graph.setStart(pNFA1.getStart());
        graph.addToTable(pNFA1);
        //获取状态数量
        int stateNum1 = pNFA1.getNumOfStates();;
        int stateNum2 = pNFA2.getNumOfStates();
        //判断是否有出入边
        if(pNFA1.haveOutsideEdge(pNFA1.getpEndStateTable()) && pNFA2.haveInsideEdge(pNFA1.getStart())) {
           //NFA2重新编号
            pNFA2.reNumber(stateNum1);
            graph.setNumOfStates(stateNum1+stateNum2);
            // 添加ε边
            for(State state:pNFA1.getpEndStateTable()) {
                Edge edge = new Edge(state.getStateId(),pNFA2.getStart().getStateId(),DriverType.NULL);
                graph.addEdge(edge);
            }
        } else {
            //NFA2重新编号
            pNFA2.reNumber(stateNum1-1);
            //合并NF1的终止和NFA2的初始状态
            pNFA2.removeStateById(stateNum1-1);
            graph.setNumOfStates(stateNum1+stateNum2-1);
        }
        for(State state: graph.getpStateTable()) {
            state.setType(StateType.UNMATCH);
        }
        graph.setpEndStateTable(new ArrayList<>());
        //加上pNFA->pNFA2的边
        graph.addToTable(pNFA2);
        return graph;
    }
    /**
     * Graph * plusClosure(Graph *pNFA)
     * 正闭包运算 添加从终止状态到初始状态的空变迁
     */
    public static Graph plusClosure(Graph pNFA) {
        Graph graph = new Graph();
        //构建初始状态、状态数
        graph.setStart(pNFA.getStart());
        graph.setNumOfStates(pNFA.getNumOfStates());
        graph.addToTable(pNFA);
        //构建PNFA从终止状态到初始状态的边
        pNFA.addEndEdgeToOtherState(pNFA.getStart());
        return graph;
    }

    /**
     * Graph * closure(Graph *pNFA)
     * 闭包运算
     */
    public static Graph closure(Graph pNFA) {
        //判断初始、终止状态有无出入边并进行改造
      int transType = pNFA.transForm();
      if(transType == 0) {
          //没有出入边 添加终止状态到初始状态的ε边
          pNFA.mergeEndState(pNFA,0);
          //状态数量、匹配状态
          pNFA.setNumOfStates(pNFA.getpStateTable().size());
          for (State state:pNFA.getpStateTable()) {
              if(state.getStateId() == 0) {
                  state.setType(StateType.MATCH); //匹配状态
                  pNFA.addEndState(state);//终止状态
              }
          }
      } else {
          // 添加从原开始状态到原终结状态的ε边 遍历终止状态集合
          for (State state:pNFA.getpEndStateTable()) {
              Edge edge = new Edge(pNFA.getStart().getStateId(),state.getStateId(),DriverType.NULL);
              pNFA.addEdge(edge);
          }
          // 添加从开始到终结的ε边
          ArrayList<Edge> edgeList = new ArrayList<>();
          //遍历终止状态集合 添加从初始状态到终止状态的边
          for(State state: pNFA.getpEndStateTable()) {
              Edge edge = new Edge(pNFA.getStart().getStateId(),state.getStateId(),DriverType.NULL);
              // 由于涉及到更改结束状态集合的操作，因此需要先遍历再添加边
              edgeList.add(edge);
          }
          for(Edge edge:edgeList) {
              pNFA.addEdge(edge);
          }
      }
        //新建NFA
        Graph graph = new Graph(pNFA);
        return graph;
    }
    /**
     * Graph * zeroOrOne(Graph *pNFA)；
     * 0或者1个运算。
     */
    public static Graph zeroOrOne(Graph pNFA) {
        Graph graph = new Graph();
        graph.setStart(pNFA.getStart());
        graph.addToTable(pNFA);
        //有误出入边进行等价改造
        graph.transForm();
        //状态数
        graph.setNumOfStates(pNFA.getpStateTable().size());
        // 添加从开始到终结的ε边
        for(State state:graph.getpEndStateTable()) {
            Edge edge = new Edge(graph.getStart().getStateId(),state.getStateId(),DriverType.NULL);
            graph.addEdge(edge);
        }
        return graph;
    }
}
