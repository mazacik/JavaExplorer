package application.frontend.component.simple;

import application.frontend.component.NodeUtil;
import javafx.scene.layout.Region;

public class SeparatorNode extends Region {
	public SeparatorNode() {
		super();
		this.setBorder(NodeUtil.getBorder(1, 1, 0, 0));
	}
}