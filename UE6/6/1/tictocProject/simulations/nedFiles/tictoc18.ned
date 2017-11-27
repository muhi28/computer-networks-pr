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

package tictocproject.simulations.nedFiles;

simple Txc18
{
    parameters:
        @signal[arrival](type="long");
        @statistic[hopCount](title="hop count"; source="arrival"; record=vector,stats; interpolationmode=none);
        @display("i=block/routing");
    gates:
        inout gate[];
}

network TicToc18
{
    parameters:
        int numCentralNodes = default(2);
    types:
        channel Channel extends ned.DelayChannel {
            delay = 100ms;
        }
    submodules:
        tic[numCentralNodes+4]: Txc18;
    
    connections:
        // connect the 2 nodes in one side to  central nodes
        tic[0].gate++ <--> Channel <--> tic[2].gate++;
        tic[1].gate++ <--> Channel <--> tic[2].gate++;
        // connect central nodes together
        for i=2..numCentralNodes+1 {
            tic[i].gate++ <--> Channel <--> tic[i+1].gate++;
        }
        // connect the 2 nodes on other side to central nodes
        tic[numCentralNodes+2].gate++ <--> Channel <--> tic[numCentralNodes+1].gate++;
        tic[numCentralNodes+3].gate++ <--> Channel <--> tic[numCentralNodes+1].gate++;
        
        
}