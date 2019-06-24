package user_interface.style;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lifecycle.InstanceManager;
import settings.SettingsEnum;
import user_interface.nodes.ColorData;
import user_interface.nodes.NodeUtil;
import user_interface.nodes.base.CheckBoxNode;
import user_interface.nodes.base.EditNode;
import user_interface.nodes.base.TextNode;
import user_interface.nodes.node.IntroStageNode;
import user_interface.style.enums.ColorType;

import java.util.ArrayList;

public abstract class StyleUtil {
    private static Font font = new Font(InstanceManager.getSettings().intValueOf(SettingsEnum.FONTSIZE));

    public static void applyScrollbarStyle(Node node) {
        try {
            node.applyCss();
            node.lookup(".track").setStyle("-fx-background-color: transparent;" +
                    " -fx-border-color: transparent;" +
                    " -fx-background-radius: 0.0em;" +
                    " -fx-border-radius: 0.0em;" +
                    " -fx-padding: 0.0 0.0 0.0 0.0;");
            node.lookup(".scroll-bar").setStyle("-fx-background-color: transparent;" +
                    " -fx-pref-width: 15;" +
                    " -fx-padding: 3 2 3 3;");
            node.lookup(".increment-button").setStyle("-fx-background-color: transparent;" +
                    " -fx-background-radius: 0.0em;" +
                    " -fx-padding: 0.0 0.0 0.0 0.0;");
            node.lookup(".decrement-button").setStyle("-fx-background-color: transparent;" +
                    " -fx-background-radius: 0.0em;" +
                    " -fx-padding: 0.0 0.0 0.0 0.0;");
            node.lookup(".increment-arrow").setStyle("-fx-padding: 0.0em 0.0;");
            node.lookup(".decrement-arrow").setStyle("-fx-padding: 0.0em 0.0;");
            node.lookup(".thumb").setStyle("-fx-background-color: derive(black, 90.0%);" +
                    " -fx-background-insets: 0.0, 0.0, 0.0;" +
                    " -fx-background-radius: 0.0em;");
            node.lookup(".viewport").setStyle("-fx-background-color: transparent;");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void applyStyle(Region region) {
        ArrayList<ColorData> colorDataList = new ArrayList<>();
        for (ColorData colorData : NodeUtil.getNodeList()) {
            if (colorData.getRegion() == region) {
                colorDataList.add(colorData);
            }
        }
        applyStyle(colorDataList);
    }
    public static void applyStyle(ArrayList<ColorData> colorDataList) {
        for (ColorData colorData : colorDataList) {
            colorData.getRegion().setBackground(ColorUtil.getBackgroundDef(colorData));
            if (colorData.getRegion() instanceof HBox || colorData.getRegion() instanceof VBox) {
                colorData.getRegion().setOnMouseEntered(event -> colorData.getRegion().setBackground(ColorUtil.getBackgroundAlt(colorData)));
                colorData.getRegion().setOnMouseExited(event -> colorData.getRegion().setBackground(ColorUtil.getBackgroundDef(colorData)));
            } else if (colorData.getRegion() instanceof TextNode) {
                TextNode label = ((TextNode) colorData.getRegion());
                if (colorData.getTextFillDef() != ColorType.NULL) {
                    label.setTextFill(ColorUtil.getTextColorDef(colorData));
                }
                if (colorData.getBackgroundAlt() != ColorType.NULL && colorData.getTextFillAlt() != ColorType.NULL) {
                    label.setOnMouseExited(event -> {
                        label.setBackground(ColorUtil.getBackgroundDef(colorData));
                        label.setTextFill(ColorUtil.getTextColorDef(colorData));
                        //label.setCursor(Cursor.DEFAULT);
                    });
                    label.setOnMouseEntered(event -> {
                        label.setBackground(ColorUtil.getBackgroundAlt(colorData));
                        label.setTextFill(ColorUtil.getTextColorAlt(colorData));
                        //label.setCursor(Cursor.HAND);
                    });
                } else if (colorData.getBackgroundAlt() != ColorType.NULL) {
                    label.setOnMouseExited(event -> {
                        label.setBackground(ColorUtil.getBackgroundDef(colorData));
                        //label.setCursor(Cursor.DEFAULT);
                    });
                    label.setOnMouseEntered(event -> {
                        label.setBackground(ColorUtil.getBackgroundAlt(colorData));
                        //label.setCursor(Cursor.HAND);
                    });
                } else if (colorData.getTextFillAlt() != ColorType.NULL) {
                    label.setOnMouseExited(event -> {
                        label.setTextFill(ColorUtil.getTextColorDef(colorData));
                        //label.setCursor(Cursor.DEFAULT);
                    });
                    label.setOnMouseEntered(event -> {
                        label.setTextFill(ColorUtil.getTextColorAlt(colorData));
                        //label.setCursor(Cursor.HAND);
                    });
                }
			} else if (colorData.getRegion() instanceof IntroStageNode) {
				IntroStageNode introStageNode = ((IntroStageNode) colorData.getRegion());
				introStageNode.setBackground(ColorUtil.getBackgroundDef(colorData));
				introStageNode.setOnMouseEntered(event -> {
					introStageNode.setBackground(ColorUtil.getBackgroundAlt(colorData));
					introStageNode.setCursor(Cursor.HAND);
					introStageNode.getNodeRemove().setVisible(true);
                });
				introStageNode.setOnMouseExited(event -> {
					introStageNode.setBackground(ColorUtil.getBackgroundDef(colorData));
					introStageNode.setCursor(Cursor.DEFAULT);
					introStageNode.getNodeRemove().setVisible(false);
                });
			} else if (colorData.getRegion() instanceof EditNode) {
				EditNode editNode = ((EditNode) colorData.getRegion());
                Color color = ColorUtil.getTextColorDef(colorData);
                String colorString = String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
				editNode.setStyle("-fx-text-fill: " + colorString + ";");
            } else if (colorData.getRegion() instanceof CheckBoxNode) {
                CheckBoxNode checkBoxNode = ((CheckBoxNode) colorData.getRegion());
                if (colorData.getTextFillDef() != ColorType.NULL) {
                    checkBoxNode.setTextFill(ColorUtil.getTextColorDef(colorData));
                }
            }
        }
    }
    public static void applyStyle() {
        applyStyle(NodeUtil.getNodeList());
    }

    public static Font getFont() {
        return font;
    }
}