/*
 * SimpleNet.cc
 *
 *  Created on: Nov 26, 2017
 *      Author: thompson
 */

#include <string.h>
#include <omnetpp.h>

using namespace omnetpp;

class servernode:public cSimpleModule
{
protected:
    virtual void handleMessage(cMessage *msg) override;
};

class clientnode:public cSimpleModule
{
    virtual void initialize() override;
    virtual void handleMessage(cMessage *msg) override;
};

Define_Module(servernode);
Define_Module(clientnode);

void servernode::handleMessage(cMessage *msg)
{
    send(new cMessage("Bar"), "out");
}

void clientnode::initialize()
{
    send(new cMessage("Foo"), "out");
}

void clientnode::handleMessage(cMessage *msg)
{
    send(new cMessage("Foo"), "out");
}
