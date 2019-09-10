package Game;


import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class PlatformPrototype extends Pane {
    public Rectangle rectangle;
    public int height;
    public ImageView imageView;

    public void motion(){
        AnimationTimer animationTimer;
        final int[] k = {1};
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (getTranslateX() > GameMain.WIDTH - imageView.getImage().getWidth()){
                    k[0] = -1;
                }else if (getTranslateX() < 0){
                    k[0] = 1;
                }
                setTranslateX(getTranslateX() + k[0] * 1);
            }
        };
        animationTimer.start();
    }
}