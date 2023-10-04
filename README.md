# Single Server, Key-Value Store (TCP and UDP)

## Split 2 different terminals:
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
