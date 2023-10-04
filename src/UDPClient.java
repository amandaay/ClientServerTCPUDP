// UDPClient.java
import java.net.*;
import java.io.*;
import java.util.Stack;

/**
 * UDPClient
 * In order to:
 * compile: javac UDPClient.java
 * make sure UDPServer is running in the same port
 * to run: java UDPClient <message> <Hostname> <Port number>
 */
public class UDPClient {
    private static final int TIMEOUT_MS = 10000; // 10 seconds

    public static void main(String args[]) {
        System.out.println("Starting UDPClient:");
        // args give message contents and server hostname
        DatagramSocket aSocket = null;
        if (args.length < 3) {
            System.out.println("Usage: java UDPClient <message> <Hostname> <Port number>");
            System.exit(1);
        }
        try {
            System.out.println("Enter at least 5 PUTs, 5 GETs, 5 DELETEs operation.");
            aSocket = new DatagramSocket();

            InetAddress aHost = InetAddress.getByName(args[1]);
            int serverPort = Integer.parseInt(args[2]);
            Stack<String> sendBuilder = new Stack<>();
            Stack<String> responseBuilder = new Stack<>();

            while (true) {
                // Get the current timestamp
                String timestamp = Utils.getCurrentTimestamp();
                // Read from console
                String userInput = System.console().readLine();
                // Send to Server
                SendToServer(userInput, aHost, serverPort, aSocket, sendBuilder);
                String sending = sendBuilder.pop();
                System.out.println(timestamp + ", "+ sending);
                // Receive a response
                AckFromServer(aSocket, responseBuilder);
                String responseTop = responseBuilder.pop();
                System.out.println(timestamp + ", " + responseTop.substring(responseTop.indexOf("*") + 1));
                // Check if the client wants to terminate
                if (responseTop.contains("Shutting down as requested.")) {
                    break;
                }
                if (!responseTop.contains("Are you sure you want to shutdown? (y/n)")) {
                    System.out.println(timestamp + ", Enter operation:\nPUT <key> <value> or GET <key> or DELETE <key> or SHUTDOWN");
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }

    private static void SendToServer(String userInput, InetAddress aHost, int serverPort, DatagramSocket aSocket, Stack<String> sendBuilder) {
        try {
            aSocket.setSoTimeout(TIMEOUT_MS);
            byte[] m = userInput.getBytes();
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            String message = "Sending: " + new String(request.getData());
            sendBuilder.push(message);
        } catch (SocketTimeoutException e) {
            System.out.println("Server did not respond within the timeout.");
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    private static void AckFromServer(DatagramSocket aSocket, Stack<String> responseBuilder) {
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            String message = new String(reply.getData());
            responseBuilder.push(message);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}