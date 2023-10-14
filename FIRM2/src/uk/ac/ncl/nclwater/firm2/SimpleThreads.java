package uk.ac.ncl.nclwater.firm2;

public class SimpleThreads {

    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private static class MessageLoop
            implements Runnable {
        public void run() {
            String[] importantInfo = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
            try {
                for (String s : importantInfo) {
                    // Pause for 4 seconds
                    Thread.sleep(4000);
                    // Print a message
                    threadMessage(s);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();
    }
}

