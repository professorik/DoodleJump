package Game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Bird extends Pane {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Point2D velocity;
    private ImageView rectangle2;

    private Circle circle;

    private long x = 0;

    //TODO: доделать скелет

    public Bird() {
        velocity = new Point2D(0, 0);
        setTranslateX(300);
        setTranslateY(100);
        rectangle2 = new ImageView(new Image("/resources/temp1406587188.png" , 60 , 80 , true , true , false));
        getChildren().addAll(rectangle2);
    }

    public void moveY(int value) {
        boolean downMove = value > 0 ? true : false;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Platform platform : GameMain.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (this.getTranslateY() < platform.getTranslateY() - 40) {
                        setTranslateY(getTranslateY() - 1);
                        jump();
                    }
                }
            }
            if (getTranslateY() < 0) {
                setTranslateY(0);
            }
            if (getTranslateY() > GameMain.HEIGHT - 20 - rectangle2.getImage().getHeight()/2) {
                setTranslateY(GameMain.HEIGHT - 20 - rectangle2.getImage().getHeight()/2);
            }
            setTranslateY(getTranslateY() + (downMove ? 1 : -1));
        }
    }

    public void moveX(int value) {
        for (int i = 0; i < Math.abs(value); i++) {
            setTranslateX(getTranslateX() + Math.abs(value)/value);
            for (Platform platform : GameMain.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (this.getTranslateY() < platform.getTranslateY() - 40) {
                        setTranslateY(getTranslateY() - 1);
                        jump();
                    }
                }
            }
        }
    }

    public void jump() {
        velocity = new Point2D(0, -15);
    }
}
