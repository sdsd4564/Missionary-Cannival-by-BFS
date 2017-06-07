package com.company;


public class SearchNode {
    int leftMissionary;
    int leftCannibal;
    int rightMissionary;
    int rightCannibal;
    int boat;
    SearchNode parentSearchNode;

    SearchNode(int leftMissionary, int leftCannibal, int rightMissionary, int rightCannibal, int boat, SearchNode parentSearchNode) {
        this.leftMissionary = leftMissionary;
        this.leftCannibal = leftCannibal;
        this.rightMissionary = rightMissionary;
        this.rightCannibal = rightCannibal;
        this.boat = boat;
        this.parentSearchNode = parentSearchNode;
    }

    // 노드 출력용
    void PrintNode() {
        System.out.print("(" + leftMissionary + "," + leftCannibal + "," + rightMissionary + "," + rightCannibal + "," + boat + ")");
    }

    // HashCode 수정, HashSet 사용을 위해 HashCode를 선교사와 식인종, 배 위치 값으로 참조
    @Override
    public int hashCode() {
        return Integer.toString(leftMissionary).hashCode()
                + Integer.toString(leftCannibal).hashCode()
                + Integer.toString(rightMissionary).hashCode()
                + Integer.toString(rightCannibal).hashCode()
                + Integer.toString(boat).hashCode();
    }

    // HashSet 사용을 위해 수정, 각 선교사값과 식인종값과 배 위치값을 비교해서 boolean값 반환.
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        SearchNode other = (SearchNode) obj;
        if (this.rightCannibal == other.rightCannibal && this.rightMissionary == other.rightMissionary &&
                this.leftMissionary == other.leftMissionary && this.leftCannibal == other.leftCannibal && this.boat == other.boat)
            return true;
        else
            return false;
    }

}
