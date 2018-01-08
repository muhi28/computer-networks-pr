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

#include "HTTPServer.h"
#include "HTTPClientMsg_m.h"
#include "HTTPServerMsg_m.h"
#include "../tcp/TCPControlInfo_m.h"

Define_Module(HTTPServer);

int tcpCommand, tcpStatus;
inet::IPv4Address ipv4_client, ipv4_server;
inet::IPv6Address ipv6_client, ipv6_server;

int data_index = 0;
std::vector<std::string> data{("<html>\n"
        "\t<head><title>Test</title></head>\n"
        "\t<body>\n"
        "\t\t<img src=\"logo.gif\" />\n"
        "\t\t<h1>Welcome</h1>\n"
        "\t\t<img src=\"TechnikErleben.png\" />\n"
        "\t</body>\n"
        "</html>\n"),
        "logo.gif",
        "TechnikErleben.png"};

void HTTPServer::initialize()
{
    tcpCommand = 0;
    tcpStatus = 2;
    //ipv4_client = inet::IPv4Address("209.173.21.12");             -- We do not know these at current
    //ipv6_client = inet::IPv6Address("0:0:0:0:0:ffff:d1ad:150c");  -- We do not know these at current
    ipv4_server = inet::IPv4Address("209.173.21.12");
    ipv6_server = inet::IPv6Address("0:0:0:0:0:ffff:d1ad:150c");
    destPort = par("destPort");
    //srcPort = par("srcPort");                                     -- We do not know these at current


}

void HTTPServer::handleMessage(cMessage *msg)
{
    // FIXME Blind code below
    TCPControlInfo* tci = check_and_cast<TCPControlInfo*>(msg->getControlInfo());
    HTTPClientMsg* hcm = check_and_cast<HTTPClientMsg*>(msg);

/*    if(tci->getTcpStatus() == 2 && tci->getTcpCommand() == 1) // Closed connection <-> Requested connection
    {
        ipv4_client = tci->getSrcIPv4();
        ipv6_client = tci->getSrcIPv6();
        srcPort = tci->getSrcPort();
    }
*/
    if(tci->getTcpStatus() == 1 && tci->getTcpCommand() == 0)   //
    {
        // Read request
        std::string request = hcm->getRequest();
        std::string method = hcm->getMethod();
        if(method == "GET")
        {
            while(data_index != data.size())
            {
                std::string response = data.at(data_index++);

                HTTPServerMsg *hsm = new HTTPServerMsg;
                hsm->setResponse(response.c_str());

                TCPControlInfo* tci = new TCPControlInfo;

                hsm->setControlInfo(tci);
                send(hsm, "toLowerLayer");
            }
        }else{
            bubble("Wrong Method. Terminating.");
            EV << "Wrong Method. Terminating." << std::endl;
        }
    }
}
