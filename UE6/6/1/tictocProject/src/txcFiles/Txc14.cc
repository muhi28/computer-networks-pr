/*
 * Txc14.cc
 *
 *  Created on: 26.11.2017
 *      Author: muhamed
 */



#include <stdio.h>
#include <string.h>
#include <omnetpp.h>
#include "msgData/tictoc14_m.h"

using namespace omnetpp;

/**
 * Module class
 */
class Txc14 : public cSimpleModule
{
  private:
    long numSent;
    long numReceived;

  protected:
    virtual TicTocMsg14 *generateMessage();
    virtual void forwardMessage(TicTocMsg14 *msg);
    virtual void refreshDisplay() const override;
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
};

Define_Module(Txc14);

/**
 * Initializes the used variables and creates the first image, if
 * and only if the module index == 0.
 */
void Txc14::initialize()
{
    // Initialize variables
    numSent = 0;
    numReceived = 0;
    WATCH(numSent);
    WATCH(numReceived);
    // Module 0 sends first message
    if (getIndex() == 0) {

        // Boot the process scheduling the initial message as a self-message.
        TicTocMsg14 *msg = generateMessage();
        numSent++;
        scheduleAt(0.0, msg);
    }
}

/**
 * Used to handle the incoming message.
 */
void Txc14::handleMessage(cMessage *msg)
{
    TicTocMsg14 *ttmsg = check_and_cast<TicTocMsg14 *>(msg);
    if (ttmsg->getDestination() == getIndex()) {
        // Message arrived
        int hopcount = ttmsg->getHopCount();
        EV << "Message " << ttmsg << " arrived after " << hopcount << " hops.\n";
        numReceived++;
        delete ttmsg;
        bubble("ARRIVED, starting new one!");

        // Generate new/another Message.
        EV << "Generating another message: ";
        TicTocMsg14 *newmsg = generateMessage();
        EV << newmsg << endl;
        forwardMessage(newmsg);
        numSent++;
    }
    else {

        forwardMessage(ttmsg);
    }
}

/**
 * Produces source / destination addresses and generates
 * a new Message with the defined source and destination.
 */
TicTocMsg14 *Txc14::generateMessage()
{
    // Produces source and destination addresses.
    int src = getIndex();  //  module index
    int n = getVectorSize();  // module vector size
    int dest = intuniform(0, n-2);

    if (dest >= src)
        dest++;

    char msgname[20];
    sprintf(msgname, "tic-%d-to-%d", src, dest);

    // Create message and set source and destination.
    TicTocMsg14 *msg = new TicTocMsg14(msgname);
    msg->setSource(src);
    msg->setDestination(dest);

    return msg;
}


void Txc14::forwardMessage(TicTocMsg14 *msg)
{
    // Increment hop count.
    msg->setHopCount(msg->getHopCount()+1);
    // Same routing as before: random gate.
    int n = gateSize("gate");
    int k = intuniform(0, n-1);
    EV << "Forwarding message " << msg << " on gate[" << k << "]\n";
    send(msg, "gate$o", k);
}

/**
 * Used to modify the displays string during runtime.
 */
void Txc14::refreshDisplay() const
{
    char buf[40];
    sprintf(buf, "rcvd: %ld sent: %ld", numReceived, numSent);
    getDisplayString().setTagArg("t", 0, buf);
}
