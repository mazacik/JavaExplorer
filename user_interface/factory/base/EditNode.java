package user_interface.factory.base;

import javafx.scene.control.TextField;
import system.CommonUtil;
import user_interface.factory.NodeUtil;
import user_interface.factory.util.enums.ColorType;

public class EditNode extends TextField {
    public EditNode(String promptText, EditNodeType type) {
        this.setFont(CommonUtil.getFont());
        this.setBorder(NodeUtil.getBorder(1, 1, 1, 1));
        this.setPromptText(promptText);
        this.skinProperty().addListener((observable, oldValue, newValue) -> setStyle("-fx-prompt-text-fill: grey;"));

        switch (type) {
            case DEFAULT:
                //todo
                break;
            case NUMERIC:
                //todo
                break;
            case NUMERIC_POSITIVE:
                this.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("") && !isNumberPositive(newValue)) {
                        this.setText(oldValue);
                    }
                });
                break;
        }

        NodeUtil.addToManager(this, ColorType.ALT, ColorType.ALT, ColorType.DEF, ColorType.DEF);
    }
    public EditNode(String promptText) {
        this(promptText, EditNodeType.DEFAULT);
    }
    public EditNode() {
        this("", EditNodeType.DEFAULT);
    }

    public boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    public boolean isNumberPositive(String str) {
        if (!isNumber(str)) return false;
        return !(str.charAt(0) == '-');
    }
}
