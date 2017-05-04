//package sample;

import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.plaf.OptionPaneUI;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {
    private ArrayList<Sprite>Snake;
    private ArrayList<String>input;
    private Label scoreLabel;
    public Scene gameScene;
    //private int cur,counter;
    private String curDir="UP";
    private Sprite snakeFoodSprite;
    //private Font sniper150,unchanged100,unchanged50;
    private boolean gameOver=false;
    private Image snakeVert,snakeHor,snakeHeadUp,snakeHeadDown,snakeHeadLeft,snakeHeadRight,snakeTailUp,snakeTailDown;
    private Image snakeTailLeft,snakeTailRight,snakeUpLeft,snakeUpRight,snakeDownLeft,snakeDownRight,snakeFood;
    private Image background;
    private int score;
    private boolean playSound=true;
    private String playerName="";
    private Timeline gameloop;
    URL resource;
    Media media;
    MediaPlayer foodPlayer,overPlayer,musicPlayer;

    private ArrayList<Image>startImage;
    private Font sniper150,unchanged100,unchanged50,unchanged36;
    private Scene startScene,menuScene;
    private int cur,counter;
    @Override
    public void start(Stage stage) throws Exception {
        /*snakeGame(stage);
        stage.show();*/
        musicPlayer=new MediaPlayer(new Media(new File("files/main.mp3").toURI().toString()));
        scoreLabel=new Label("Hello");
        scoreLabel.setFont(unchanged50);
        background= new Image("files/grasses68.png");
        snakeDownRight = new Image("files/rgp.png");
        snakeDownLeft = new Image("files/rgl.png");
        snakeUpRight = new Image("files/rdp.png");
        snakeUpLeft = new Image("files/rdl.png");
        snakeTailRight = new Image("files/op.png");
        snakeTailLeft = new Image("files/ol.png");
        snakeTailDown = new Image("files/od.png");
        snakeTailUp = new Image("files/og.png");
        snakeHeadRight = new Image("files/gr.png");
        snakeHeadLeft = new Image("files/gl.png");
        snakeHeadDown = new Image("files/gd.png");
        snakeHeadUp = new Image("files/gu.png");
        snakeHor = new Image("files/sh.png");
        snakeVert = new Image("files/sv.png");
        snakeFood=new Image("files/appleg.png");

        sniper150=Font.loadFont(new FileInputStream(new File("files/start/fonts/SNIPER__.ttf")),150);
        unchanged100=Font.loadFont(new FileInputStream(new File("files/start/fonts/unchanged.ttf")),100);
        unchanged50=Font.loadFont(new FileInputStream(new File("files/start/fonts/unchanged.ttf")),50);
        unchanged36=Font.loadFont(new FileInputStream(new File("files/start/fonts/unchanged.ttf")),36);

        boot(stage);
        stage.show();
    }

    public void initGame(){
        Snake= new ArrayList<>();
        input= new ArrayList<>();
        Group root=new Group();
        curDir="UP";
        gameScene=new Scene(root,800,800,Color.AQUAMARINE);
        Canvas canvas = new Canvas(gameScene.getWidth(), gameScene.getHeight());
        final GraphicsContext gc= canvas.getGraphicsContext2D();
        gc.setFill(Color.AQUA);
        score=0;
        //input.clear();
        gameOver=false;
        snakeFoodSprite=null;

        Snake.add(new Sprite(snakeHeadUp,200,400));
        Snake.add(new Sprite(snakeVert,200,430));
        Snake.add(new Sprite(snakeVert,200,460));
        //Snake.add(new Sprite(snakeVert,200,290));
        //Snake.add(new Sprite(snakeVert,200,320));
        //Snake.add(new Sprite(snakeVert,200,350));
        Snake.add(new Sprite(snakeTailUp,200,490));

        snakeFoodSprite=new Sprite(snakeFood,0,0);
        genFood(snakeFoodSprite);

        //resource=getClass().getResource("food.mp3");
        media=new Media(new File("files/food.mp3").toURI().toString());
        foodPlayer=new MediaPlayer(media);
        overPlayer=new MediaPlayer(new Media(new File("files/go.mp3").toURI().toString()));
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.seek(Duration.ZERO);
        });
        foodPlayer.setOnEndOfMedia(() -> {
            foodPlayer.seek(Duration.ZERO);
            foodPlayer.stop();
        });
        overPlayer.setOnEndOfMedia(() -> {
            overPlayer.seek(Duration.ZERO);
            overPlayer.stop();
        });
        if(playSound)musicPlayer.play();

    }

    public void snakeGame(Stage stage){
        initGame();
        Group root=new Group();
        Scene gameScene=new Scene(root,800,800);
        Canvas canvas=new Canvas(gameScene.getWidth(), gameScene.getHeight());
        root.getChildren().add(canvas);
        GraphicsContext gc=canvas.getGraphicsContext2D();

        gameScene.setOnKeyPressed(event -> {
            //if(!input.contains(event.getCode().toString()))
            input.add(event.getCode().toString());
        });

        gameScene.setOnKeyReleased(event -> {
            if(input.contains(event.getCode().toString()))
                input.remove(event.getCode().toString());
        });

        gameloop=new Timeline(new KeyFrame(Duration.millis(150), event -> {
            gc.clearRect(0,0,gameScene.getWidth(),gameScene.getHeight());
            gc.drawImage(background,0,0);
            if(!input.isEmpty() && !gameOver) snakeMove();
            snakeUpdate();
            isEaten(snakeFoodSprite);
            if(gameOver) {
                if(playSound)overPlayer.play();
                snakeGameOver(stage);
            }
            snakeFoodSprite.render(gc);
            scoreLabel.setText(String.valueOf(score));
            for(Sprite sn:Snake)
                sn.render(gc);

        }));
        gameloop.setCycleCount(Timeline.INDEFINITE);
        gameloop.play();
        stage.setScene(gameScene);
    }

    public void snakeMove() {
        if(curDir=="UP"|| curDir=="DOWN") {
            if(input.get(0)=="LEFT") {
                if(curDir.equals("UP")) Snake.get(0).setImage(snakeUpLeft);
                if(curDir.equals("DOWN")) Snake.get(0).setImage(snakeDownLeft);
                curDir="LEFT";
                input.remove(0);
            }
            else if(input.get(0)=="RIGHT") {
                if(curDir.equals("UP")) Snake.get(0).setImage(snakeUpRight);
                if(curDir.equals("DOWN")) Snake.get(0).setImage(snakeDownRight);
                curDir="RIGHT";
                input.remove(0);
            }
        }
        else if(curDir=="LEFT" || curDir=="RIGHT") {
            if(input.get(0)=="UP") {
                if(curDir.equals("LEFT")) Snake.get(0).setImage(snakeDownRight);
                if(curDir.equals("RIGHT")) Snake.get(0).setImage(snakeDownLeft);
                curDir="UP";
                input.remove(0);
            }
            else if(input.get(0)=="DOWN") {
                if(curDir.equals("LEFT")) Snake.get(0).setImage(snakeUpRight);
                if(curDir.equals("RIGHT")) Snake.get(0).setImage(snakeUpLeft);
                curDir="DOWN";
                input.remove(0);
            }
        }
    }
    public void snakeUpdate() {
        switch (curDir) {
            case "UP": {
                Sprite head = Snake.get(0);
                Sprite temp = new Sprite(snakeHeadUp, head.posX, head.posY - 30);
                if (!isOver(temp)) Snake.add(0, temp);
                else {
                    gameOver = true;
                    return;
                }
                if (Snake.get(1).getImage() == snakeHeadUp) Snake.get(1).setImage(snakeVert);
                updateSnakeTail();
                Snake.remove(Snake.size() - 1);
                break;
            }
            case "DOWN": {
                Sprite head = Snake.get(0);
                Sprite temp = new Sprite(snakeHeadDown, head.posX, head.posY + 30);
                if (!isOver(temp)) Snake.add(0, temp);
                else {
                    gameOver = true;
                    return;
                }
                if (Snake.get(1).getImage() == snakeHeadDown) Snake.get(1).setImage(snakeVert);
                updateSnakeTail();
                Snake.remove(Snake.size() - 1);
                break;
            }
            case "LEFT": {
                Sprite head = Snake.get(0);
                Sprite temp = new Sprite(snakeHeadLeft, head.posX - 30, head.posY);
                if (!isOver(temp)) Snake.add(0, temp);
                else {
                    gameOver = true;
                    return;
                }
                if (Snake.get(1).getImage() == snakeHeadLeft) Snake.get(1).setImage(snakeHor);
                updateSnakeTail();
                Snake.remove(Snake.size() - 1);

                break;
            }
            case "RIGHT": {
                Sprite head = Snake.get(0);
                Sprite temp = new Sprite(snakeHeadRight, head.posX + 30, head.posY);
                if (!isOver(temp)) Snake.add(0, temp);
                else {
                    gameOver = true;
                    return;
                }
                if (Snake.get(1).getImage() == snakeHeadRight) Snake.get(1).setImage(snakeHor);
                updateSnakeTail();
                Snake.remove(Snake.size() - 1);
                break;
            }
        }

    }
    public void updateSnakeTail() {
        if (Snake.get(Snake.size() - 1).getImage() == snakeTailUp && Snake.get(Snake.size() - 2).getImage() == snakeUpRight)
            Snake.get(Snake.size() - 2).setImage(snakeTailRight);
        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailUp && Snake.get(Snake.size() - 2).getImage() == snakeUpLeft)
            Snake.get(Snake.size() - 2).setImage(snakeTailLeft);

        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailRight && Snake.get(Snake.size() - 2).getImage() == snakeDownLeft)
            Snake.get(Snake.size() - 2).setImage(snakeTailUp);
        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailRight && Snake.get(Snake.size() - 2).getImage() == snakeUpLeft)
            Snake.get(Snake.size() - 2).setImage(snakeTailDown);

        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailLeft && Snake.get(Snake.size() - 2).getImage() == snakeDownRight)
            Snake.get(Snake.size() - 2).setImage(snakeTailUp);
        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailLeft && Snake.get(Snake.size() - 2).getImage() == snakeUpRight)
            Snake.get(Snake.size() - 2).setImage(snakeTailDown);

        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailDown && Snake.get(Snake.size() - 2).getImage() == snakeDownRight)
            Snake.get(Snake.size() - 2).setImage(snakeTailRight);
        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailDown && Snake.get(Snake.size() - 2).getImage() == snakeDownLeft)
            Snake.get(Snake.size() - 2).setImage(snakeTailLeft);

        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailDown || Snake.get(Snake.size() - 1).getImage() == snakeTailUp) {
            if(Snake.get(Snake.size() - 2).getImage() == snakeVert)
                Snake.get(Snake.size() - 2).setImage(Snake.get(Snake.size() - 1).getImage());
        }
        else if (Snake.get(Snake.size() - 1).getImage() == snakeTailLeft || Snake.get(Snake.size() - 1).getImage() == snakeTailRight) {
            if(Snake.get(Snake.size() - 2).getImage() == snakeHor)
                Snake.get(Snake.size() - 2).setImage(Snake.get(Snake.size() - 1).getImage());
        }

    }

    public void genFood(Sprite food) {
        int x=(int)(Math.random()*gameScene.getWidth()-20);
        int y=(int)(Math.random()*gameScene.getHeight()-20);
        food.posX=x;
        food.posY=y;
        for(Sprite s:Snake)
            if(s.intersects(food)){
                genFood(food);
                break;
            }
    }

    public void isEaten(Sprite food) {
        if(Snake.get(0).intersects(food)) {
            score+=500;
            if(playSound)foodPlayer.play();
            switch (curDir) {
                case "UP": {
                    Sprite head = Snake.get(0);
                    Sprite temp = new Sprite(snakeHeadUp, head.posX, head.posY - 30);
                    if (!isOver(temp)) Snake.add(0, temp);
                    else {
                        gameOver = true;
                        return;
                    }
                    if (Snake.get(1).getImage() == snakeHeadUp) Snake.get(1).setImage(snakeVert);
                    //updateSnakeTail();
                    //Snake.remove(Snake.size() - 1);
                    break;
                }
                case "DOWN": {
                    Sprite head = Snake.get(0);
                    Sprite temp = new Sprite(snakeHeadDown, head.posX, head.posY + 30);
                    if (!isOver(temp)) Snake.add(0, temp);
                    else {
                        gameOver = true;
                        return;
                    }
                    if (Snake.get(1).getImage() == snakeHeadDown) Snake.get(1).setImage(snakeVert);
                    //updateSnakeTail();
                    //Snake.remove(Snake.size() - 1);
                    break;
                }
                case "LEFT": {
                    Sprite head = Snake.get(0);
                    Sprite temp = new Sprite(snakeHeadLeft, head.posX - 30, head.posY);
                    if (!isOver(temp)) Snake.add(0, temp);
                    else {
                        gameOver = true;
                        return;
                    }
                    if (Snake.get(1).getImage() == snakeHeadLeft) Snake.get(1).setImage(snakeHor);
                    //updateSnakeTail();
                    //Snake.remove(Snake.size() - 1);
                    break;
                }
                case "RIGHT": {
                    Sprite head = Snake.get(0);
                    Sprite temp = new Sprite(snakeHeadRight, head.posX + 30, head.posY);
                    if (!isOver(temp)) Snake.add(0, temp);
                    else {
                        gameOver = true;
                        return;
                    }
                    if (Snake.get(1).getImage() == snakeHeadRight) Snake.get(1).setImage(snakeHor);
                    //updateSnakeTail();
                    //Snake.remove(Snake.size() - 1);
                    break;
                }
            }
            genFood(food);
        }
    }

    public boolean isOver(Sprite s) {
        if(s.posX<=0|| s.posY<=0 || s.posX>=gameScene.getWidth()-20 || s.posY>gameScene.getHeight()-20) return true;
        for(Sprite it:Snake) {
            if(it.intersects(s)) return true;
        }
        return false;
    }

    public void snakeGameOver(Stage stage) {
        gameloop.stop();
        //stage.close();
        Alert alert=new Alert(Alert.AlertType.NONE,"Your Score is: "+score, ButtonType.OK);
        alert.setTitle("Game Over!");
        alert.setHeaderText(null);
        //if(alert.getResult()==ButtonType.OK)
            //mainMenu(stage);
        alert.show();
        PauseTransition pt=new PauseTransition(Duration.seconds(2));
        pt.play();
        pt.setOnFinished(event -> mainMenu(stage));
        //mainMenu(stage);
    }

    public void boot(Stage stage) {
        if(isFirst()) introduce();
        final boolean[] vis = {true};
        Group root=new Group();
        Canvas canvas=new Canvas(800,800);
        startScene=new Scene(root,800,800, Color.AQUAMARINE);
        root.getChildren().add(canvas);
        final GraphicsContext gc=canvas.getGraphicsContext2D();
        Reflection ref=new Reflection();
        ref.setFraction(0.7);

        Label begin=new Label("RUNNING BOY");
        begin.setFont(sniper150);
        begin.setLayoutX(170);
        begin.setLayoutY(100);
        begin.setEffect(ref);
        root.getChildren().add(begin);

        Label anyKey=new Label("Press Any Key!");
        anyKey.setFont(unchanged100);
        anyKey.setLayoutX(150);
        anyKey.setLayoutY(450);
        anyKey.setVisible(false);
        root.getChildren().add(anyKey);
        startImage=new ArrayList<>();
        for(int i=0;i<9;i++)
            startImage.add(new Image("files/start/boy/"+i+".png"));
        cur=0; counter=0;
        Timeline start=new Timeline(new KeyFrame(Duration.millis(100), event -> {
            gc.clearRect(0,0,800,800);
            begin.toBack();
            gc.drawImage(startImage.get(cur),800-counter*10,200);
            cur=(cur+1)%9;
            counter++;
        }));
        start.setCycleCount(100);
        start.play();
        start.setOnFinished((event)->{
            Timeline blink;
            startScene.setOnKeyPressed((event1) ->{
                mainMenu(stage);
            });
            blink = new Timeline(new KeyFrame(Duration.millis(300),(event2)->{
                gc.clearRect(0,0,800,800);
                anyKey.setVisible(vis[0]);
                vis[0] =!vis[0];
            }));
            blink.setCycleCount(Timeline.INDEFINITE);
            blink.play();
        });
        stage.setScene(startScene);
    }

    public void mainMenu(Stage stage) {
        Group root=new Group();
        menuScene=new Scene(root,800,800,Color.AZURE);
        DropShadow shadow=new DropShadow();
        Label newGameLabel=new Label("New Game");
        Label settingsLabel=new Label("Settings");
        Label hiScoreLabel=new Label("Hall of Fame");
        Label aboutLabel=new Label("About");
        Label exitLabel=new Label("Exit");
        root.getChildren().addAll(newGameLabel,settingsLabel,hiScoreLabel,aboutLabel,exitLabel);

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

        placeLabel(newGameLabel,300,200);
        placeLabel(settingsLabel,300,280);
        placeLabel(hiScoreLabel,260,360);
        placeLabel(aboutLabel,315,440);
        placeLabel(exitLabel,330,520);

        musicPlayer.stop();

        newGameLabel.setOnMouseClicked(event->snakeGame(stage));
        settingsLabel.setOnMouseClicked(event -> settings(stage));
        hiScoreLabel.setOnMouseClicked(event -> hallOfFame(stage));
        aboutLabel.setOnMouseClicked(event -> aboutApp(stage));
        exitLabel.setOnMouseClicked(event -> System.exit(0));

        stage.setScene(menuScene);
    }

    public void placeLabel(Label label,int x,int y){
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    public boolean isFirst() {
        try{
            FileReader fr=new FileReader("files/settings.dat");
            BufferedReader bf=new BufferedReader(fr);
            String line=bf.readLine();
            while(line!=null) {
                if(line.contains("Name:")) {
                    //System.out.println(line);
                    line = line.replace("Name: ", "");
                    //System.out.println(line);
                    playerName = line;
                }
                if(line.contains("Play Sound: ")) {
                    //System.out.println(line);
                    if(line.contains("true")) playSound=true;
                    else playSound=false;
                    //System.out.println(playSound);
                }
                line=bf.readLine();
            }
            if(playerName.isEmpty()) return true;
            else  return false;
        }catch(FileNotFoundException e) {
            return true;
        }
        catch (IOException e){
            return true;
        }
        //return false;
    }

    public void introduce(){
        Alert a=new Alert(Alert.AlertType.NONE,"Hello! Lets get familiar!",ButtonType.NEXT,ButtonType.CANCEL);
        a.setHeaderText(null);
        a.setTitle("Welcome");
        a.showAndWait();
        if(a.getResult()==ButtonType.CANCEL) System.exit(0);

        TextInputDialog b=new TextInputDialog();
        b.setHeaderText(null);
        b.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        b.getDialogPane().getButtonTypes().remove(ButtonType.OK);
        Button cancel= (Button) b.getDialogPane().lookupButton(ButtonType.CANCEL);
        b.setTitle("Welcome");
        b.setContentText("Please Enter You name:");
        Optional<String> result=b.showAndWait();
        cancel.addEventFilter(ActionEvent.ACTION,event -> System.exit(0));
        if(result.isPresent()) playerName=result.get();

        writeFile();

        /*try{
            FileWriter fw = new FileWriter("files/settings.dat", false);
            PrintWriter out = new PrintWriter(new BufferedWriter(fw));
            out.println("Name: "+playerName);
            out.println("Play Sound: true");
            out.close();
        } catch (IOException e) {}*/
    }

    public void settings(Stage stage) {
        VBox root=new VBox();
        root.setPadding(new Insets(100,200,100,200));
        root.setSpacing(20);
        root.setAlignment(Pos.TOP_LEFT);
        Label soundLabel=new Label("Sound");
        soundLabel.setFont(unchanged50);
        RadioButton on=new RadioButton("ON");
        RadioButton off=new RadioButton("OFF");
        on.setFont(unchanged36);
        off.setFont(unchanged36);
        ToggleGroup tg=new ToggleGroup();
        on.setToggleGroup(tg);
        off.setToggleGroup(tg);

        Label nameLabel=new Label("Change Name");
        nameLabel.setFont(unchanged50);
        Label backLabel=new Label("MAIN MENU");
        backLabel.setFont(unchanged50);
        root.getChildren().addAll(soundLabel,on,off,nameLabel);
        root.setSpacing(50);
        root.getChildren().add(backLabel);

        nameLabel.setOnMouseClicked(event -> {
            Alert a=new Alert(Alert.AlertType.NONE,"Please Restart the Game now!",ButtonType.CLOSE);
            a.showAndWait();
            playerName="";
            writeFile();
        });
        backLabel.setOnMouseClicked(event -> mainMenu(stage));

        if(playSound)on.setSelected(true);
        else off.setSelected(true);

        tg.selectedToggleProperty().addListener(e->{
            if(on.isSelected()) playSound=true;
            else playSound=false;
            writeFile();
        });
        Scene setScene=new Scene(root,800,800,Color.CHARTREUSE);
        stage.setScene(setScene);
    }

    public void aboutApp(Stage stage){
        Alert a=new Alert(Alert.AlertType.NONE,"",ButtonType.OK);
        a.setContentText("Snake Game\n"+
                        "Autor: Arif Hosan\n"+
                        "Contact: hosan.arif0@gmail.com");
        a.showAndWait();
    }

    public void hallOfFame(Stage stage){
        Alert a=new Alert(Alert.AlertType.NONE,"",ButtonType.OK);
        a.setTitle("Sorry!!");
        a.setHeaderText(null);
        a.setContentText("Wait till the next Update!!");
        a.showAndWait();
    }

    public void writeFile(){
        try{
            FileWriter fw = new FileWriter("files/settings.dat", false);
            PrintWriter out = new PrintWriter(new BufferedWriter(fw));
            out.println("Name: "+playerName);
            out.println("Play Sound: "+playSound);
            out.close();
        } catch (IOException e) {}
    }

    public static void main(String[] args) {
        launch(args);
    }
}