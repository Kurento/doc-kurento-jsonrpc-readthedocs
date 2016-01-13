%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Securing JSON-RPC connections
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

From Chrome M47, requests to *getUserMedia* are only allowed from secure origins (HTTPS or HTTP from localhost). Since Kurento
relies heavily on the JSON-RPC library for the signaling part of applications, it is required that the JSON-RPC server offers
a secure websocket connection (:term:`WSS`), or the client will receive a *mixed content* error, as insecure WS connections may not be initialised from a secure HTTPS connection.

Securing JSON-RPC Servers
-------------------------

Enabling secure Websocket connections is fairly easy in Spring. The only requirement is to have a certificate, either self-signed or 
issued by a certification authority. The certificate must be stored in a :wikipedia:`keystore <en,Keystore>`, so it can be later used by the :term:JVM. Depending on whether you have acquired a certificate or want to generate your own, you will need to perform
different operations

* Certificates issued by certification authorities can be imported with the command:
 
 .. sourcecode:: bash

    keytool -importcert -file certificate.cer -keystore keystore.jks -alias "Alias"

* A keystore holding a self-signed certificate can be generated with the following command:

 .. sourcecode:: bash

    keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 360 -keysize 2048


The file *keystore.jks* must be located the project’s root path, and a file named application.properties must exist in ``src/main/resources/``, with the following content:

.. sourcecode:: bash

  server.port: 8443
  server.ssl.key-store: keystore.jks
  server.ssl.key-store-password: yourPassword
  server.ssl.keyStoreType: JKS
  server.ssl.keyAlias: yourKeyAlias

You can also specify the location of the properties file. Just issue the flag ``-Dspring.config.location=<path-to-properties>`` when launching the Spring-Boot based app. In order to change the location of the *keystore.jks* file, it is enough to change the key 
``server.ssl.key-store``. The complete official documentation form the Spring project can be found `here <https://docs.spring.io/spring-boot/docs/current/reference/html/howto-embedded-servlet-containers.html#howto-configure-ssl>`__


Connecting JSON-RPC Clients to secure servers
---------------------------------------------

JSON-RPC clients can connect to servers exposing a seure connection. By default, the Websocket library used will try to validate the
certificate used by the server. In case of self-signed certificates, the client must be instructed to prevent skip this validation
step. This can be acchieved by creating a ``SslContextFactory``, and using the factory in the client.

.. code-block:: java

    SslContextFactory contextFactory = new SslContextFactory();
    contextFactory.setValidateCerts(false);

    JsonRpcClientWebSocket client = new JsonRpcClientWebSocket(uri, contextFactory);

