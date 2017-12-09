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

#include <omnetpp.h>
#include "UDP.h"
#include "UDPSegment_m.h"
#include "UDPControlInfo_m.h"
#include "../3rdParty/IPv4Address.h"
#include "../3rdParty/IPv6Address.h"

Define_Module(UDP);

const char* fromApp = "fromUpperLayer";
const char* toApp = "toUpperLayer";
const char* fromNet = "fromLowerLayer";
const char* toNet = "toLowerLayer";

bool debug_UDP = true;

void UDP::initialize(){}

void UDP::handleMessage(cMessage *msg)
{

	if (msg->arrivedOn(fromApp)) {
		// 2.2: Handle Message from Application layer
	    // 12.2: Handle Message from
		if(debug_UDP){ EV << "UDP-Layer::Received Message from upper layer." << std::endl; }
	    this->handleAppMessage((cPacket*)msg);
	}

	else if (msg->arrivedOn(fromNet)) {
	    // 7. Incomming Mession from network
	    if(debug_UDP){ EV << "UDP-Layer::Received Message from network." << std::endl; }
		this->handleUDPSegment((cPacket*)msg);
	}
}

void UDP::handleAppMessage(cPacket *msg)
{
    // 2.3:
    // 3. cast to http msg -- TODO
    // We fetched message from the Application layer <-> We want to parse it correspondingly to HTTPServer/Client.msg
        // 4. remove controlinfo
        // -- It does not contain controlinfo. [?? FIXME ??]
            // 5. create udp segment and use controlinfo to set UDP fields
            UDPSegment *udps = new UDPSegment;
            // FIXME: Parameter for UDPS
                // 6. encapsulate http msg and send to lower layer

    if(getParentModule()->getName() == "ClientHost")
    {
        HTTPClientMsg *cm = check_and_cast<HTTPClientMsg*>(msg);
        cm->removeControlInfo();
        udps.encapsulate(cm);
        send(udps, toNet);

    }else{  // == "ServerHost"
        HTTPServerMsg *sm = check_and_cast<HTTPServerMsg*>(msg);
        sm->removeControlInfo();
        udps.encapsulate(sm);
        send(udps, toNet);
    }
    if(debug_UDP){ EV << "UDP-Layer::Transmitting encapsulated datapack to outer Network." << std::endl;}
}

void UDP::handleUDPSegment(cPacket *msg) {
    // 8 cast to udp segment
    UDPSegment *udps = check_and_cast<*UDPSegmet>(msg);
    // 9.. create controlinfo and use UDP fields to set values
    // FIXME How the flying fuck

    // 10. decapsulate http msg
    udps->decapsulate();

    // 11. attach controlinfo and sent to upper layer
    udps->setControlInfo(cObject* p);


    if(debug_UDP){ EV << "UDP-Layer::Transmitting decapsulated datapack to application layer." << std::endl; }
    send(udps, toApp);
}
