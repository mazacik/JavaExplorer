package project.backend;

import javafx.scene.image.ImageView;
import project.frontend.ColoredText;
import project.frontend.GalleryPane;

import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseItem implements Serializable {
    private String fullPath;
    private String simpleName;
    private String extension;
    private ArrayList<String> tags;
    private transient ImageView imageView;
    private transient ColoredText coloredText;

    String getExtension() {
        return this.extension;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    String getSimpleName() {
        return this.simpleName;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ColoredText getColoredText() {
        return coloredText;
    }

    void setExtension(String Extension) {
        this.extension = Extension;
    }

    void setFullPath(String FullPath) {
        this.fullPath = FullPath;
    }

    void setSimpleName(String SimpleName) {
        this.simpleName = SimpleName;
    }

    void setTags(ArrayList<String> Tags) {
        this.tags = Tags;
    }

    void setImageView(ImageView imageView) {
        this.imageView = (imageView != null) ? imageView : new ImageView();
        this.imageView.setOnMouseClicked(event -> GalleryPane.imageViewClicked(this));
    }

    void setColoredText(ColoredText coloredText) {
        this.coloredText = coloredText;
    }
}