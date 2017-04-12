/**
 * Created by Yue on 12-Apr-17.
 */
public class AgentFunction {

    private static final String AGENT_NAME = "Agent Smith";

    private int leftCount;
    private int rightCount;

    public AgentFunction() {
        // Consturctor to perform initialization tasks
        leftCount = 0;
        rightCount = 0;
    }

    public synchronized int act(double score, int observation) {
        // Example of Leslie Kaelbling's optimal agent model,
        // minus the bottom part of the Finite State Machine

        if (observation == Observation.NO_OBSERVATION) {
            // reset belief state
            leftCount = 0;
            rightCount = 0;

            // begin by listening
            return Action.ACTION_LISTEN;
        }
        else {
            // update internal count
            if (observation == Observation.GROWL_LEFT)
                leftCount++;
            else
                rightCount++;

            // perform action
            if (leftCount >= 2) {
                // reset listen count since we made an informed decision
                leftCount = 0;

                return Action.ACTION_OPEN_RIGHT;
            }
            else if (rightCount >= 2) {
                // reset listen count since we made an informed decision
                rightCount = 0;

                return Action.ACTION_OPEN_LEFT;
            }
            else if (leftCount >= 1 && rightCount >= 1) {
                // we obtained dis-confirming growls. Reset our belief state
                leftCount = 0;
                rightCount = 0;

                return Action.ACTION_LISTEN;
            }
            else {
                // not enough information to make informed decision, simply listen

                return Action.ACTION_LISTEN;
            }
        }
    }


    public static String getAgentName() {
        return AGENT_NAME;
    }
}
