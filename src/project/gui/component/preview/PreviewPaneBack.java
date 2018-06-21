package project.gui.component.preview;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import project.backend.Settings;
import project.database.part.DatabaseItem;
import project.gui.GUIController;
import project.gui.component.gallery.GalleryPane;

public class PreviewPaneBack {
    /* lazy singleton */
    private static PreviewPaneBack instance;
    public static PreviewPaneBack getInstance() {
        if (instance == null) instance = new PreviewPaneBack();
        return instance;
    }

    /* imports */
    private final Canvas canvas = PreviewPane.getInstance().getCanvas();

    /* constructors */
    private PreviewPaneBack() {
        PreviewPaneListener.getInstance();
    }

    /* variables */
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private DatabaseItem currentDatabaseItem = null;
    private Image currentPreviewImage = null;

    /* public methods */
    public void reloadContent() {
        if (!GUIController.isPreviewFullscreen()) return;
        if (GalleryPane.getInstance().getCurrentFocusedItem() == null) return;
        if (!GalleryPane.getInstance().getCurrentFocusedItem().equals(currentDatabaseItem)) loadImage();

        double imageWidth = currentPreviewImage.getWidth();
        double imageHeight = currentPreviewImage.getHeight();
        double maxWidth = canvas.getWidth();
        double maxHeight = canvas.getHeight();

        // scale image to fit width
        double resultWidth = maxWidth;
        double resultHeight = imageHeight * maxWidth / imageWidth;

        // if scaled image is too tall, scale to fit height instead
        if (resultHeight > maxHeight) {
            resultHeight = maxHeight;
            resultWidth = imageWidth * maxHeight / imageHeight;
        }

        double resultX = canvas.getWidth() / 2 - resultWidth / 2;
        double resultY = canvas.getHeight() / 2 - resultHeight / 2;

        gc.clearRect(0, 0, PreviewPane.getInstance().getWidth(), PreviewPane.getInstance().getHeight());
        gc.drawImage(currentPreviewImage, resultX, resultY, resultWidth, resultHeight);
    }

    /* private methods */
    private void loadImage() {
        currentDatabaseItem = GalleryPane.getInstance().getCurrentFocusedItem();
        currentPreviewImage = new Image("file:" + Settings.getMainDirectoryPath() + "/" + currentDatabaseItem.getName());
    }
}
