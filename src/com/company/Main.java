package com.company;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
* OS    : Windows 10 Pro 64bit
* CPU   : Intel(R) Core(TM) i3-6100 CPU @ 3.70Hz 3.70GHz
* Tool  : IntelliJ IDEA 2016.3.6
*/

public class Main {
    public static void main(String[] args) {

        int depth = -1;                                 // 깊이 확인
        int inputMissionary;                            // 선교사 수
        int inputCannibal;                              // 식인종 수
        int maxPassengers;                              // 배의 최대 탑승 인원
        Queue<Node> mQueue = new LinkedList<>();        // 사용할 큐(Queue)
        HashSet<Node> checked = new HashSet<>();        // 중복확인용 HashSet, 체크를 위해 노드 클래스에 HashCode와 equal 메소드 오버라이드

        Scanner mScanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("선교사의 수를 입력 : ");
                inputMissionary = mScanner.nextInt();
                System.out.print("식인종의 수를 입력 : ");
                inputCannibal = mScanner.nextInt();
                System.out.print("배의 탑승인원을 입력(2 ~ 선교사, 식인종 중 최소값) : ");
                maxPassengers = mScanner.nextInt();
                if (inputCannibal > inputMissionary             // 식인종이 선교사보다 많거나
                        || maxPassengers < 2                    // 배 탑승인원이 2명보다 작거나
                        || maxPassengers > inputCannibal)       // 배 탑승인원이 식인종보다 많거나
                    throw new Exception();
                else if (inputCannibal == inputMissionary) {                    // 선교사와 식인종의 수가 같을 땐, 최소 3명까지만...
                    if (inputCannibal <= 3 && inputMissionary <= 3) break;
                    else throw new Exception();
                } else break;
            } catch (Exception e) {
                mScanner = new Scanner(System.in);
                System.out.println("선교사의 수가 식인종보다 적거나, 잘못된 입력입니다.");
            }
        }
        Node IS = new Node(inputMissionary, inputCannibal, 0, 0, 0, null);        // 초기상태(Initial State)
        Node GS = new Node(0, 0, inputMissionary, inputCannibal, 1, null);        // 목표상태(Goal State)

        Node CS;        // 현재상태(Current State)
        mQueue.add(IS); // 초기상태 큐 삽입

        while (!mQueue.isEmpty()) {
            CS = mQueue.poll();

            if (CS.equals(GS)) {
                //목표를 찾았다면 목표의 부모노드를 통해 경로 출력
                while (CS != null) {
                    CS.PrintNode();
                    if (CS.parentNode != null)
                        System.out.print("\n\t↑\n");
                    CS = CS.parentNode;
                    depth++;
                }
                System.out.print("\n* 깊이 : " + depth);
                break;
            }

            if (!checked.contains(CS)                                                          // 현재상태가 중복되지 않을때
                    && (CS.rightMissionary >= CS.rightCannibal || CS.rightMissionary == 0)     // 오른쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                    && (CS.leftMissionary >= CS.leftCannibal || CS.leftMissionary == 0)) {     // 왼쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                if (CS.boat == 0) {
                    // 보트가 왼쪽에 있을때. 즉, leftMissionary, leftCannibal 값을 수정
                    for (int i = 1; i <= maxPassengers; i++) {
                        mQueue.add(new Node(CS.leftMissionary - i, CS.leftCannibal, CS.rightMissionary + i, CS.rightCannibal, 1, CS));
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal - i, CS.rightMissionary, CS.rightCannibal + i, 1, CS));
                    }
                    if (CS.leftCannibal > 0 && CS.leftMissionary > 0)
                        mQueue.add(new Node(CS.leftMissionary - 1, CS.leftCannibal - 1, CS.rightMissionary + 1, CS.rightCannibal + 1, 1, CS));


                } else {
                    //보트가 오른쪽에 있을때. 즉, rightMissionary, rightCannibal 값을 수정
                    for (int i = 1; i <= maxPassengers; i++) {
                        mQueue.add(new Node(CS.leftMissionary + i, CS.leftCannibal, CS.rightMissionary - i, CS.rightCannibal, 0, CS));
                        mQueue.add(new Node(CS.leftMissionary, CS.leftCannibal + i, CS.rightMissionary, CS.rightCannibal - i, 0, CS));
                    }
                    if (CS.rightCannibal > 0 && CS.rightMissionary > 0)
                        mQueue.add(new Node(CS.leftMissionary + 1, CS.leftCannibal + 1, CS.rightMissionary - 1, CS.rightCannibal - 1, 0, CS));
                }
                // 중복확인을 위해 HashSet에 값 추가
                checked.add(CS);
            }
        }
    }
}
