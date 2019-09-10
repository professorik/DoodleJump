package Game.Platforms;

import Game.PlatformPrototype;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class SimplePlatform extends PlatformPrototype {

    public SimplePlatform(int height, boolean isMotion) {
        this.height = height;
        rectangle = new Rectangle(80 , 20);
        imageView = new ImageView(new Image("/resources/block.png", 90 , 80 , true ,true, false));
        getChildren().add(imageView);
        if (isMotion){
            motion();
        }
    }
}
