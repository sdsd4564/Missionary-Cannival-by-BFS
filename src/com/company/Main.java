package com.company;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private static final int WIDTH_SIZE = 1600;
    private static final int HEIGHT_SIZE = 900;

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
        stage.setTitle("Missionary And Cannibal lol");
        TranslateTransition toTheLeft = new TranslateTransition();
        TranslateTransition toTheRight = new TranslateTransition();
        toTheLeft.setDuration(Duration.millis(1000));
        toTheRight.setDuration(Duration.millis(1000));

        ImageView missionary1 = new ImageView();
        ImageView missionary2 = new ImageView();
        ImageView missionary3 = new ImageView();
        ImageView cannibal1 = new ImageView();
        ImageView cannibal2 = new ImageView();
        ImageView cannibal3 = new ImageView();
        ImageView boat = new ImageView();

        imageViewInitialize(missionary1, "missionary.png", 700, -50);
        imageViewInitialize(missionary2, "missionary.png", 650, -50);
        imageViewInitialize(missionary3, "missionary.png", 675, -100);
        imageViewInitialize(cannibal1, "cannibal.png", 475, -130);
        imageViewInitialize(cannibal2, "cannibal.png", 450, -100);
        imageViewInitialize(cannibal3, "cannibal.png", 500, -100);
        imageViewInitialize(boat, "boat.png", 300, 100);
        toTheLeft.setNode(boat);
        toTheRight.setNode(boat);

        toTheLeft.setByX(-500);
        toTheRight.setByX(500);


        stage.setWidth(WIDTH_SIZE);
        stage.setHeight(HEIGHT_SIZE);
        Pane sp = new StackPane();
        sp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            boolean flag = true;
            @Override
            public void handle(MouseEvent event) {
                if (flag) {
                    goToThe(true, missionary1, cannibal1, boat);
                } else {
                    goToThe(false, missionary1, cannibal1, boat);
                }
                flag = !flag;
                cannibal1.setFocusTraversable(true);
            }
        });
        BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, true);
        sp.setBackground(new Background(new BackgroundImage(new Image("/backgroundimg.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                bgSize)));
        sp.getChildren().addAll(boat, missionary3, missionary1, missionary2, cannibal1, cannibal2, cannibal3);

        stage.setResizable(false);
        stage.setScene(new Scene(sp));

        stage.show();
    }

    private void imageViewInitialize(ImageView iv, String imagePath, double x, double y) {
        iv.setImage(new Image(imagePath));
        iv.setTranslateX(x);
        iv.setTranslateY(y);
    }

    private void goToThe(boolean isRight, ImageView iv1, ImageView iv2, ImageView boat) {
        TranslateTransition translateTransition = new TranslateTransition();
        TranslateTransition translateTransition1 = new TranslateTransition();
        TranslateTransition translateTransition2 = new TranslateTransition();
        iv1.setTranslateX(isRight ? 250 : -250);
        iv2.setTranslateX(isRight ? 380 : -120);
        iv1.setTranslateY(0);
        iv2.setTranslateY(0);
        translateTransition.setDuration(Duration.millis(1000));
        translateTransition1.setDuration(Duration.millis(1000));
        translateTransition2.setDuration(Duration.millis(1000));
        translateTransition.setNode(boat);  translateTransition.setByX(isRight ? -500 : 500);
        translateTransition1.setNode(iv1);  translateTransition1.setByX(isRight ? -500 : 500);
        translateTransition2.setNode(iv2);  translateTransition2.setByX(isRight ? -500 : 500);
        translateTransition.play();
        translateTransition1.play();
        translateTransition2.play();
    }
}
