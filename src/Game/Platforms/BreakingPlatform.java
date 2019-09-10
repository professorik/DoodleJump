package Game.Platforms;

import Game.PlatformPrototype;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class BreakingPlatform extends PlatformPrototype {

    public BreakingPlatform(int height, boolean isMotion) {
        this.height = height;
        rectangle = new Rectangle(80 , 20);
        imageView = new ImageView(new Image("/resources/doodle_platforms_break.png", 90 , 80 , true ,true, false));
        getChildren().add(imageView);
        if (isMotion){
            motion();
        }
    }

    //Чет изображение поменять надо
    public void breakPlatform(){
        super.imageView.setImage(new Image("/resources/break.gif" , 100 , 100 , true , true, false));
    }
}
