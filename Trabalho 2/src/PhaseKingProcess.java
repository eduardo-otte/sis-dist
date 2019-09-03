import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class PhaseKingProcess extends Thread {
    private int pid;

    // Connection
    private int port;
    private MulticastSocket multicastSocket;
    private InetAddress group;

    // Message
    private byte[] buffer = new byte[1000];
    private final byte arrayFiller = 0;
    private String value;

    // Encryption
    private ArrayList<PublicKey> publicKeys;
    private PrivateKey privateKey;

    PhaseKingProcess(int _pid, String _value, int _port, String inetAddress, ArrayList<PublicKey> _publicKeys, PrivateKey _privateKey) {
        pid = _pid;
        value = _value;
        port = _port;
        publicKeys = _publicKeys;
        privateKey = _privateKey;

        try {
            group = InetAddress.getByName(inetAddress);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void phaseKing() {

        int numberOfFaults = publicKeys.size() / 4;
        for(int phase = 0; phase < numberOfFaults + 1; phase++) {
            // Primeira rodada
            sendMessage();
        }
    }

    private void sendMessage() {
        byte[] message = value.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(message, message.length, group, port);
        try {
            multicastSocket.send(datagramPacket);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
