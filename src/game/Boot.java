package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Arif on 17-Aug-16.
 */
public class Boot extends Application {
    private ArrayList<Image> startImage;
    private Font sniper150, unchanged100, unchanged50;
    private Scene startScene, menuScene;
    private int cur, counter;

    @Override
    public void start(Stage stage) throws Exception {
        sniper150 = Font.loadFont(new FileInputStream(new File("files/start/fonts/SNIPER__.ttf")), 150);
        unchanged100 = Font.loadFont(new FileInputStream(new File("/files/0.start/fonts/unchanged.ttf")), 100);
        unchanged50 = Font.loadFont(new FileInputStream(new File("files/start/fonts/unchanged.ttf")), 50);
        boot(stage);
        stage.show();
    }

    public void boot(Stage stage) {
        final boolean[] vis = {true};
        Group root = new Group();
        Canvas canvas = new Canvas(800, 800);
        startScene = new Scene(root, 800, 800, Color.AQUAMARINE);
        root.getChildren().add(canvas);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        Reflection ref = new Reflection();
        ref.setFraction(0.7);

        Label begin = new Label("SNAKE");
        begin.setFont(sniper150);
        begin.setLayoutX(170);
        begin.setLayoutY(100);
        begin.setEffect(ref);
        root.getChildren().add(begin);

        Label anyKey = new Label("Press Any Key!");
        anyKey.setFont(unchanged100);
        anyKey.setLayoutX(150);
        anyKey.setLayoutY(450);
        anyKey.setVisible(false);
        root.getChildren().add(anyKey);
        startImage = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            startImage.add(new Image("files/start/boy/" + i + ".png"));
        cur = 0;
        counter = 0;
        Timeline start = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            gc.clearRect(0, 0, 800, 800);
            begin.toBack();
            gc.drawImage(startImage.get(cur), 800 - counter * 10, 200);
            cur = (cur + 1) % 9;
            counter++;
        }));
        start.setCycleCount(1);
        start.play();
        start.setOnFinished((event) -> {
            Timeline blink;
            startScene.setOnKeyPressed((event1) -> {
                mainMenu(stage);
            });
            blink = new Timeline(new KeyFrame(Duration.millis(300), (event2) -> {
                gc.clearRect(0, 0, 800, 800);
                anyKey.setVisible(vis[0]);
                vis[0] = !vis[0];
            }));
            blink.setCycleCount(Timeline.INDEFINITE);
            blink.play();
        });
        stage.setScene(startScene);
    }

    public void mainMenu(Stage stage) {
        Group root = new Group();
        menuScene = new Scene(root, 800, 800, Color.AZURE);
        DropShadow shadow = new DropShadow();
        Label newGameLabel = new Label("New Game");
        Label settingsLabel = new Label("Settings");
        Label hiScoreLabel = new Label("Hall of Fame");
        Label aboutLabel = new Label("About");
        Label exitLabel = new Label("Exit");
        root.getChildren().addAll(newGameLabel, settingsLabel, hiScoreLabel, aboutLabel, exitLabel);

        newGameLabel.setFont(unchanged50);
        settingsLabel.setFont(unchanged50);
        hiScoreLabel.setFont(unchanged50);
        aboutLabel.setFont(unchanged50);
        exitLabel.setFont(unchanged50);

        newGameLabel.setEffect(shadow);
        settingsLabel.setEffect(shadow);
        hiScoreLabel.setEffect(shadow);
        aboutLabel.setEffect(shadow);
        exitLabel.setEffect(shadow);

        placeLabel(newGameLabel, 300, 200);
        placeLabel(settingsLabel, 300, 280);
        placeLabel(hiScoreLabel, 260, 360);
        placeLabel(aboutLabel, 315, 440);
        placeLabel(exitLabel, 330, 520);

        newGameLabel.setOnMouseClicked(event -> {
        });
        //settingsLabel.setOnMouseClicked(event -> stage.setScene(settingScene));
        //hiScoreLabel.setOnMouseClicked(event -> stage.setScene(hiScoreScene));
        //aboutLabel.setOnMouseClicked(event -> stage.setScene(aboutScene));
        exitLabel.setOnMouseClicked(event -> System.exit(0));

        stage.setScene(menuScene);
    }

    public void placeLabel(Label label, int x, int y) {
        label.setLayoutX(x);
        label.setLayoutY(y);
    }
}
