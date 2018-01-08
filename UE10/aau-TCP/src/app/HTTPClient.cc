//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see http://www.gnu.org/licenses/.
//
// (c) 2017 Christian Timmerer, Alpen-Adria-Universität Klagenfurt / Bitmovin Inc.
//          christian.timmerer@itec.aau.at / christian.timmerer@bitmovin.com
//
// 621.800 (17W) Computer Networks and Network Programming

#include "HTTPClient.h"
#include "HTTPClientMsg_m.h"
#include "HTTPServerMsg_m.h"
#include "../tcp/TCPControlInfo_m.h"

Define_Module(HTTPClient);

char const* request = "";
char const* method = "GET";
int tcpCommand, tcpStatus;
inet::IPv4Address ipv4_client, ipv4_server;
inet::IPv6Address ipv6_client, ipv6_server;

void HTTPClient::initialize()
{
    ipv4_client = inet::IPv4Address("209.173.21.12");
    ipv6_client = inet::IPv6Address("0:0:0:0:0:ffff:d1ad:150c");
    ipv4_server = inet::IPv4Address("209.173.21.12");
    ipv6_server = inet::IPv6Address("0:0:0:0:0:ffff:d1ad:150c");

    destPort = par("destPort");
    srcPort = par("srcPort");

   /*     int tcpCommand = 0;      // 0 ... do nothing, 1 ... open connection, 2 ... close connection
    *     int tcpStatus = 0;       // 1 ... connection is open, 2 ... connection is closed
    */

    // Initiate Contact
    tcpCommand = 1;
    tcpStatus = 2;

    TCPControlInfo* tci = new TCPControlInfo;
    tci->setTcpCommand(tcpCommand);
    tci->setTcpStatus(tcpStatus);

    scheduleAt(simTime(), tci);
}

void HTTPClient::handleMessage(cMessage *msg)
{
    TCPControlInfo* tci = check_and_cast<TCPControlInfo*>(msg);

    HTTPClientMsg* hcm = new HTTPClientMsg;

    if(msg->isSelfMessage())
    {   // Connect
        tcpCommand = 1; // Open Connection
        tcpStatus = 2;  // Connection is closed

        // Setup ControlInfo
        TCPControlInfo* tci = new TCPControlInfo;
        tci->setDestIPv4(ipv4_server);
        tci->setDestPort(destPort);
        tci->setTcpCommand(tcpCommand);
        tci->setTcpStatus(tcpStatus);

        // Link Info to HTTPMessage
        // hcm->setControlInfo(tci);
        bubble("connect()");
        send(tci, "toLowerLayer");
    }
    /*     int tcpCommand = 0;      // 0 ... do nothing, 1 ... open connection, 2 ... close connection
        *     int tcpStatus = 0;       // 1 ... connection is open, 2 ... connection is closed
        */

    if(tci->getTcpStatus() == 1 && tci->getTcpCommand() == 0)   // Connection is now open
    {
        request = "/test";
        hcm->setRequest(request);
        hcm->setMethod(method);
        send(hcm, "toLowerLayer");
    }
/*
       if(msg->isSelfMessage()) {
           // CONNECTION SETUP
           httpMsg = check_and_cast<HTTPMsg*>(msg);
           httpMsg->setSeq(seq);
           httpMsg->setMethod("");
           httpMsg->setResource("connect");
       } else {
           httpMsg = check_and_cast<HTTPMsg*>(msg);

           // TODO: read httpMsg and send no httpMsg or close connection
           if(httpMsg->getData() == "") {
               httpMsg = new HTTPMsg();
               httpMsg->setSeq(seq);
               httpMsg->setMethod("GET");
               httpMsg->setResource("/");
           } else {
               httpMsg = new HTTPMsg();
               httpMsg->setSeq(seq);
               httpMsg->setMethod("");
               httpMsg->setResource("close");
           }
       }

       send(httpMsg, "out");
*/
}
