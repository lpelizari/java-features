package exceptions;

public class SuppressedExceptionExample {

    public static void main(String[] args) {
        try {
            methodWithSuppressedException();
        } catch (Exception e) {
            System.err.println("Caught: " + e);
            for (Throwable t : e.getSuppressed()) {
                System.err.println("Suppressed: " + t);
            }
        }
    }

    static void methodWithSuppressedException() throws Exception {
        Exception primary = new Exception("Primary Exception");
        try {
            throw new Exception("Suppressed Exception");
        } catch (Exception e) {
            primary.addSuppressed(e);
        }
        throw primary;
    }
}
