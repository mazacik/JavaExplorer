package user_interface.utils;

import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import user_interface.factory.ColorData;
import user_interface.factory.base.CheckBoxNode;
import user_interface.factory.base.TextNode;
import user_interface.factory.node.IntroWindowCell;
import user_interface.singleton.side.GroupNode;
import user_interface.utils.enums.ColorType;

import java.util.ArrayList;

public abstract class StyleUtil {
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

    public static void applyStyle(ArrayList<ColorData> colorDataList) {
        for (ColorData colorData : colorDataList) {
            colorData.getRegion().setBackground(ColorUtil.getBackgroundDef(colorData));
            if (colorData.getRegion() instanceof TextNode) {
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
            } else if (colorData.getRegion() instanceof GroupNode) {
                if (colorData.getBackgroundAlt() != ColorType.NULL) {
                    colorData.getRegion().setOnMouseExited(event -> {
                        colorData.getRegion().setBackground(ColorUtil.getBackgroundDef(colorData));
                        //colorData.getRegion().setCursor(Cursor.DEFAULT);
                    });
                    colorData.getRegion().setOnMouseEntered(event -> {
                        colorData.getRegion().setBackground(ColorUtil.getBackgroundAlt(colorData));
                        //colorData.getRegion().setCursor(Cursor.HAND);
                    });
                }
            } else if (colorData.getRegion() instanceof IntroWindowCell) {
                IntroWindowCell introWindowCell = ((IntroWindowCell) colorData.getRegion());
                introWindowCell.setBackground(ColorUtil.getBackgroundDef(colorData));
                introWindowCell.setOnMouseEntered(event -> {
                    introWindowCell.setBackground(ColorUtil.getBackgroundAlt(colorData));
                    introWindowCell.setCursor(Cursor.HAND);
                    introWindowCell.getNodeRemove().setVisible(true);
                });
                introWindowCell.setOnMouseExited(event -> {
                    introWindowCell.setBackground(ColorUtil.getBackgroundDef(colorData));
                    introWindowCell.setCursor(Cursor.DEFAULT);
                    introWindowCell.getNodeRemove().setVisible(false);
                });
            } else if (colorData.getRegion() instanceof TextField) {
                TextField textField = ((TextField) colorData.getRegion());
                Color color = ColorUtil.getTextColorDef(colorData);
                String colorString = String.format("#%02X%02X%02X",
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
                textField.setStyle("-fx-text-fill: " + colorString + ";");
            } else if (colorData.getRegion() instanceof CheckBoxNode) {
                CheckBoxNode checkBoxNode = ((CheckBoxNode) colorData.getRegion());
                if (colorData.getTextFillDef() != ColorType.NULL) {
                    checkBoxNode.setTextFill(ColorUtil.getTextColorDef(colorData));
                }
            }
        }
    }
    public static void applyStyle(ObservableList<Node> nodes) {
        ArrayList<ColorData> needsUpdate = new ArrayList<>();
        for (ColorData colorData : NodeUtil.getNodeList()) {
            for (Node node : nodes) {
                if (colorData.getRegion() != null && colorData.getRegion() == node) {
                    needsUpdate.add(colorData);
                }
            }
        }
        applyStyle(needsUpdate);
    }
    public static void applyStyle(Scene scene) {
        ArrayList<ColorData> colorDataList = new ArrayList<>();
        for (ColorData colorData : NodeUtil.getNodeList()) {
            if (colorData.getRegion().getScene() != null && colorData.getRegion().getScene().equals(scene)) {
                colorDataList.add(colorData);
            }
        }
        applyStyle(colorDataList);
    }
    public static void applyStyle(Pane pane) {
        ArrayList<ColorData> colorDataList = new ArrayList<>();
        for (ColorData colorData : NodeUtil.getNodeList()) {
            Region region = colorData.getRegion();
            Parent parent = region.getParent();
            if ((parent != null && parent.equals(pane)) || region == pane) {
                colorDataList.add(colorData);
            }
        }
        applyStyle(colorDataList);
    }
    public static void applyStyle() {
        applyStyle(NodeUtil.getNodeList());
    }
}
