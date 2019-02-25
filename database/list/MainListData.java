package database.list;

import database.object.DataObject;
import system.InstanceRepo;
import system.SerializationUtil;

import java.lang.reflect.Type;
import java.util.Comparator;

public class MainListData extends BaseList<DataObject> implements InstanceRepo {
    private static final String dataFile = "data\\data.json";

    public void writeToDisk() {
        Type typeToken = SerializationUtil.TypeTokenEnum.DATALIST.getValue();
        String path = coreSettings.getCurrentDirectory() + dataFile;
        SerializationUtil.writeJSON(mainListData, typeToken, path);
    }
    public MainListData readFromDisk() {
        Type typeToken = SerializationUtil.TypeTokenEnum.DATALIST.getValue();
        String path = coreSettings.getCurrentDirectory() + dataFile;
        return (MainListData) SerializationUtil.readJSON(typeToken, path);
    }

    public void sort() {
        super.sort(Comparator.comparing(DataObject::getName));
    }
}
