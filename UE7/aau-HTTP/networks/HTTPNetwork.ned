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

package networks;

import src.app.HTTPClient;
import src.app.HTTPServer;

network HTTPNetwork
{
        @display("bgb=460,100");
    submodules:
        // TODO 2
        HTTPClient: HTTPClient {
            @display("p=75,50");
        }
        HTTPServer: HTTPServer {

            @display("p=375,50");
        }
    connections:
        // TODO 3
        HTTPClient.toLowerLayer --> {delay = 200ms;} --> HTTPServer.fromLowerLayer;
        HTTPServer.toLowerLayer --> {delay = 200ms;} --> HTTPClient.fromLowerLayer;
}
