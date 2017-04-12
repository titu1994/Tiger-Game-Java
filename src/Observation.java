/**
 * Created by Yue on 12-Apr-17.
 */
public class Observation {

    public static final int NO_OBSERVATION = 0;
    public static final int GROWL_LEFT = 1;
    public static final int GROWL_RIGHT = -1;

    public static String getName(int observation) {
        if (observation == NO_OBSERVATION)
            return "No Observation";
        else if (observation == GROWL_LEFT)
            return "Growl Left";
        else
            return "Growl Right";
    }

}
