// TCPClient.java
import java.net.*;
import java.io.*;

/**
 * Must run TCPServer first in order to run this
 * compile: javac TCPClient.java
 * run: java TCPClient <IP> <Port>
 */
public class TCPClient {
    private static final int TIMEOUT_MS = 10000; // 10 seconds

    public static void main (String[] args) {
        System.out.println("Starting the TCPClient:");
        // client must take in ( hostname or IP address of the server ) and Port no. of the server
        if (args.length < 2) {
            System.out.println("Usage: java TCPClient <client> <Port Number>");
            System.exit(1);
        }
        String ClientHostname = args[0];
        int ClientPort = Integer.parseInt(args[1]);
        Socket client = null;

        try {
            System.out.println("Enter at least 5 PUTs, 5 GETs, 5 DELETEs operation.");
            // open connection to a server
            client = new Socket(ClientHostname, ClientPort);
            client.setSoTimeout(TIMEOUT_MS); // Set the socket timeout
            // get an input file handle from the socket and read the input
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            while (true) {
                // Get the current timestamp
                String timestamp = Utils.getCurrentTimestamp();
                // Read from console
                String userInput = System.console().readLine();
                // Send it to Server
                out.writeUTF(userInput);
                System.out.println(timestamp + ", Sending: " + userInput);
                // Receive response from Server
                String response = in.readUTF();
                int indexCleanResponse = response.indexOf("*");
                System.out.println(timestamp + ", " + response.substring(response.indexOf("*") + 1));
                // Check if the client wants to terminate
                if (response.contains("Shutting down as requested.")) {
                    break;
                }
                // Allow client to make sure if 5 operators are not done
                if (!response.contains("Are you sure you want to shutdown? (y/n)")) {
                    System.out.println(timestamp + ", Enter operation:\nPUT <key> <value> or GET <key> or DELETE <key> or SHUTDOWN");
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            System.out.println("Server did not respond within the timeout.");
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (client != null) {
                try {
                    // close connection and exit
                    client.close();
                } catch (IOException e) {
                    /* close fail*/
                }
            }
        }
    }
}