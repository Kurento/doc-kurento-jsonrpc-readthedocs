%%%%%%%%%%%%%%%
Json-Rpc Server
%%%%%%%%%%%%%%%

This is a :term:`JAVA` implementation of a :term:`JSON-RPC` server. It supports v2.0 only, which implies that Notifications can be used. 
However, the only possible transport is Websockets. It is published as a `Maven artifact <https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22kurento-jsonrpc-server%22>`_, 
allowing developers to easily manage it as a dependency, by including the following dependency in their project’s pom:

.. code-block:: xml

   <dependency>
       <groupId>org.kurento</groupId>
       <artifactId>kurento-jsonrpc-server</artifactId>
       <version>6.6.1-SNAPSHOT</version>
   </dependency>
         
The server is based on :term:`Spring Boot` |SPRING_BOOT_VERSION|. The usage is very simple, and analogous to the creation and configuration of a WebSocketHandler from Spring. 
It is basically composed of the server’s configuration, and a class that implements the handler for the requests received. The following code implements
a handler for :term:`JSON-RPC` requests, that contains a JsonObject as params data type. This handler will send back the params received to the client. Since the
request handling always sends back a response, the library will send an automatic empty response if the programmer does not purposefully do so. In the following
example, if the request does not invoke the echo method, it will send back an empty response:

.. code-block:: java

   import org.kurento.jsonrpc.DefaultJsonRpcHandler;
   import org.kurento.jsonrpc.Transaction;
   import org.kurento.jsonrpc.message.Request;

   import com.google.gson.JsonObject;

   public class EchoJsonRpcHandler extends DefaultJsonRpcHandler<JsonObject> {

       @Override
       public void handleRequest(Transaction transaction,
               Request<JsonObject> request) throws Exception {
           if ("echo".equalsIgnoreCase(request.getMethod())) {
               transaction.sendResponse(request.getParams());
           }
       }
   }

The first argument of the method is the Transaction, which represents a message exchange between a client and the server. The methods available in this object 
(overloads not included), and it’s different uses are:

* **sendResponse**: sends a response back to the client.
* **sendError**: sends an Error back to the client.
* **getSession**: returns the JSON-RPC session assigned to the client.
* **startAsync**: in case the programmer wants to answer the Request outside of the call to the handleRequest method, he can make use of this method to signal the server to not answer just yet. This can be used when the request requires a long time to process, and the server not be locked.
* **isNotification**: evaluates whether the message received is a notification, in which case it mustn’t be answered.

Inside the method *handleRequest*, the developer can access any of the fields from a JSON-RPC Request (*method*, *params*, *id* or *jsonrpc*). This is where the methods 
invoked should be managed. Besides the methods processed in this class, the server handles also the following special *method* values:

* **close**: The client send this method when gracefully closing the connection. This allows the server to close connections and release resources.
* **reconnect**: A client that has been disconnected, can issue this message to be attached to an existing session. The sessionId is a mandatory param.
* **ping**: simple ping-pong message exchange to provide heartbeat mechanism.

The class *DefaultJsonRpcHandler* is generified with the payload that comes with the request. In the previous code, the payload expected is a JsonObject, 
but it could also be a plain String, or any other object.

To configure a WebSocket-based JSON-RPC server to use this handler, developers can use the dedicated JsonRpcConfiguration, for mapping the above websocket handler 
to a specific URL (http://localhost:8080/echo in this case):

.. code-block:: java

   import org.kurento.jsonrpc.internal.server.config.JsonRpcConfiguration;
   import org.kurento.jsonrpc.server.JsonRpcConfigurer;
   import org.kurento.jsonrpc.server.JsonRpcHandlerRegistry;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Import;
   
   @Import(JsonRpcConfiguration.class)
   public class EchoServerApplication implements JsonRpcConfigurer { 
   
       @Override
       public void registerJsonRpcHandlers(JsonRpcHandlerRegistry registry) { 
           registry.addHandler(new EchoJsonRpcHandler(), "/echo"); // “/echo” is the path relative to the server’s URL
       } 
   
   }

Session control
---------------

Each client connecting to this server, will be assigned a unique sessionId. This provides a session concept, that can expand through several websocket sessions. 
Having this notion of JSON-RPC session, allows to bind a set of properties to one particular session. This gives the developers implementing a server the capability of having a stateful server session, which the user can recover once reconnected. The methods available in this object are
 
* **getSessionId**: The ID assigned to this session. It can be used to track down the session, and register it in servers and map it to other resources.
* **getRegisterInfo**: This is set by the client upon connection, and it is accessible by the server through this method.
* **isNew**: will be true if the message is the first message of the session.
* **close**: gracefully closes the connection.
* **setReconnectionTimeout**: sets the time that the server will wait for a reconnection, before closing the session.
* **getAttributes**: returns that attribute map from the session


Handlers
--------

Advanced properties
*******************

When registering a particular handler, there are a number of properties that can be configured. These are accessed from a fluent API in the DefaultJsonRpcHandler

* **withSockJS()** - Enables SockJS as WS library, which provides a fallback to HTTP if the upgrade fails. The client should be a SockJS capable client. There’s more info `here <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-fallback>`_.
* **withLabel(String)** - Adds a label that is used when requests are handled. This allows having a friendly name in the log files, to track executions more easily.
* **withPingWatchdog(true|false)** - The ping watchdog is a functionality that monitors the  health of the heartbeat mechanism, allowing to detect when a regular ping message is not received in the expected time. This informs the server that, though the websocket connection might still be open, the client on the other side is not working as expected.
* **withAllowedOrigins(String[])** - By default, only clients connecting from the same origin (host and port) as the application is served are allowed, limiting the clustering and load-balancing capabilities. This method takes an array of strings with the allowed origins. The `official Spring-Boot <http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-server-allowed-origins>`_ documentation offers details about how this works.

Reacting to connection events
*****************************

The handler offers the possibility to override some methods related to connection events. The methods available are:

.. code-block:: java

   import org.kurento.jsonrpc.DefaultJsonRpcHandler;
   import com.google.gson.JsonObject;
   
   public class EchoJsonRpcHandler extends DefaultJsonRpcHandler<JsonObject> { 
   
       // ...
   
       @Override
       public void afterConnectionEstablished(Session session) throws Exception { 
           // Do something useful here
       } 
   
       @Override
       public void afterConnectionClosed(Session session, String status) 
               throws Exception { 
           // Do something useful here
       } 
   
       @Override
       public void handleTransportError(Session session, Throwable exception) 
               throws Exception { 
           // Do something useful here
       } 
   
       @Override
       public void handleUncaughtException(Session session, Exception exception) { 
           // Do something useful here  
       } 
   }

Notifications
-------------
A Notification is a Request object without an "id" member. A *Request* object that is a Notification signifies the sender's lack of interest in the corresponding *Response* object, and as such no *Response* object needs to be returned.

Notifications are not confirmable by definition, since they do not have a Response object to be returned. As such, the sender would not be aware of any errors (like e.g. "Invalid params","Internal error")

The server is able to send notifications to connected clients using their ongoing *session* objects. For this purpose, it is needed 
to store the *Session* object of each client upon connection. This can be achieved by overriding the *afterConnectionEstablished* method of the handler

.. code-block:: Java

   public class EchoJsonRpcHandler extends DefaultJsonRpcHandler<JsonObject> { 
   
       public final Map<String, Session> sessions = new HashMap<>();

       @Override
       public void afterConnectionEstablished(Session session) {
           String clientId = (String) session.getAttributes().get("clientId");
           sessions.put(clientId, session);
       }
    
       @Override
       public void afterConnectionClosed(Session session, String status) 
            throws Exception { 
           String clientId = (String) session.getAttributes().get("clientId");
           sessions.remove(clientId);
       }

       // Other methods
   }

How a session is paired with each client is something that depends on the business logic of the appllication. In this case, we are assuming that the
session holds a *clientId* property, that can be used to uniquely identify each client. It is also possible to use the *sessionId*,
a :term:UUID provided by the library as session identifier, but they are not meaningful for the application using the library. It is advisable to not leave sessions registered once clients disconnect, so we are overriding the *afterConnectionClosed* method and removing the stored *session* object there.

Notifications are sent to connected clients through their stablished session. Again, how to map sessions to clients in particular is out of the scope of
this document, as it depends on the business logic of the application. Assuming that the *handler* object is in the same scope, the following snippet
shows how a notification to a particular client would be sent

.. code-block:: Java

  public void sendNotification(String clientId, String method, Object params) 
      throws IOException {
    handler.sessions.get(clientId).sendNotification(method, params);
  }

JavaDoc
-------

* `kurento-jsonrpc-server <./_static/langdoc/javadoc/server/index.html>`_