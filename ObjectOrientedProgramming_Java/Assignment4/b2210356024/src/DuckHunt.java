import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DuckHunt extends Application {

    public static double SCALE = 3;
    public static double VOLUME = 0.025;
    private Stage primaryStage;
    private StackPane backgroundPane;
    private MediaPlayer mediaPlayer;
    private boolean backgroundScreenToTitleScreen;
    private Background background;
    private final String[] backgroundChoices = new String[]{
            "assets/background/1.png",
            "assets/background/2.png",
            "assets/background/3.png",
            "assets/background/4.png",
            "assets/background/5.png",
            "assets/background/6.png"
    };
    private final int[] backgroundIndex = {0};
    private ImageView crosshairImageView;
    private final String[] crosshairChoices = new String[]{
            "assets/crosshair/1.png",
            "assets/crosshair/2.png",
            "assets/crosshair/3.png",
            "assets/crosshair/4.png",
            "assets/crosshair/5.png",
            "assets/crosshair/6.png",
            "assets/crosshair/7.png"
    };

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("HUBBM Duck Hunt");
        primaryStage.setResizable(false); // To prevent distortion when going full screen.
        primaryStage.getIcons().add(new Image("assets/favicon/1.png"));
        loadTitleScreen();
        primaryStage.show();
    }

    /**
     * Loads and displays the title screen of the game.
     * This method sets up the title screen using JavaFX. It creates a stack pane and sets a background image,
     * adds a guide text, applies a fade animation to the text, and sets up event handlers for keyboard interactions.
     * The title screen provides options to start the game by pressing the Enter key or exit the game by pressing
     * the Escape key.
     *
     * @implNote The title screen is the initial screen that the player sees when starting the game. It is responsible
     *           for providing a visual introduction to the game and offering options for starting or exiting the game.
     *           The title screen includes a background image, a guide text with instructions, and a fade animation applied
     *           to the guide text. The fade animation creates a pulsating effect on the guide text, making it more visually
     *           appealing. The title screen also plays a title music effect, but only if the user hasn't navigated from
     *           the background screen to the title screen.
     *           The title screen listens to key press events for the Enter and Escape keys. Pressing the Enter key
     *           triggers the transition to the background screen where the game starts. Pressing the Escape key
     *           triggers the game to exit by calling the Platform.exit() method.
     *
     * @see #loadBackgroundScreen()
     * @see #playTheEffect(String, int)
     */
    private void loadTitleScreen() {

        StackPane titlePane = new StackPane();

        background = new Background(getBackgroundImage("assets/welcome/1.png"));
        titlePane.setBackground(background);

        Text guideText = new Text("PRESS ENTER TO PLAY\nPRESS ESC TO EXIT");
        guideText.setFont(Font.font("Arial", FontWeight.BOLD, 50 * SCALE / 3));
        guideText.setTextAlignment(TextAlignment.CENTER);
        guideText.setFill(Color.ORANGE);
        guideText.setTranslateY(150 * SCALE / 3);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), guideText);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();

        titlePane.getChildren().add(guideText);

        // If user comes from Background Screen, does not run this "if block"s codes again.
        if (!backgroundScreenToTitleScreen) {
            playTheEffect("assets/effects/Title.mp3", MediaPlayer.INDEFINITE);
        }

        titlePane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loadBackgroundScreen();
            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                Platform.exit();
            }
        });

        primaryStage.setScene(new Scene(titlePane, 256 * SCALE, 240 * SCALE));
        titlePane.requestFocus();
    }

    /**
     * Loads and displays the background selection screen of the game.
     * This method sets up the background selection screen using JavaFX. It creates a stack pane and adds a guide text
     * with instructions for navigation and game start. The method sets up event handlers for keyboard interactions,
     * allowing the user to navigate between background choices and select a crosshair. The background screen provides
     * options to start the game by pressing the Enter key, return to the title screen by pressing the Escape key, and
     * navigate through background choices using the arrow keys.
     *
     * @implNote The background screen is a secondary screen that allows the player to select a background image and a
     *           crosshair before starting the game. It provides a visual preview of different backgrounds and allows the
     *           player to navigate through them using the arrow keys. The selected background and crosshair choices are
     *           reflected in the displayed graphics. The background screen also listens to key press events for the Enter
     *           and Escape keys to trigger the transition to the game screen or return to the title screen, respectively.
     *
     * @see #getBackgroundImage(String)
     * @see #setCrosshair(int[])
     * @see #playTheEffect(String, int)
     * @see #loadGameScreen1()
     */
    private void loadBackgroundScreen() {
        backgroundPane = new StackPane();

        Text guideText = new Text("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");
        guideText.setFont(Font.font("Arial", FontWeight.BOLD, 25 * SCALE / 3));
        guideText.setTextAlignment(TextAlignment.CENTER);
        guideText.setFill(Color.ORANGE);
        guideText.setTranslateY(-275 * SCALE / 3);
        backgroundPane.getChildren().add(guideText);

        // Start selection with 1.png background
        backgroundIndex[0] = 0; // initialize screen with default choice
        background = new Background(getBackgroundImage(backgroundChoices[backgroundIndex[0]]));
        backgroundPane.setBackground(background);

        // Start selection with 1.png crosshair (red one)
        final int[] crosshairIndex = {0}; // initialize screen with default choice
        setCrosshair(crosshairIndex);

        backgroundPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                // If you press ENTER two times repeatedly, It firstly stops the Title.mp3.
                // Then, plays Intro.mp3.
                // Then, when you press ENTER, It stops playing Intro.mp3 and starts playing Intro.mp3
                // And, when finally Intro.mp3 ends, Player goes to Game Screen.
                mediaPlayer.stop();
                playTheEffect("assets/effects/Intro.mp3", 1);
                mediaPlayer.setOnEndOfMedia(this::loadGameScreen1); // when the media reaches its end, load next scene.
            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                backgroundScreenToTitleScreen = true;
                loadTitleScreen();
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                backgroundIndex[0] = (backgroundIndex[0] + 1) % 6; // Firstly, increment index and then get mod from 6.
                background = new Background(getBackgroundImage(backgroundChoices[backgroundIndex[0]]));
                backgroundPane.setBackground(background);
            } else if (event.getCode().equals(KeyCode.LEFT)) {
                if (backgroundIndex[0] == 0) {
                    backgroundIndex[0] = 5;
                } else {
                    backgroundIndex[0] = (backgroundIndex[0] - 1);
                }
                background = new Background(getBackgroundImage(backgroundChoices[(backgroundIndex[0])]));
                backgroundPane.setBackground(background);
            } else if (event.getCode().equals(KeyCode.UP)) {
                backgroundPane.getChildren().remove(crosshairImageView);
                crosshairIndex[0] = (crosshairIndex[0] + 1) % 7;
                setCrosshair(crosshairIndex);
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                backgroundPane.getChildren().remove(crosshairImageView);
                if (crosshairIndex[0] == 0) {
                    crosshairIndex[0] = 6;
                    setCrosshair(crosshairIndex);
                } else {
                    crosshairIndex[0] = (crosshairIndex[0] - 1);
                    setCrosshair(crosshairIndex);
                }
            }
        });

        primaryStage.setScene(new Scene(backgroundPane, 256 * SCALE, 240 * SCALE));
        backgroundPane.requestFocus();
    }

    /**
     * Loads and displays the first game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated 1 duck that move horizontally. The duck can
     * be shot by clicking on it with the mouse cursor. The game tracks the remaining ammo count,
     * plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen1() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edge of the stage.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // First level: one duck moves horizontally.
        ducks.add(new Duck(50 * SCALE / 3, 100 * SCALE / 3, 2 * SCALE / 3, 0 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/LevelCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (ammoCount[0] >= 0 && ducks.size() == 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen2();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {

                    String imagePath;
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (animationCounter / frameDelay % 3 == 0) {
                        // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                        imagePath = "assets/duck_black/4.png";
                    } else if (animationCounter / frameDelay % 3 == 1) {
                        // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                        imagePath = "assets/duck_black/5.png";
                    } else {
                        // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                        imagePath = "assets/duck_black/6.png";
                    }

                    if (duck.getSpeedX() > 0) {
                        // Duck moving to the right, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0) {
                        // Duck moving to the left, draw the reflected image
                        Image reflectedImage = reflectImageHorizontally(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else {
                        // Duck not moving horizontally, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    }

                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 1 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "YOU WIN!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play next level" text
                    String flashingText = "Press ENTER to play next level";
                    double flashingTextWidth = new Text(flashingText).getLayoutBounds().getWidth();
                    double flashingTextX = (canvas.getWidth() - flashingTextWidth) / 2;
                    double flashingTextY = (canvas.getHeight() / 2) + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30); // To make flash's toggling times longer.

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(flashingText, flashingTextX, flashingTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Loads and displays the second game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated ducks that move in cross directions. The ducks can
     * be shot by clicking on them with the mouse cursor. The game tracks the remaining ammo count,
     * plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen2() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edge of the stage.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // Second level: one duck moves in cross directions.
        ducks.add(new Duck(100 * SCALE / 3, 150 * SCALE / 3, -2 * SCALE / 3, 2 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/LevelCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (ammoCount[0] >= 0 && ducks.size() == 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen3();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {

                    String imagePath;
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (animationCounter / frameDelay % 3 == 0) {
                        // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                        imagePath = "assets/duck_red/1.png";
                    } else if (animationCounter / frameDelay % 3 == 1) {
                        // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                        imagePath = "assets/duck_red/2.png";
                    } else {
                        // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                        imagePath = "assets/duck_red/3.png";
                    }

                    if (duck.getSpeedX() > 0 && duck.getSpeedY() > 0) {
                        // Duck moving to the right, draw the normal image
                        Image reflectedImage = reflectImageVertically(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0 && duck.getSpeedY() > 0) {
                        // Duck moving to the left, draw the reflected image
                        Image reflectedImage = reflectImageHorizontally(reflectImageVertically(imagePath));
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() > 0 && duck.getSpeedY() < 0) {
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0 && duck.getSpeedY() < 0) {
                        Image reflectedImage = reflectImageHorizontally(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else {
                        // Duck not moving horizontally, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    }

                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 2 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "YOU WIN!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play next level" text
                    String flashingText = "Press ENTER to play next level";
                    double flashingTextWidth = new Text(flashingText).getLayoutBounds().getWidth();
                    double flashingTextX = (canvas.getWidth() - flashingTextWidth) / 2;
                    double flashingTextY = (canvas.getHeight() / 2) + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30); // To make flash's toggling times longer.

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(flashingText, flashingTextX, flashingTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Loads and displays the third game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated ducks that move horizontally. The ducks can
     * be shot by clicking on them with the mouse cursor. The game tracks the remaining ammo count,
     * plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen3() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edge of the stage.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // Third level: two duck moves horizontally.
        ducks.add(new Duck(150 * SCALE / 3, 150 * SCALE / 3, 2 * SCALE / 3, 0 * SCALE / 3));
        ducks.add(new Duck(100 * SCALE / 3, 150 * SCALE / 3, -2 * SCALE / 3, 0 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/LevelCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (ammoCount[0] >= 0 && ducks.size() == 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen4();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {

                    String imagePath;
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (animationCounter / frameDelay % 3 == 0) {
                        // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                        imagePath = "assets/duck_blue/4.png";
                    } else if (animationCounter / frameDelay % 3 == 1) {
                        // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                        imagePath = "assets/duck_blue/5.png";
                    } else {
                        // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                        imagePath = "assets/duck_blue/6.png";
                    }

                    if (duck.getSpeedX() > 0) {
                        // Duck moving to the right, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0) {
                        // Duck moving to the left, draw the reflected image
                        Image reflectedImage = reflectImageHorizontally(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else {
                        // Duck not moving horizontally, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    }

                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 3 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "YOU WIN!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play next level" text
                    String flashingText = "Press ENTER to play next level";
                    double flashingTextWidth = new Text(flashingText).getLayoutBounds().getWidth();
                    double flashingTextX = (canvas.getWidth() - flashingTextWidth) / 2;
                    double flashingTextY = (canvas.getHeight() / 2) + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30); // To make flash's toggling times longer.

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(flashingText, flashingTextX, flashingTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Loads and displays the fourth game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated ducks that move in cross directions. The ducks can
     * be shot by clicking on them with the mouse cursor. The game tracks the remaining ammo count,
     * plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen4() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values as middle of the default cursor
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edges of the scene.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // Fourth level: two ducks moves in cross directions.
        ducks.add(new Duck(10 * SCALE / 3, 200 * SCALE / 3, -2 * SCALE / 3, 2 * SCALE / 3));
        ducks.add(new Duck(200 * SCALE / 3, 200 * SCALE / 3, 2 * SCALE / 3, -2 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/LevelCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (ammoCount[0] >= 0 && ducks.size() == 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen5();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {

                    String imagePath;
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (animationCounter / frameDelay % 3 == 0) {
                        // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                        imagePath = "assets/duck_black/1.png";
                    } else if (animationCounter / frameDelay % 3 == 1) {
                        // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                        imagePath = "assets/duck_black/2.png";
                    } else {
                        // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                        imagePath = "assets/duck_black/3.png";
                    }

                    if (duck.getSpeedX() > 0 && duck.getSpeedY() > 0) {
                        // Duck moving to the right, draw the normal image
                        Image reflectedImage = reflectImageVertically(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0 && duck.getSpeedY() > 0) {
                        // Duck moving to the left, draw the reflected image
                        Image reflectedImage = reflectImageHorizontally(reflectImageVertically(imagePath));
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() > 0 && duck.getSpeedY() < 0) {
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else if (duck.getSpeedX() < 0 && duck.getSpeedY() < 0) {
                        Image reflectedImage = reflectImageHorizontally(imagePath);
                        gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    } else {
                        // Duck not moving horizontally, draw the normal image
                        gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                    }

                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 4 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "YOU WIN!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play next level" text
                    String flashingText = "Press ENTER to play next level";
                    double flashingTextWidth = new Text(flashingText).getLayoutBounds().getWidth();
                    double flashingTextX = (canvas.getWidth() - flashingTextWidth) / 2;
                    double flashingTextY = (canvas.getHeight() / 2) + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30); // To make flash's toggling times longer.

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(flashingText, flashingTextX, flashingTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Loads and displays the fifth game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated ducks that move horizontally and in cross directions.
     * The ducks can be shot by clicking on them with the mouse cursor. The game tracks the remaining
     * ammo count, plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen5() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edge of the scene.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // Fifth level: two ducks moves horizontally and one duck moves in cross directions.
        ducks.add(new Duck(20 * SCALE / 3, 210 * SCALE / 3, -2 * SCALE / 3, 0 * SCALE / 3));
        ducks.add(new Duck(150 * SCALE / 3, 10 * SCALE / 3, 2 * SCALE / 3, 0 * SCALE / 3));
        ducks.add(new Duck(200 * SCALE / 3, 5 * SCALE / 3, -2 * SCALE / 3, -2 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/LevelCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (ammoCount[0] >= 0 && ducks.size() == 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen6();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (ammoCount[0] < ducks.size() && ducks.size() != 0 && event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (duck.getSpeedY() == 0) {
                        String imagePath;
                        if (animationCounter / frameDelay % 3 == 0) {
                            // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                            imagePath = "assets/duck_black/4.png";
                        } else if (animationCounter / frameDelay % 3 == 1) {
                            // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                            imagePath = "assets/duck_black/5.png";
                        } else {
                            // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                            imagePath = "assets/duck_black/6.png";
                        }

                        if (duck.getSpeedX() > 0) {
                            // Duck moving to the right, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0) {
                            // Duck moving to the left, draw the reflected image
                            Image reflectedImage = reflectImageHorizontally(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else {
                            // Duck not moving horizontally, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        }

                    } else {
                        String imagePath;
                        if (animationCounter / frameDelay % 3 == 0) {
                            // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                            imagePath = "assets/duck_red/1.png";
                        } else if (animationCounter / frameDelay % 3 == 1) {
                            // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                            imagePath = "assets/duck_red/2.png";
                        } else {
                            // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                            imagePath = "assets/duck_red/3.png";
                        }

                        if (duck.getSpeedX() > 0 && duck.getSpeedY() > 0) {
                            // Duck moving to the right, draw the normal image
                            Image reflectedImage = reflectImageVertically(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0 && duck.getSpeedY() > 0) {
                            // Duck moving to the left, draw the reflected image
                            Image reflectedImage = reflectImageHorizontally(reflectImageVertically(imagePath));
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() > 0 && duck.getSpeedY() < 0) {
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0 && duck.getSpeedY() < 0) {
                            Image reflectedImage = reflectImageHorizontally(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else {
                            // Duck not moving horizontally, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        }

                    }
                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 5 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "YOU WIN!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play next level" text
                    String flashingText = "Press ENTER to play next level";
                    double flashingTextWidth = new Text(flashingText).getLayoutBounds().getWidth();
                    double flashingTextX = (canvas.getWidth() - flashingTextWidth) / 2;
                    double flashingTextY = (canvas.getHeight() / 2) + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30); // To make flash's toggling times longer.

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(flashingText, flashingTextX, flashingTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Loads and displays the sixth game screen.
     * This method sets up the game screen using JavaFX. It creates a canvas and graphics context
     * to render the game elements. The method initializes event handlers for mouse and keyboard
     * interactions, updates the game state, and displays the graphics based on the current state.
     * The game screen includes a stack pane containing the canvas, with appropriate alignment
     * and dimensions.
     *
     * @implNote The game screen includes animated ducks that move horizontally and in cross directions.
     * The ducks can be shot by clicking on them with the mouse cursor. The game tracks the remaining
     * ammo count, plays sound effects for shooting and game events, and displays game over or win messages.
     * The screen also provides options to proceed to the next level, play again, or exit the game.
     *
     * @see Duck
     */
    private void loadGameScreen6() {
        Canvas canvas = new Canvas(256 * SCALE, 240 * SCALE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane gamePane = new StackPane(canvas);
        gamePane.setAlignment(Pos.CENTER);

        gamePane.setOnMouseEntered(event -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image crosshairImage = crosshairImageView.snapshot(params, null);
            WritableImage alteredImage = new WritableImage(crosshairImage.getPixelReader(), (int) crosshairImage.getWidth(), (int) crosshairImage.getHeight());

            int offsetX = (int) (alteredImage.getWidth() / 2); // Calculate the offset in the x-axis
            int offsetY = (int) (alteredImage.getHeight() / 2); // Calculate the offset in the y-axis

            for (int y = 0; y < alteredImage.getHeight(); y++) {
                for (int x = 0; x < alteredImage.getWidth(); x++) {
                    if (alteredImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                        alteredImage.getPixelWriter().setColor(x, y, Color.TRANSPARENT);
                    }
                }
            }

            ImageCursor customCursor = new ImageCursor(alteredImage, offsetX, offsetY); // Set the offset values
            gamePane.setCursor(customCursor);
        });


        // If following code line does not exist, there may be some bugs at the edge of the scene.
        gamePane.setOnMouseExited(event -> gamePane.setCursor(Cursor.DEFAULT));

        List<Duck> ducks = new ArrayList<>();

        // Last level: two ducks moves horizontally and two duck moves in cross directions.
        ducks.add(new Duck(20 * SCALE / 3, 210 * SCALE / 3, -2 * SCALE / 3, 0 * SCALE / 3));
        ducks.add(new Duck(150 * SCALE / 3, 10 * SCALE / 3, 2 * SCALE / 3, 0 * SCALE / 3));
        ducks.add(new Duck(200 * SCALE / 3, 5 * SCALE / 3, -2 * SCALE / 3, -2 * SCALE / 3));
        ducks.add(new Duck(11 * SCALE / 3, 199 * SCALE / 3, -2 * SCALE / 3, 2 * SCALE / 3));

        // Set ammoCount 3 times of ducks.
        int[] ammoCount = {ducks.size() * 3};

        canvas.setOnMouseClicked(event -> {
            if (ammoCount[0] > 0) {
                playTheEffect("assets/effects/Gunshot.mp3", 1);
                ammoCount[0] -= 1;
                for (Duck duck : ducks) {
                    if (duck.isShot(event.getX(), event.getY())) {
                        playTheEffect("assets/effects/DuckFalls.mp3", 1);
                        ducks.remove(duck);
                        break; // Due to list is changing during game, break statement must be putted.
                    }
                }
                if (ammoCount[0] >= 0 && ducks.size() == 0) {
                    playTheEffect("assets/effects/GameCompleted.mp3", 1);
                } else if (ammoCount[0] == 0) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                } else if (ammoCount[0] < ducks.size()) {
                    playTheEffect("assets/effects/GameOver.mp3", 1);
                }
            }
        });

        canvas.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mediaPlayer.stop();
                loadGameScreen1();
            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                mediaPlayer.stop();
                loadTitleScreen();
            }
        });

        new AnimationTimer() {
            private int flashCounter = 0;
            private int animationCounter = 0;

            @Override
            public void handle(long now) {
                gc.drawImage(new Image(backgroundChoices[backgroundIndex[0]]), 0, 0, 256 * SCALE, 240 * SCALE); // BACKGROUND

                for (Duck duck : ducks) {
                    // Adjust the delay value to control the animation speed
                    int frameDelay = 25;
                    if (duck.getSpeedY() == 0) {
                        String imagePath;
                        if (animationCounter / frameDelay % 3 == 0) {
                            // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                            imagePath = "assets/duck_red/4.png";
                        } else if (animationCounter / frameDelay % 3 == 1) {
                            // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                            imagePath = "assets/duck_red/5.png";
                        } else {
                            // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                            imagePath = "assets/duck_red/6.png";
                        }

                        if (duck.getSpeedX() > 0) {
                            // Duck moving to the right, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0) {
                            // Duck moving to the left, draw the reflected image
                            Image reflectedImage = reflectImageHorizontally(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else {
                            // Duck not moving horizontally, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        }

                    } else {
                        String imagePath;
                        if (animationCounter / frameDelay % 3 == 0) {
                            // Use the first asset folder for animationCounter/frameDelay values divisible by 3
                            imagePath = "assets/duck_black/1.png";
                        } else if (animationCounter / frameDelay % 3 == 1) {
                            // Use the second asset folder for animationCounter/frameDelay values with remainder 1
                            imagePath = "assets/duck_black/2.png";
                        } else {
                            // Use the third asset folder for animationCounter/frameDelay values with remainder 2
                            imagePath = "assets/duck_black/3.png";
                        }

                        if (duck.getSpeedX() > 0 && duck.getSpeedY() > 0) {
                            // Duck moving to the right, draw the normal image
                            Image reflectedImage = reflectImageVertically(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0 && duck.getSpeedY() > 0) {
                            // Duck moving to the left, draw the reflected image
                            Image reflectedImage = reflectImageHorizontally(reflectImageVertically(imagePath));
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() > 0 && duck.getSpeedY() < 0) {
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else if (duck.getSpeedX() < 0 && duck.getSpeedY() < 0) {
                            Image reflectedImage = reflectImageHorizontally(imagePath);
                            gc.drawImage(reflectedImage, duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        } else {
                            // Duck not moving horizontally, draw the normal image
                            gc.drawImage(new Image(imagePath), duck.getX(), duck.getY(), 27 * SCALE, 31 * SCALE);
                        }

                    }
                    duck.moveTheDuck();
                    animationCounter++;
                }

                gc.drawImage(new Image("assets/foreground/" + backgroundChoices[backgroundIndex[0]].split("/")[2]), 0, 0, 256 * SCALE, 240 * SCALE); // FOREGROUND; in front of background and duck because is drawn after them.

                gc.setFill(Color.ORANGE);

                // Draw level text
                String levelText = "Level " + 6 + "/" + 6;
                double levelTextWidth = new Text(levelText).getLayoutBounds().getWidth();
                gc.fillText(levelText, (canvas.getWidth() - levelTextWidth) / 2, 20);

                // Draw ammo text
                String ammoText = "Ammo Left: " + ammoCount[0];
                double ammoTextWidth = new Text(ammoText).getLayoutBounds().getWidth();
                gc.fillText(ammoText, canvas.getWidth() - ammoTextWidth - 10, 20);

                boolean flash;
                if (ammoCount[0] >= 0 && ducks.size() == 0) {

                    // Draw "YOU WIN!" text
                    String winText = "You have completed the game!";
                    double winTextWidth = new Text(winText).getLayoutBounds().getWidth();
                    gc.fillText(winText, (canvas.getWidth() - winTextWidth) / 2, canvas.getHeight() / 2);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = canvas.getHeight() / 2 + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                } else if (ammoCount[0] < ducks.size() && ducks.size() != 0) {
                    // Draw "GAME OVER!" text
                    String gameOverText = "GAME OVER!";
                    double gameOverTextWidth = new Text(gameOverText).getLayoutBounds().getWidth();
                    double gameOverTextX = (canvas.getWidth() - gameOverTextWidth) / 2;
                    double gameOverTextY = canvas.getHeight() / 2;

                    gc.fillText(gameOverText, gameOverTextX, gameOverTextY);

                    // Flashing "Press ENTER to play again" text
                    String playAgainText = "Press ENTER to play again";
                    double playAgainTextWidth = new Text(playAgainText).getLayoutBounds().getWidth();
                    double playAgainTextX = (canvas.getWidth() - playAgainTextWidth) / 2;
                    double playAgainTextY = gameOverTextY + 20;

                    // Flashing "Press ESC to exit" text
                    String exitText = "Press ESC to exit";
                    double exitTextWidth = new Text(exitText).getLayoutBounds().getWidth();
                    double exitTextX = (canvas.getWidth() - exitTextWidth) / 2;
                    double exitTextY = playAgainTextY + 20;

                    // Toggle flash
                    flashCounter++;
                    flash = (flashCounter % 60 < 30);

                    // Draw flashing text
                    if (flash) {
                        gc.fillText(playAgainText, playAgainTextX, playAgainTextY);
                        gc.fillText(exitText, exitTextX, exitTextY);
                    }
                }
            }
        }.start();


        primaryStage.setScene(new Scene(gamePane, 256 * SCALE, 240 * SCALE));
        gamePane.requestFocus();
        canvas.requestFocus();
    }

    /**
     * Sets the crosshair image based on the given crosshair index.
     * The method creates an ImageView with the specified crosshair image and dimensions,
     * and adds it to the background pane of the game screen.
     *
     * @param crosshairIndex An array representing the index of the crosshair image in the crosshairChoices array.
     */
    private void setCrosshair(int[] crosshairIndex) {
        crosshairImageView = new ImageView(new Image(crosshairChoices[crosshairIndex[0]]));
        crosshairImageView.setFitHeight(30 * SCALE / 3);
        crosshairImageView.setFitWidth(30 * SCALE / 3);
        backgroundPane.getChildren().add(crosshairImageView);
    }

    /**
     * Retrieves a background image based on the given URL.
     * The method loads the image from the specified URL and creates a BackgroundImage object
     * with the image, background repeat properties, position, and size. The size of the background
     * image is determined by the image's width and height multiplied by the SCALE value.
     *
     * @param url The URL of the background image.
     * @return A BackgroundImage object representing the loaded background image.
     * @implNote The method assumes that the SCALE parameter is not greater than 3 to avoid potential issues.
     *           If a larger SCALE value is used, the background image may not be displayed correctly.
     */
    private BackgroundImage getBackgroundImage(String url) {
        Image image = new Image(url);
        return new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(image.getWidth() * SCALE, image.getHeight() * SCALE, true, true, true, true));
        // Have problem when SCALE parameter is bigger than 3.
    }

    /**
     * Plays the specified audio effect.
     * The method creates a MediaPlayer object with the audio file located at the given URL.
     * The audio effect is played with the specified cycle count and volume level.
     *
     * @param url         The URL of the audio file to be played.
     * @param cycleCount  The number of times the audio effect should be repeated.
     * @implNote The method assumes that the audio file exists at the specified URL.
     *           The URL should be a valid file path or a resource path accessible by the application.
     *           The cycleCount value determines how many times the audio effect will be played.
     *           A value of 1 plays the effect once, 2 plays it twice, and so on.
     *           The VOLUME constant determines the volume level of the audio effect.
     */
    private void playTheEffect(String url, int cycleCount) {
        mediaPlayer = new MediaPlayer(new Media(new File(url).toURI().toString()));
        mediaPlayer.setCycleCount(cycleCount);
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }

    /**
     * Reflects the given image horizontally.
     * The method creates a new image that is a horizontally reflected version of the original image.
     * It iterates over the pixels of the original image from left to right, creating a reflected image
     * by copying each pixel to the corresponding position in the reflected image from right to left.
     *
     * @param imageUrl The URL or file path of the image to be reflected.
     * @return The reflected image.
     * @implNote The method assumes that the image exists at the specified URL or file path.
     *           The method uses the JavaFX classes Image, WritableImage, PixelWriter, and PixelReader
     *           to manipulate the pixels of the image.
     *           The reflected image has the same width and height as the original image.
     *           Each pixel in the reflected image is obtained by copying the corresponding pixel
     *           from the original image, but from right to left.
     */
    public Image reflectImageHorizontally(String imageUrl) {
        Image image = new Image(imageUrl);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage reflectedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = reflectedImage.getPixelWriter();
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(width - x - 1, y);
                pixelWriter.setColor(x, y, color);
            }
        }

        return reflectedImage;
    }

    /**
     * Reflects the given image vertically.
     * The method creates a new image that is a vertically reflected version of the original image.
     * It iterates over the pixels of the original image from top to bottom, creating a reflected image
     * by copying each pixel to the corresponding position in the reflected image from bottom to top.
     *
     * @param imageUrl The URL or file path of the image to be reflected.
     * @return The reflected image.
     * @implNote The method assumes that the image exists at the specified URL or file path.
     *           The method uses the JavaFX classes Image, WritableImage, PixelWriter, and PixelReader
     *           to manipulate the pixels of the image.
     *           The reflected image has the same width and height as the original image.
     *           Each pixel in the reflected image is obtained by copying the corresponding pixel
     *           from the original image, but from bottom to top.
     */
    public Image reflectImageVertically(String imageUrl) {
        Image image = new Image(imageUrl);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage reflectedImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = reflectedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, height - y - 1);
                pixelWriter.setColor(x, y, color);
            }
        }

        return reflectedImage;
    }

    /**
     * Reflects the given image horizontally.
     * The method creates a new image that is a horizontally reflected version of the original image.
     * It iterates over the pixels of the original image from left to right, creating a reflected image
     * by copying each pixel to the corresponding position in the reflected image from right to left.
     *
     * @param image The image to be reflected horizontally.
     * @return The reflected image.
     * @implNote The method assumes that the image is not null and has a valid width and height.
     *           The method uses the JavaFX classes WritableImage, PixelWriter, and PixelReader
     *           to manipulate the pixels of the image.
     *           The reflected image has the same width and height as the original image.
     *           Each pixel in the reflected image is obtained by copying the corresponding pixel
     *           from the original image, but from right to left.
     */
    public Image reflectImageHorizontally(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage reflectedImage = new WritableImage(width, height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = reflectedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(width - x - 1, y);
                pixelWriter.setColor(x, y, color);
            }
        }

        return reflectedImage;
    }
}
