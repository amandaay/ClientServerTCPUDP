//TCPServer.java
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class TCPServer {
    public static void main(String[] args) {
        System.out.println("Starting the TCPServer:");
        // accept port number
        if (args.length < 1) {
            System.out.println("Usage: java TCPServer <Port Number>");
            System.exit(1);
        }
        int ServerPort = Integer.parseInt(args[0]);
        try {
            // Register service on port
            ServerSocket listenSocket = new ServerSocket(ServerPort);
            while (true) {
                // Wait and accept a connection and stays open until cmd+c/ctrl+c
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
        }
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    public Connection(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            // Create I/O steams for communicating with the server
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }


public void run() {
    try { // an echo server
        KeyValueStore keyValueStore = new KeyValueStore();
        int putCount = 0;
        int getCount = 0;
        int delCount = 0;
        String prevInput = "";

        while (true) {
            String userInput = in.readUTF();
            List<String> inputList = Arrays.asList(userInput.split("\\s+"));

            // Get current timestamp in milliseconds
            String timestamp = Utils.getCurrentTimestamp();

            // Log received request
            Utils.logRequest(clientSocket.getInetAddress(), clientSocket.getPort(), userInput);

            // print timestamped message to standard output
            System.out.println(timestamp + ", Received: " + userInput);
            if (inputList.get(0).equals("SHUTDOWN")) {
                prevInput = "SHUTDOWN";
            }

            // Terminates client upon response
            if (prevInput.equals("SHUTDOWN") && (inputList.get(0).equalsIgnoreCase("y") || (putCount >= 5 && getCount >=5 && delCount>=5))) {
                out.writeUTF("Shutting down as requested.\nYou must kill the server to shut down completely.");
            } else if (prevInput.equals("SHUTDOWN") && inputList.get(0).equalsIgnoreCase("n")) {
                prevInput = "";
                out.writeUTF("Let's continue");
                continue;
            }

            // Handle all the unknown operations
            if (!inputList.get(0).equals("PUT") && !inputList.get(0).equals("GET") && !inputList.get(0).equals("DELETE") && !inputList.get(0).equals("SHUTDOWN")){
                out.writeUTF("Received unknown operation.\nTry again!");
                continue;
            }

            // Handle the PUT operator
            if (inputList.get(0).equals("PUT")) {
                if (inputList.size() < 3) {
                    out.writeUTF("PUT must have key value pair, try again");
                    continue;
                }
                String value = inputList.get(2);
                String key = inputList.get(1);
                String putResponse = keyValueStore.put(key, value, putCount);
                putCount = Utils.handleCount(putResponse, putCount, "PUTs");
                out.writeUTF(putResponse);
            }

            // Handle Get Operator
            if (inputList.get(0).equals("GET")) {
                if (inputList.size() != 2) {
                    out.writeUTF("GET must have a key, try again");
                    continue;
                }
                String key = inputList.get(1);
                String getResponse = keyValueStore.get(key, getCount);
                getCount = Utils.handleCount(getResponse, getCount, "GETs");
                out.writeUTF(getResponse);
            }

            // Handle Delete operator
            if (inputList.get(0).equals("DELETE")) {
                if (inputList.size() != 2) {
                    out.writeUTF("DELETE must have a key, try again");
                    continue;
                }
                String key = inputList.get(1);
                String delResponse = keyValueStore.delete(key, delCount);
                delCount = Utils.handleCount(delResponse, delCount, "DELETEs");
                out.writeUTF(delResponse);
            }

            // Handle when clients want to terminate
            if (inputList.get(0).equals("SHUTDOWN") && (putCount < 5 || getCount <5 || delCount<5)) {
                out.writeUTF("You haven't completed at least 5 operator each. Are you sure you want to shutdown? (y/n)");
            }
        }
    } catch (EOFException e) {
        System.out.println("EOF: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("IO: " + e.getMessage());
    } finally {
        try {
            clientSocket.close();
        } catch (IOException e) {
            /* close failed*/
        }

    }
}
}

