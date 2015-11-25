.. image:: images/kurento-rect-logo3.png
   :alt:    Kurento logo
   :align:  center

|
|

%%%%%%%%%%%
Description
%%%%%%%%%%%

This document describes the implementation of the :term:`JSON-RPC` client and server in the Kurento project. A proper introduction to the :term:`WebSocket` 
protocol is beyond the scope of this document. At a minimum however it’s important to understand that HTTP is used only for the initial handshake, 
which relies on a mechanism built into :term:`HTTP` to request a protocol upgrade (or in this case a protocol switch) to which the server can respond 
with HTTP status 101 (switching protocols) if it agrees. Assuming the handshake succeeds the :term:`TCP` socket underlying the HTTP upgrade request 
remains open and both client and server can use it to send messages to each other. For information about the protocol itself, please refer to 
this page in the project’s documentation. 

As the :term:`JSON-RPC` v2.0 specification describes, the protocol implies the existence of a client issuing 
requests, and the presence of a server to process those requests. This comes in opposition with v1.0, which used a peer-to-peer architecture, 
where both peers were client and server.
