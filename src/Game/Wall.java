package Game;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class Wall extends Pane {
    public Rectangle rectangle;
    public int height;
    public ImageView imageView;

    public Wall(int height) {
        this.height = height;
        rectangle = new Rectangle(138 , height);
        imageView = new ImageView("/resources/pipe.png");
        getChildren().add(imageView);
    }
}
