%%%%%%%%%%%%%%
Code structure
%%%%%%%%%%%%%%

Kurento has implemented a JSON-RPC server in Java and a JSON-RPC client in Java and another in Javascript. All implementations are hosted on github: 

- **Java** - https://github.com/Kurento/kurento-java/tree/master/kurento-jsonrpc
- **Javascript** - https://github.com/Kurento/kurento-jsonrpc-js

The Java implementation contains a Maven project with the following modules:

- `kurento-java <https://github.com/Kurento/kurento-java>`_ - reactor project
- `kurento-java/kurento-jsonrpc/kurento-jsonrpc-server <https://github.com/Kurento/kurento-java/tree/master/kurento-jsonrpc/kurento-jsonrpc-server>`_ - Kurento's own implementation of a
  JSON-RPC server.
- `kurento-java/kurento-jsonrpc/kurento-jsonrpc-client <https://github.com/Kurento/kurento-java/tree/master/kurento-jsonrpc/kurento-jsonrpc-client>`_ - Java client of the kurento-jsonrpc-server, or any other websocket server that implements the JSON-RPC protocol.
- `kurento-java/kurento-jsonrpc/kurento-jsonrpc-demo-server <https://github.com/Kurento/kurento-java/tree/master/kurento-jsonrpc/kurento-jsonrpc-demo-server>`_ - It is a demo application of the Kurento JsonRpc Server library. It consists of a WebSocket server that includes several test handlers of JsonRpc messages.


The Javascript implementation contains:

- `kurento-jsonrpc-js <https://github.com/Kurento/kurento-jsonrpc-js>`_ - Javascript client of the kurento-jsonrpc-server, or any other websocket server that implements the JSON-RPC protocol. This library minified is available `here. <http://builds.kurento.org/release/5.0.5/js/kurento-jsonrpc.min.js>`_




