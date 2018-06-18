package project.database;

import javafx.scene.image.Image;
import project.gui.component.gallery.part.GalleryTile;

import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseItem implements Serializable {
    /* variables */
    private String name;
    private ArrayList<String> tags;

    private transient Image image;
    private transient GalleryTile galleryTile;

    /* getters */
    public String getName() {
        return name;
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public Image getImage() {
        return image;
    }
    public GalleryTile getGalleryTile() {
        return galleryTile;
    }

    /* setters */
    public void setName(String SimpleName) {
        this.name = SimpleName;
    }
    public void setTags(ArrayList<String> Tags) {
        this.tags = Tags;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public void setGalleryTile(GalleryTile galleryTile) {
        this.galleryTile = (galleryTile != null) ? galleryTile : new GalleryTile(this);
    }
}
