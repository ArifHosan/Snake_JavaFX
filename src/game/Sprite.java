package game;//package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Created by Arif on 12-Aug-16.
 */

public class Sprite {
    Image image;
    int posX, posY;
    double velocityX, velocityY;
    double width, height;
    double OFFSET_x, OFFSET_y;

    Sprite(Image i) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();
    }

    Sprite(int x, int y) {
        width = 10;
        height = 10;
        posX = x;
        posY = y;
        velocityX = 0;
        velocityY = 0;
    }

    public Sprite(Image i, int x, int y) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();
        posX = x;
        posY = y;
        velocityX = 0;
        velocityY = 0;
    }

    /*void setOFFSET(int x,int y) {
        OFFSET_x=x;
        OFFSET_y=y;
    }*/

    void setImage(String file) {
        image = new Image(file);
        width = image.getWidth();
        height = image.getHeight();
    }

    void setImage(Image i) {
        image = i;
        width = image.getWidth();
        height = image.getHeight();
    }

    Image getImage() {
        return image;
    }

    void update(double time) {
        posX += velocityX * time;
        posY += velocityY * time;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, posX, posY);
    }

    /*public void render(GraphicsContext gc) {
        gc.fillRect(posX,posY,width,height);
    }*/
    public Rectangle2D getBound() {
        return new Rectangle2D(posX, posY, width, height);
    }

    public boolean intersects(Sprite s) {
        return s.getBound().intersects(this.getBound());
    }
}
