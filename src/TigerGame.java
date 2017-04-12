import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by Yue on 12-Apr-17.
 */
public class TigerGame {

    private static ExecutorService executor;

    private static AgentFunction agent;
    private static Engine engine;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        executor = Executors.newCachedThreadPool();
        ArrayList<Future<Double>> gameFutures = new ArrayList<>();

        agent = new AgentFunction();
        engine = new Engine(agent);

        int iterationCount = 1000000;
        int bufferSize = 1000;

        Future<Double> gameFuture;

        System.out.println("Name : " + AgentFunction.getAgentName());
        System.out.println();

        long t1 = System.currentTimeMillis();

        double totalScore = 0.0;
        double currentScore;

        // queue up iteration count number of games
        for (int i = 0; i < iterationCount; i++) {
            if (i % bufferSize != 0 && i != 0) {
                // add to buffer
                gameFuture = executor.submit(buildGameCallable(i));
                gameFutures.add(gameFuture);
            }
            else {
                // retrieve results from games in buffer
                for (Future<Double> f : gameFutures) {
                    currentScore = f.get();
                    totalScore += currentScore;
                }

                // dump the games which have already been played
                gameFutures.clear();

                // create a new game
                gameFuture = executor.submit(buildGameCallable(i));
                gameFutures.add(gameFuture);
            }
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Total Score : " + totalScore);
        System.out.println("Average Score : " + (totalScore / (double) iterationCount));

        System.out.println();
        System.out.println("Executed in time : " + ((t2 - t1) / 1000.) + " seconds");

        endGame();

    }

    private static synchronized Callable<Double> buildGameCallable(final int gameID) {
        Callable<Double> callable = () -> engine.playGame(gameID);

        return callable;
    }

    private static synchronized void endGame() {
        if (executor != null) {
            executor.shutdown();

            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                        System.err.println("Pool did not terminate");
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }

            executor = null;
        }
    }
}
