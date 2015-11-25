%%%%%%%%%%%%%%%%%%
JSON-RPC CLIENT JS
%%%%%%%%%%%%%%%%%%

This is the Javascript client of the kurento-jsonrpc-server, or any other websocket server that implements the :term:`JSON-RPC` protocol.
It allows a Javascript program to make JSON-RPC calls to any jsonrpc-server. Is also published as a `bower dependency <https://github.com/Kurento/kurento-jsonrpc-bower>`_.

JsonRpcClient
-------------

Create client
*************

For creating a client that will send requests, you need to create a configuration object like in the next example:

.. code-block:: javascript

   var configuration = {
         hearbeat: 5000,
         sendCloseMessage : false,
         ws : {
           uri : ws_uri,
           useSockJS: false,
           onconnected : connectCallback,
           ondisconnect : disconnectCallback,
           onreconnecting : disconnectCallback,
           onreconnected : connectCallback
         },
         rpc : {
           requestTimeout : 15000,
           treeStopped : treeStopped,
           iceCandidate : remoteOnIceCandidate,       
         }
       };
   
   var jsonRpcClientWs = new JsonRpcClient(configuration);
   

This configuration object has several options: in one hand, the configuration about transport on the other hand the configuration about methods that the client has to 
call when get a response. Also, it can configure the interval for each heartbeat and if you want send a message before closing the connection. 

* **Configuration**

.. code-block:: javascript

   {
       heartbeat: interval in ms for each heartbeat message,
       sendCloseMessage: true / false, before closing the connection, it sends a close_session message,
       ws: {
           uri: URItoconntectto,
           useSockJS: true(useSockJS)/false(useWebSocket)bydefault,
           onconnected: callback method to invoke when connection is successful,
           ondisconnect: callback method to invoke when the connection is lost,
           onreconnecting: callback method to invoke when the client is reconnecting,
           onreconnected: callback method to invoke when the client succesfully reconnects        
       },
       rpc: {
           requestTimeout: timeoutforarequest,
           sessionStatusChanged: callback method for changes in session status,
           mediaRenegotiation: mediaRenegotiation
           ...
       [Other methods you can add on rpc field are: 
   treeStopped : treeStopped
      iceCandidate : remoteOnIceCandidate]
       }
   }

If heartbeat is defined, each x milliseconds the client sends a ping to the server for keeping the connection.

Sending requests
----------------

A JSON-RPC call is represented by sending a Request object to a Server using send method. Such object has the following members:

* **method**: A String containing the name of the method to be invoked
* **params**: A Structured value that holds the parameter values to be used during the invocation of the method. This member may be omitted, and the type comes defined by the server. It is a json object.
* **callback**: A method with error and response. This method is called when the request is ended.


.. code-block:: javascript

   var params = { 
                interval: 5000 
                 };
   
   jsonrpcClient.send(“ping”, params , function(error, response){
            if(error) {
               ...
            } else {
               ...
            }
         });

Server responses
****************

When the Server receives a rpc call, it will answer with a Response, except in the case of Notifications. The Response is expressed as a single JSON Object, 
with the following members:

* **jsonrpc**: a string specifying the version of the JSON-RPC protocol, “2.0” in this case
* **result**: this member exists only in case of success. The value is determined by the method invoked on the Server.
* **error**: this member exists only in there was an error triggered during invocation. The type is an Error Object
* **id**: This is a required member, that must match the value of the id member in the Request. 

Responses will have either “result” or “error” member, but not both.

Error objects
*************

When a rpc call encounters an error, the Response Object contains the error member with a value that is a Object with the following members:

* **code**: A number that indicates the error type
* **message**: a short description of the error
* **data**: A Primitive or Structured value that contains additional information about the error. This may be omitted, and is defined by the Server (e.g. detailed error information, nested errors etc.).

Other methods
*************

* **close**: Closing jsonRpcClient explicitly by client.
* **reconnect**: Trying to reconnect the connection.
* **forceClose**: It used for testing, forcing close the connection.

WebSocket With Reconnection
---------------------------

This jsonrpc client uses an implementation of websocket with reconnection. This implementation allows the connection always alive.

It is based on states and calls methods when any of next situation happens:

* **onConnected**
* **onDisconnected**
* **onReconnecting**
* **onReconnected**

It has a configuration object like next example and this object is part of jsonrpc client’s configuration object. 

.. code-block:: javascript

   {
      uri: URItoconntectto,
      useSockJS: true(useSockJS)/false(useWebSocket)bydefault,
      onconnected: callback method to invoke when connection is successful,
      ondisconnect: callback method to invoke when the connection is lost,
      onreconnecting: callback method to invoke when the client is reconnecting,
      onreconnected: callback method to invoke when the client succesfully reconnects 
   }


