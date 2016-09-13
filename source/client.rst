%%%%%%%%%%%%%%%
Json-Rpc Client
%%%%%%%%%%%%%%%

This is the Java client of the kurento-jsonrpc-server, or any other websocket server that implements the :term:`JSON-RPC` protocol. It allows a Java program to make 
JSON-RPC calls to the kurento-jsonrpc-server. Is also published as a Maven `dependency <https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22kurento-jsonrpc-client%22>`_, 
to be added to the project’s pom:

.. code-block:: xml

   <dependency>
       <groupId>org.kurento</groupId>
       <artifactId>kurento-jsonrpc-server</artifactId>
       <version>6.6.1-SNAPSHOT</version>
   </dependency>

Creating a client
-----------------

Contrary to the server, the client is framework-agnostic, so it can be used in regular Java applications, Java EE, Spring… Creating a client 
that will send requests to a certain server is very straightforward. The URI of the server is passed to the *JsonRpcClientWebSocket* in the constructor, 
here assuming that it is deployed in the same machine:

.. code-block:: java

   JsonRpcClient client = new JsonRpcClientWebSocket("ws://localhost:8080/echo");


Sending requests
----------------

A JSON-RPC call is represented by sending a Request object to a Server. Such object has the following members

* **jsonrpc**: a string specifying the version of the JSON-RPC protocol, “2.0” in this case
* **method**: A String containing the name of the method to be invoked
* **params**: A Structured value that holds the parameter values to be used during the invocation of the method. This member may be omitted, and the type comes defined by the server
* **id**: An identifier established by the Client. If it is not included it is assumed to be a notification. The Server replies with the same value in the Response object if included. This member is used to correlate the context between the two objects.

From all these members, users only have to set the "method" and the "params", as the other two are managed by the library. 

The server defined in the previous section expects a JsonObject, and answers to the *echo* method only, bouncing back the "params" in the request. It is expected that 
the response to *client.sendRequest(request)* will be the wrapped *params* in the *Response<JsonElement>* object that the Server sends back to the client:

.. code-block:: java

   Request<JsonObject> request = new Request<>();
   request.setMethod("echo");
   JsonObject params = new JsonObject(); 
   params.addProperty("some property", "Some Value");
   request.setParams(params);
   Response<JsonElement> response = client.sendRequest(request);

Other messages: notifications
*****************************

A Notification is a Request object without an "id" member. A Request object that is a Notification signifies the Client's lack of interest in the corresponding 
Response object, and as such no Response object needs to be returned to the client. Notifications are not confirmable by definition, since they do not have a 
Response object to be returned. As such, the Client would not be aware of any errors (like e.g. "Invalid params","Internal error")::

   client.sendNotification("echo");
   
Server responses
****************

When the Server receives a rpc call, it will answer with a Response, except in the case of Notifications. The Response is expressed as a single JSON Object, 
with the following members:

* **jsonrpc**: a string specifying the version of the JSON-RPC protocol, “2.0” in this case
* **result**: this member exists only in case of success. The value is determined by the method invoked on the Server.
* **error** this member exists only in there was an error triggered during invocation. The type is an Error Object
* **id**: This is a required member, that must match the value of the id member in the Request. 

Responses will have either “result” or “error” member, but not both.

Error objects
=============

When a rpc call encounters an error, the Response Object contains the error member with a value that is a Object with the following members:

* **code**: A number that indicates the error type
* **message**: a short description of the error
* **data**: A Primitive or Structured value that contains additional information about the error. This may be omitted, and is defined by the Server (e.g. detailed error information, nested errors etc.).

Adding connection listeners
---------------------------

The client offers the possibility to set-up a listener for certain connection events. A user can define a **JsonRpcWSConnectionListener** that offers overrides of certain 
methods. Once the connection listener is defined, it can be passed in the constructor of the client, and the client will invoke the methods once the corresponding 
events are produced:

.. code-block:: java

   JsonRpcWSConnectionListener listener = new JsonRpcWSConnectionListener() { 
               
       @Override
       public void reconnected(boolean sameServer) { 
           // ... 
       } 
               
       @Override
       public void disconnected() { 
           // ... 
       } 
               
       @Override
       public void connectionFailed() { 
           // ... 
       } 
       
       @Override
       public void connected() { 
           // ... 
       } 
   } ;
   JsonRpcClient client = new JsonRpcClientWebSocket("ws://localhost:8080/echo", listener);


Managing heartbeat
------------------

As pointed out in the server, there is a heartbeat mechanism that consists in sending ping messages in regular intervals. This can controlled in the client thought the 
following methods:

* **enableHeartbeat**: this enables the heartbeat mechanism. The default interval is 5s, but this can be changed through the overload of this method, that receives a number as parameter.
* **disableHeartbeat**: stops the regular send of ping messages.

Changing default timeouts
-------------------------

Not only the ping message interval is configurable. Other configurable timeouts are:

* **Connection timeout**: This is the time waiting for the connection to be established when the client connect to the server.
* **Idle timeout**: If no message is sent during a certain period, the connection is considered idle and closed.
* **Request timeout**: the server should answer the request under a certain response time. If the message is not answered in that time, the request is assumed not to be received by the server, and the client yields a TransportException

JavaDoc
-------

* `kurento-jsonrpc-client <./_static/langdoc/javadoc/client/index.html>`_