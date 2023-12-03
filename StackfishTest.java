public class StackfishTest {

    public static void main(String[] args) {
        Stackfish stackfish = new Stackfish();

        // Test the algorithm speed at each depth
        for (int depth = 1; depth <= 6; depth++) {
            long startTime = System.currentTimeMillis();

            // Run the algorithm with the specified depth
            stackfish.negamax(new char[8][8], new ArrayList<>(), new ArrayList<>(), true, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            System.out.println("Depth " + depth + ": " + timeTaken + "ms");
        }
    }
}