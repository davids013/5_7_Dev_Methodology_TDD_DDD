package test_resources;

public abstract class TestInit {
    public static int testCounter;
    public static long startTime;
    public static long startTestTime;

    public static void beforeAll() {
        startTime = System.currentTimeMillis();
        System.out.println("\t\tTesting...");
        testCounter = 0;
    }

    public static void beforeEach() {
        testCounter++;
        System.out.println("\tTest " + testCounter + " started");
        startTestTime = System.currentTimeMillis();
    }

    public static void afterEach() {
        System.out.println("\tTest " + testCounter + " completed in "
                + (System.currentTimeMillis() - startTestTime) + " ms\n");
    }

    public static void afterAll() {
        System.out.println("\tTesting completed in "
                + (System.currentTimeMillis() - startTime) + " ms");
    }
}