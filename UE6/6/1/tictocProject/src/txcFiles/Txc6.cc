/*
 * Txc6.cc
 *
 *  Created on: 25.11.2017
 *      Author: muhamed
 */



#include <string.h>
#include <omnetpp.h>

using namespace omnetpp;

class Txc6 : public cSimpleModule
{
    private:
        cMessage *event;
        cMessage *tictocMsg;

  protected:
    // The following redefined virtual function holds the algorithm.
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
};
// The module class needs to be registered with OMNeT++
Define_Module(Txc6);

void Txc6::initialize()
{
        //Create the event object  -> just any message
       event = new cMessage("event");

       tictocMsg = nullptr; // not tictoc message yet



    if (strcmp("tic", getName()) == 0 || par("sendMsgOnInit").boolValue() == true) {

        //We don't start right away, but instead send an message to ourselves
        // (a "self-message") -- we'll do the first sending when it arrives
        // back to us, at t=5.0s simulated time.
        EV << "Scheduling first send to t=5.0s\n";
        tictocMsg = new cMessage("tictocMsg");
        scheduleAt(5.0, event);
    }


}
void Txc6::handleMessage(cMessage *msg)
{


    // There are several ways of distinguishing messages, for example by message
        // kind (an int attribute of cMessage) or by class using dynamic_cast
        // (provided you subclass from cMessage). In this code we just check if we
        // recognize the pointer, which (if feasible) is the easiest and fastest
        // method.
        if (msg == event) {

            //If the self-message arrived, we can start to send tictocMsg.

            EV << "Wait period is over, sending back message\n";
            send(tictocMsg, "out");
            tictocMsg = nullptr;
        }
        else {

            // If the message we receives is not out self-message, then it must be the
            // message from out partner.We remember its pointer in the tictocMsg variable,
            // then schedule our self-message to come back to us in 1s simulated time.


            EV << "Message arrived, starting to wait 10 sec...\n";
            tictocMsg = msg;
            scheduleAt(simTime()+10.0, event);
        }
}
