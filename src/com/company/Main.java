package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/*
* OS    : Windows 10 Pro 64bit
* CPU   : Intel(R) Core(TM) i3-6100 CPU @ 3.70Hz 3.70GHz
* Tool  : IntelliJ IDEA 2016.3.6
* */

public class Main {

    public static void main(String[] args) {
        int depth = -1;                                 // 깊이 확인
        Queue<Node> mQueue = new LinkedList<>();        // 사용할 큐(Queue)
        HashSet<Node> checked = new HashSet<>();        // 중복확인용 HashSet, 체크를 위해 노드 클래스에 HashCode와 equal 메소드 오버라이드

        Node IS = new Node(3, 3, 0, 0, 0, null);        // 초기상태(Initial State)
        Node GS = new Node(0, 0, 3, 3, 1, null);        // 목표상태(Goal State)

        Node CS;        // 현재상태(Current State)
        mQueue.add(IS); // 초기상태 큐 삽입

        while (!mQueue.isEmpty()) {
            CS = mQueue.poll();

            if (CS.equals(GS)) {
                //목표를 찾았다면 목표의 부모노드를 통해 경로 출력
                while (CS != null) {
                    CS.PrintNode();
                    if (CS.parentNode != null)
                        System.out.print(" <- ");
                    CS = CS.parentNode;
                    depth++;
                }
                System.out.println("\n* 깊이 : " + depth);
                break;
            }

            if (!checked.contains(CS)                                                          // 현재상태가 중복되지 않을때
                    && (CS.rightMissionary >= CS.rightCannibal || CS.rightMissionary == 0)     // 오른쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                    && (CS.leftMissionary >= CS.leftCannibal || CS.leftMissionary == 0)) {     // 왼쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                if (CS.boat == 0) {
                    /*
                    * 보트가 왼쪽에 있을때. 즉, leftMissionary와 leftCannival만 값을 수정
                    */
                    if (CS.leftMissionary > 1) {
                        mQueue.add(new Node(CS.leftMissionary - 2, CS.leftCannibal, CS.rightMissionary + 2, CS.rightCannibal, 1, CS));
                        mQueue.add(new Node(CS.leftMissionary - 1, CS.leftCannibal, CS.rightMissionary + 1, CS.rightCannibal, 1, CS));
                    } else if (CS.leftMissionary == 1)
                        mQueue.add(new Node(CS.leftMissionary - 1, CS.leftCannibal, CS.rightMissionary + 1, CS.rightCannibal, 1, CS));

                    if (CS.leftCannibal > 1) {
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal - 2, CS.rightMissionary, CS.rightCannibal + 2, 1, CS));
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal - 1, CS.rightMissionary, CS.rightCannibal + 1, 1, CS));
                    } else if (CS.leftCannibal == 1) {
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal - 1, CS.rightMissionary, CS.rightCannibal + 1, 1, CS));
                    }
                    if (CS.leftCannibal > 0 && CS.leftMissionary > 0)
                        mQueue.add(new Node(CS.leftMissionary - 1, CS.leftCannibal - 1,
                                CS.rightMissionary + 1, CS.rightCannibal + 1, 1, CS));

                } else {
                    /*
                    * 보트가 오른쪽에 있을때. 즉, rightMissionary와 rightCannival만 값을 수정
                    */
                    if (CS.rightMissionary > 1) {
                        mQueue.add(new Node(CS.leftMissionary + 2, CS.leftCannibal, CS.rightMissionary - 2, CS.rightCannibal, 0, CS));
                        mQueue.add(new Node(CS.leftMissionary + 1, CS.leftCannibal, CS.rightMissionary - 1, CS.rightCannibal, 0, CS));
                    } else if (CS.rightMissionary == 1) {
                        mQueue.add(new Node(CS.leftMissionary + 1, CS.leftCannibal, CS.rightMissionary - 1, CS.rightCannibal, 0, CS));
                    }

                    if (CS.rightCannibal > 1) {
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal + 2, CS.rightMissionary, CS.rightCannibal - 2, 0, CS));
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal + 1, CS.rightMissionary, CS.rightCannibal - 1, 0, CS));
                    } else if (CS.rightCannibal == 1) {
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal + 1, CS.rightMissionary, CS.rightCannibal - 1, 0, CS));
                    }

                    if (CS.rightCannibal > 0 && CS.rightMissionary > 0)
                        mQueue.add(new Node(CS.leftMissionary + 1, CS.leftCannibal + 1,
                                CS.rightMissionary - 1, CS.rightCannibal - 1, 0, CS));

                }
                // 중복확인을 위해 HashSet에 값 추가
                checked.add(CS);
            }
        }
    }
}
