package Game;

import Game.Platforms.BreakingPlatform;
import Game.Platforms.IcePlatform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Date;

public class Bird extends Pane {

    public Point2D velocity;
    public ImageView rectangle;

    //TODO: доделать скелет
    public Bird() {
        velocity = new Point2D(0, 0);
        setTranslateX(370);
        setTranslateY(670);
        rectangle = new ImageView(new Image("/resources/player_1.png", 60 , 80 , true , true , false));
        getChildren().addAll(rectangle);
    }



    public ImageView getRectangle() {
        return rectangle;
    }

    public void moveY(int value) {
        boolean downMove = value > 0 ? true : false;
        for (int i = 0; i < Math.abs(value); i++) {
            for (PlatformPrototype platform : GameMain.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent()) ) {
                    if (this.getTranslateY() < platform.getTranslateY() - 40 && value > 2  && this.getTranslateX() + 30 > platform.getTranslateX()
                            && this.getTranslateX() < platform.getTranslateX() + 70) {
                        //setTranslateY(getTranslateY() - 1);
                        if (platform instanceof BreakingPlatform){
                            ((BreakingPlatform) platform).breakPlatform();
                        }else if (platform instanceof IcePlatform){
                            ((IcePlatform) platform).breakPlatform();
                            jump();
                        }else {
                            jump();
                        }
                    }
                }
            }
            setTranslateY(getTranslateY() + (downMove ? 1 : -1));
        }
    }

    public void moveX(int value) {
        for (int i = 0; i < Math.abs(value); i++) {
            setTranslateX((getTranslateX() + Math.abs(value)/value) % 800);
            if (getTranslateX() < -3){
                setTranslateX(790 + getTranslateX());
            }
            for (PlatformPrototype platform : GameMain.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (this.getTranslateY() < platform.getTranslateY() - 40 && GameMain.jp == false) {
                        setTranslateY(getTranslateY() - 1);
                    }
                }
            }
        }
    }

    public void jump() {
        velocity = new Point2D(0, -15);
        GameMain.time = new Date();
        GameMain.jp = true;
    }
}
