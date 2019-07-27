package userinterface.scene;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import main.InstanceManager;
import userinterface.nodes.NodeUtil;
import userinterface.nodes.base.TextNode;
import userinterface.style.SizeUtil;
import userinterface.style.StyleUtil;
import userinterface.style.enums.ColorType;

public class LoadingScene {
    private final Scene loadingScene;
    private final TextNode progressTextNode = new TextNode("", ColorType.DEF, ColorType.DEF);

    LoadingScene() {
        loadingScene = create();
    }

    private Scene create() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        borderPane.setCenter(progressTextNode);
        borderPane.setPrefWidth(300);
        borderPane.setBorder(NodeUtil.getBorder(1, 1, 1, 1));
		StyleUtil.addToManager(borderPane, ColorType.DEF);

        return new Scene(borderPane);
    }
    void show() {
        InstanceManager.getMainStage().setScene(loadingScene);
        InstanceManager.getMainStage().setWidth(SizeUtil.getUsableScreenWidth() / 6);
        InstanceManager.getMainStage().setHeight(SizeUtil.getUsableScreenHeight() / 16);
        InstanceManager.getMainStage().centerOnScreen();

        InstanceManager.getLogger().debug("waiting for directory");
    }

    public Scene getInstance() {
        return loadingScene;
    }
    public TextNode getProgressTextNode() {
        return progressTextNode;
    }
}