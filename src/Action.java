/**
 * Created by Yue on 12-Apr-17.
 */
public class Action {

    public static final int ACTION_LISTEN = 0;
    public static final int ACTION_OPEN_LEFT = 1;
    public static final int ACTION_OPEN_RIGHT = -1;

    public static String getName(int action) {
        if (action == ACTION_LISTEN)
            return "Listen";
        else if (action == ACTION_OPEN_LEFT)
            return "Open Left";
        else
            return "Open Right";
    }

}
