package ui;

import control.Select;
import javafx.scene.image.Image;
import misc.FileUtil;
import ui.stage.SimpleMessageStage;

public abstract class EntityDetailsUtil {
	public static void show() {
		Image entityImage = new Image("file:" + FileUtil.getFileEntity(Select.getTarget()));
		int width = (int) entityImage.getWidth();
		int height = (int) entityImage.getHeight();
		
		SimpleMessageStage.show("Details", width + "x" + height);
	}
}