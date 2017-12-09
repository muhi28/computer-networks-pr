import java.nio.ByteBuffer;

/** TODO
 * Not implemented yet.
 */
public class Ue1_3 {
    private static ByteBuffer bb;

    public void init(){
        System.out.println("Executing UE1_3.");

        boolean isData, isUrgent;
        int sequenceNumber; // Must be 16 bit
        byte[] payload = new byte[4]; // 32 bit = 32/8 = 4 byte

        isData = true;
        isUrgent = false;
        sequenceNumber = 12341;
        for(byte b = 0; b < payload.length; b++){
            payload[b] = b;
        }


        byte [] message = createMsg(isData, isUrgent, sequenceNumber, payload);
        printMessage(message);
        }

    private void printMessage(byte[] message) {
        byte read;
        bb.rewind();        // Resets position to 0;
        while((read = bb.get()) != -1){
            System.out.print(read);
        }
    }

    private static byte[] createMsg(boolean isData, boolean isUrgent, int sequenceNumber, byte[] payload) throws IllegalArgumentException {
        // TODO
        if(sequenceNumber > (Math.pow(2, 16)-1)){

        }
        if(payload.length == 0){
            // Error
        }

        byte[] msg = new byte[1024];
        bb = ByteBuffer.wrap(msg);

        byte[] tab = "\t".getBytes();
        byte[] nl = "\n".getBytes();

        // Subdata
        byte[] version = {0,0,0,1,0};
        byte[] reserved = {0,0,0,0,0,0,0,0,0};
        byte isDataFlag, isUrgentFlag;
        if(isData){
            isDataFlag = 1;
        }else{
            isDataFlag = 0;
        }
        if(isUrgent){
            isUrgentFlag = 1;
        }else{
            isUrgentFlag = 0;
        }

        // First set of numbers
        for(int i = 0; i < 30; i++){
            bb.putInt((i % 10));
        }

        bb.put(nl);
        addPlusMinus(bb);

        // First information
        bb.put(nl);
        bb.putChar('|');
        bb.put(tab);
        bb.putChar('V');
        bb.putChar('=');
        bb.put(version);
        bb.put(tab);
        bb.put(reserved);
        bb.put(tab);
        bb.put(isDataFlag);
        bb.put(tab);
        bb.put(isUrgentFlag);
        bb.put(tab);
        bb.putInt(sequenceNumber);
        bb.put(nl);

        addPlusMinus(bb);
        bb.put(nl);
        // Second Information
        bb.putChar('|');
        bb.putInt(payload.length);
        bb.put(nl);

        addPlusMinus(bb);
        bb.put(nl);
        // Last Information
        bb.put(payload);
        bb.put(nl);
        addPlusMinus(bb);

        return msg;
    }

    private static void addPlusMinus(ByteBuffer bb){
        for(int i = 0; i < 30; i++){
            bb.putChar('+');
            bb.putChar('-');
        }
        bb.putChar('\n');
    }
}
