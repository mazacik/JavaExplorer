package user_interface.factory.buttons;

import com.sun.jna.platform.FileUtils;
import control.filter.FilterManager;
import control.reload.Reload;
import database.object.DataObject;
import database.object.TagObject;
import javafx.scene.input.MouseButton;
import lifecycle.InstanceManager;
import org.apache.commons.text.WordUtils;
import system.ClipboardUtil;
import user_interface.factory.base.TextNode;
import user_interface.factory.menu.ClickMenuBase;
import user_interface.factory.stage.GroupEditStage;
import user_interface.factory.stage.OkCancelStage;
import user_interface.factory.util.ColorData;
import user_interface.factory.util.enums.ColorType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ButtonFactory {
    private final ColorData colorData = new ColorData(ColorType.DEF, ColorType.ALT, ColorType.DEF, ColorType.DEF);

    private ButtonFactory() {
        if (FactoryLoader.instance != null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " already instantiated");
        }
    }
    public static ButtonFactory getInstance() {
        return FactoryLoader.instance;
    }
    public TextNode get(ButtonTemplates buttonTemplate) {
        switch (buttonTemplate) {
            case OBJ_SIMILAR:
                return objSimilar();
            case OBJ_OPEN:
                return objOpen();
            case OBJ_EDIT:
                return objEdit();
            case OBJ_COPY_NAME:
                return objCopyName();
            case OBJ_COPY_PATH:
                return objCopyPath();
            case OBJ_DELETE:
                return objDelete();
            case SEL_DELETE:
                return selDelete();
            case TAG_EDIT:
                return tagEdit();
            case TAG_REMOVE:
                return tagRemove();
        }

        return objSimilar();
    }
    private TextNode objSimilar() {
        TextNode textNode = new TextNode("Show Similar", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                FilterManager.showSimilar(InstanceManager.getTarget().getCurrentTarget());
                InstanceManager.getReload().doReload();
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode objOpen() {
        TextNode textNode = new TextNode("Open", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String fullPath = InstanceManager.getTarget().getCurrentTarget().getSourcePath();
                try {
                    Desktop.getDesktop().open(new File(fullPath));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClickMenuBase.hideAll();
                }
            }
        });
        return textNode;
    }
    private TextNode objEdit() {
        TextNode textNode = new TextNode("Edit", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String fullPath = InstanceManager.getTarget().getCurrentTarget().getSourcePath();
                try {
                    //todo cross platform support
                    Runtime.getRuntime().exec("C:\\WINDOWS\\system32\\mspaint.exe " + fullPath);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ClickMenuBase.hideAll();
                }
            }
        });
        return textNode;
    }
    private TextNode objCopyName() {
        TextNode textNode = new TextNode("Copy Name", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ClipboardUtil.setClipboardContent(InstanceManager.getTarget().getCurrentTarget().getName());
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode objCopyPath() {
        TextNode textNode = new TextNode("Copy Path", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ClipboardUtil.setClipboardContent(InstanceManager.getTarget().getCurrentTarget().getSourcePath());
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode objDelete() {
        TextNode textNode = new TextNode("Delete", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.deleteCurrentTarget();
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode selDelete() {
        TextNode textNode = new TextNode("Delete", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.deleteSelection();
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode tagEdit() {
        TextNode textNode = new TextNode("Edit", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!InstanceManager.getClickMenuInfo().getName().isEmpty()) {
                    InstanceManager.getMainInfoList().edit(InstanceManager.getMainInfoList().getTagObject(InstanceManager.getClickMenuInfo().getGroup(), InstanceManager.getClickMenuInfo().getName()));
                } else {
                    String oldGroup = InstanceManager.getClickMenuInfo().getGroup();
                    String newGroup = WordUtils.capitalize(new GroupEditStage(oldGroup).getResult().toLowerCase());
                    if (newGroup.isEmpty()) return;

                    InstanceManager.getMainInfoList().forEach(tagObject -> {
                        if (tagObject.getGroup().equals(oldGroup)) {
                            tagObject.setGroup(newGroup);
                        }
                    });

                    ArrayList<String> expandedGroupsL = InstanceManager.getTagListViewL().getExpandedGroupsList();
                    if (expandedGroupsL.contains(oldGroup)) {
                        expandedGroupsL.remove(oldGroup);
                        expandedGroupsL.add(newGroup);
                    }

                    ArrayList<String> expandedGroupsR = InstanceManager.getTagListViewR().getExpandedGroupsList();
                    if (expandedGroupsR.contains(oldGroup)) {
                        expandedGroupsR.remove(oldGroup);
                        expandedGroupsR.add(newGroup);
                    }

                    InstanceManager.getReload().notifyChangeIn(Reload.Control.INFO);
                }
                InstanceManager.getReload().doReload();
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private TextNode tagRemove() {
        TextNode textNode = new TextNode("Remove", colorData);
        textNode.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                TagObject tagObject = InstanceManager.getMainInfoList().getTagObject(InstanceManager.getClickMenuInfo().getGroup(), InstanceManager.getClickMenuInfo().getName());
                InstanceManager.getMainDataList().forEach(dataObject -> dataObject.getTagList().remove(tagObject));
                InstanceManager.getMainInfoList().remove(tagObject);
                InstanceManager.getReload().doReload();
                ClickMenuBase.hideAll();
            }
        });
        return textNode;
    }
    private void deleteDataObject(DataObject dataObject) {
        if (InstanceManager.getFilter().contains(dataObject)) {
            String sourcePath = InstanceManager.getTarget().getCurrentTarget().getSourcePath();
            String cachePath = InstanceManager.getTarget().getCurrentTarget().getCachePath();

            FileUtils fo = FileUtils.getInstance();
            if (fo.hasTrash()) {
                try {
                    fo.moveToTrash(new File[]{new File(sourcePath), new File(cachePath)});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            InstanceManager.getTileView().getTilePane().getChildren().remove(dataObject.getBaseTile());
            InstanceManager.getSelect().remove(dataObject);
            InstanceManager.getFilter().remove(dataObject);
            InstanceManager.getMainDataList().remove(dataObject);
        }
    }
    private void deleteSelection() {
        if (InstanceManager.getSelect().isEmpty()) {
            InstanceManager.getLogger().debug(this, "deleteSelection() - empty selection");
            return;
        }

        ArrayList<DataObject> dataObjectsToDelete = new ArrayList<>();
        InstanceManager.getSelect().forEach(dataObject -> {
            if (dataObject.getMergeID() != 0 && !InstanceManager.getTileView().getExpandedGroups().contains(dataObject.getMergeID())) {
                dataObjectsToDelete.addAll(dataObject.getMergeGroup());
            } else {
                dataObjectsToDelete.add(dataObject);
            }
        });

        OkCancelStage okCancelStage = new OkCancelStage("Delete " + dataObjectsToDelete.size() + " file(s)?");
        if (okCancelStage.getResult()) {
            InstanceManager.getTarget().storePosition();
            dataObjectsToDelete.forEach(this::deleteDataObject);
            InstanceManager.getTarget().restorePosition();

            InstanceManager.getReload().notifyChangeIn(Reload.Control.FILTER, Reload.Control.TARGET);
            InstanceManager.getReload().doReload();
        }
    }
    private void deleteCurrentTarget() {
        DataObject currentTarget = InstanceManager.getTarget().getCurrentTarget();
        if (currentTarget.getMergeID() != 0) {
            if (!InstanceManager.getTileView().getExpandedGroups().contains(currentTarget.getMergeID())) {
                OkCancelStage okCancelStage = new OkCancelStage("Delete " + currentTarget.getMergeGroup().size() + " file(s)?");
                if (okCancelStage.getResult()) {
                    currentTarget.getMergeGroup().forEach(this::deleteDataObject);
                    InstanceManager.getReload().doReload();
                }
            }
        } else {
            String sourcePath = InstanceManager.getTarget().getCurrentTarget().getSourcePath();
            OkCancelStage okCancelStage = new OkCancelStage("Delete file: " + sourcePath + "?");
            if (okCancelStage.getResult()) {
                InstanceManager.getTarget().storePosition();
                this.deleteDataObject(currentTarget);
                InstanceManager.getTarget().restorePosition();

                InstanceManager.getReload().notifyChangeIn(Reload.Control.FILTER, Reload.Control.TARGET);
                InstanceManager.getReload().doReload();
            }
        }
    }
    private static class FactoryLoader {
        private static final ButtonFactory instance = new ButtonFactory();
    }
}
