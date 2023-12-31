## Assignment Overview:
The purpose of this assignment is about implementing single-threaded UDP and TCP client-server communication in Java while handling at least five of the operation of 5 PUTs, 5 GETs, and 5 DELETEs. Specifically, to store these operators in a Hash Map with a key-value pair fashion. When the user interacts with the client, the client should be able to communicate with the server side seamlessly. In addition, the assignment emphasizes on robust error-handling while logging their behavior throughout my project.

## Technical Impression:
I found this assignment rather valuable to gain hands-on practical experience of how socket programming works in Java. This enhanced my understanding in TCP and UDP use cases. Additionally, the assignment has reinforced my ability to write code in a modular fashion and document input/error as needed. My initial approach was having only TCP server/client and UDP server/client. However, after refactoring my code. I have written a separate KeyValueStore class and Utility class to improve code modularity, avoid repeated code, and easier potential extension.

A potential improvement would be enhancing code modularity and readability. While I have included two additional class (KeyValueStore and Utility), it could be beneficial to potentially have one common server/client parsing whether we want TCP or UDP to share common code. Additionally, error handling can be more specific.

## Use cases:
In practice, this situation can be applied to a distributed system where key-value data is needed to be managed. For example, in a distributed cache system, clients can use the key to request and store data. Thus, the server can handle PUT, GET, and DELETE efficiently. PUT, to update and store the data in cache; GET, to retrieve the data from cache; and DELETE, to remove data from the cache. Future improvement suggestion would be a potential ability to shut down the server remotely as needed for maintenance.
