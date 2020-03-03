package ui.main.side;

import base.CustomList;
import base.entity.Entity;
import base.tag.TagList;
import control.Select;
import control.filter.Filter;
import control.reload.Reload;
import enums.Direction;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import ui.custom.ClickMenu;
import ui.decorator.Decorator;
import ui.node.EditNode;
import ui.node.textnode.TextNode;
import ui.node.textnode.TextNodeTemplates;

public class SelectPane extends SidePaneBase {
	private final TextNode nodeText;
	private final EditNode nodeSearch;
	
	private TagNode match;
	private String query;
	private boolean searchLock = false;
	
	public boolean refresh() {
		refreshTitle();
		
		CustomList<String> stringListIntersect = new CustomList<>();
		Select.getEntities().getTagListIntersect().forEach(tag -> stringListIntersect.add(tag.getStringValue()));
		CustomList<String> stringListUnion = new CustomList<>();
		Select.getEntities().getTagList().forEach(tag -> stringListUnion.add(tag.getStringValue()));
		
		getTagNodes().forEach(tagNode -> this.refreshNodeColor(tagNode, stringListIntersect, stringListUnion));
		
		return true;
	}
	private void refreshNodeColor(TagNode tagNode, CustomList<String> stringListIntersect, CustomList<String> stringListUnion) {
		String stringNode = tagNode.getStringValue();
		for (String stringTag : stringListIntersect) {
			if (stringTag.startsWith(stringNode)) {
				tagNode.setTextFill(Decorator.getColorPositive());
				return;
			}
		}
		for (String stringTag : stringListUnion) {
			if (stringTag.startsWith(stringNode)) {
				tagNode.setTextFill(Decorator.getColorUnion());
				return;
			}
		}
		tagNode.setTextFill(Decorator.getColorPrimary());
	}
	private void refreshTitle() {
		int hiddenTilesCount = 0;
		for (Entity entity : Select.getEntities()) {
			if (!Filter.getEntities().contains(entity)) {
				hiddenTilesCount++;
			}
		}
		
		String text = "Selection: " + Select.getEntities().size();
		if (hiddenTilesCount > 0) text += " (" + hiddenTilesCount + " hidden)";
		
		nodeText.setText(text);
	}
	
	public void nextMatch(Direction searchDirection, boolean isControlDown) {
		if (match != null) {
			CustomList<TagNode> tagNodes = getTagNodes();
			int i = tagNodes.indexOf(match);
			TagNode tagNode;
			
			while (true) {
				switch (searchDirection) {
					case UP:
						i--;
						break;
					case DOWN:
						i++;
						break;
					default:
						throw new IllegalArgumentException("Invalid search direction: " + searchDirection.name());
				}
				
				if (i == 0) {
					i = tagNodes.size() - 1;
				} else if (i == tagNodes.size()) {
					i = 0;
				}
				
				tagNode = tagNodes.get(i);
				
				if (isControlDown) {
					if (tagNode == match || (tagNode.isLast() && tagNode.getText().toLowerCase().contains(query))) {
						break;
					}
				} else {
					if (tagNode == match || tagNode.isLast()) {
						break;
					}
				}
			}
			
			closeNodes();
			match.setBackground(Background.EMPTY);
			match.setBackgroundLock(false);
			
			match = tagNode;
			match.setBackground(Decorator.getBackgroundSecondary());
			match.setBackgroundLock(true);
			match.getParentNodes().forEach(TagNode::open);
		}
	}
	public void closeNodes() {
		new CustomList<>(openNodes).forEach(TagNode::close);
	}
	
	public EditNode getNodeSearch() {
		return nodeSearch;
	}
	
	private SelectPane() {
		nodeText = new TextNode("", true, true, false, true);
		nodeText.setBorder(Decorator.getBorder(0, 0, 1, 0));
		nodeText.prefWidthProperty().bind(this.widthProperty());
		
		nodeSearch = new EditNode("", "Quick Search");
		nodeSearch.setBorder(Decorator.getBorder(0, 0, 1, 0));
		nodeSearch.prefWidthProperty().bind(this.widthProperty());
		nodeSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!searchLock) {
				if (match != null) {
					match.setBackground(Background.EMPTY);
					match.setBackgroundLock(false);
					match = null;
				}
				
				closeNodes();
				
				if (newValue.length() > 0) {
					query = newValue.toLowerCase();
					
					for (TagNode tagNode : this.getTagNodes()) {
						if (tagNode.getText().toLowerCase().contains(query)) {
							if (tagNode.isLast()) {
								match = tagNode;
								break;
							} else if (match == null) {
								match = tagNode;
							}
						}
					}
					
					if (match != null) {
						match.setBackground(Decorator.getBackgroundSecondary());
						match.setBackgroundLock(true);
						match.getParentNodes().forEach(TagNode::open);
					}
				} else {
					match = null;
				}
			}
		});
		nodeSearch.setOnAction(event -> {
			if (match != null) {
				match.setBackground(Background.EMPTY);
				match.setBackgroundLock(false);
				
				searchLock = true;
				nodeSearch.clear();
				searchLock = false;
				
				Select.getEntities().addTag(TagList.getMain().getTag(match.getStringValue()).getID());
				
				Reload.start();
			}
		});
		
		ClickMenu.install(nodeText, Direction.LEFT, MouseButton.PRIMARY, 0, -1
				, TextNodeTemplates.SELECTION_SET_ALL.get()
				, TextNodeTemplates.SELECTION_SET_NONE.get()
		);
		
		this.setBorder(Decorator.getBorder(0, 0, 0, 1));
		this.getChildren().addAll(nodeText, nodeSearch, scrollPane);
	}
	private static class Loader {
		private static final SelectPane INSTANCE = new SelectPane();
	}
	public static SelectPane getInstance() {
		return Loader.INSTANCE;
	}
}
