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
//char const* client_out = "toLowerLayer";
//char const* client_in = "fromLowerLayer";

void HTTPClient::initialize()
{
//    ipv4_client = inet::IPv4Adress("209.173.21.12");
//    ipv6_client = inet:: ipv6Adress("0:0:0:0:0:ffff:d1ad:150c");

    /*destport
    srcport
    serveripv4
    clientipv4
    tcpcommand
    status
    countMsg

    controlinfo = new tcpControlinfo
    selfmsg->setcontrollinfo(ci);
    */

    request = "/test";

    HTTPClientMsg* hcm = new HTTPClientMsg;
    hcm->setRequest(request);
    hcm->setMethod(method);

    send(hcm, "toLowerLayer");
}

void HTTPClient::handleMessage(cMessage *msg)
{

    // TODO use from previous exercise and extend if needed
}
