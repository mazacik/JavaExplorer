package user_interface.singleton.side;

import control.Reload;
import database.object.DataObject;
import database.object.TagObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lifecycle.InstanceManager;
import user_interface.factory.ColorData;
import user_interface.factory.base.EditNode;
import user_interface.factory.base.Separator;
import user_interface.factory.base.TextNode;
import user_interface.factory.menu.ClickMenuLeft;
import user_interface.singleton.NodeBase;
import user_interface.utils.ColorUtil;
import user_interface.utils.NodeUtil;
import user_interface.utils.SizeUtil;
import user_interface.utils.StyleUtil;
import user_interface.utils.enums.ColorType;
import utils.enums.Direction;

import java.util.ArrayList;

public class SelectPane extends VBox implements NodeBase {
    private final TextNode nodeTitle;
    private final EditNode tfSearch;
    private String actualText = "";

    private final TextNode nodeSelectAll;
    private final TextNode nodeSelectNone;
    private final TextNode nodeSelectMerge;

    private final VBox tagListBox;
    private final ScrollPane tagListScrollPane;
    private final ArrayList<String> expandedGroupsList;

    public SelectPane() {
        ColorData colorDataSimple = new ColorData(ColorType.DEF, ColorType.ALT, ColorType.DEF, ColorType.DEF);

        nodeTitle = new TextNode("", colorDataSimple);
        nodeTitle.setBorder(NodeUtil.getBorder(0, 0, 1, 0));
        nodeTitle.prefWidthProperty().bind(this.widthProperty());

        tfSearch = new EditNode("Search tags to add to selection");
        tfSearch.setBorder(NodeUtil.getBorder(0, 0, 1, 0));

        nodeSelectAll = new TextNode("Select All", colorDataSimple);
        nodeSelectNone = new TextNode("Select None", colorDataSimple);
        nodeSelectMerge = new TextNode("Merge Selection", colorDataSimple);
        ClickMenuLeft.install(nodeTitle, Direction.LEFT, nodeSelectAll, nodeSelectNone, new Separator(), nodeSelectMerge);

        tagListBox = NodeUtil.getVBox(ColorType.DEF);
        tagListScrollPane = new ScrollPane();
        tagListScrollPane.setContent(tagListBox);
        tagListScrollPane.setFitToWidth(true);
        tagListScrollPane.setFitToHeight(true);
        tagListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tagListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        NodeUtil.addToManager(tagListScrollPane, ColorType.DEF);

        expandedGroupsList = new ArrayList<>();

        this.setPrefWidth(SizeUtil.getUsableScreenWidth());
        this.setMinWidth(SizeUtil.getMinWidthSideLists());
        this.getChildren().addAll(nodeTitle, tfSearch, tagListScrollPane);
    }

    public boolean reload() {
        tagListBox.getChildren().clear();
        int hiddenTilesCount = 0;
        for (DataObject dataObject : InstanceManager.getSelect()) {
            if (!InstanceManager.getFilter().contains(dataObject)) {
                hiddenTilesCount++;
            }
        }

        String text = "Selection: " + InstanceManager.getSelect().size() + " file(s)";
        if (hiddenTilesCount > 0) {
            text += ", " + hiddenTilesCount + " filtered out";
        }

        nodeTitle.setText(text);
        this.actuallyReload();
        StyleUtil.applyStyle(tagListBox);
        return true;
    }
    private void actuallyReload() {
        Color textColorDefault = ColorUtil.getTextColorDef();
        Color textColorPositive = ColorUtil.getTextColorPos();
        Color textColorShare = ColorUtil.getTextColorShr();

        ArrayList<String> groupsInter;
        ArrayList<String> groupsShare;
        if (InstanceManager.getSelect().size() == 0) {
            if (InstanceManager.getTarget().getCurrentTarget() != null) {
                groupsInter = InstanceManager.getTarget().getCurrentTarget().getTagList().getGroups();
                groupsShare = new ArrayList<>();
            } else {
                return;
            }
        } else {
            groupsInter = InstanceManager.getSelect().getIntersectingTags().getGroups();
            groupsShare = InstanceManager.getSelect().getSharedTags().getGroups();
        }

        for (String group : InstanceManager.getTagListMain().getGroups()) {
            GroupNode groupNode;
            if (groupsInter.contains(group)) {
                groupNode = NodeUtil.getGroupNode(this, group, textColorPositive);
            } else if (groupsShare.contains(group)) {
                groupNode = NodeUtil.getGroupNode(this, group, textColorShare);
            } else {
                groupNode = NodeUtil.getGroupNode(this, group, textColorDefault);
            }

            ArrayList<String> namesInter;
            ArrayList<String> namesShare;
            if (InstanceManager.getSelect().size() == 0) {
                namesInter = InstanceManager.getTarget().getCurrentTarget().getTagList().getNames(group);
                namesShare = new ArrayList<>();
            } else {
                namesInter = InstanceManager.getSelect().getIntersectingTags().getNames(group);
                namesShare = InstanceManager.getSelect().getSharedTags().getNames(group);
            }

            for (String name : InstanceManager.getTagListMain().getNames(group)) {
                if (namesInter.contains(name)) {
                    groupNode.getNameNodes().add(this.createNameNode(name, textColorPositive, groupNode));
                } else if (namesShare.contains(name)) {
                    groupNode.getNameNodes().add(this.createNameNode(name, textColorShare, groupNode));
                } else {
                    groupNode.getNameNodes().add(this.createNameNode(name, textColorDefault, groupNode));
                }
            }

            tagListBox.getChildren().add(groupNode);

            if (expandedGroupsList.contains(group)) {
                groupNode.setArrowExpanded(true);
                tagListBox.getChildren().addAll(groupNode.getNameNodes());
            }
        }
    }
    private TextNode createNameNode(String name, Color textColor, GroupNode groupNode) {
        TextNode nameNode = new TextNode(name, ColorType.DEF, ColorType.ALT, ColorType.NULL, ColorType.NULL);

        nameNode.setTextFill(textColor);
        nameNode.setAlignment(Pos.CENTER_LEFT);
        nameNode.prefWidthProperty().bind(tagListBox.widthProperty());
        nameNode.setPadding(new Insets(0, 0, 0, 50));
        nameNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                changeNodeState(groupNode, nameNode);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                InstanceManager.getClickMenuInfo().setGroup(groupNode.getText());
                InstanceManager.getClickMenuInfo().setName(nameNode.getText());
                InstanceManager.getClickMenuInfo().show(nameNode, event);
            }
        });

        return nameNode;
    }

    public void changeNodeState(GroupNode groupNode, TextNode nameNode) {
        TagObject tagObject = InstanceManager.getTagListMain().getTagObject(groupNode.getText(), nameNode.getText());
        if (nameNode.getTextFill().equals(ColorUtil.getTextColorPos()) || nameNode.getTextFill().equals(ColorUtil.getTextColorShr())) {
            nameNode.setTextFill(ColorUtil.getTextColorDef());
            this.removeTagObjectFromSelection(tagObject);
        } else {
            nameNode.setTextFill(ColorUtil.getTextColorPos());
            this.addTagObjectToSelection(tagObject);
        }

        InstanceManager.getReload().flag(Reload.Control.INFO);
        InstanceManager.getReload().doReload();
    }
    public void addTagObjectToSelection(TagObject tagObject) {
        if (InstanceManager.getSelect().size() < 1) {
            DataObject currentTargetedItem = InstanceManager.getTarget().getCurrentTarget();
            if (currentTargetedItem != null) {
                currentTargetedItem.getTagList().add(tagObject);
            }
        } else {
            InstanceManager.getSelect().addTagObject(tagObject);
        }
    }
    public void removeTagObjectFromSelection(TagObject tagObject) {
        if (InstanceManager.getSelect().size() < 1) {
            DataObject currentTargetedItem = InstanceManager.getTarget().getCurrentTarget();
            if (currentTargetedItem != null) {
                currentTargetedItem.getTagList().remove(tagObject);
            }
        } else {
            InstanceManager.getSelect().removeTagObject(tagObject);
        }
    }

    public ScrollPane getTagListScrollPane() {
        return tagListScrollPane;
    }
    public TextField getTfSearch() {
        return tfSearch;
    }
    public String getActualText() {
        return actualText;
    }
    public void setActualText(String actualText) {
        this.actualText = actualText;
    }
    public ArrayList<String> getExpandedGroupsList() {
        return expandedGroupsList;
    }

    public TextNode getNodeSelectAll() {
        return nodeSelectAll;
    }
    public TextNode getNodeSelectNone() {
        return nodeSelectNone;
    }
    public TextNode getNodeSelectMerge() {
        return nodeSelectMerge;
    }
    public VBox getTagListBox() {
        return tagListBox;
    }
}
