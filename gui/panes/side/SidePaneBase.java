package application.gui.panes.side;

import application.database.list.CustomList;
import application.database.object.TagObject;
import application.gui.decorator.SizeUtil;
import application.gui.nodes.simple.TextNode;
import application.gui.panes.NodeBase;
import application.main.Instances;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class SidePaneBase extends VBox implements NodeBase, SidePaneBaseInterface {
	protected TextNode nodeTitle;
	protected VBox groupNodes;
	protected ScrollPane scrollPane;
	
	protected boolean needsReload;
	
	public SidePaneBase() {
		groupNodes = new VBox();
		
		scrollPane = new ScrollPane();
		scrollPane.setContent(groupNodes);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setBackground(Background.EMPTY);
		
		needsReload = false;
		
		this.setPrefWidth(SizeUtil.getUsableScreenWidth());
		this.setMinWidth(SizeUtil.getMinWidthSideLists());
	}
	
	public void updateNodes() {
		//	populate primary helpers
		CustomList<String> groupsCurrent = new CustomList<>();
		CustomList<TagObject> tagsMain = new CustomList<>(Instances.getTagListMain());
		CustomList<TagObject> tagsCurrent = new CustomList<>();
		
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				groupsCurrent.add(groupNode.getGroup());
				for (TextNode nameNode : groupNode.getNameNodes()) {
					tagsCurrent.add(Instances.getTagListMain().getTagObject(groupNode.getGroup(), nameNode.getText()));
				}
			}
		}
		
		//	populate secondary helpers
		CustomList<TagObject> tagsToAdd = new CustomList<>();
		for (TagObject tagMain : tagsMain) {
			if (!tagsCurrent.contains(tagMain)) {
				tagsToAdd.add(tagMain);
			}
		}
		ArrayList<TagObject> tagsToRemove = new ArrayList<>();
		for (TagObject tagCurrent : tagsCurrent) {
			if (!tagsMain.contains(tagCurrent)) {
				tagsToRemove.add(tagCurrent);
			}
		}
		
		//	check if any changes are necessary (add)
		if (!tagsCurrent.containsAll(tagsMain)) {
			for (TagObject tagObject : tagsToAdd) {
				//	check if the GroupNode exists, if not, add it
				String group = tagObject.getGroup();
				int index;
				if (!groupsCurrent.contains(group)) {
					groupsCurrent.add(group);
					groupsCurrent.sort(Comparator.naturalOrder());
					index = groupsCurrent.indexOf(group);
					GroupNode groupNode = new GroupNode(this, group);
					groupNodes.getChildren().add(index, groupNode);
				} else {
					index = groupsCurrent.indexOf(group);
				}
				//	add NameNode to the respective GroupNode and sort
				Node node = groupNodes.getChildren().get(index);
				if (node instanceof GroupNode) {
					GroupNode groupNode = (GroupNode) node;
					groupNode.addNameNode(tagObject.getName());
					groupNode.sortNameNodes();
				}
			}
		}
		
		//	check if any changes are necessary (remove)
		if (!tagsMain.containsAll(tagsCurrent)) {
			for (TagObject tagObject : tagsToRemove) {
				//	use helper to find the GroupNode
				//	remove NameNode from the GroupNode
				Node node = groupNodes.getChildren().get(groupsCurrent.indexOf(tagObject.getGroup()));
				if (node instanceof GroupNode) {
					GroupNode groupNode = (GroupNode) node;
					groupNode.removeNameNode(tagObject.getName());
					//	if GroupNode is empty, remove it
					if (groupNode.getNameNodes().isEmpty()) groupNodes.getChildren().remove(groupNode);
				}
			}
		}
	}
	
	public void updateGroupNode(String oldGroup, String newGroup) {
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				if (groupNode.getGroup().equals(oldGroup)) {
					groupNode.setGroup(newGroup);
					break;
				}
			}
		}
	}
	
	public void updateNameNode(String group, String oldName, String newName) {
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				if (groupNode.getGroup().equals(group)) {
					groupNode.updateNameNode(oldName, newName);
					break;
				}
			}
		}
	}
	public void removeNameNode(String group, String name) {
		GroupNode groupNode = null;
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				groupNode = (GroupNode) node;
				if (groupNode.getGroup().equals(group)) {
					groupNode.removeNameNode(name);
					break;
				}
			}
		}
		if (groupNode != null && groupNode.getNameNodes().isEmpty()) groupNodes.getChildren().remove(groupNode);
	}
	
	public void expandAll() {
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				groupNode.showNameNodes();
			}
		}
	}
	public void collapseAll() {
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				groupNode.hideNameNodes();
			}
		}
	}
	
	public ScrollPane getScrollPane() {
		return scrollPane;
	}
	public VBox getGroupNodes() {
		return groupNodes;
	}
	
	public boolean getNeedsReload() {
		return needsReload;
	}
	public void setNeedsReload(boolean needsReload) {
		this.needsReload = needsReload;
	}
}
