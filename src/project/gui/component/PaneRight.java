package project.gui.component;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import project.control.FilterControl;
import project.control.FocusControl;
import project.control.SelectionControl;
import project.database.control.TagElementControl;
import project.database.element.DataElement;
import project.database.element.TagElement;
import project.gui.change.ChangeEventControl;
import project.gui.change.ChangeEventEnum;
import project.gui.change.ChangeEventListener;
import project.gui.control.GUIStage;

import java.util.ArrayList;

public class PaneRight extends BorderPane implements ChangeEventListener {
    /* components */
    private final ListView<String> listView = new ListView<>();
    private final ComboBox cbGroup = new ComboBox();
    private final ComboBox cbName = new ComboBox();
    private final Button btnAdd = new Button("Add");
    private final Button btnManage = new Button("New");

    /* constructors */
    public PaneRight() {
        initializeComponents();
        initializeProperties();
    }

    /* initialize */
    private void initializeComponents() {
        btnAdd.setStyle("-fx-focus-color: transparent;");
        btnAdd.setMinWidth(25);
        btnAdd.prefWidthProperty().bind(prefWidthProperty());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        btnAdd.setMaxWidth(getMaxWidth());
        btnManage.setMaxWidth(getMaxWidth());
        cbGroup.setMaxWidth(getMaxWidth());
        cbName.setMaxWidth(getMaxWidth());

        btnManage.prefWidthProperty().bind(prefWidthProperty());
        btnManage.setOnAction(event -> {
            TagElement newTagElement = TagElementControl.create();
            if (newTagElement == null) return;
            TagElementControl.add(newTagElement);
            cbGroup.setValue(newTagElement.getGroup());
            cbName.setValue(newTagElement.getName());
        });

        cbGroup.prefWidthProperty().bind(prefWidthProperty());
        cbGroup.setOnShown(event -> {
            cbGroup.getItems().clear();
            cbGroup.getItems().addAll(TagElementControl.getGroups());
        });

        cbName.prefWidthProperty().bind(prefWidthProperty());
        cbName.setOnShown(event -> {
            cbName.getItems().clear();
            if (cbGroup.getValue() != null && !cbGroup.getValue().toString().isEmpty()) {
                cbName.getItems().addAll(TagElementControl.getNamesInGroup(cbGroup.getValue().toString()));
            }
        });

        btnAdd.setOnAction(event -> addTagToSelection());

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addTagToSelection();
            } else if (event.getCode() == KeyCode.DELETE) {
                FilterControl.removeTagElementSelectionFromDataElementSelection();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                GUIStage.getPaneTop().requestFocus();
            }
        });

        ContextMenu listContextMenu = new ContextMenu();
        MenuItem menuRemoveTag = new MenuItem("Remove");
        menuRemoveTag.setOnAction(event -> FilterControl.removeTagElementSelectionFromDataElementSelection());
        listContextMenu.getItems().add(menuRemoveTag);
        listView.setContextMenu(listContextMenu);
    }
    private void initializeProperties() {
        setMinWidth(150);
        setPrefWidth(200);
        setMaxWidth(300);

        setCenter(listView);
        setBottom(new VBox(2, cbGroup, cbName, btnAdd, btnManage));

        ChangeEventControl.subscribe(this, ChangeEventEnum.FOCUS, ChangeEventEnum.SELECTION);
    }

    /* public */
    public void refreshComponent() {
        ArrayList<String> sharedTags = new ArrayList<>();
        if (SelectionControl.isSelectionEmpty()) {
            DataElement currentFocusedItem = FocusControl.getCurrentFocus();
            if (currentFocusedItem != null) {
                for (TagElement tagElement : currentFocusedItem.getTagElements()) {
                    sharedTags.add(tagElement.getGroupAndName());
                }
            }
        } else {
            for (TagElement tagElement : SelectionControl.getIntersectingTags()) {
                sharedTags.add(tagElement.getGroupAndName());
            }
        }
        listView.getItems().setAll(sharedTags);
    }

    /* private */
    private void addTagToSelection() {
        Object cbGroupValue = cbGroup.getValue();
        Object cbNameValue = cbName.getValue();
        String group = "";
        String name = "";

        if (cbGroupValue != null) {
            group = cbGroupValue.toString();
        }
        if (cbNameValue != null) {
            name = cbNameValue.toString();
        }

        if (!group.isEmpty() && !name.isEmpty()) {
            TagElement tagElement = TagElementControl.getTagElement(group, name);
            if (SelectionControl.isSelectionEmpty()) {
                DataElement currentFocusedItem = FocusControl.getCurrentFocus();
                if (currentFocusedItem != null) {
                    currentFocusedItem.getTagElements().add(tagElement);
                }
            } else {
                FilterControl.addTagElementToDataElementSelection(tagElement);
            }
        }
    }

    /* get */
    public ListView<String> getListView() {
        return listView;
    }
}
