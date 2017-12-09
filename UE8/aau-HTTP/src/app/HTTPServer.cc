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
#include "../udp/UDPControlInfo_m.h"

Define_Module(HTTPServer);

const char* server_out = "toLowerLayer";
const char* server_in  = "fromLowerLayer";

bool debug_HTTPServer = true;

std::vector<std::string> data{("<html>\n"
        "\t<head><title>Test</title></head>\n"
        "\t<body>\n"
        "\t\t<img src=\"logo.gif\" />\n"
        "\t\t<h1>Welcome</h1>\n"
        "\t\t<img src=\"TechnikErleben.png\" />\n"
        "\t</body>\n"
        "</html>\n"),   // Fucking stringops in c++. This is actually valid
        "logo.gif",
        "TechnikErleben.png"};

void HTTPServer::initialize(){}

void HTTPServer::handleMessage(cMessage *msg)
{
    if(debug_HTTPServer){ EV << "Server::Received Message." << std::endl; }
    HTTPClientMsg *m = check_and_cast<HTTPClientMsg*>(msg);
    std::string method = m->getMethod();
    std::string resource = m->getResource();

    HTTPServerMsg *sm = new HTTPServerMsg;
    if(!method.compare("GET"))
    {
        if(debug_HTTPServer){ EV << "Server::Sending Message." << std::endl;}

        // Assume cases - Data aligned above is fetched via predetermined index
        if(resource.find("/test"))
        {
            sm->setResponse(data[0].c_str());
            send(sm, server_out);
        }
        else if(resource.find("/test/logo.gif"))
        {
            sm->setResponse(data[1].c_str());
            send(sm, server_out);
        }
        else if(resource.find("/test/TechnikErleben.png"))
        {
            sm->setResponse(data[2].c_str());
            send(sm, server_out);
        }else{
            sm->setResponse("Error 404");
            send(sm, server_out);
        }

    }else{
        // Ignore and kill connection
        EV << "Server::Fetched faulty method-command. Terminating." << std::endl;
    }

}
