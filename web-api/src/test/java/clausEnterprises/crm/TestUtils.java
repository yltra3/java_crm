package clausEnterprises.crm;

public class TestUtils {
    private TestUtils() {
    }

    public static void printMyName() {
        String methodName = "unknown";
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            methodName = stackTraceElements.length > 1 ? testName(stackTraceElements[1]) : testName(stackTraceElements[0]);
        }
        System.out.println("-------------------------------------------------\n" +
                "Run test [" + methodName + "]\n" +
                "--------------------------------------------------");
    }

    private static String testName(StackTraceElement ste) {
        return ste.getClassName() + "." + ste.getMethodName();
    }
}
