package com.example.demo9;


import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HelloApplication extends Application {

    // Grid pane for thumbnails and stack pane for full image view
    private GridPane thumbnailGrid;
    private StackPane fullImageViewPane;
    private ImageView fullImageView;

    // Array of image paths (both for thumbnails and full-size images)
    private String[] imagePaths = {
            "src\\main\\resources\\images\\thumb1.jpg",  // Thumbnail 1
            "src\\main\\resources\\images\\thumb2.jpg",   // Thumbnail 2
            "src\\main\\resources\\images\\thumb3.jpg",   // Thumbnail 3
            "src\\main\\resources\\images\\thumb4.jpg", // Thumbnail 4
    };

    // To track the current image index in the slideshow
    private int currentImageIndex = 0;

    // Initialize method to load thumbnails and set up slideshow
    @Override
    public void start(Stage stage) {
        // Initialize main containers
        thumbnailGrid = new GridPane();
        fullImageViewPane = new StackPane();
        fullImageView = new ImageView();

        // Set up layout for full image view
        fullImageViewPane.setVisible(false);
        fullImageViewPane.getChildren().add(fullImageView);

        // Set up grid layout for thumbnails
        loadThumbnails();

        // Buttons for previous and back navigation
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackToThumbnails());

        Button previousButton = new Button("Previous");
        previousButton.setOnAction(e -> previousImage());

        // Main layout container (VBox holds both thumbnail grid and full image view)
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(thumbnailGrid, fullImageViewPane, previousButton, backButton);

        // Create scene and set it up on the stage
        Scene scene = new Scene(mainLayout, 800, 600);

       

        // Set background color for the main layout using internal CSS
        mainLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20; -fx-alignment: center;");

        scene.setOnKeyPressed(this::handleKeyEvent);

        stage.setTitle("Image Gallery");
        stage.setScene(scene);
        stage.show();

        // Start slideshow when the application is initialized
        startSlideshow();
    }

    // Load thumbnails into the grid pane
    private void loadThumbnails() {
        for (int i = 0; i < imagePaths.length; i++) {
            final int index = i;  // Capture 'i' as a final variable
            ImageView thumbnail = new ImageView(new Image("file:" + imagePaths[i]));
            thumbnail.setFitHeight(100);
            thumbnail.setFitWidth(100);
            thumbnail.setPreserveRatio(true);
            thumbnail.setOnMouseClicked(event -> showFullImage(index));  // Use final 'index'

            // Apply hover effect (scale transition) for zoom-in effect on thumbnails
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), thumbnail);
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            thumbnail.setOnMouseEntered(e -> scaleTransition.play());
            thumbnail.setOnMouseExited(e -> scaleTransition.setRate(-1));

            // Add the thumbnail to the grid
            thumbnailGrid.add(thumbnail, i % 4, i / 4);
        }
    }

    // Start the slideshow, automatically advancing every 3 seconds
    private void startSlideshow() {
        Timeline slideshowTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> nextImage())
        );
        slideshowTimeline.setCycleCount(Timeline.INDEFINITE);  // Repeat indefinitely
        slideshowTimeline.play();
    }

    // Show the next image in the slideshow
    private void nextImage() {
        currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
        showFullImage(currentImageIndex);
    }

    // Show the full-size image when a thumbnail is clicked
    private void showFullImage(int index) {
        // Apply fade-in transition to full image view
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), fullImageViewPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fullImageViewPane.setVisible(true);
        fullImageView.setImage(new Image("file:" + imagePaths[index]));
        fadeTransition.play();

        // Hide the thumbnails with a fade-out transition
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.5), thumbnailGrid);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> thumbnailGrid.setVisible(false));
        fadeOutTransition.play();
    }

    // Go back to the thumbnail grid after viewing the full-size image
    private void goBackToThumbnails() {
        // Apply fade-out transition for full image view
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.5), fullImageViewPane);
        fadeOutTransition.setFromValue(1);
        fadeOutTransition.setToValue(0);
        fadeOutTransition.setOnFinished(event -> fullImageViewPane.setVisible(false));
        fadeOutTransition.play();

        // Apply fade-in transition to show thumbnails
        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), thumbnailGrid);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);
        thumbnailGrid.setVisible(true);
        fadeInTransition.play();
    }

    // Handle key events for navigation
    private void handleKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT:
                previousImage();
                break;
            case RIGHT:
                nextImage();
                break;
            default:
                break;
        }
    }

    // Show the previous image in the slideshow
    private void previousImage() {
        currentImageIndex = (currentImageIndex - 1 + imagePaths.length) % imagePaths.length;
        showFullImage(currentImageIndex);
    }

    // Start the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
