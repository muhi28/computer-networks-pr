import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 */
public class Exec {
    public static void main(String[]args) throws Exception {
        int port = 6789;

        fetchLocalHost();
        POP3Server p3s = new POP3Server(port);
        p3s.run();
    }

    /**
     * @return Host-IP
     * @throws UnknownHostException
     */
    public static void fetchLocalHost() throws Exception{
        System.out.println("Server running on:");
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

        for(NetworkInterface netInt : Collections.list(nets)){
            System.out.printf("\tDisplay Name: %s\n", netInt.getDisplayName());
            System.out.printf("\tName: %s\n", netInt.getName());

            Enumeration<InetAddress> iAddr = netInt.getInetAddresses();
            for(InetAddress iA : Collections.list(iAddr)){
                System.out.printf("\tInetAddress: %s\n", iA);
            }
            System.out.println();
        }
    }
}
