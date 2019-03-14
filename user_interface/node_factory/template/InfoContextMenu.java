package user_interface.node_factory.template;

import database.object.InfoObject;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import system.InstanceRepo;

public class InfoContextMenu extends ContextMenu implements InstanceRepo {
    private final MenuItem menuEdit = new MenuItem("Edit Tag");
    private final MenuItem menuRemove = new MenuItem("Remove Tag");

    private InfoObject infoObject = null;

    public InfoContextMenu() {
        this.getItems().addAll(menuEdit, menuRemove);
        menuEdit.setOnAction(event -> mainInfoList.edit(infoObject));
        menuRemove.setOnAction(event -> mainInfoList.remove(infoObject));
    }

    public void show(Node anchor, double screenX, double screenY) {
        super.show(anchor, screenX, screenY);
    }
    public void show(Node anchor, MouseEvent event) {
        super.show(anchor, event.getScreenX(), event.getScreenY());
    }

    public void setInfoObject(InfoObject infoObject) {
        this.infoObject = infoObject;
    }
}
