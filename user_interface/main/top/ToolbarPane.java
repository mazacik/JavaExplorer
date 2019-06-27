package user_interface.main.top;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import main.InstanceManager;
import user_interface.main.NodeBase;
import user_interface.nodes.ColorData;
import user_interface.nodes.NodeUtil;
import user_interface.nodes.base.Separator;
import user_interface.nodes.base.TextNode;
import user_interface.nodes.buttons.ButtonFactory;
import user_interface.nodes.buttons.ButtonTemplates;
import user_interface.nodes.menu.ClickMenuLeft;
import user_interface.style.SizeUtil;
import user_interface.style.enums.ColorType;
import utils.enums.Direction;

public class ToolbarPane extends BorderPane implements NodeBase {
    private final TextNode nodeSave;
    private final TextNode nodeImport;
    private final TextNode nodeSettings;
    private final TextNode nodeExit;

    private final TextNode nodeRandom;
    private final TextNode nodeFullview;

    private final TextNode nodeInfo;

    private final TextNode nodeTarget;

    public ToolbarPane() {
        ColorData colorData = new ColorData(ColorType.DEF, ColorType.ALT, ColorType.DEF, ColorType.DEF);

        TextNode nodeFile = new TextNode("File", colorData);
        nodeSave = new TextNode("Save", colorData);
        nodeImport = new TextNode("Import", colorData);
        nodeSettings = new TextNode("Settings", colorData);
        nodeExit = new TextNode("Exit", colorData);
        ClickMenuLeft.install(nodeFile, Direction.DOWN, nodeSave, nodeImport, nodeSettings, new Separator(), nodeExit);

        nodeRandom = new TextNode("Random", colorData);
        nodeFullview = new TextNode("MediaPane", colorData);
        HBox hBoxTools = NodeUtil.getHBox(ColorType.DEF, ColorType.DEF, nodeRandom, nodeFullview);
        hBoxTools.setBorder(NodeUtil.getBorder(0, 1, 0, 1));
        NodeUtil.addToManager(hBoxTools, ColorType.DEF);

        nodeInfo = new TextNode("", ColorType.DEF, ColorType.DEF);

        ButtonFactory buttonFactory = ButtonFactory.getInstance();
        TextNode nodeShowSimilar = buttonFactory.get(ButtonTemplates.OBJ_SIMILAR);
        TextNode nodeOpen = buttonFactory.get(ButtonTemplates.OBJ_OPEN);
        TextNode nodeEdit = buttonFactory.get(ButtonTemplates.OBJ_EDIT);
        TextNode nodeCopyName = buttonFactory.get(ButtonTemplates.OBJ_COPY_NAME);
        TextNode nodeCopyPath = buttonFactory.get(ButtonTemplates.OBJ_COPY_PATH);
		TextNode nodeUnmergeGroup = buttonFactory.get(ButtonTemplates.GRP_UNMERGE);
        TextNode nodeDeleteTarget = buttonFactory.get(ButtonTemplates.OBJ_DELETE);
        nodeTarget = new TextNode("", colorData);
        ClickMenuLeft.install(nodeTarget, Direction.DOWN,
                nodeShowSimilar,
                nodeOpen,
                nodeEdit,
                nodeCopyName,
                nodeCopyPath,
				nodeUnmergeGroup,
                nodeDeleteTarget
        );

        HBox hBoxMain = NodeUtil.getHBox(ColorType.DEF, ColorType.DEF);
        hBoxMain.getChildren().add(nodeFile);
        hBoxMain.getChildren().add(hBoxTools);
        NodeUtil.addToManager(hBoxMain, ColorType.DEF);

        this.setBorder(NodeUtil.getBorder(0, 0, 1, 0));
        this.setPrefHeight(SizeUtil.getPrefHeightTopMenu());
        this.setLeft(hBoxMain);
        this.setCenter(nodeInfo);
        BorderPane.setAlignment(nodeInfo, Pos.CENTER);
        this.setRight(nodeTarget);
        NodeUtil.addToManager(this, ColorType.DEF);
    }

    public boolean reload() {
        nodeTarget.setText(InstanceManager.getTarget().getCurrentTarget().getName());
        return true;
    }

    public TextNode getNodeSave() {
        return nodeSave;
    }
    public TextNode getNodeImport() {
        return nodeImport;
    }
    public TextNode getNodeSettings() {
        return nodeSettings;
    }
    public TextNode getNodeExit() {
        return nodeExit;
    }

    public TextNode getNodeRandom() {
        return nodeRandom;
    }
    public TextNode getNodeFullview() {
        return nodeFullview;
    }
    public TextNode getNodeInfo() {
        return nodeInfo;
    }
}
