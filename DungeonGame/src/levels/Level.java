package levels;

import rooms.Room;

public interface Level {
    Room[][] getMap();
    int getInitialRow();
    int getInitialCol();
    String getLevelName();
}
