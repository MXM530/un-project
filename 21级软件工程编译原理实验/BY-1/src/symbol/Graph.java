//package symbol;
//
//import java.util.ArrayList;
//
//public class Graph {
//
//        static private int graphIdNum = 0;
//        private int graphId;
//        private int numOfStates;
//        private State start;
//        private ArrayList<State> pEndStateTable;
//        private ArrayList<Edge> pEdgeTable;
//        private ArrayList<State> pStateTable;
//
//        public Graph() {
//                this.graphId = graphIdNum++;
//                this.numOfStates = 0;
//                this.pEndStateTable = new ArrayList<>();
//                this.pEdgeTable = new ArrayList<>();
//                this.pStateTable = new ArrayList<>();
//        }
//
//        public Graph(Graph graph) {
//                this.graphId = graphIdNum++;
//                this.numOfStates = graph.getNumOfStates();
//                this.start = graph.getStart();
//                this.pEndStateTable = graph.getpEndStateTable();
//                this.pEdgeTable = graph.getpEdgeTable();
//                this.pStateTable = graph.getpStateTable();
//        }
//
//        public static void setGraphIdNum(int graphIdNum) {
//                Graph.graphIdNum = graphIdNum;
//        }
//
//        public State getStart() {
//                return start;
//        }
//
//        public void setStart(State start) {
//                this.start = start;
//        }
//
//        public int getGraphId() {
//                return graphId;
//        }
//
//        public void setGraphId(int graphId) {
//                this.graphId = graphId;
//        }
//
//        public int getNumOfStates() {
//                return numOfStates;
//        }
//
//        public void setNumOfStates(int numOfStates) {
//                this.numOfStates = numOfStates;
//        }
//
//        public ArrayList<State> getpEndStateTable() {
//                return pEndStateTable;
//        }
//
//        public void setpEndStateTable(ArrayList<State> pEndStateTable) {
//                this.pEndStateTable = pEndStateTable;
//        }
//
//        public ArrayList<Edge> getpEdgeTable() {
//                return pEdgeTable;
//        }
//
//        public void setpEdgeTable(ArrayList<Edge> pEdgeTable) {
//                this.pEdgeTable = pEdgeTable;
//        }
//
//        public ArrayList<State> getpStateTable() {
//                return pStateTable;
//        }
//
//        public void setpStateTable(ArrayList<State> pStateTable) {
//                this.pStateTable = pStateTable;
//        }
//
//        public void addState(State pState) {
//                pStateTable.add(pState);
//                numOfStates++;
//        }
//
//        public void addEdge(Edge edge) {
//                pEdgeTable.add(edge);
//                for (State s : pStateTable) {
//                        if (edge.getNextState() == s.getStateId()) {
//                                if (s.getType() == StateType.MATCH && !pEndStateTable.contains(s)) {
//                                        pEndStateTable.add(s);
//                                }
//                                return;
//                        }
//                }
//        }
//
//        public void addEndEdge(State end) {
//                for (State state : pEndStateTable) {
//                        state.setType(StateType.UNMATCH);
//                        Edge edge = new Edge(state.getStateId(), end.getStateId(), DriverType.NULL);
//                        pEdgeTable.add(edge);
//                }
//                pEndStateTable.clear();
//                pEndStateTable.add(end);
//        }
//
//        public void addEndEdgeToOtherState(State start) {
//                for (Edge edge : pEdgeTable) {
//                        for (State state : pEndStateTable) {
//                                if (edge.getNextState() == state.getStateId()) {
//                                        edge.setNextState(start.getStateId());
//                                }
//                        }
//                }
//                pEndStateTable.clear();
//                pEndStateTable.add(start);
//                start.setType(StateType.MATCH);
//        }
//
//        public void addEdgeToEnd(int stateId) {
//                for (State state : pEndStateTable) {
//                        Edge edge = new Edge(stateId, state.getStateId(), DriverType.NULL);
//                        pEdgeTable.add(edge);
//                }
//        }
//
//        public void reNumber(int index) {
//                // 状态重新编号
//                for (State state : pStateTable) {
//                        int stateId = state.getStateId();
//                        state.setStateId(stateId+ index);
//                }
//                // 边重新编号
//                for (Edge edge : pEdgeTable) {
//                        int fromState = edge.getFromState();
//                        edge.setFromState(fromState + index);
//                        int nextState = edge.getNextState();
//                        edge.setNextState(nextState + index);
//                }
//        }
//
//        public void addToTable(Graph g) {
//                pEdgeTable.addAll(g.getpEdgeTable());
//                pStateTable.addAll(g.getpStateTable());
//                pEndStateTable.addAll(g.pEndStateTable);
//        }
//
//        public void addEndState(State state) {
//                pEndStateTable.add(state);
//        }
//
//        public void removeStateById(int stateId) {
//                for (State state : pStateTable) {
//                        if (state.getStateId() == stateId) {
//                                pStateTable.remove(state);
//                        }
//                }
//                numOfStates --;
//        }
//
//        public boolean haveOutsideEdge(ArrayList<State> stateArrayList) {
//                /**
//                 * 判断是否有边从结束状态出发
//                 */
//                for (Edge edge : pEdgeTable) {
//                        for (State state : stateArrayList) {
//                                if (edge.getFromState() == state.getStateId()) {
//                                        return true;
//                                }
//                        }
//                }
//                return false;
//        }
//
//        public boolean haveInsideEdge(State start) {
//                /**
//                 * 判断是否有边到达初始状态
//                 */
//                for (Edge edge : pEdgeTable) {
//                        if (edge.getNextState() == start.getStateId()) {
//                                return true;
//                        }
//                }
//                return false;
//        }
//
//        public int transForm() {
//                /**
//                 * 对NFA进行等价改造
//                 */
//                int transType = 0;
//                // 初始状态有入边
//                if (haveInsideEdge(start)) {
//                        // 新增一个状态
//                        State newStart = new State(0, StateType.UNMATCH, LexemeCategory.EMPTY);
//                        addState(newStart);
//                        // 重新编号
//                        reNumber(1);
//                        // 添加一条newStart到初始状态的ε边
//                        Edge edge = new Edge(0, 1, DriverType.NULL);
//                        pEdgeTable.add(edge);
//                        // 设置初始状态
//                        start = newStart;
//                        // 最低位为1表示初始状态有入边
//                        transType |= 1;
//                }
//                if (haveOutsideEdge(pEndStateTable)) {
//                        // 新增一个状态
//                        State newEnd = new State(pStateTable.size(), StateType.UNMATCH, LexemeCategory.EMPTY);
//                        addState(newEnd);
//                        // 添加结束状态到newEnd的ε边
//                        for (State state: pEndStateTable) {
//                                Edge edge = new Edge(state.getStateId(), newEnd.getStateId(), DriverType.NULL);
//                                pEdgeTable.add(edge);
//                        }
//                        // 清空当前结束状态
//                        pEdgeTable.clear();
//                        // 设置新的结束状态
//                        pEndStateTable.add(newEnd);
//                        // 第二低位为1表示终结状态有出边
//                        transType |= 2;
//                }
//                return transType;
//        }
//
//        public void mergeStart(Graph NFA) {
//                State start = NFA.getStart();
//                for (Edge edge: NFA.getpEdgeTable()) {
//                        // 找到所有从初始状态出发的边
//                        if (edge.getFromState() == start.getStateId()) {
//                                edge.setFromState(0);
//                        }
//                }
//                pStateTable.remove(start);
//        }
//
//        public void mergeEndState(Graph NFA, int endStateId) {
//                ArrayList<State> endRemoveList = new ArrayList<>();
//                for (State state: NFA.pEndStateTable) {
//                        for (Edge edge: NFA.getpEdgeTable()) {
//                                // 找到所有到达终结状态的边
//                                if (edge.getNextState() == state.getStateId()) {
//                                        edge.setNextState(endStateId);
//                                }
//                        }
//                        pStateTable.remove(state);
//                        endRemoveList.add(state);
//                }
//                for (State state: endRemoveList) {
//                        pEndStateTable.remove(state);
//                }
//        }
//
//        @Override
//        public String toString() {
//                String s = "Graph{" + '\n';
//                s += " graphId: " + graphId + '\n';
//                s += " numOfStates: " + numOfStates + '\n';
//                s += " start: " + start.toString() + '\n';
//                s += " pEndStateTable: " + pEndStateTable.size() + '\n';
//                for (State state : pEndStateTable) {
//                        s += "  " + state.toString() + '\n';
//                }
//                s += " pEdgeTable: " + pEdgeTable.size() + '\n';
//                for (Edge edge : pEdgeTable) {
//                        s += "  " + edge.toString() + '\n';
//                }
//                s += " pStateTable: " + numOfStates + '\n';
//                for (State state : pStateTable) {
//                        s += "  " + state.toString() + '\n';
//                }
//                s +="}";
//                return s;
//        }
//}

package symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class Graph {
        static private int graphIdNum = 0;
        private int graphId;
        private int numOfStates; //状态序号
        private State start;
        private ArrayList<Edge> pEdgeTable; //边集合
        private ArrayList<State> pEndStateTable; //终止状态集合
        private ArrayList<State> pStateTable; //状态集合


        /**
         * init
         */
        public Graph() {
                this.graphId = graphIdNum++;
                this.numOfStates = 0;
                this.pEdgeTable = new ArrayList<>();
                this.pEndStateTable = new ArrayList<>();
                this.pStateTable = new ArrayList<>();
        }
        public Graph(Graph graph) {
                this.graphId = graphIdNum++;
                this.numOfStates = graph.getNumOfStates();
                this.start = graph.getStart();
                this.pEndStateTable = graph.getpEndStateTable();
                this.pEdgeTable = graph.getpEdgeTable();
                this.pStateTable = graph.getpStateTable();
        }

        public static void setGraphIdNum(int graphIdNum) {
                Graph.graphIdNum = graphIdNum;
        }

        public State getStart() {
                return start;
        }

        public void setStart(State start) {
                this.start = start;
        }

        public int getGraphId() {
                return graphId;
        }

        public void setGraphId(int graphId) {
                this.graphId = graphId;
        }

        public int getNumOfStates() {
                return numOfStates;
        }

        public void setNumOfStates(int numOfStates) {
                this.numOfStates = numOfStates;
        }

        public ArrayList<State> getpEndStateTable() {
                return pEndStateTable;
        }

        public void setpEndStateTable(ArrayList<State> pEndStateTable) {
                this.pEndStateTable = pEndStateTable;
        }

        public ArrayList<Edge> getpEdgeTable() {
                return pEdgeTable;
        }

        public void setpEdgeTable(ArrayList<Edge> pEdgeTable) {
                this.pEdgeTable = pEdgeTable;
        }

        public ArrayList<State> getpStateTable() {
                return pStateTable;
        }

        public void setpStateTable(ArrayList<State> pStateTable) {
                this.pStateTable = pStateTable;
        }

        /**
         * 添加状态
         * @param pState
         */
        public void addState(State pState) {
                pStateTable.add(pState);
                numOfStates++;
        }

        /**
         * 添加边 判断是否指向结束状态
         * @param edge
         */
        public void addEdge(Edge edge) {
                pEdgeTable.add(edge);
                for (State s : pStateTable) {
                        if (edge.getNextState() == s.getStateId()) {
                                //是否是结束状态
                                if (s.getType() == StateType.MATCH && !pEndStateTable.contains(s)) {
                                        pEndStateTable.add(s);
                                }
                                return;
                        }
                }
        }

        /**
         * 添加结束状态的出边（空变迁边）
         * @param end
         */
        public void addEndEdge(State end) {
                for (State state : pEndStateTable) {
                        state.setType(StateType.UNMATCH);
                        Edge edge = new Edge(state.getStateId(), end.getStateId(), DriverType.NULL);
                        pEdgeTable.add(edge);
                }
                pEndStateTable.clear();
                pEndStateTable.add(end);
        }

        /**
         * 一条边到终止状态 转换成 这条边到初始状态
         * @param start
         */
        public void addEndEdgeToOtherState(State start) {
                for (Edge edge : pEdgeTable) {
                        for (State state : pEndStateTable) {
                                if (edge.getNextState() == state.getStateId()) {
                                        edge.setNextState(start.getStateId());
                                }
                        }
                }
                pEndStateTable.clear();
                pEndStateTable.add(start);
                start.setType(StateType.MATCH);
        }

        /**
         * 一条边到终止状态
         * @param stateId
         */
        public void addEdgeToEnd(int stateId) {
                for (State state : pEndStateTable) {
                        Edge edge = new Edge(stateId, state.getStateId(), DriverType.NULL);
                        pEdgeTable.add(edge);
                }
        }

        public void addToTable(Graph g) {
                pEdgeTable.addAll(g.getpEdgeTable());
                pStateTable.addAll(g.getpStateTable());
                pEndStateTable.addAll(g.pEndStateTable);
        }

        /**
         * 添加结束状态集合
         * @param state
         */
        public void addEndState(State state) {
                pEndStateTable.add(state);
        }

        /**
         *  移除状态
         */
        public void removeStateById(int stateId) {
                Iterator<State> iterator = pStateTable.iterator();
                while (iterator.hasNext()){
                        State state = iterator.next();
                        if (state.getStateId() == stateId) {
                                iterator.remove();
                        }
                }
//                for (State state : pStateTable) {
//                        if (state.getStateId() == stateId) {
//                                pStateTable.remove(state);
//                        }
//                }
                numOfStates --;
        }

        /**
         * 状态、边重新编号
         * @param index
         */
        public void reNumber(int index) {
                // 状态重新编号
                for (State state : pStateTable) {
                        int stateId = state.getStateId();
                        state.setStateId(stateId+ index);
                }
                // 边指向的状态也进行重新编号
                for (Edge edge : pEdgeTable) {
                        int fromState = edge.getFromState();
                        edge.setFromState(fromState + index);
                        int nextState = edge.getNextState();
                        edge.setNextState(nextState + index);
                }
        }
        /**
         * 判断状态否有出边
         */
        public boolean haveOutsideEdge(ArrayList<State> stateArrayList) {
                for (Edge edge : pEdgeTable) {
                        for (State state : stateArrayList) {
                                if (edge.getFromState() == state.getStateId()) {
                                        return true;
                                }
                        }
                }
                return false;
        }
        /**
         * 判断start状态是否有入边
         */
        public boolean haveInsideEdge(State start) {
                for (Edge edge : pEdgeTable) {
                        if (edge.getNextState() == start.getStateId()) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 对NFA进行等价改造
         */
        public int transForm() {
                int transType = 0;//标志初始、结束状态是否有出入边
                // 初始状态有入边
                if (haveInsideEdge(start)) {
                        // 新增一个初始状态
                        State newStart = new State(0, StateType.UNMATCH, LexemeCategory.EMPTY);
                        addState(newStart);
                        // 重新编号(序号+1）
                        reNumber(1);
                        // 添加一条newStart到原初始状态的ε边
                        Edge edge = new Edge(0, 1, DriverType.NULL);
                        pEdgeTable.add(edge);
                        // 设置新的初始状态
                        start = newStart;
                        // 最低位为1表示初始状态有入边
                        transType |= 1;
                }
                //结束状态有出边
                if (haveOutsideEdge(pEndStateTable)) {
                        // 新增一个终止状态
                        State newEnd = new State(pStateTable.size(), StateType.UNMATCH, LexemeCategory.EMPTY);
                        addState(newEnd);
                        // 添加结束状态到newEnd的ε边
                        for (State state: pEndStateTable) {
                                Edge edge = new Edge(state.getStateId(), newEnd.getStateId(), DriverType.NULL);
                                pEdgeTable.add(edge);
                        }
                        // 清空当前结束状态
                        pEdgeTable.clear();
                        // 设置新的结束状态
                        pEndStateTable.add(newEnd);
                        // 第二低位为1表示终结状态有出边
                        transType |= 2;
                }
                return transType;
        }

        /**
         * 合并初始状态
         * @param NFA
         */
        public void mergeStart(Graph NFA) {
                //原来初始状态
                State start = NFA.getStart();
                for (Edge edge: NFA.getpEdgeTable()) {
                        // 找到所有从初始状态出发的边
                        if (edge.getFromState() == start.getStateId()) {
                                edge.setFromState(0);
                        }
                }
                pStateTable.remove(start);
        }

        /**
         * 合并终结状态
         * @param NFA
         * @param endStateId
         */
        public void mergeEndState(Graph NFA, int endStateId) {
                ArrayList<State> newEndList = new ArrayList<>();
                for (State state: NFA.pEndStateTable) {
                        for (Edge edge: NFA.getpEdgeTable()) {
                                // 找到所有到达终结状态的边
                                if (edge.getNextState() == state.getStateId()) {
                                        edge.setNextState(endStateId);
                                }
                        }
                        pStateTable.remove(state);
                        newEndList.add(state);
                }
                for (State state: newEndList) {
                        pEndStateTable.remove(state);
                }
        }

        @Override
        public String toString() {
                String s = "Graph{" + '\n';
                s += " graphId: " + graphId + '\n';
                s += " numOfStates: " + numOfStates + '\n';
                s += " start: " + start.toString() + '\n';
                s += " pEndStateTable: " + pEndStateTable.size() + '\n';
                for (State state : pEndStateTable) {
                        s += "  " + state.toString() + '\n';
                }
                s += " pEdgeTable: " + pEdgeTable.size() + '\n';
                for (Edge edge : pEdgeTable) {
                        s += "  " + edge.toString() + '\n';
                }
                s += " pStateTable: " + numOfStates + '\n';
                for (State state : pStateTable) {
                        s += "  " + state.toString() + '\n';
                }
                return s+"}";
        }
}


