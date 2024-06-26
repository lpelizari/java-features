package exceptions;

public class StackTraceExample {

    public static void main(String[] args) {
        try {
            methodThatThrowsException();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element);
            }
        }
    }

    static void methodThatThrowsException() throws Exception {
        throw new Exception("Exception with stack trace");
    }
}
