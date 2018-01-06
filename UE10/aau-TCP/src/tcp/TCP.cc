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

const bool debug_TCP = true;
int seqNr, ackNr, headerLength, payload, srcPort, destPort, commando, status;
std::vector<cPacket> packets;

void TCP::initialize()
{
    seqNr = 100;
    ackNr = 0;
    payload = 1;
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

    cPacket* httpMsg = check_and_cast<Packet*>(msg);
    controlinfo check and cast tcp on httpmsg
    tcpseg = new tcp seg

    if(controlinfo->gettcpcommand == 0 && controlinfo->gettcpstatus() == 0)
    {
        if(connection == 0){
            bubble();
            EV << ;
            tcpseg->set
            seq++
        }
    }
    else if(gettcpCommand == 2)
        tcp->set
        this->send(tcpSeg, out)
        seqNr++

        else if

}

void TCP::handleTCPSegment(cPacket *msg)
{
    if(connection != 1)
    {
        tcpSeg->Sets
        connection = -2
        send(tcpSeg, out);
        ackNr++;
    }
    else if
    {
        int tmpAckNr = tcpSeg->getAckNr
                tcp->sets
                connection = 1;
        send(tcpseg, out);
    }
    else if
    {
        //SERVER
        connection = 1
                cMessage *HTTPMsg ...
    }
    else if(connection == 1 && flags)
    {
        TCPControlinfo *ci = ..
                set
                Httpsmg
                msg->setControlinfo(ci)
    }
    else if
    {
        tcp segs -> sets


    }
    else if
    {
        cMessage * HTTPmsg = new cMessage();
        tcpCi = new tci
                ->setCi(ci);
        send(HTTPmsg)
    }
}
