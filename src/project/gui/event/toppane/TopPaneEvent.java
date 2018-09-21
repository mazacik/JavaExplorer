package project.gui.event.toppane;

import javafx.stage.WindowEvent;
import project.Main;
import project.control.Filter;
import project.control.FilterControl;
import project.control.MainControl;
import project.database.loader.Serialization;
import project.gui.component.GUINode;
import project.gui.component.toppane.TopPane;
import project.gui.custom.generic.NumberInputWindow;

public abstract class TopPaneEvent {
    public static void initialize() {
        onAction_menuSave();
        onAction_menuExit();

        onAction_menuSelectAll();
        onAction_menuClearSelection();

        onAction_menuUntaggedOnly();
        onAction_menuMaxXTags();
        onAction_menuModeWhitelistAll();
        onAction_menuModeWhitelistAny();
        onAction_menuModeBlacklistAll();
        onAction_menuModeBlacklistAny();
        onAction_menuRefresh();
        onAction_menuReset();
    }

    private static void onAction_menuSave() {
        TopPane.getMenuSave().setOnAction(event -> Serialization.writeToDisk());
    }
    private static void onAction_menuExit() {
        TopPane.getMenuExit().setOnAction(event -> TopPane.getInstance().fireEvent(new WindowEvent(Main.getStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));
    }

    private static void onAction_menuSelectAll() {
        TopPane.getMenuSelectAll().setOnAction(event -> {
            MainControl.getSelectionControl().addDataObject(MainControl.getFilterControl().getCollection());
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }
    private static void onAction_menuClearSelection() {
        TopPane.getMenuClearSelection().setOnAction(event -> {
            MainControl.getSelectionControl().clearDataObjects();
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }

    private static void onAction_menuUntaggedOnly() {
        TopPane.getMenuUntaggedOnly().setOnAction(event -> {
            MainControl.getFilterControl().setFilter(Filter.SHOW_UNTAGGED);
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }
    private static void onAction_menuMaxXTags() {
        TopPane.getMenuMaxXTags().setOnAction(event -> {
            int maxTags = new NumberInputWindow("Filter Settings", "Maximum number of tags:").getResultValue();
            Filter.setMaxTagsValue(maxTags);

            MainControl.getFilterControl().setFilter(Filter.SHOW_MAX_X_TAGS);
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }
    private static void onAction_menuModeWhitelistAll() {
        TopPane.getMenuModeWhitelistAll().setOnAction(event -> {
            TopPane.getMenuModeWhitelistAll().setSelected(true);
            TopPane.getMenuModeWhitelistAny().setSelected(false);
            MainControl.getFilterControl().setWhitelistMode(FilterControl.FilterMode.All);
            MainControl.getFilterControl().applyFilter();
        });
    }
    private static void onAction_menuModeWhitelistAny() {
        TopPane.getMenuModeWhitelistAny().setOnAction(event -> {
            TopPane.getMenuModeWhitelistAll().setSelected(false);
            TopPane.getMenuModeWhitelistAny().setSelected(true);
            MainControl.getFilterControl().setWhitelistMode(FilterControl.FilterMode.Any);
            MainControl.getFilterControl().applyFilter();
        });
    }
    private static void onAction_menuModeBlacklistAll() {
        TopPane.getMenuModeBlacklistAll().setOnAction(event -> {
            TopPane.getMenuModeBlacklistAll().setSelected(true);
            TopPane.getMenuModeBlacklistAny().setSelected(false);
            MainControl.getFilterControl().setBlacklistMode(FilterControl.FilterMode.All);
            MainControl.getFilterControl().applyFilter();
        });
    }
    private static void onAction_menuModeBlacklistAny() {
        TopPane.getMenuModeBlacklistAny().setOnAction(event -> {
            TopPane.getMenuModeBlacklistAll().setSelected(false);
            TopPane.getMenuModeBlacklistAny().setSelected(true);
            MainControl.getFilterControl().setBlacklistMode(FilterControl.FilterMode.Any);
            MainControl.getFilterControl().applyFilter();
        });
    }
    private static void onAction_menuRefresh() {
        TopPane.getMenuRefresh().setOnAction(event -> {
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }
    private static void onAction_menuReset() {
        TopPane.getMenuReset().setOnAction(event -> {
            MainControl.getFilterControl().setFilter(Filter.SHOW_EVERYTHING);
            MainControl.getReloadControl().reload(true, GUINode.GALLERYPANE, GUINode.RIGHTPANE);
        });
    }
}
