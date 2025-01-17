package common;

public class ExceptionUtils {

    public static String exceptionStackTrace(Exception e) {
        var stackTrace = e.getStackTrace();
        var buf = new StringBuilder();
        buf.append("    ");
        buf.append(e.getClass().getSimpleName());
        buf.append(": ");
        buf.append(e.getMessage());
        for (int i = 0; i < stackTrace.length; i++) {
            buf.append("\n");
            buf.append("      ").append(stackTrace[i]);
        }
        return buf.toString();
    }
}
