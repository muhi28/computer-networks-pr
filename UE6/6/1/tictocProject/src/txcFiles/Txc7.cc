/*
 * Txc7.cc
 *
 *  Created on: 25.11.2017
 *      Author: muhamed
 */



#include <stdio.h>
#include <string.h>
#include <omnetpp.h>

using namespace omnetpp;

class Txc7 : public cSimpleModule
{
  private:
    cMessage *event;
    cMessage *tictocMsg;

  protected:
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
};

Define_Module(Txc7);



void Txc7::initialize()
{
    event = new cMessage("event");
    tictocMsg = nullptr;
    if (strcmp("tic", getName()) == 0) {
        EV << "Scheduling first send to t=5.0s\n";
        scheduleAt(5.0, event);
        tictocMsg = new cMessage("tictocMsg");
    }
}
void Txc7::handleMessage(cMessage *msg)
{
    if (msg == event) {
        EV << "Wait period is over, sending back message\n";
        send(tictocMsg, "out");
        tictocMsg = nullptr;
    }
    else {
        // "Lose" the message with 0.1 probability:
        if (uniform(0, 1) < 0.1) {
            EV << "\"Losing\" message\n";
            delete msg;
        }
        else {
            // The "delayTime" module parameter can be set to values like
            // "exponential(5)" (tictoc7.ned, omnetpp.ini), and then here
            // we'll get a different delay every time.
            simtime_t delay = par("delayTime");
            EV << "Message arrived, starting to wait " << delay << " secs...\n";
            tictocMsg = msg;
            scheduleAt(simTime()+delay, event);
        }
    }
}

