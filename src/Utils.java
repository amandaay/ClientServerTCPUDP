import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility functions to avoid repeated code
 */
public class Utils {
    // Gets current timestamp
    public static String getCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        return dateFormat.format(new Date(currentTimeMillis));
    }

    // Handle at least 5 operators
    public static int handleCount(String response, int count, String operator) {
        int countIndex = response.indexOf(operator);
        if (countIndex != -1 ) {
            count = Integer.parseInt(response.substring(0, countIndex));
        }
        return count;
    }

    // Handle Log Request
    public static void logRequest(InetAddress clientAddress, int clientPort, String requestMessage) {
        String logMessage = "Received request from " + clientAddress + ": " + clientPort + " - " + requestMessage;
        System.out.println(logMessage);
    }
}