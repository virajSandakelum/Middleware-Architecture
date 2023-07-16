Task 2: Publishers and Subscribers


1.  Improve the client-server implementation for the Server to handle multiple concurrent Client connections.

2.  Multipleclientapplicationsshouldbeabletoconnecttotheserverandthetypedtextina given client CLI should be displayed on the
    Server terminal CLI.

3.  The client application should take the third command line argument to indicate whether the client will act as either
    “Publisher” or “Subscriber”.
    
    E.g.: my_client_app 192.168.10.2 5000 PUBLISHER
          my_client_app 192.168.10.2 5000 SUBSCRIBER

4.  Further, the server should echo the messages sent by any “Publisher” clients to all the “Subscriber” clients terminals. For
    example, when a single server is connected with 10 client applications, when client terminal that is in Publisher mode type a text, it should be displayed on all remaining client terminals that are connected as Subscribers.

5.  The Publisher messages should be only shown on Subscriber terminals, not on any Publisher terminals.