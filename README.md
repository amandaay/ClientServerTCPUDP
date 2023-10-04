# Single Server, Key-Value Store (TCP and UDP)

## Start with a split of 2 different terminals:
One for server and one for client.

### In both terminals TCP/UDP
`cd src`

#### Server Terminal
1. Compile with `javac TCPServer.java` / `javac UDPServer.java`
2. *For TCP, ensure you run server before client.*
3. `java TCPServer <Port Number>` / `java UDPServer <Port Number>`

#### Client Terminal
1. Compile with `javac TCPClient.java` / `javac UDPClient.java`
2. `java TCPClient <IP> <Port Number>` / `java UDPClient <message> <Hostname> <Port number>`

#### Sample TCP Output
![TCP Output Sample (Part 1)](https://github.com/amandaay/ClientServerTCPUDP/blob/main/tcpPart1.png)

![TCP Output Sample (Part 2)](https://github.com/amandaay/ClientServerTCPUDP/blob/main/tcpPart2.png)

#### Sample UDP Output

![UDP Output Sample (Part 1)](https://github.com/amandaay/ClientServerTCPUDP/blob/main/udpPart1.png)

![UDP Output Sample (Part 2)](https://github.com/amandaay/ClientServerTCPUDP/blob/main/udpPart2.png)
