
package UE6.Ex2;

simple servernode
{
    gates:
        input in;
        output out;
}

simple clientnode
{
    gates:
        input in;
        output out;
}

channel connection extends ned.DatarateChannel
{
 	datarate = 10 Mbps;
 	delay = 500 ms;   
}

network SimpleNet
{
    @display("bgb=434,172");
    submodules:
        server: servernode {
            @display("i=device/server");
        }
        client: clientnode {
            @display("i=device/pc4;p=356,131");
        }
    connections:
        server.out --> connection --> client.in;
        client.out --> connection --> server.in;
}
