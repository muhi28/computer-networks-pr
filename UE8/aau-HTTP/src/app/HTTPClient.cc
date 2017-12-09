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
#include "../udp/UDPControlInfo_m.h"

Define_Module(HTTPClient);
const char* method = "GET";
char const* resource = nullptr;

const char* client_out = "toLowerLayer";    // For the f* protocol: Don't double-'out' in Client & Server.
const char* client_in  = "fromLowerLayer";

bool debug_HTTPClient = true;

void HTTPClient::initialize() {
    // 1. Initialize a Request to a server
    resource = "/test";

    HTTPClientMsg* m = new HTTPClientMsg;
    m->setMethod(method);        // string method
    m->setResource(resource);    // string resource

    if(debug_HTTPClient){ EV << "Client::Sending initial request." << std::endl; }
    send(m, client_out);
    // 2.1 Continue UDP-Segmentation in UDP.cc
}

void HTTPClient::handleMessage(cMessage *msg) {
    if(debug_HTTPClient){ EV << "Client::Received data." << std::endl; }
    // Cast response to expected messagetype
    HTTPServerMsg *m = check_and_cast<HTTPServerMsg*>(msg);

    // Read response from message
    std::string response = m->getResponse();
    EV << "Client::Response was: " << '\n' << response << std::endl;

    // FIXME maybe, there are prettier ways of filtering for possible content
    if(response.find(".gif"))
    {
        if(debug_HTTPClient){ EV << "Client::Requesting data on .gif" << std::endl; }

        HTTPClientMsg *cm_gif = new HTTPClientMsg;
        cm_gif->setMethod(method);
        cm_gif->setResource(resource+"/logo.gif");
        send(cm_gif, client_out);

    }
    if(response.find(".png"))
    {
        if(debug_HTTPClient){ EV << "Client::Requesting data on .png" << std::endl; }

        HTTPClientMsg *cm_png = new HTTPClientMsg;
        cm_png->setMethod(method);
        cm_png->setResource(resource+"/TechnikErleben.png");
        send(cm_png, client_out);
    }
    // }else{} -- We're done at this point
}
