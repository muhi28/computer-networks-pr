package tictocproject.simulations.nedFiles;

simple Txc2
{
    parameters:
        @display("i=block/routing"); // add a default icon
    gates:
        input in;
        output out;
}
//
// Make the two module look a bit different with colorization effect.
// Use cyan for `tic', and yellow for `toc'.
//
network Tictoc2
{
    @display("bgb=182,191");
    submodules:
        tic: Txc2 {
            parameters:
                @display("i=,cyan"); // do not change the icon (first arg of i=) just colorize it
        }
        toc: Txc2 {
            parameters:
                @display("i=,gold;p=139,150"); // here too
        }
    connections:
        tic.out --> {  delay = 100ms; } --> toc.in;
        tic.in <-- {  delay = 100ms; } <-- toc.out;
}
