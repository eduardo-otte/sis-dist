import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class PhaseKingProcess extends Thread {
    private int pid;
    private int numberOfProcesses;
    private ArrayList<Integer> knowProcesses;
    private VoteTally voteTally;

    // Connection
    private int port;
    private MulticastSocket multicastSocket;
    private InetAddress group;

    // Message
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
        numberOfProcesses = publicKeys.size();

        // TODO: Implement proper warm-up process
        Integer[] pidArray = {0, 1, 2, 3, 4};
        knowProcesses = new ArrayList<>(Arrays.asList(pidArray));

        try {
            group = InetAddress.getByName(inetAddress);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void run() {
    	//WarmUp
    	warmUp(publicKeys.get(pid),numberOfProcesses);
        int numberOfFaults = numberOfProcesses / 4;
        for(int phase = 0; phase < numberOfFaults + 1; phase++) {
            voteTally = new VoteTally();

            // First round
            System.out.println("Begin first round");
            for(int i : knowProcesses) {
                if(i == pid) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        voteTally.voteFor(value);
                        sendMessage(value);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    receiveFirstRoundVote(i);
                }
            }

            String mostVoted = voteTally.getMostVoted();
            System.out.println("Voting finished");
            System.out.println("Most voted: " + mostVoted + " : " + voteTally.getVotesFor(mostVoted));

            // Second round
            System.out.println("Begin second round");
            if(phase == pid) {
                sendPhaseKingDecision();
            } else {
                receivePhaseKingDecision();
            }
        }

        System.out.println("Decision for process with id " + pid + " : " + value);
    }

    private void receiveFirstRoundVote(int processId) {
        try {
            boolean correctMessageReceived = false;
            byte[] buffer = new byte[100];

            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(messageIn);

            while(!correctMessageReceived) {
                String senderProcess = new String(messageIn.getData()).trim().split(":")[0];
                String receivedMessage = new String(messageIn.getData()).trim().split(":")[1];

                String message = new String(messageIn.getData()).trim();
                System.out.println(message);

                if(Integer.parseInt(senderProcess) == processId) {
                    System.out.println("Voting for " + receivedMessage);
                    voteTally.voteFor(receivedMessage);
                    correctMessageReceived = true;
                } else {
                    multicastSocket.receive(messageIn);
                }
            }
        } catch (Exception e) {
            System.out.println("Message receiving exception: " + e.getMessage());
        }
    }

    private void sendPhaseKingDecision() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Phase king sending exception: " + e.getMessage());
        }

        String mostVoted = voteTally.getMostVoted();
        int mostVotes = voteTally.getVotesFor(mostVoted);

        //TODO: Turn into object member
        int numberOfFaults = numberOfProcesses/4;

        //TODO: Implement signature by encryption
        if(mostVotes > numberOfProcesses/2 + numberOfFaults) {
            value = mostVoted;
        }
        sendMessage(value);
    }

    private void receivePhaseKingDecision() {
        byte[] buffer = new byte[100];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

        //TODO: Implement signature by encryption
        try {
            multicastSocket.receive(messageIn);
            value = new String(messageIn.getData()).trim().split(":")[1];
        } catch (IOException e) {
            System.out.println("Exception while receiving phase king decision: " + e.getMessage());
        }
    }

    private void sendMessage(String messageValue) {
        String messageString = pid + ":" + messageValue;
        byte[] message = messageString.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(message, message.length, group, port);
        try {
            multicastSocket.send(datagramPacket);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void sendMyKey(String myMessage) {
    	byte[] message = myMessage.getBytes();
    	DatagramPacket messageOut = new DatagramPacket(message, message.length, group, port);
        try {
            multicastSocket.send(messageOut);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void warmUp(PublicKey myPublicKey, int _numberOfProcesses) {
    	/*
    	 * _publicKeys guarda todas as chaves de processos conhecidos pela tread
    	 * threadsFinished e hasKeys sao usados para o controle das chaves da thread atual e 
    	 * para verificar se as outras threads ja receberam todas as chaves
    	 * */
    	String terminated = "terminated";
    	String myMessage = pid+ "-" + publicKeys.get(pid);
    	String finishMessage = pid +"-"+terminated;
    	
    	
    	String[] _publicKeys = new String[_numberOfProcesses];
    	_publicKeys[pid] = myPublicKey.toString();
    	
    	boolean[] threadsFinished = new boolean[_numberOfProcesses]; //if a thread has all keys, send a signal to the others
    	boolean[] hasKeys = new boolean[_numberOfProcesses]; 
    	for (int i=0; i<_numberOfProcesses; i++) {
    		threadsFinished[i] = false;
    		hasKeys[i] = false;
    	}
    	hasKeys[pid] = true;
    	int threadsFinishedCounter = 0; //how many thread has all keys
    	int hasKeysCounter = 1; //how many keys the thread has
    	boolean end = false; // to end the function
    	
    	sendMyKey(myMessage);
    	
        while(!end) { //leave just when every thread has all keys
        	byte[] buffer = new byte[100];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                multicastSocket.receive(messageIn);
                int _pid = Integer.parseInt(new String(messageIn.getData()).trim().split("-")[0]);
                String rest = new String(messageIn.getData()).trim().split("-")[1];
                if(rest.equals(terminated)) { 	//Some thread has all the keys
                	if(threadsFinished[_pid] == false) {
                		threadsFinished[_pid] = true;
                		threadsFinishedCounter++;
                		if(threadsFinishedCounter == _numberOfProcesses) {
                    		end=true;
                    	}
                	}
                	System.out.println(pid+ " tem " + threadsFinishedCounter + " processos terminados:");
                }
                
                else {	//some thread sent its key
                	if(_pid!=pid) {
                		sendMyKey(myMessage);
                	}
                	if(hasKeys[_pid] == false) {
                		String newkey = new String(messageIn.getData()).trim().split("-")[1];
                		_publicKeys[_pid] = newkey;
                		hasKeys[_pid] = true;
                		hasKeysCounter++;
                		System.out.println(pid +" got key from "+_pid);
                		System.out.println(pid +" has " +hasKeysCounter + " keys ");
                		if(hasKeysCounter == _numberOfProcesses) {
                			sendMyKey(finishMessage);
                			System.out.println(pid +" enviou terminated");
                    	}
                	}
                	
                }
            } 
            catch (IOException e) {
                System.out.println("Exception while receiving key decision: " + e.getMessage());
            }  
        }
        System.out.print("Processo" + pid + "tem as chaves: ");
        for (int i=0; i<_numberOfProcesses; i++) {
        	System.out.print(i+"-"+_publicKeys[i] );
    	}
    }
}


