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

/* TCP: Protokollnummer 6
 *
 * TCPControlinfo tcpinfo -> Set fields in HTTPClient::initialize()
 * msg->setControllinfo(TCPinfo)
 * send(msg, out);
 *
 * HTTPServer Initialize()
 * Init ClientIpv4 = new Ipv4Address();
 *
 * HandleMessage
 * -> cPacket *smsg = check and cast <cPacket*> (msg);
 * TCPControllinfo *tcpInfo = Check_and_cast<TCPControlinfo*>(serverMsg->getControlInfo);
 *
 * Sync-Bit:
 * OppositeSeqNum = tcpSeg -> getSeqnr();
 * TcpSeg -> set
 * incommingTransmission = true;
 * send(newTCPSegment, out);
 *
 * if(receivedAckNum == (mySeqNum+1))
 * tcpControlinfo->setTcpStatus(TCP_STATUS_OPEN);
 * send(cInfo, out);
 *
 * tcp-seg_c -> tcp-seg_s
 * TCPControllinfo -> App
 */

#include "TCP.h"
#include <stdio.h>
#include "../3rdParty/IPv4Address.h"
#include "../3rdParty/IPv6Address.h"

Define_Module(TCP);

int seqNr, ackNr, seqNew, ackNew, headerLength, payload;
bool connection_setup, dataTransmission, connection_teardown;
std::vector<int> queue;

void TCP::initialize()
{
    seqNr = 100;
    ackNr = 0;
    payload = 1;
    headerLength = 20;

    if(getParentModule()->getFullName() == "Server") // FIXME Server !== Server
    {
        // Redundant as fuck due to default-vals
        bubble("listen()!");
        TCPControlInfo* tci = new TCPControlInfo;
        tci->setDestPort(80);
        send(tci, "toUpperLayer");
    }
}

void TCP::handleMessage(cMessage *msg){
    if (msg->arrivedOn("fromUpperLayer")) {
        // Comes from appliction.
        this->handleAppMessage((cPacket*)msg);
    }

    else if (msg->arrivedOn("fromLowerLayer")) {
        // Comes from lower layer.
        this->handleTCPSegment((cPacket*) msg);
    }
}

void TCP::handleAppMessage(cPacket *msg)
{
    TCPControlInfo* tci = check_and_cast<TCPControlInfo*>(msg->getControlInfo());

    if(tci->getTcpStatus() == 2 && tci->getTcpCommand() == 1)   // Con is closed && required to open con
    {
        TCPSegment* ts_out = new TCPSegment;
        ts_out->setSyn(true);
        ts_out->setSeqNr(seqNr);
        send(ts_out, "toLowerLayer");
    }
    if(tci->getTcpStatus() == 0 && tci->getTcpCommand() == 1 ) // TCP do nothing -- Connection is open
    {
        TCPSegment* ts_out = new TCPSegment;
                ts_out->setAck(true);
                ts_out->setSeqNr(seqNr);
                ts_out->setAckNr(ackNr);

                ts_out->encapsulate(msg);               // HTTP*Message encapsulated & to next operator
                send(ts_out, "toLowerLayer");
    }
    if(tci->getTcpStatus() == 2 && tci->getTcpCommand() == 1) // TCP close Con -- Con is open
    {
        TCPSegment* ts_out = new TCPSegment;
        ts_out->setFin(true);
        ts_out->setAck(true);
        ts_out->setSeqNr(seqNr);
        ts_out->setAckNr(ackNr);
        send(ts_out, "toLowerLayer");
    }
}

void TCP::handleTCPSegment(cPacket *msg)
{
    TCPSegment* ts = check_and_cast<TCPSegment*>(msg);
    ackNr = ts->getAckNr();
    seqNr = ts->getSeqNr();
    queue.push_back(seqNr);

    // Case 1
    if(ts->getSyn() == true && ts->getAck() == false && connection_setup)
    {
              TCPSegment* ts = new TCPSegment;
              // FIXME
              ackNew = seqNr + payload;    // Seq+Payload
              seqNew = rand() % 301;

              ts->setSyn(true);
              ts->setAck(true);
              ts->setSeqNr(seqNew);
              ts->setAckNr(ackNew);

              send(ts, "toLowerLayer");
    }
    if(ts->getSyn() == true && ts->getAck() == true && connection_setup)
    {
              cPacket* p = new cPacket;
              TCPControlInfo* tci = new TCPControlInfo;
              // connection open()
              tci->setTcpStatus(1);
              tci->setTcpCommand(0);

              p->setControlInfo(tci);
              send(p, "toUpperLayer");

              // Response for server
              TCPSegment* ts = new TCPSegment;
              ackNew = seqNr + payload;    // Seq+Payload
              seqNew = ackNr;
              ts->setAck(true);


    }
    if(ts->getAck() == true && ts->getSyn() == false)
    {
        // FIXME WHAT TO DO DAMMIT
        buildupComplete = true;
    }
    if(ts->getFin() == true)
    {
        // FIXME WHAT TO DO DAMMIT
        terminateConnection = true;
    }
}
