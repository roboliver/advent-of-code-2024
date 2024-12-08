package common;

public class ExceptionUtils {

    public static String exceptionStackTrace(Exception e) {
        var stackTrace = e.getStackTrace();
        var buf = new StringBuilder();
        for (int i = 0; i < stackTrace.length; i++) {
            if (i > 0) {
                buf.append("\n");
            }
            buf.append("    ").append(stackTrace[i]);
        }
        return buf.toString();
    }
}
