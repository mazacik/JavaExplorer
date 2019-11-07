package application.frontend.pane.top;

import application.backend.base.CustomList;
import application.backend.base.entity.Entity;
import application.backend.control.reload.Reloadable;
import application.backend.util.EntityGroupUtil;
import application.backend.util.enums.Direction;
import application.frontend.component.ClickMenu;
import application.frontend.component.buttons.ButtonTemplates;
import application.frontend.component.custom.TitleBar;
import application.frontend.component.simple.SeparatorNode;
import application.frontend.component.simple.TextNode;
import application.frontend.decorator.SizeUtil;
import application.main.InstanceCollector;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ToolbarPane extends TitleBar implements Reloadable, InstanceCollector {
	private TextNode nodeTarget;
	
	public ToolbarPane() {
	
	}
	
	public void init() {
		super.init("");
		
		methodsToInvokeOnNextReload = new CustomList<>();
		
		TextNode nodeFile = new TextNode("File", true, true, false, true);
		TextNode nodeSave = ButtonTemplates.APPLICATION_SAVE.get();
		TextNode nodeImport = ButtonTemplates.APPLICATION_IMPORT.get();
		TextNode nodeExit = ButtonTemplates.APPLICATION_EXIT.get();
		ClickMenu.install(nodeFile, Direction.DOWN, nodeSave, nodeImport, new SeparatorNode(), nodeExit);
		
		TextNode nodeRandom = ButtonTemplates.FILTER_RANDOM.get();
		HBox hBoxTools = new HBox(nodeRandom);
		hBoxTools.setAlignment(Pos.CENTER);
		
		nodeTarget = new TextNode("", true, true, false, true);
		ClickMenu.install(nodeTarget, Direction.DOWN, MouseButton.PRIMARY, ClickMenu.StaticInstance.ENTITY);
		
		HBox hBox = new HBox(nodeFile, hBoxTools);
		hBox.setAlignment(Pos.CENTER);
		
		this.setPrefHeight(SizeUtil.getPrefHeightTopMenu());
		this.setLeft(hBox);
		this.setCenter(nodeTarget);
	}
	
	private CustomList<Method> methodsToInvokeOnNextReload;
	@Override
	public CustomList<Method> getMethodsToInvokeOnNextReload() {
		return methodsToInvokeOnNextReload;
	}
	public boolean reload() {
		Logger.getGlobal().info(this.toString());
		
		Entity currentTarget = target.get();
		if (currentTarget.getEntityGroupID() != 0) {
			CustomList<Entity> entityGroup = EntityGroupUtil.getEntityGroup(currentTarget);
			String entityGroupIndex = (entityGroup.indexOf(currentTarget) + 1) + "/" + entityGroup.size();
			nodeTarget.setText("[" + entityGroupIndex + "] " + currentTarget.getName());
		} else {
			nodeTarget.setText(currentTarget.getName());
		}
		
		return true;
	}
}
