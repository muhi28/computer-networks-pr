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

}

void HTTPServer::handleMessage(cMessage *msg)
{


}
