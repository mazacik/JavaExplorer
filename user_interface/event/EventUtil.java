package user_interface.event;

public class EventUtil {
    public static void init() {
        new MainStageEvent();
        new TopMenuEvent();
        new TileViewEvent();
        new FullViewEvent();
    }
}