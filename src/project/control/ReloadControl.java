package project.control;

import project.database.control.DataElementControl;
import project.database.control.TagElementControl;
import project.gui.component.GalleryPane.GalleryPane;
import project.gui.component.LeftPane.LeftPane;
import project.gui.component.PreviewPane.PreviewPane;
import project.gui.component.RightPane.RightPane;
import project.gui.component.TopPane.TopPane;

public abstract class ReloadControl {
    /* vars */
    private static boolean reloadTopPane = false;
    private static boolean reloadLeftPane = false;
    private static boolean reloadGalleryPane = false;
    private static boolean reloadPreviewPane = false;
    private static boolean reloadRightPane = false;

    /* public */
    public static void requestGlobalReload(boolean sortElementControls) {
        if (sortElementControls) {
            DataElementControl.sortAll();
            TagElementControl.sortAll();
        }
        TopPane.reload();
        LeftPane.reload();
        GalleryPane.reload();
        PreviewPane.reload();
        RightPane.reload();
    }
    public static void requestComponentReload(Class... components) {
        ReloadControl.requestComponentReload(false, components);
    }
    public static void requestComponentReload(boolean doReload, Class... components) {
        for (Class component : components) {
            if (component.equals(TopPane.class)) {
                reloadTopPane = true;
            } else if (component.equals(LeftPane.class)) {
                reloadLeftPane = true;
            } else if (component.equals(GalleryPane.class)) {
                reloadGalleryPane = true;
            } else if (component.equals(PreviewPane.class)) {
                reloadPreviewPane = true;
            } else if (component.equals(RightPane.class)) {
                reloadRightPane = true;
            }
        }
        if (doReload) forceReload();
    }
    public static void forceReload() {
        if (reloadTopPane) {
            TopPane.reload();
            reloadTopPane = false;
        }
        if (reloadLeftPane) {
            LeftPane.reload();
            reloadLeftPane = false;
        }
        if (reloadGalleryPane) {
            GalleryPane.reload();
            reloadGalleryPane = false;
        }
        if (reloadPreviewPane) {
            PreviewPane.reload();
            reloadPreviewPane = false;
        }
        if (reloadRightPane) {
            RightPane.reload();
            reloadRightPane = false;
        }
    }
}
