# Secure_De_duplication_IdashTrack1
In Config file, following parameters need to be specified:

Num_Server: Number of server except the main server.
ServerPorts: Ports for servers
ServerIPs: Ips for servers
Num_Center: Number of center to check for de-duplication
DataSizeCenter: Number of records in the file which will be checked for de-duplication
InputFileId: File id for the input file
Server1Port: main server port which will perform major computations
Center1Port: Center port (Center which will input records to check duplication)

Description of Parameters:
At first we intialize our config. parameters. Num_Server denotes the number of servers participate in the computation excluding the main srrver. Then we intialize ports and Ips for the servers.
Num_Center denotes the number of centers where input records will be checked for duplication. Next, we intialize our input records size which will be checked for de-duplication and InputFileId sets the input file. 
Server1Port denotes the main server ports which will perform major part of the computation and Center1Port denotes the port for the center from where we consider the input records.

Description of the Scheme:
Our scheme operates based on the mechanism of blind signature. At first Center1(input center) multiplies the input records with a random number(rc) and passes the records to Server1(main server). Main servers sign the records 
with public key(e) and multiply with its random number(rs) and forwards to other centers. The other centers signs the records with their private key)(d) and returns the records to Server1. 
Server1 divides the signed records with the random number(rs) and forwards the records other servers (s2,s3,.....). The centers sign their records with private key(d) after multplication with random number (rc), 
and generates bloom filters. In our schemes the center holds  pre-generated signed bloom filters. The signed bloom filters are passed to other servers (s2,s3,.....) for matching.
 The servers matches the bloom filter recieved from centers with the generated bloom filter with the rcived records from Server1. 
 The servers passes the duplication result to the Center1(input center) who intiated de-duplication with input files. The client filter the results recived from servers and get the final result.
