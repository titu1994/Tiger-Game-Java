import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Yue on 12-Apr-17.
 */
public class Engine {

    private AgentFunction agent;

    private double listeningAccuracy = 0.85;
    private double reward = 10;
    private double listeneingPenalty = -1;
    private double tigerPenalty = -100;
    private boolean verbose = false;

    // game variales
    private long seed = 0;
    private Random random;
    private boolean tigerLocation;

    public Engine(AgentFunction agent) {
        this(agent, -10405);
    }

    public Engine(AgentFunction agent, long seed) {
        this.agent = agent;

        this.seed = seed;
        if (seed != -10405)
            random = new Random(seed);
        else
            random = new Random();

        // random initial tiger location.
        // false means left, true means right
        tigerLocation = random.nextBoolean();
    }

    private synchronized void resetState() {
        tigerLocation = random.nextBoolean(); // false indicates TL, true indicates TR
    }

    private synchronized void updateState(int action) {
        if (action == Action.ACTION_OPEN_LEFT || action == Action.ACTION_OPEN_RIGHT) {
            tigerLocation = random.nextBoolean(); // false indicates TL, true indicates TR. Tiger reset when door opened.
        }
    }

    private synchronized int observe() {
        double probability = random.nextDouble() - 0.015; // bias factor for java random number

        if (probability <= listeningAccuracy) {
            if (!tigerLocation) // tiger on left
                return Observation.GROWL_LEFT;
            else // tiger on right
                return Observation.GROWL_RIGHT;
        } else {
            if (!tigerLocation) // tiger on left, but wrong observation (right)
                return Observation.GROWL_RIGHT;
            else  // tiger on right, but wrong observation (left)
                return Observation.GROWL_LEFT;
        }
    }

    private synchronized boolean checkWrongDoorOpened(int action) {
        if (!tigerLocation && action == Action.ACTION_OPEN_LEFT) // tiger on left and opened left door
            return true;
        else if (tigerLocation && action == Action.ACTION_OPEN_RIGHT) // tiger on right and opened right door
            return true;
        else
            return false;
    }


    private synchronized double reward(int action) {
        if (action == Action.ACTION_OPEN_LEFT || action == Action.ACTION_OPEN_RIGHT) {
            if (checkWrongDoorOpened(action)) {
                return tigerPenalty; // opened wrong door, get tiger penalty
            } else {
                return reward; // opened right door, get reward
            }
        } else {
            return listeneingPenalty; // listened, get listening penalty
        }
    }

    public synchronized double playGame(int gameID) {
        if (verbose)
            System.out.println("Beginning game : " + (gameID + 1) + "\n");

        resetState(); // initialize the game

        if (verbose)
            System.out.println("Observed : " + Observation.getName(Observation.NO_OBSERVATION)); // initial no observation

        int observation = Observation.NO_OBSERVATION;
        int action = agent.act(0, observation);
        double score = reward(action);
        double totalScore = score;

        if (verbose) System.out.println("Current tiger location = " + tigerLocation);

        if (verbose) System.out.println("Performed action : " + Action.getName(action) + " \n");

        while (!checkWrongDoorOpened(action)) {
            if (action == Action.ACTION_OPEN_LEFT || action == Action.ACTION_OPEN_RIGHT) {
                updateState(action); //
                observation = Observation.NO_OBSERVATION;
            } else {
                observation = observe();
            }

            if (verbose) System.out.println("Observed : " + Observation.getName(observation) + " \n");

            action = agent.act(score, observation);
            score = reward(action);
            totalScore += score;

            if (verbose) System.out.println("Current tiger location = " + tigerLocation);

            if (verbose) System.out.println("Performed action : " + Action.getName(action) + "\n");
        }

        score += tigerPenalty; // game ending penalty

        if (verbose) {
            System.out.println("Game : " + (gameID + 1));
            System.out.println("Score : " + score);
            System.out.println();
        }

        return totalScore;
    }


}
