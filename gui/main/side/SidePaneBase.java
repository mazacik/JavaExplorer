package gui.main.side;

import baseobject.CustomList;
import baseobject.tag.Tag;
import baseobject.tag.TagList;
import control.reload.Reloadable;
import gui.component.simple.TextNode;
import gui.decorator.SizeUtil;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import main.InstanceCollector;

import java.lang.reflect.Method;
import java.util.Comparator;

public abstract class SidePaneBase extends VBox implements Reloadable, SidePaneBaseInterface, InstanceCollector {
	protected TextNode nodeTitle;
	protected VBox groupNodes;
	protected ScrollPane scrollPane;
	
	public SidePaneBase() {
		methodsToInvokeOnNextReload = new CustomList<>();
		
		groupNodes = new VBox();
		
		scrollPane = new ScrollPane();
		scrollPane.setContent(groupNodes);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setBackground(Background.EMPTY);
		
		this.setPrefWidth(SizeUtil.getUsableScreenWidth());
		this.setMinWidth(SizeUtil.getMinWidthSideLists());
	}
	
	private CustomList<Method> methodsToInvokeOnNextReload;
	@Override
	public CustomList<Method> getMethodsToInvokeOnNextReload() {
		return methodsToInvokeOnNextReload;
	}
	public boolean reload() {
		//	populate primary helpers
		CustomList<String> groupsCurrent = new CustomList<>();
		CustomList<Tag> tagsMain = new CustomList<>(tagListMain);
		CustomList<Tag> tagsCurrent = new CustomList<>();
		
		for (Node node : groupNodes.getChildren()) {
			if (node instanceof GroupNode) {
				GroupNode groupNode = (GroupNode) node;
				groupsCurrent.add(groupNode.getGroup());
				for (TextNode nameNode : groupNode.getNameNodes()) {
					tagsCurrent.add(tagListMain.getTag(groupNode.getGroup(), nameNode.getText()));
				}
			}
		}
		
		//	populate secondary helpers
		CustomList<Tag> tagsToAdd = new CustomList<>();
		for (Tag tagMain : tagsMain) {
			if (!tagsCurrent.contains(tagMain)) {
				tagsToAdd.add(tagMain);
			}
		}
		TagList tagsToRemove = new TagList();
		for (Tag tagCurrent : tagsCurrent) {
			if (!tagsMain.contains(tagCurrent)) {
				tagsToRemove.add(tagCurrent);
			}
		}
		
		//	check if any changes are necessary (add)
		if (!tagsCurrent.containsAll(tagsMain)) {
			for (Tag tag : tagsToAdd) {
				//	check if the GroupNode exists, if not, add it
				String group = tag.getGroup();
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
					groupNode.addNameNode(tag.getName());
					groupNode.sortNameNodes();
				}
			}
		}
		
		//	check if any changes are necessary (remove)
		if (!tagsMain.containsAll(tagsCurrent)) {
			for (Tag tag : tagsToRemove) {
				//	use helper to find the GroupNode
				//	remove NameNode from the GroupNode
				Node node = groupNodes.getChildren().get(groupsCurrent.indexOf(tag.getGroup()));
				if (node instanceof GroupNode) {
					GroupNode groupNode = (GroupNode) node;
					groupNode.removeNameNode(tag.getName());
					//	if GroupNode is empty, remove it
					if (groupNode.getNameNodes().isEmpty()) groupNodes.getChildren().remove(groupNode);
				}
			}
		}
		
		return true;
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
	public CustomList<GroupNode> getGroupNodes() {
		CustomList<GroupNode> customList = new CustomList<>();
		groupNodes.getChildren().forEach(node -> {
			if (node instanceof GroupNode) {
				customList.add((GroupNode) node);
			}
		});
		return customList;
	}
}
