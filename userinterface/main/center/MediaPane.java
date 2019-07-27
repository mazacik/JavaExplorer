package userinterface.main.center;

import database.object.DataObject;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.InstanceManager;
import userinterface.main.NodeBase;
import userinterface.nodes.NodeUtil;
import userinterface.scene.SceneUtil;

public class MediaPane extends BorderPane implements NodeBase {
    private final Canvas canvas;
    private final ImageView gifPlayer;
    private final VideoPlayer videoPlayer;
    private final MediaViewControls controls;

    private Image currentImage = null;
    private DataObject currentCache = null;

    private Image placeholder = null;

    public MediaPane() {
        canvas = new Canvas();
        gifPlayer = new ImageView();
        videoPlayer = VideoPlayer.create(canvas);
        controls = new MediaViewControls(canvas, videoPlayer);

        GalleryPane galleryPane = InstanceManager.getGalleryPane();

        gifPlayer.fitWidthProperty().bind(galleryPane.widthProperty());
        gifPlayer.fitHeightProperty().bind(galleryPane.heightProperty());

        this.setBorder(NodeUtil.getBorder(0, 1, 0, 1));
        this.setCenter(canvas);
    }

    public boolean reload() {
        DataObject currentTarget = InstanceManager.getTarget().getCurrentTarget();
        if (SceneUtil.isFullView() && currentTarget != null) {
            switch (currentTarget.getFileType()) {
                case IMAGE:
                    reloadAsImage(currentTarget);
                    break;
                case GIF:
                    reloadAsGif(currentTarget);
                    break;
                case VIDEO:
                    reloadAsVideo(currentTarget);
                    break;
            }
            return true;
        }

        return false;
    }

    private void reloadAsImage(DataObject currentTarget) {
        controls.setVideoMode(false);

        if (this.getCenter() != canvas) this.setCenter(canvas);

        if (currentCache == null || !currentCache.equals(currentTarget)) {
            currentCache = currentTarget;
            currentImage = new Image("file:" + currentCache.getPath());
        }

        double imageWidth = currentImage.getWidth();
        double imageHeight = currentImage.getHeight();
        double maxWidth = canvas.getWidth();
        double maxHeight = canvas.getHeight();

        boolean upScale = true;

        double resultWidth;
        double resultHeight;

        //noinspection ConstantConditions
        if (!upScale && imageWidth < maxWidth && imageHeight < maxHeight) {
            // image is smaller than canvas, upscaling is off
            resultWidth = imageWidth;
            resultHeight = imageHeight;
        } else {
            // scale image to fit width
            resultWidth = maxWidth;
            resultHeight = imageHeight * maxWidth / imageWidth;

            // if scaled image is too tall, scale to fit height instead
            if (resultHeight > maxHeight) {
                resultHeight = maxHeight;
                resultWidth = imageWidth * maxHeight / imageHeight;
            }
        }

        double resultX = maxWidth / 2 - resultWidth / 2;
        double resultY = maxHeight / 2 - resultHeight / 2;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(currentImage, resultX, resultY, resultWidth, resultHeight);
    }
    private void reloadAsGif(DataObject currentTarget) {
        controls.setVideoMode(false);

        if (this.getCenter() != gifPlayer) this.setCenter(gifPlayer);

        if (currentCache == null || !currentCache.equals(currentTarget)) {
            currentCache = currentTarget;
            currentImage = new Image("file:" + currentCache.getPath());
        }

        gifPlayer.setImage(currentImage);
    }
    private void reloadAsVideo(DataObject currentTarget) {
        if (this.getCenter() != canvas) this.setCenter(canvas);
	
		if (VideoPlayer.doLibsExist()) {
            controls.setVideoMode(true);

            if (currentCache == null || !currentCache.equals(currentTarget)) {
                currentCache = currentTarget;
                videoPlayer.start(currentTarget.getPath());
            } else {
                videoPlayer.resume();
            }
        } else {
            controls.setVideoMode(false);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            if (placeholder == null) placeholder = createPlaceholder();
            gc.drawImage(placeholder, 0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    private Image createPlaceholder() {
        Label label = new Label("Placeholder");
        label.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        label.setWrapText(true);
        label.setFont(new Font(100));
        label.setAlignment(Pos.CENTER);

        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        label.setMinWidth(width);
        label.setMinHeight(height);
        label.setMaxWidth(width);
        label.setMaxHeight(height);

        WritableImage img = new WritableImage(width, height);
        Scene scene = new Scene(new Group(label));
        scene.setFill(Color.GRAY);
        scene.snapshot(img);
        return img;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public VideoPlayer getVideoPlayer() {
        return videoPlayer;
    }
    public MediaViewControls getControls() {
        return controls;
    }
}