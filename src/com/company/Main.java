package com.company;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/*
* OS    : Windows 10 Pro 64bit
* CPU   : Intel(R) Core(TM) i3-6100 CPU @ 3.70Hz 3.70GHz
* Tool  : IntelliJ IDEA 2016.3.6
* */

public class Main extends Application {

    private static Node finalGoalState;

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
                finalGoalState = CS;
                while (CS != null) {
                    CS.PrintNode();
                    if (CS.parentNode != null)
                        System.out.print("\n\t↑\n");
                    CS = CS.parentNode;
                    depth++;
                }
                System.out.print("\n* 깊이 : " + depth);
                launch(args);
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

    @Override
    public void start(Stage stage) throws Exception {
        final int WIDTH_SIZE = 1600;
        final int HEIGHT_SIZE = 900;
        ImageView empty = new ImageView();



        Queue<ImageView> leftMissionary = new LinkedList<>();
        Queue<ImageView> leftCannibal = new LinkedList<>();
        Queue<ImageView> rightMissionary = new LinkedList<>();
        Queue<ImageView> rightCannibal = new LinkedList<>();
        stage.setTitle("Missionary And Cannibal lol");

        ImageView missionary1 = new ImageView();
        imageViewInitialize(missionary1, "missionary.png", 600, -50);
        ImageView missionary2 = new ImageView();
        imageViewInitialize(missionary2, "missionary.png", 700, -50);
        ImageView missionary3 = new ImageView();
        imageViewInitialize(missionary3, "missionary.png", 800, -50);
        ImageView cannibal1 = new ImageView();
        imageViewInitialize(cannibal1, "cannibal.png", 500, -100);
        ImageView cannibal2 = new ImageView();
        imageViewInitialize(cannibal2, "cannibal.png", 600, -100);
        ImageView cannibal3 = new ImageView();
        imageViewInitialize(cannibal3, "cannibal.png", 700, -100);
        ImageView boat = new ImageView();
        imageViewInitialize(boat, "boat.png", 300, 100);
        rightMissionary.add(missionary3);
        rightMissionary.add(missionary2);
        rightMissionary.add(missionary1);
        rightCannibal.add(cannibal3);
        rightCannibal.add(cannibal2);
        rightCannibal.add(cannibal1);

        stage.setWidth(WIDTH_SIZE);
        stage.setHeight(HEIGHT_SIZE);
        Pane sp = new StackPane();


        sp.setOnMousePressed(new EventHandler<MouseEvent>() {
            Node finalState = finalGoalState;
            long time = System.currentTimeMillis();

            @Override
            public void handle(MouseEvent event) {
                if ((System.currentTimeMillis() - time) / 1000.0 > 1.1) {
                    Queue<ImageView> temp1 = new LinkedList<>();
                    temp1.addAll(leftMissionary);
                    Queue<ImageView> temp2 = new LinkedList<>();
                    temp2.addAll(leftCannibal);
                    Queue<ImageView> temp3 = new LinkedList<>();
                    temp3.addAll(rightMissionary);
                    Queue<ImageView> temp4 = new LinkedList<>();
                    temp4.addAll(rightCannibal);

                    for (int i = 0; i < leftMissionary.size(); i++)
                        setPosition(temp1.poll(), i == 0 ? -600 : i == 1 ? -700 : -800, -50);
                    for (int i = 0; i < leftCannibal.size(); i++)
                        setPosition(temp2.poll(), i == 0 ? -550 : i == 1 ? -650 : -750, -100);
                    for (int i = 0; i < rightMissionary.size(); i++)
                        setPosition(temp3.poll(), i == 0 ? 600 : i == 1 ? 700 : 800, -50);
                    for (int i = 0; i < rightCannibal.size(); i++)
                        setPosition(temp4.poll(), i == 0 ? 500 : i == 1 ? 600 : 700, -100);

                    if (finalState.parentNode == null) {
                        Alert jobDone = new Alert(Alert.AlertType.INFORMATION);
                        jobDone.setTitle("진행완료");
                        jobDone.setContentText("진행이 완료되었습니다.");
                        jobDone.setHeaderText(null);
                        jobDone.show();
                    } else {
                        int moveMissionary = finalState.rightMissionary - finalState.parentNode.rightMissionary;
                        int moveCannibal = finalState.rightCannibal - finalState.parentNode.rightCannibal;

                        if (moveCannibal == moveMissionary) {
                            switch (moveCannibal + moveMissionary) {
                                case 2: {
                                    goTo(rightMissionary.peek(), rightCannibal.peek(), boat, finalState);
                                    leftMissionary.add(rightMissionary.poll());
                                    leftCannibal.add(rightCannibal.poll());
                                    break;
                                }
                                case -2: {
                                    goTo(leftMissionary.peek(), leftCannibal.peek(), boat, finalState);
                                    rightMissionary.add(leftMissionary.poll());
                                    rightCannibal.add(leftCannibal.poll());
                                    break;
                                }
                            }
                        } else {
                            switch (moveMissionary) {
                                case 2: {
                                    ImageView tmp = rightMissionary.poll();
                                    goTo(tmp, rightMissionary.peek(), boat, finalState);
                                    leftMissionary.add(tmp);
                                    leftMissionary.add(rightMissionary.poll());
                                    break;
                                }
                                case 1: {
                                    goTo(rightMissionary.peek(), empty, boat, finalState);
                                    leftMissionary.add(rightMissionary.poll());
                                    break;
                                }
                                case -1: {
                                    goTo(leftMissionary.peek(), empty, boat, finalState);
                                    rightMissionary.add(leftMissionary.poll());
                                    break;
                                }
                                case -2: {
                                    ImageView tmp = leftMissionary.poll();
                                    goTo(tmp, leftMissionary.peek(), boat, finalState);
                                    rightMissionary.add(tmp);
                                    rightMissionary.add(leftMissionary.poll());
                                    break;
                                }
                            }
                            switch (moveCannibal) {
                                case 2: {
                                    ImageView tmp = rightCannibal.poll();
                                    goTo(tmp, rightCannibal.peek(), boat, finalState);
                                    leftCannibal.add(tmp);
                                    leftCannibal.add(rightCannibal.poll());
                                    break;
                                }
                                case 1: {
                                    goTo(rightCannibal.peek(), empty, boat, finalState);
                                    leftCannibal.add(rightCannibal.poll());
                                    break;
                                }
                                case -1: {
                                    goTo(leftCannibal.peek(), empty, boat, finalState);
                                    rightCannibal.add(leftCannibal.poll());
                                    break;
                                }
                                case -2: {
                                    ImageView tmp = leftCannibal.poll();
                                    goTo(tmp, leftCannibal.peek(), boat, finalState);
                                    rightCannibal.add(tmp);
                                    rightCannibal.add(leftCannibal.poll());
                                    break;
                                }
                            }
                        }
                        System.out.println("leftCannibal : " + leftCannibal.size() + "\trightCannibal : " + rightCannibal.size());
                        System.out.println("leftMissinary : " + leftMissionary.size() + "\trightMissinary : " + rightMissionary.size());
                        System.out.println("----------------------------------------------");
                        finalState = finalState.parentNode;
                        time = System.currentTimeMillis();
                    }
                }
            }
        });

        BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
        sp.setBackground(new Background(new BackgroundImage(new Image("/backgroundimg.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                bgSize)));
        sp.getChildren().addAll(boat, cannibal1, cannibal2, cannibal3, missionary1, missionary2, missionary3, empty);
        stage.setResizable(false);
        stage.setScene(new Scene(sp));

        stage.show();
        Alert jobDone = new Alert(Alert.AlertType.INFORMATION);
        jobDone.setTitle("안내");
        jobDone.setContentText("마우스를 클릭할 떄 마다 진행됩니다.");
        jobDone.setHeaderText(null);
        jobDone.show();
    }

    private void setPosition(ImageView iv, double x, double y) {
        iv.setTranslateX(x);
        iv.setTranslateY(y);
    }

    private void imageViewInitialize(ImageView iv, String imagePath, double x, double y) {
        iv.setImage(new Image(imagePath));
        setPosition(iv, x, y);
    }

    private void goTo(ImageView iv1, ImageView iv2, ImageView boat, Node node) {
        TranslateTransition translateTransition = new TranslateTransition();
        TranslateTransition translateTransition1 = new TranslateTransition();
        TranslateTransition translateTransition2 = new TranslateTransition();
        setPosition(iv1, node.boat != 0 ? 250 : -250, 0);
        setPosition(iv2, node.boat != 0 ? 380 : -120, 0);

        translateTransition.setDuration(Duration.millis(1000));
        translateTransition1.setDuration(Duration.millis(1000));
        translateTransition2.setDuration(Duration.millis(1000));
        translateTransition.setNode(boat);
        translateTransition.setByX(node.boat != 0 ? -500 : 500);
        translateTransition1.setNode(iv1);
        translateTransition1.setByX(node.boat != 0 ? -500 : 500);
        translateTransition2.setNode(iv2);
        translateTransition2.setByX(node.boat != 0 ? -500 : 500);
        translateTransition.play();
        translateTransition1.play();
        translateTransition2.play();
    }
}
