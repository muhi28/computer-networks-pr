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
#include <algorithm>

Define_Module(HTTPClient);

static std::string clrf = "\r\n";
static char* const out = "toLowerLayer";    // But it fuckin works.

static char const* meth = nullptr;
static char const* res = nullptr;
static char const* filepath_pre = "/test/";
static char const* filepath_suf = "";
// BEWARE:: char* const is not char const*

static bool debug_client = true;

void HTTPClient::initialize() {
    // Create Message
    HTTPClientMsg *m = new HTTPClientMsg;

    // Setup parameters using global pointer
    meth = "GET";
    res = (filepath_pre + clrf).c_str();    // Creates a '\0'-terminated string using all args.

    // Set parameter within message using Pointer
    m->setMethod(meth);
    m->setResource(res);

    if(debug_client) { EV << "CLIENT :: Sending request: " << meth << " " << res << '\n'; }

    // Sendout Message to
    send(m, out);
}


void HTTPClient::handleMessage(cMessage *msg) {
    // Handle incomming message
    HTTPServerMsg *response = check_and_cast<HTTPServerMsg*>(msg);
    std::string filecontent = response->getResponse();

    if(debug_client){ EV << "CLIENT :: Received Response: " << filecontent << std::endl; }

    // https://stackoverflow.com/questions/5059049/read-a-string-line-by-line-using-c
    std::istringstream iss(filecontent);
    std::string line;
    while (std::getline(iss, line)) {
        if(debug_client){ EV << "Scanning message: " << line << std::endl;}
        if(line.find("logo.gif"))
        {
            // Request .gif
            HTTPClientMsg *r1 = new HTTPClientMsg;
            meth = "GET";
            res = (filepath_pre + "logo.gif").c_str();
            r1 ->setMethod(meth);
            r1 -> setResource(res);
            send(r1, out);
        }
        if(line.find("TechnikErleben.png"))
        {
            // Request .png
            HTTPClientMsg *r2 = new HTTPClientMsg;
            meth = "GET";
            res = (filepath_pre + "TechnikErleben.png").c_str();
            r2 -> setMethod(meth);
            r2 -> setResource(res);
            send(r2, out);
        }
    }
}
