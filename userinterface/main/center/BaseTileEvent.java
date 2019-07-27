package userinterface.main.center;

import control.Reload;
import database.object.DataObject;
import javafx.scene.input.MouseEvent;
import main.InstanceManager;
import settings.SettingsEnum;

public class BaseTileEvent {
	public BaseTileEvent(BaseTile baseTile) {
		onMousePress(baseTile);
		//onMouseHover(baseTile);
		//onMouseRelease(baseTile);
		
		onMouseClick(baseTile);
	}
	
	private void onMousePress(BaseTile baseTile) {
		baseTile.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			event.consume();
			switch (event.getButton()) {
				case PRIMARY:
					onLeftPress(baseTile, event);
					break;
				case SECONDARY:
					break;
				default:
					break;
			}
		});
	}
	private void onLeftPress(BaseTile baseTile, MouseEvent event) {
		DataObject dataObject = baseTile.getParentObject();
		
		if (isOnGroupButton(event)) {
			onGroupButtonPress(dataObject);
		} else {
			InstanceManager.getTarget().set(dataObject);
			
			if (event.isControlDown()) {
				InstanceManager.getSelect().swapState(dataObject);
			} else if (event.isShiftDown()) {
				InstanceManager.getSelect().shiftSelectTo(dataObject);
			} else {
				InstanceManager.getSelect().set(dataObject);
			}
		}
		
		InstanceManager.getReload().doReload();
		InstanceManager.getClickMenuData().hide();
	}
	
	private boolean isOnGroupButton(MouseEvent event) {
		int tileSize = InstanceManager.getSettings().intValueOf(SettingsEnum.THUMBSIZE);
		return event.getX() > tileSize - BaseTile.getEffectGroupSize() && event.getY() < BaseTile.getEffectGroupSize();
	}
	public static void onGroupButtonPress(DataObject dataObject) {
		if (dataObject.getMergeID() == 0) return;
		if (!InstanceManager.getGalleryPane().getExpandedGroups().contains(dataObject.getMergeID())) {
			InstanceManager.getGalleryPane().getExpandedGroups().add(dataObject.getMergeID());
		} else {
			//noinspection RedundantCollectionOperation
			InstanceManager.getGalleryPane().getExpandedGroups().remove(InstanceManager.getGalleryPane().getExpandedGroups().indexOf(dataObject.getMergeID()));
		}
		if (!dataObject.getMergeGroup().contains(InstanceManager.getTarget().getCurrentTarget()))
			InstanceManager.getTarget().set(dataObject.getMergeGroup().getFirst());
		InstanceManager.getReload().notify(Reload.Control.OBJ);
	}
	
	private void onMouseClick(BaseTile baseTile) {
		baseTile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			event.consume();
			switch (event.getButton()) {
				case PRIMARY:
					break;
				case SECONDARY:
					onRightClick(baseTile, event);
					break;
				default:
					break;
			}
		});
	}
	private void onRightClick(BaseTile sender, MouseEvent event) {
		DataObject dataObject = sender.getParentObject();
		
		if (!InstanceManager.getSelect().contains(dataObject)) {
			InstanceManager.getSelect().add(dataObject);
		}
		
		InstanceManager.getTarget().set(dataObject);
		InstanceManager.getReload().doReload();
		InstanceManager.getClickMenuData().show(sender, event);
	}
}