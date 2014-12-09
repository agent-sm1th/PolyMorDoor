package za.co.polymorph.polymordoor.util.door;

/**
 * Created by Legion on 14/11/28.
 */
public class DoorUtil {


    public static String getDoorId(DoorType door) {
        String name = "";

        switch(door) {
            case DOOR_TOP:
                name = "1";
                break;
            case DOOR_BOTTOM:
                name = "2";
                break;
        }

        return name;
    }


    public static String getDoorName(DoorType door) {
        String name = "";

        switch(door) {
            case DOOR_TOP:
                name = "Mount Doom";
                break;
            case DOOR_BOTTOM:
                name = "MorDoor";
                break;
        }

        return name;
    }
}
