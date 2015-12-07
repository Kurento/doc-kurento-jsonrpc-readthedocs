%%%%%%%%
Glossary
%%%%%%%%

This is a glossary of terms that often appear in discussion about multimedia
transmissions. Most of the terms are described and linked to its wikipedia, RFC
or W3C relevant documents. Some of the terms are specific to :term:`kurento`.


.. glossary::
    HTTP
        The :wikipedia:`Hypertext Transfer Protocol <en,Hypertext_Transfer_Protocol>`
        is an application protocol for distributed, collaborative, hypermedia
        information systems. HTTP is the foundation of data communication for
        the World Wide Web.

        .. seealso:: :rfc:`2616`

    JSON
        `JSON <http://json.org>`__ (JavaScript Object Notation) is a lightweight
        data-interchange format. It is designed to be easy to understand and
        write for humans and easy to parse for machines.

    JSON-RPC
        `JSON-RPC <http://json-rpc.org/>`__ is a simple remote procedure
        call protocol encoded in JSON. JSON-RPC allows for notifications
        and for multiple calls to be sent to the server which may be
        answered out of order.

    Kurento
        `Kurento <http://kurento.org>`__ is a platform for the development of multimedia
        enabled applications. Kurento is the Esperanto term for the English word
        'stream'. We chose this name because we believe the Esperanto principles are
        inspiring for what the multimedia community needs: simplicity, openness and
        universality. Kurento is open source, released under LGPL 2.1, and has several
        components, providing solutions to most multimedia common services
        requirements. Those components include: :term:`Kurento Media Server`,
        :term:`Kurento API`, :term:`Kurento Protocol`, and :term:`Kurento Client`.

    Kurento API
         **Kurento API** is an object oriented API to create media pipelines to control
         media. It can be seen as and interface to Kurento Media Server. It can be used from the
         Kurento Protocol or from Kurento Clients.

    Kurento Client
         A **Kurento Client** is a programming library (Java or JavaScript) used to control
         **Kurento Media Server** from an application. For example, with this library, any developer
         can create a web application that uses Kurento Media Server to receive audio and video from
         the user web browser, process it and send it back again over Internet. Kurento Client
         exposes the :term:`Kurento API <Kurento API>` to app developers.

    Kurento Protocol
         Communication between KMS and clients by means of :term:`JSON-RPC` messages.
         It is based on :term:`WebSocket` that uses :term:`JSON-RPC` V2.0 messages for making
         requests and sending responses.

    KMS
    Kurento Media Server
         **Kurento Media Server** is the core element of Kurento since it responsible for media
         transmission, processing, loading and recording.

    Maven
        `Maven <http://maven.apache.org/>`_ is a build automation tool used primarily for Java projects.

    Sphinx
        Documentation generation system used for Brandtalk documentation.

        .. seealso:: `Easy and beautiful documentation with Sphinx <http://www.ibm.com/developerworks/linux/library/os-sphinx-documentation/index.html?ca=dat>`_

    WebSocket
    WebSockets
        `WebSocket <https://www.websocket.org/>`__ specification (developed as
        part of the HTML5 initiative) defines a full-duplex single socket
        connection over which messages can be sent between client and server.
