package userinterface.nodes.menu;

import userinterface.nodes.ColorData;
import userinterface.nodes.base.Separator;
import userinterface.nodes.base.TextNode;
import userinterface.nodes.buttons.ButtonFactory;
import userinterface.nodes.buttons.ButtonTemplates;
import userinterface.style.enums.ColorType;
import utils.enums.Direction;

public class ClickMenuData extends ClickMenuRight {
    public ClickMenuData() {
        ColorData colorData = new ColorData(ColorType.DEF, ColorType.ALT, ColorType.DEF, ColorType.DEF);

        ButtonFactory buttonFactory = ButtonFactory.getInstance();
        TextNode nodeShowSimilar = buttonFactory.get(ButtonTemplates.OBJ_SIMILAR);
        TextNode nodeOpen = buttonFactory.get(ButtonTemplates.OBJ_OPEN);
        TextNode nodeEdit = buttonFactory.get(ButtonTemplates.OBJ_EDIT);
        TextNode nodeCopyName = buttonFactory.get(ButtonTemplates.OBJ_COPY_NAME);
        TextNode nodeCopyPath = buttonFactory.get(ButtonTemplates.OBJ_COPY_PATH);
        TextNode nodeDeleteTarget = buttonFactory.get(ButtonTemplates.OBJ_DELETE);
		TextNode nodeMergeSelection = buttonFactory.get(ButtonTemplates.SEL_MERGE);
        TextNode nodeDeleteSelection = buttonFactory.get(ButtonTemplates.SEL_DELETE);

        TextNode nodeObject = new TextNode("Object >", colorData);
        ClickMenuLeft.install(this, nodeObject, Direction.RIGHT,
                nodeShowSimilar,
                new Separator(),
                nodeOpen,
                nodeEdit,
                new Separator(),
                nodeCopyName,
                nodeCopyPath,
                nodeDeleteTarget
        );

        TextNode nodeSelection = new TextNode("Selection >", colorData);
        ClickMenuLeft.install(this, nodeSelection, Direction.RIGHT,
				nodeMergeSelection,
                nodeDeleteSelection
        );

        this.getChildren().addAll(nodeObject, nodeSelection);

        instanceList.add(this);
    }
}