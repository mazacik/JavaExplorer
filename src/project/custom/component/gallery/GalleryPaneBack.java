package project.custom.component.gallery;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import project.common.Database;
import project.common.Utility;
import project.database.DatabaseItem;

public class GalleryPaneBack {
    /* lazy singleton */
    private static GalleryPaneBack instance;
    public static GalleryPaneBack getInstance() {
        if (instance == null) instance = new GalleryPaneBack();
        return instance;
    }

    /* imports */
    private final TilePane tilePane = GalleryPaneFront.getInstance().getTilePane();
    private final ObservableList<Node> galleryTiles = tilePane.getChildren();

    /* constructors */
    private GalleryPaneBack() {
        GalleryPaneListener.getInstance();
    }

    /* public methods */
    public void reloadContent() {
        if (Utility.isPreviewFullscreen()) return;
        galleryTiles.clear();
        for (DatabaseItem databaseItem : Database.getDatabaseItemsFiltered())
            galleryTiles.add(databaseItem.getGalleryTile());
    }

    public int getColumnCount() {
        int tilePaneWidth = (int) tilePane.getWidth();
        int prefTileWidth = (int) tilePane.getPrefTileWidth();
        return tilePaneWidth / prefTileWidth;
    }

    public void adjustViewportPositionToFocus(){
        int columnCount = GalleryPaneBack.getInstance().getColumnCount();
        int currentIndex = Database.getDatabaseItemsFiltered().indexOf(GalleryPaneFront.getInstance().getCurrentFocusedItem());
        int currentRow = currentIndex / columnCount;

        double viewportHeight = GalleryPaneFront.getInstance().getViewportBounds().getHeight();
        double contentHeight = tilePane.getHeight() - viewportHeight;
        double rowHeight = tilePane.getPrefTileHeight() + tilePane.getVgap();

        double rowToContentRatio = rowHeight / contentHeight;
        double viewportToContentRatio = viewportHeight / contentHeight;

        Bounds viewportBounds = GalleryPaneFront.getInstance().getViewportBounds();
        Bounds tileBounds = galleryTiles.get(currentIndex).getBoundsInParent();

        double viewportTop = viewportBounds.getMaxY() * -1 + viewportBounds.getHeight();
        double viewportBottom = viewportBounds.getMinY() * -1 + viewportBounds.getHeight();
        double tileTop = tileBounds.getMaxY();
        double tileBottom = tileBounds.getMinY();

        if (viewportTop + rowHeight > tileTop) {
            GalleryPaneFront.getInstance().setVvalue(currentRow * rowToContentRatio);
        } else if (viewportBottom - rowHeight < tileBottom) {
            GalleryPaneFront.getInstance().setVvalue((currentRow + 1) * rowToContentRatio - viewportToContentRatio);
        }
    }
}