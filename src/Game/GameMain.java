package Game;

import Game.Platforms.BreakingPlatform;
import Game.Platforms.IcePlatform;
import Game.Platforms.SimplePlatform;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    public static ArrayList<PlatformPrototype> platforms = new ArrayList<>();

    public static double score = 0;
    public Label label = new Label("" + score);

    private double platformsX;
    private double platformsY;
    private double distans;
    public static boolean jp;
    //UI
    private Button buttonRestart;
    private Label scoreLose;
    private Label bestScore;
    private ImageView imageView;
    private Group UI_lose;

    boolean flag1 = false;
    boolean flag2 = false;


    private static final double speedH = 0.6;

    private AnimationTimer animationTimer;

    public static Date time = new Date();

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(getGameRoom());

        scene.getStylesheets().addAll(this.getClass().getResource("/styles/style.css").toExternalForm());

        scene.setOnMouseClicked(event -> bird.jump());
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                flag1 = true;
                flag2 = false;
                bird.rectangle.setImage(new Image("/resources/player_2.png", 60, 80, true, true, false));
            } else if (event.getCode() == KeyCode.D) {
                flag2 = true;
                flag1 = false;
                bird.rectangle.setImage(new Image("/resources/player_1.png", 60, 80, true, true, false));
            } else if (event.getCode() == KeyCode.SPACE) {
                bird.jump();
            }
        });
        scene.setOnKeyReleased(event -> {
            flag1 = false;
            flag2 = false;
        });

        primaryStage.getIcons().add(new Image("/resources/Doodle_jump_icon.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Doodle Jump");
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
        gameRoot.setPrefSize(WIDTH, HEIGHT);

        //TODO: вместо цикла вставлять блоки
        for (int i = 0; i < 1; ++i) {
            SimplePlatform platform = new SimplePlatform(20, true);
            platform.setTranslateX(345);
            platform.setTranslateY(750);
            platforms.add(platform);
        }
        platformsX = 345;
        platformsY = 750;
        while (platformsY > -30000) {
            SimplePlatform platform = new SimplePlatform(20, Math.random() * 10 > 5);
            IcePlatform icePlatform = new IcePlatform(20 ,Math.random() * 10 > 5);
            BreakingPlatform breakingPlatform = new BreakingPlatform(20 ,Math.random() * 10 > 5);

            platformsX = Math.random() * 656;
            platformsY = platformsY - (Math.random() * 180 + 30);

            platform.setTranslateX(platformsX);
            platform.setTranslateY(platformsY);

            icePlatform.setTranslateX(platformsX + 90);
            icePlatform.setTranslateY(platformsY);

            breakingPlatform.setTranslateX(platformsX - 90);
            breakingPlatform.setTranslateY(platformsY);

            platforms.add(platform);
            platforms.add(icePlatform);
            platforms.add(breakingPlatform);
        }

        //То как двигать сцену (нам нужно вниз) в строке №172 класса GameMain
        gameRoot.getChildren().addAll(platforms);
        gameRoot.getChildren().add(bird);
        label.setTranslateX(WIDTH / 2 - 10);
        label.setTranslateY(20);
        appRoot.getChildren().addAll(gameRoot, label);
        return appRoot;
    }

    public void update() {
        clearPlatfroms();

        if (new Date().getTime() - time.getTime() > 1500) {
            //TODO: все, что при лузе
            isLose = true;
        }
        if (bird.velocity.getY() > distans)
            distans = bird.velocity.getY();
        if (bird.velocity.getY() >= distans) {
            bird.translateYProperty().addListener((obs, old, newValue) -> {
                int offset = newValue.intValue();
                //if (offset < 400) {
                gameRoot.setTranslateY(Math.max(-(offset - 400), gameRoot.getTranslateY()));
                //}
            });
        }
        if (isLose) {
            label.setVisible(false);
            scoreLose.setText(label.getText());
            UI_lose.getChildren().clear();
            UI_lose.getChildren().addAll(buttonRestart, imageView, scoreLose, bestScore);
            try {
                FileReader fr = new FileReader("src/best_result.txt");
                Scanner scanner = new Scanner(fr);
                String bestScoreInt = scanner.hasNext() ? scanner.nextLine() : "";
                bestScore.setText(bestScoreInt);
                fr.close();
                if (score > new BigDecimal(bestScoreInt).longValue()) {
                    FileWriter fw = new FileWriter("src/best_result.txt");
                    fw.write(String.valueOf((int) score));
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            scoreLose.setTranslateX(WIDTH / 2 - 10 * scoreLose.getText().length());
            bestScore.setTranslateX(WIDTH / 2 - 10 * bestScore.getText().length());
            animationTimer.stop();
        } else {
            if (bird.velocity.getY() < 5) {
                //Скорость падения
                // System.out.println((new Date().getTime() - time.getTime())/50);
                bird.velocity = bird.velocity.add(0, 0.3);
                jp = false;
            }
            if (flag1) {
                //Движение влево
                bird.velocity = bird.velocity.add(-speedH, 0);
            } else if (flag2) {
                //Движение вправо
                bird.velocity = bird.velocity.add(speedH, 0);
            } else {
                //TODO: настроить коеффициент "затухания" объекта
                bird.velocity = new Point2D(bird.velocity.getX() / 1.05, bird.velocity.getY());
            }

            bird.moveX((int) bird.velocity.getX());
            bird.moveY((int) bird.velocity.getY() + (int) (new Date().getTime() - time.getTime()) / 100);
            score = gameRoot.getTranslateY();
            label.setText("" + (int) score);
            label.setTranslateX(WIDTH / 2 - 10 * label.getText().length());
        }
    }

    private void remove(ImageView rectangle) {
    }

    AnimationTimer menuAnim;

    private void initUILoseElem(Stage primaryStage) {
        Image imageRestart = new Image("/resources/restart.png");
        buttonRestart = new Button("", new ImageView(imageRestart));
        buttonRestart.setTranslateX((WIDTH - imageRestart.getWidth()) / 2);
        buttonRestart.setTranslateY((HEIGHT - imageRestart.getHeight()) / 2);
        buttonRestart.setStyle("-fx-background-color: transparent");
        buttonRestart.setOnAction(event -> {
            appRoot = new Pane();
            gameRoot = new Pane();
            platforms.clear();
            score = 0;
            label = new Label("" + score);
            isLose = false;
            bird = new Bird();
            UI_lose = new Group();
            distans = 0;

            Scene scene = new Scene(getGameRoom());
            scene.getStylesheets().addAll(this.getClass().getResource("/styles/style.css").toExternalForm());
            scene.setOnMouseClicked(event2 -> bird.jump());
            scene.setOnKeyPressed(event1 -> {
                if (event1.getCode() == KeyCode.A) {
                    flag1 = true;
                    flag2 = false;
                    bird.rectangle.setImage(new Image("/resources/player_2.png", 60, 80, true, true, false));
                } else if (event1.getCode() == KeyCode.D) {
                    flag2 = true;
                    flag1 = false;
                    bird.rectangle.setImage(new Image("/resources/player_1.png", 60, 80, true, true, false));
                } else if (event1.getCode() == KeyCode.SPACE) {
                    bird.jump();
                }
            });
            scene.setOnKeyReleased(event12 -> {
                flag1 = false;
                flag2 = false;
            });

            primaryStage.setScene(scene);
            //  initUILoseElem(primaryStage);
            appRoot.getChildren().addAll(UI_lose);
            time = new Date();
            animationTimer.start();
        });
        imageView = new ImageView("/resources/score.png");
        imageView.setTranslateX((WIDTH - imageView.getImage().getWidth() + 15) / 2);
        imageView.setTranslateY(HEIGHT / 2 - 1.3 * imageView.getImage().getHeight());
        scoreLose = new Label();
        scoreLose.setTranslateX(WIDTH / 2 - 10);
        scoreLose.setTranslateY(HEIGHT / 4 - 30);
        bestScore = new Label();
        bestScore.setTranslateX(WIDTH / 2 - 20);
        bestScore.setTranslateY(HEIGHT / 2 - 160);
        time = new Date();
        menuAnim = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //    loseMenuAnim();
            }
        };
        menuAnim.start();
    }

    private void loseMenuAnim() {
        if (buttonRestart.getTranslateY() > HEIGHT / 2) {
            buttonRestart.setTranslateY(buttonRestart.getTranslateY() - 1);
        } else {
            menuAnim.stop();
        }
    }


    //FIXME: ДЛЯ ДИМОНА
    private void clearPlatfroms() {
        for (int i = 0; i < platforms.size(); ++i) {
            if (gameRoot.getTranslateY() + platforms.get(i).getTranslateY() > HEIGHT) {
                platforms.remove(i);
            }
        }
    }

    /**
     * @param platform HEIGHT - height of screen
     * @return boolean
     */
    private boolean isUnderScreen(SimplePlatform platform) {
        return gameRoot.getTranslateY() + platform.getTranslateY() > HEIGHT;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
