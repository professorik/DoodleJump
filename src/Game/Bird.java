package Game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.Date;

public class Bird extends Pane {

    public Point2D velocity;
    private ImageView rectangle2;

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
        GameMain.time = new Date();
    }
}
