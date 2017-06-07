package com.company;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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

public class Main extends Application {

    private static SearchNode finalGoalState;
    private static int finalMaxPassengers;

    public static void main(String[] args) {

        int depth = -1;                                 // 깊이 확인
        int inputMissionary;                            // 선교사 수
        int inputCannibal;                              // 식인종 수
        int maxPassengers;                              // 배의 최대 탑승 인원
        Queue<SearchNode> mQueue = new LinkedList<>();        // 사용할 큐(Queue)
        HashSet<SearchNode> checked = new HashSet<>();        // 중복확인용 HashSet, 체크를 위해 노드 클래스에 HashCode와 equal 메소드 오버라이드


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
        finalMaxPassengers = maxPassengers;
        SearchNode IS = new SearchNode(inputMissionary, inputCannibal, 0, 0, 0, null);        // 초기상태(Initial State)
        SearchNode GS = new SearchNode(0, 0, inputMissionary, inputCannibal, 1, null);        // 목표상태(Goal State)

        SearchNode CS;        // 현재상태(Current State)
        mQueue.add(IS); // 초기상태 큐 삽입

        while (!mQueue.isEmpty()) {
            CS = mQueue.poll();

            if (CS.equals(GS)) {
                //목표를 찾았다면 목표의 부모노드를 통해 경로 출력
                finalGoalState = CS;
                while (CS != null) {
                    CS.PrintNode();
                    if (CS.parentSearchNode != null)
                        System.out.print("\n\t↑\n");
                    CS = CS.parentSearchNode;
                    depth++;
                }
                System.out.println("\n* 깊이 : " + depth);
                launch(args);
                break;
            }

            if (!checked.contains(CS)                                                          // 현재상태가 중복되지 않을때
                    && (CS.rightMissionary >= CS.rightCannibal || CS.rightMissionary == 0)     // 오른쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                    && (CS.leftMissionary >= CS.leftCannibal || CS.leftMissionary == 0)) {     // 왼쪽 선교사가 식인종보다 같거나 클때 + 선교사가 아예 없을떄
                if (CS.boat == 0) {
                    // 보트가 왼쪽에 있을때. 즉, leftMissionary, leftCannibal 값을 수정
                    for (int i = 1; i <= maxPassengers; i++) {
                        mQueue.add(new SearchNode(CS.leftMissionary - i, CS.leftCannibal, CS.rightMissionary + i, CS.rightCannibal, 1, CS));
                        mQueue.add(new SearchNode(CS.leftMissionary, CS.leftCannibal - i, CS.rightMissionary, CS.rightCannibal + i, 1, CS));
                    }
                    if (CS.leftCannibal > 0 && CS.leftMissionary > 0)
                        mQueue.add(new SearchNode(CS.leftMissionary - 1, CS.leftCannibal - 1, CS.rightMissionary + 1, CS.rightCannibal + 1, 1, CS));


                } else {
                    //보트가 오른쪽에 있을때. 즉, rightMissionary, rightCannibal 값을 수정
                    for (int i = 1; i <= maxPassengers; i++) {
                        mQueue.add(new SearchNode(CS.leftMissionary + i, CS.leftCannibal, CS.rightMissionary - i, CS.rightCannibal, 0, CS));
                        mQueue.add(new SearchNode(CS.leftMissionary, CS.leftCannibal + i, CS.rightMissionary, CS.rightCannibal - i, 0, CS));
                    }
                    if (CS.rightCannibal > 0 && CS.rightMissionary > 0)
                        mQueue.add(new SearchNode(CS.leftMissionary + 1, CS.leftCannibal + 1, CS.rightMissionary - 1, CS.rightCannibal - 1, 0, CS));
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
        ImageView onCannibal = new ImageView("cannibal.png");
        ImageView onMissionary = new ImageView("missionary.png");
        Text countOnCannibal = new Text();
        countOnCannibal.setId("fancytext");
        Text countOnMissionary = new Text();
        countOnMissionary.setId("fancytext");
        stage.setTitle("Missionary And Cannibal lol");
        onCannibal.setVisible(false);
        onMissionary.setVisible(false);
        Text inputState = new Text(200, 808, "선교사 " + finalGoalState.rightMissionary + " 식인종 " + finalGoalState.rightCannibal + " 최대탑승인원 " + finalMaxPassengers);
        inputState.setId("bottomText");

        Text countRightC = new Text(1370, 209, finalGoalState.rightCannibal + "");
        Text countRightM = new Text(1250, 152, finalGoalState.rightMissionary + "");
        Text countLeftC = new Text(97, 266, "");
        Text countLeftM = new Text(217, 242, "");
        countRightC.setId("fancytext");
        countRightM.setId("fancytext");
        countLeftC.setId("fancytext");
        countLeftM.setId("fancytext");
        ImageView rightM = new ImageView();
        ImageView rightC = new ImageView();
        ImageView leftM = new ImageView();
        leftM.setVisible(false);
        ImageView leftC = new ImageView();
        leftC.setVisible(false);
        imageViewInitialize(rightM, "missionary.png", 1216.0, 167.0);
        imageViewInitialize(rightC, "cannibal.png", 1351.0, 219.0);
        imageViewInitialize(leftM, "missionary.png", 197.0, 252.0);
        imageViewInitialize(leftC, "cannibal.png", 77.0, 276.0);
        ImageView boat = new ImageView();
        imageViewInitialize(boat, "boat.png", 955.0, 493.0);

        stage.setWidth(WIDTH_SIZE);
        stage.setHeight(HEIGHT_SIZE);
        Pane sp = new Pane();

        sp.setOnMousePressed(new EventHandler<MouseEvent>() {
            SearchNode finalState = finalGoalState;
            long time = System.currentTimeMillis();

            @Override
            public void handle(MouseEvent event) {
                if ((System.currentTimeMillis() - time) / 1000.0 > 1.1) {
                    if (finalState.parentSearchNode == null) {
                        onCannibal.setVisible(false);
                        onMissionary.setVisible(false);
                        countOnCannibal.setText("");
                        countOnMissionary.setText("");
                        countLeftC.setText(finalState.leftCannibal+"");
                        countLeftM.setText(finalState.leftMissionary+"");
                        Alert jobDone = new Alert(Alert.AlertType.INFORMATION);
                        jobDone.setTitle("진행완료");
                        jobDone.setContentText("진행이 완료되었습니다.");
                        jobDone.setHeaderText(null);
                        jobDone.show();
                    } else {
                        int moveMissionary = Math.abs(finalState.rightMissionary - finalState.parentSearchNode.rightMissionary);
                        int moveCannibal = Math.abs(finalState.rightCannibal - finalState.parentSearchNode.rightCannibal);

                        countOnCannibal.setText(moveCannibal == 0 ? "" : "" + moveCannibal);
                        countOnMissionary.setText(moveMissionary == 0 ? "" : moveMissionary + "");
                        onCannibal.setVisible(moveCannibal != 0);
                        onMissionary.setVisible(moveMissionary != 0);
                        goTo(moveCannibal == 0 ? empty : onCannibal,
                                moveMissionary == 0 ? empty : onMissionary,
                                boat, countOnCannibal, countOnMissionary, finalState);

                        setCurrentState(finalState, countLeftM, countLeftC, countRightM, countRightC, leftM, leftC, rightM, rightC);
                        finalState = finalState.parentSearchNode;
                        finalState.PrintNode();
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

        sp.getChildren().addAll(boat, rightC, rightM, leftC, leftM, empty,
                countRightC, countRightM, countLeftC, countLeftM, onMissionary, onCannibal, countOnCannibal, countOnMissionary, inputState);
        stage.setResizable(false);
        Scene scene = new Scene(sp);
        stage.setScene(scene);
        scene.getStylesheets().add(Main.class.getResource("/com/company/stylesheet.css").toExternalForm());
        stage.show();
        Alert jobDone = new Alert(Alert.AlertType.INFORMATION);
        jobDone.setTitle("안내");
        jobDone.setContentText("마우스를 클릭할 떄 마다 진행됩니다.");
        jobDone.setHeaderText(null);
        jobDone.show();
    }

    private void setPosition(Node iv, double x, double y) {
        iv.setTranslateX(x);
        iv.setLayoutY(y);
    }

    private void setCurrentState(SearchNode node, Text t1, Text t2, Text t3, Text t4, ImageView iv1, ImageView iv2, ImageView iv3, ImageView iv4) {
        t1.setText(node.boat == 1 ? node.leftMissionary == 0 ? "" : node.leftMissionary + "" : node.parentSearchNode.leftMissionary == 0 ? "" : node.parentSearchNode.leftMissionary + "");
        t2.setText(node.boat == 1 ? node.leftCannibal == 0 ? "" : node.leftCannibal + "" : node.parentSearchNode.leftCannibal == 0 ? "" : node.parentSearchNode.leftCannibal + "");
        t3.setText(node.boat == 0 ? node.rightMissionary == 0 ? "" : node.rightMissionary + "" : node.parentSearchNode.rightMissionary == 0 ? "" : node.parentSearchNode.rightMissionary + "");
        t4.setText(node.boat == 0 ? node.rightCannibal == 0 ? "" : node.rightCannibal + "" : node.parentSearchNode.rightCannibal == 0 ? "" : node.parentSearchNode.rightCannibal + "");
        iv1.setVisible(node.boat == 1 ? node.leftMissionary != 0 : node.parentSearchNode.leftMissionary != 0);
        iv2.setVisible(node.boat == 1 ? node.leftCannibal != 0 : node.parentSearchNode.leftCannibal != 0);
        iv3.setVisible(node.boat == 0 ? node.rightMissionary != 0 : node.parentSearchNode.rightMissionary != 0);
        iv4.setVisible(node.boat == 0 ? node.rightCannibal != 0 : node.parentSearchNode.rightCannibal != 0);
    }

    private void imageViewInitialize(ImageView iv, String imagePath, double x, double y) {
        iv.setImage(new Image(imagePath));
        setPosition(iv, x, y);
    }

    private void goTo(ImageView iv1, ImageView iv2, ImageView boat, Text t1, Text t2, SearchNode searchNode) {
        TranslateTransition translateTransition = new TranslateTransition();
        TranslateTransition translateTransition1 = new TranslateTransition();
        TranslateTransition translateTransition2 = new TranslateTransition();
        TranslateTransition translateTransition3 = new TranslateTransition();
        TranslateTransition translateTransition4 = new TranslateTransition();
        setPosition(iv1, searchNode.boat != 0 ? 1025 : 525, 313);
        setPosition(iv2, searchNode.boat != 0 ? 1160 : 660, 313);
        setPosition(t1, searchNode.boat != 0 ? 1045 : 545, 303);
        setPosition(t2, searchNode.boat != 0 ? 1180 : 680, 303);

        translateTransition.setDuration(Duration.millis(1000));
        translateTransition1.setDuration(Duration.millis(1000));
        translateTransition2.setDuration(Duration.millis(1000));
        translateTransition3.setDuration(Duration.millis(1000));
        translateTransition4.setDuration(Duration.millis(1000));
        translateTransition.setNode(boat);
        translateTransition.setByX(searchNode.boat != 0 ? -500 : 500);
        translateTransition1.setNode(iv1);
        translateTransition1.setByX(searchNode.boat != 0 ? -500 : 500);
        translateTransition2.setNode(iv2);
        translateTransition2.setByX(searchNode.boat != 0 ? -500 : 500);
        translateTransition3.setNode(t1);
        translateTransition3.setByX(searchNode.boat != 0 ? -500 : 500);
        translateTransition4.setNode(t2);
        translateTransition4.setByX(searchNode.boat != 0 ? -500 : 500);
        translateTransition.play();
        translateTransition1.play();
        translateTransition2.play();
        translateTransition3.play();
        translateTransition4.play();
    }
}
