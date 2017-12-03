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

Define_Module(HTTPServer);

static const char* test =
        "<html>\n"
        "\t<head><title>Test</title></head>\n"
        "\t<body>\n"
        "\t\t<img src=\"logo.gif\" />\n"
        "\t\t<h1>Welcome</h1>\n"
        "\t\t<img src=\"TechnikErleben.png\" />\n"
        "\t</body>\n"
        "</html>\n";
static const char* png = "/test/logo.gif";
static const char* gif = "/test/TechnikErleben.gif";

static const char* out = "toLowerLayer";    // But it fuckin works.

static char const* content = nullptr;
static bool server_debug = true;


void HTTPServer::initialize()
{
    // ??? Nömte dom. Server usually awaits requests, thus empty init
}

void HTTPServer::handleMessage(cMessage *msg)
{
    // Read incomming message
    HTTPClientMsg *request = check_and_cast<HTTPClientMsg*>(msg);
    const char *meth = request->getMethod();
    const char *res = request->getResource();

    if(server_debug){ EV << "SERVER :: Received Message.\nScanning request: " << meth << " " << res << std::endl; }

    // Check Method
    if(checkMeth(meth)) // Did pass Method- check :: Respond
    {
        // Filter requested Resource
        std::string path = checkResource(res);
        // .. path now contains Errorcode OR real path. Check for that
        if(path.find("Status-Code 400") != std::string::npos)
        {
            // Error fetched. Send that.
            HTTPServerMsg *err_response = new HTTPServerMsg;
            err_response->setResponse(path.c_str());
            send(err_response, out);
        }else{
            // Send File
            HTTPServerMsg *response = new HTTPServerMsg;
            response->setResponse(setupMessage(path));
            send(response, out);
        }
    }else{              // Did not pass Method-check :: ignore & break connection

    }

}

bool HTTPServer::checkMeth(const char *meth)
{
    std::string method(meth);

    if(method.find("GET") != std::string::npos) // FIXME needs a better solution
    {
        if(server_debug){ EV << "SERVER :: Checking Method.\n\tMethod passed." << std::endl; }
        return true;
    }
    if(server_debug){ EV << "SERVER :: Checking Method.\n\tMethod failed." << std::endl; }
    return true;
}

std::string HTTPServer::checkResource(const char *res)
{
    //const char *res =     "/test/\r\n";
    //                      "/test/logo.gif\r\n";
    //                      "/test/technikErleben.png\r\n";
    std::string filterPath(res);
    std::string crlf("\r\n");
    if(server_debug){ EV << "SERVER :: Filtering Path: " << filterPath << std::endl; }

    size_t match = filterPath.rfind(crlf);
    if(match != std::string::npos)
    {
        filterPath.replace(match, crlf.length(), "");
        if(server_debug){ EV << "SERVER :: Matched '\\r\\n' :: new Path: " << '\''<< filterPath << '\'' << std::endl; }
        return filterPath;  // Needs return here, otherwise destroyed by exiting scope
    }else{
        if(server_debug){ EV << "SERVER :: Mismatched -- Time to overhaul HTTPServer::checkResource." << std::endl; }
    }

    return "Status-Code 400 :: Bad Request";
}

const char* setupMessage(std::string s)
{
    if(!s.compare("/test/"))    // RETURNS 0 <-> Thus !
    {
        return test;
    }
    if(!s.compare("/test/logo.gif"))
    {
        return png;
    }
    if(!s.compare("/test/TechnikErleben.gif"))
    {
        return gif;
    }
    return "Status-Code 404 :: File not Found";
}
