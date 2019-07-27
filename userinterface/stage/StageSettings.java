package userinterface.stage;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.InstanceManager;
import userinterface.nodes.ColorData;
import userinterface.nodes.NodeUtil;
import userinterface.nodes.base.TextNode;
import userinterface.style.SizeUtil;
import userinterface.style.StyleUtil;
import userinterface.style.enums.ColorType;

import java.util.ArrayList;

public class StageSettings extends Stage implements StageBase {
	StageSettings() {
        double spacing = SizeUtil.getGlobalSpacing();
        VBox vBox = NodeUtil.getVBox(ColorType.DEF, ColorType.DEF);
        vBox.setPadding(new Insets(spacing));
        vBox.setSpacing(spacing);
        ArrayList<TextNode> labels = new ArrayList<>();

        InstanceManager.getSettings().getSettingsList().forEach(setting -> {
            TextNode label = new TextNode(setting.getSettingsEnum().toString(), ColorType.DEF, ColorType.DEF);
            labels.add(label);
            TextField textField = new TextField(String.valueOf(setting.getValue()));
            textField.setFont(StyleUtil.getFont());
            textField.setBorder(NodeUtil.getBorder(1, 1, 1, 1));
			StyleUtil.addToManager(textField, ColorType.ALT, ColorType.ALT, ColorType.DEF, ColorType.DEF);
            HBox hBox = NodeUtil.getHBox(ColorType.DEF, ColorType.DEF, label, textField);
            vBox.getChildren().add(hBox);
        });

        ColorData colorData = new ColorData(ColorType.DEF, ColorType.ALT, ColorType.DEF, ColorType.DEF);
        TextNode lblCancel = new TextNode("Cancel", colorData);
        TextNode lblOK = new TextNode("OK", colorData);
        lblOK.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {

            }
        });
        lblCancel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.close();
            }
        });
        HBox hBox = NodeUtil.getHBox(ColorType.DEF, ColorType.DEF, lblCancel, lblOK);

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);

        this.setScene(scene);
        this.setOnShown(event -> {
			//NodeUtil.equalizeWidth(labels);
			NodeUtil.equalizeWidth(vBox);
            this.centerOnScreen();
        });
    }
	@Override
	public Object _show(String... args) {
		return null;
	}
}