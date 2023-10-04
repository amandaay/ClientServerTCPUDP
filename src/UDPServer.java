// UDPServer.java
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * In order to:
 * compile: javac UDPServer.java
 * to run: java UDPServer <Port number>
 */
public class UDPServer {
    public static void main(String args[]) {
        System.out.println("Starting the UDPServer:");
        DatagramSocket aSocket = null;
        if (args.length < 1) {
            System.out.println("Usage: java UDPServer <Port Number>");
            System.exit(1);
        }
        try {
            int ServerPort = Integer.parseInt(args[0]);
            aSocket = new DatagramSocket(ServerPort);

            byte[] buffer = new byte[1000];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            KeyValueStore keyValueStore = new KeyValueStore();
            int putCount = 0;
            int getCount = 0;
            int delCount = 0;
            String prevInput = "";

            while (true) {
                try {
                aSocket.receive(request);
                String userInput = new String(request.getData(), 0, request.getLength());
                List<String> inputList = Arrays.asList(userInput.split("\\s+"));

                // Get current timestamp in milliseconds
                String timestamp = Utils.getCurrentTimestamp();

                // Log received request
                logRequest(request, userInput);

                // print timestamped message to standard output
                System.out.println(timestamp + ", Received: " + userInput);
                if (inputList.get(0).equals("SHUTDOWN")) {
                    prevInput = "SHUTDOWN";
                }

                // Terminates client upon response
                if (prevInput.equals("SHUTDOWN") && (inputList.get(0).equalsIgnoreCase("y") || (putCount >= 5 && getCount >=5 && delCount>=5))) {
                    AckToClient("Shutting down as requested.\nYou must kill the server to shut down completely.", request, aSocket);
                } else if (prevInput.equals("SHUTDOWN") && inputList.get(0).equalsIgnoreCase("n")) {
                  prevInput = "";
                  AckToClient("Let's continue", request, aSocket);
                  continue;
                }

                // Take care all the unknown operations
                if (!inputList.get(0).equals("PUT") && !inputList.get(0).equals("GET") && !inputList.get(0).equals("DELETE") && !inputList.get(0).equals("SHUTDOWN")){
                    AckToClient("Received unknown operation.\nTry again!", request, aSocket);
                    continue;
                }

                // Handle the PUT operator
                if (inputList.get(0).equals("PUT")) {
                    if (inputList.size() < 3) {
                        AckToClient("PUT must have key value pair, try again", request, aSocket);
                        continue;
                    }
                    String key = inputList.get(1);
                    String value = inputList.get(2);
                    String putResponse = keyValueStore.put(key, value, putCount);
                    putCount = Utils.handleCount(putResponse, putCount, "PUTs");
                    AckToClient(putResponse, request, aSocket);
                }
                // Handle Get Operator
                if (inputList.get(0).equals("GET")) {
                    if (inputList.size() != 2) {
                        AckToClient("GET must have a key, try again", request, aSocket);
                        continue;
                    }
                    String key = inputList.get(1);
                    String getResponse = keyValueStore.get(key, getCount);
                    getCount = Utils.handleCount(getResponse, getCount, "GETs");
                    AckToClient(getResponse, request, aSocket);
                }
                // Handle Delete operator
                if (inputList.get(0).equals("DELETE")) {
                    if (inputList.size() != 2) {
                        AckToClient("DELETE must have a key, try again", request, aSocket);
                        continue;
                    }
                    String key = inputList.get(1);
                    String delResponse = keyValueStore.delete(key, delCount);
                    delCount = Utils.handleCount(delResponse, delCount, "DELETEs");
                    AckToClient(delResponse, request, aSocket);
                }

                // Handle when clients want to terminate
                if (inputList.get(0).equals("SHUTDOWN") && (putCount < 5 || getCount <5 || delCount<5)) {
                    AckToClient("You haven't completed at least 5 operator each. Are you sure you want to shutdown? (y/n)", request, aSocket);
                }

                } catch (IOException e) {
                    String malformedMessage = "Received malformed request of length " + request.getLength() + " from " + request.getAddress() + ":" + request.getPort();
                    System.out.println(malformedMessage);
            }}
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } finally {
            if (aSocket != null && !aSocket.isClosed()) {
                aSocket.close();
            }
        }
    }

    private static void AckToClient(String Msg, DatagramPacket request, DatagramSocket socket) {
        try {
            byte[] msg = Msg.getBytes();
            DatagramPacket response = new DatagramPacket(msg, msg.length, request.getAddress(), request.getPort());
            socket.send(response);
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    // Log received request with source InetAddress and port
    private static void logRequest(DatagramPacket request, String requestMessage) {
        InetAddress clientAddress = request.getAddress();
        int clientPort = request.getPort();
        String logMessage = "Received request from " + clientAddress + ": " + clientPort + " - " + requestMessage;
        System.out.println(logMessage);
    }
}
