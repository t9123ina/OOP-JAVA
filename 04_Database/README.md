# Overview of assignment

This assignment implement:
* A mechanism to store Entity-Relationship data as a collection of formatted text files
* A Socket Server to listen for incoming connections and manipulate the stored data
* A Query language that allows operations to be performed on the stored data

## Communication Protocol
The database server should listen on port 8888 and receive incoming queries in the specified query language.
It should interrogate the stored data and return the result of the query to the client via the network connection.
To allow the client application to detect the end of a response, you should terminate the entire message with an "End of Transmission" (EOT) sequence. 
This consists of the EOT character (ASCII value 4) on a line on its own after the content of the response.
## Error handling
The query interpreter should identify any errors in the construction of queries (for example queries not conforming to the BNF or queries that include unknown identifiers). 
When an error is identified, the sever would return an error message that provides the user with information about the nature of the issue.

