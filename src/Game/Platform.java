package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Platform extends Pane {
    public Rectangle rectangle;
    public int height;
    public ImageView imageView;

    public Platform(int height) {
        this.height = height;
        rectangle = new Rectangle(100 , 20);
        imageView = new ImageView(new Image("/resources/restart.png", 100 , 80 , true ,true, false));
        getChildren().add(imageView);
    }
}
