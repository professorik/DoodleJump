package Game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMain extends Application {

    public static Pane appRoot = new Pane();
    public static Pane gameRoot = new Pane();

    //600 , 590, 350
    public static int WIDTH = 758;
    public static int HEIGHT = 886;
    public static int X_ENTER = 450;

    public Bird bird = new Bird();

    public static boolean isLose = false;

    public static ArrayList<Wall> walls = new ArrayList<>();
    public static ArrayList<Platform> platforms = new ArrayList<>();

    public static int score = 0;
    public Label label = new Label("" + score);

    //UI
    private Button buttonRestart;
    private Label scoreLose;
    private Label bestScore;
    private ImageView imageView;
    private Group UI_lose;

    boolean flag1 = false;
    boolean flag2 = false;

    private AnimationTimer animationTimer;

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(getGameRoom());

        scene.getStylesheets().addAll(this.getClass().getResource("/styles/style.css").toExternalForm());

        scene.setOnMouseClicked(event -> bird.jump());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.A){
                    flag1 = true; flag2 = false;
                   // bird.velocity = new Point2D(-30 , bird.velocity.getY());
                   // bird.moveX((int) bird.velocity.getX());
                }else if (event.getCode() == KeyCode.D){
                    flag2 = true; flag1 = false;
                   // bird.velocity = new Point2D(30 , bird.velocity.getY());
                   // bird.moveX((int) bird.velocity.getX());
                }else if (event.getCode() == KeyCode.SPACE){
                    bird.jump();
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                flag1 = false;
                flag2 = false;
            }
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Floppy Bird!");
        primaryStage.setScene(scene);
        primaryStage.show();

        UI_lose = new Group();
        initUILoseElem(primaryStage);
        appRoot.getChildren().addAll(UI_lose);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        animationTimer.start();
    }

    private Parent getGameRoom() {

        gameRoot.setPrefSize(WIDTH , HEIGHT);

        for (int i = 0; i < 100; i++) {
            int enter = (int)(Math.random()*100 + 300);
            int height = new Random().nextInt(WIDTH - enter);

            Wall wall = new Wall(height);
            wall.setTranslateX(i * X_ENTER + WIDTH);
            wall.setTranslateY(height - WIDTH);
            wall.setRotate(180);

            walls.add(wall);

            Wall wall1 = new Wall(WIDTH - height - enter);
            wall1.setTranslateX(wall.getTranslateX());
            wall1.setTranslateY(height + enter);

            walls.add(wall1);
        }


        Platform platform = new Platform(20);
        platform.setTranslateX(250);
        platform.setTranslateY(700);
        platforms.add(platform);

        gameRoot.getChildren().addAll(platforms);
        gameRoot.getChildren().addAll(walls);
        gameRoot.getChildren().add(bird);
        label.setTranslateX(WIDTH/2 - 10); label.setTranslateY(HEIGHT/4 - 100);
        appRoot.getChildren().addAll(gameRoot, label);
        return appRoot;
    }

    public void update(){
        if (isLose){
            label.setVisible(false);
            scoreLose.setText(label.getText());
            UI_lose.getChildren().clear();
            UI_lose.getChildren().addAll(buttonRestart, imageView, scoreLose, bestScore);
            try {
                FileReader fr = new FileReader( "src/best_result.txt" );
                Scanner scanner = new Scanner(fr);
                String bestScoreInt = scanner.hasNext()? scanner.nextLine(): "";
                bestScore.setText(bestScoreInt);
                fr.close();
                if (score > Integer.valueOf(bestScoreInt)){
                    FileWriter fw = new FileWriter("src/best_result.txt");
                    fw.write(String.valueOf(score));
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            scoreLose.setTranslateX(WIDTH/2 - 10 * scoreLose.getText().length());
            bestScore.setTranslateX(WIDTH/2 - 10 * bestScore.getText().length());
            animationTimer.stop();
        }else {
            if (bird.velocity.getY() < 5) {
                //Скорость падения
                bird.velocity = bird.velocity.add(0, 1);
            }
            if (flag1) {
                //Движение влево
                bird.velocity = bird.velocity.add(-1, 0);
            }else if (flag2){
                //Движение вправо
                bird.velocity = bird.velocity.add(1, 0);
            }else {
                //TODO: настроить коеффициент "затухания" объекта
                bird.velocity = new Point2D(bird.velocity.getX() / 1.05 , bird.velocity.getY());
            }

            bird.moveX((int) bird.velocity.getX());
            bird.moveY((int) bird.velocity.getY());
            label.setText("" + score);
           /* bird.translateXProperty().addListener((obs, old, newValue) -> {
                int offset = newValue.intValue();
                if (offset > 200) {
                    gameRoot.setTranslateX(-(offset - 200));
                }
            });*/
        }
    }

    private void initUILoseElem(Stage primaryStage){
        Image imageRestart = new Image("/resources/restart.png");
        buttonRestart = new Button("", new ImageView(imageRestart));
        buttonRestart.setTranslateX((WIDTH - imageRestart.getWidth())/2);
        buttonRestart.setTranslateY((HEIGHT - imageRestart.getHeight())/2);
        buttonRestart.setStyle("-fx-background-color: transparent");
        buttonRestart.setOnAction(event -> {
            appRoot = new Pane();
            gameRoot = new Pane();
            walls.clear();
            score = 0;
            label = new Label("" + score);
            isLose = false;
            bird = new Bird();
            UI_lose = new Group();
            Scene scene = new Scene(getGameRoom());
            scene.getStylesheets().addAll(this.getClass().getResource("/styles/style.css").toExternalForm());
            scene.setOnMouseClicked(event2 -> bird.jump());
            primaryStage.setScene(scene);
            initUILoseElem(primaryStage);
            appRoot.getChildren().addAll(UI_lose);
            animationTimer.start();
        });
        imageView = new ImageView("/resources/score.png");
        imageView.setTranslateX((WIDTH - imageView.getImage().getWidth() + 15)/2);
        imageView.setTranslateY(HEIGHT/2 - 1.3*imageView.getImage().getHeight());
        scoreLose = new Label(); scoreLose.setTranslateX(WIDTH/2 - 10); scoreLose.setTranslateY(HEIGHT/4 - 30);
        bestScore = new Label(); bestScore.setTranslateX(WIDTH/2 - 20); bestScore.setTranslateY(HEIGHT/2 - 160);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
