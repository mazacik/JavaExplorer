package project.gui.event.listener.datacontextmenu;

import project.gui.GUIInstance;
import project.gui.event.handler.contextmenu.EventHandlerContextMenuDelete;

public abstract class EventListenerContextMenuDelete {
    public static void onAction() {
        GUIInstance.getDataObjectContextMenu().getMenuDelete().setOnAction(event -> EventHandlerContextMenuDelete.onAction());
    }
}