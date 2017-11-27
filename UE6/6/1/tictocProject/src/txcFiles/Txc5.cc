/*
 * Txc5.cc
 *
 *  Created on: 25.11.2017
 *      Author: muhamed
 */




#include <string.h>
#include <omnetpp.h>

using namespace omnetpp;

class Txc5 : public cSimpleModule
{
    private:
        int counter; // coutner variable

  protected:
    // The following redefined virtual function holds the algorithm.
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
};
// The module class needs to be registered with OMNeT++
Define_Module(Txc5);

void Txc5::initialize()
{
       counter = par("limit");

    // Initialize is called at the beginning of the simulation.
    // To bootstrap the tic-toc-tic-toc process, one of the modules needs
    // to send the first message. Let this be `tic'.
    // Am I Tic or Toc?

       WATCH(counter);

    if (strcmp("tic", getName()) == 0 || par("sendMsgOnInit").boolValue() == true) {
        // create and send first message on gate "out". "tictocMsg" is an
        // arbitrary string which will be the name of the message object.

        EV << "Send initial message\n";
        cMessage *msg = new cMessage("tictocMsg");
        send(msg, "out");
    }


}
void Txc5::handleMessage(cMessage *msg)
{


    // The handleMessage() method is called whenever a message arrives
    // at the module. Here, we just send it to the other module, through
    // gate `out'. Because both `tic' and `toc' does the same, the message
    // will bounce between the two.

    counter--;

    if (counter == 0) {
        EV << getName()<< "'s counter reached zero, deleting message !!!\n";
        delete msg;

    }
    else {
        EV << getName() << "'s counter is " << counter << ", sending back message\n";
        send(msg, "out");
    }
}
