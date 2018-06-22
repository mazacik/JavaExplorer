package project.gui.stage.generic;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class DirectoryChooserWindow {
    /* components */
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private Window ownerWindow;

    /* constructors */
    public DirectoryChooserWindow(Window ownerWindow, String title, String initialDirectory) {
        this.ownerWindow = ownerWindow;
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(new File(initialDirectory));
    }

    /* public methods */
    public String getResultValue() {
        File selectedDirectory = directoryChooser.showDialog(ownerWindow);
        return selectedDirectory.getAbsolutePath();
    }
}