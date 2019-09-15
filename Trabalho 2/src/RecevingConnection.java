import java.net.DatagramPacket;
import java.net.MulticastSocket;

class ReceivingConnection extends Thread {
    private int pid;
    private int numberOfProcesses;
    private VoteTally voteTally;

    private final MulticastSocket multicastSocket;
    private byte[] buffer = new byte[100];

    // Vote counters
    private int counter0 = 0;
    private int counter1 = 0;

    public ReceivingConnection(MulticastSocket _multicastSocket, int _pid, VoteTally _voteTally) {
        multicastSocket = _multicastSocket;
        pid = _pid;
        voteTally = _voteTally;
    }

    public void run() {
        try {
            boolean correctMessageReceived = false;
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(messageIn);

            while(!correctMessageReceived) {
                String senderProcess = new String(messageIn.getData()).trim().split(":")[0];
                String receivedMessage = new String(messageIn.getData()).trim().split(":")[1];

                String message = new String(messageIn.getData()).trim();
                System.out.println(message);

                if(Integer.parseInt(senderProcess) == pid) {
                    System.out.println("Voting for " + receivedMessage);
                    voteTally.voteFor(receivedMessage);
                    correctMessageReceived = true;
                } else {
                    multicastSocket.receive(messageIn);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in thread: " + Thread.currentThread().getName());
            System.out.println("Receiving thread exception: " + e.getMessage());
        }
    }
}