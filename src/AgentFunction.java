import sun.management.Agent;

import java.io.*;

public class AgentFunction implements Serializable {

    public static final long serialVersionUID = 151129951;

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

    protected synchronized void serialize() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("agent.dat"))){
            oos.writeObject(this);
            System.out.println("Saved the agent in 'agent.dat'.");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to serialize to file. (FileNotFoundException)");
        } catch (IOException e) {
            System.out.println("Unable to serialize to file. (IOException");
        }
    }

    protected static synchronized AgentFunction deserialize() {
        AgentFunction agent = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("agent.dat"))) {
            agent = (AgentFunction) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found, therefore cannot deserialize. Creating new agent.");
            agent = new AgentFunction();
        } catch (IOException e) {
            System.out.println("IO Exception occured. Creating new agent.");
            agent = new AgentFunction();

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception occured. Creating default agent.");
            agent = new AgentFunction();
        }

        return agent;
    }

    public static String getAgentName() {
        return AGENT_NAME;
    }
}
