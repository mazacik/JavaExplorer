package settings;

public enum SettingsNamespace {
    MAINSCENE_WIDTH("mainSceneWidth"),
    MAINSCENE_HEIGHT("mainSceneHeight"),
    TILEVIEW_ICONSIZE("tileViewIconSize"),
    GLOBAL_SPACING("globalSpacing"),
    ;

    private String value;

    SettingsNamespace(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}