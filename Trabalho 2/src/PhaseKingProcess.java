import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
    private ArrayList<PublicKey> publicKeys = new ArrayList<PublicKey>();
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private ArrayList<String> publicKeysStrings =  new ArrayList<String>();
    
    private volatile boolean warmUpEnded = false;

    PhaseKingProcess(int _pid, String _value, int _port, String inetAddress,int _numberOfProcesses) {
        pid = _pid;
        value = _value;
        port = _port;
        numberOfProcesses = _numberOfProcesses;
        
        generateKeyPair();
        
     
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
    	warmUp();
        while (!warmUpEnded) { Thread.onSpinWait(); }
    		
		System.out.println(pid+ " sleeping...");
		try {
			TimeUnit.MILLISECONDS.sleep(5000);
		} catch(InterruptedException e){
			System.out.print(e.getMessage()); 
		}
		
		System.out.println(pid + " may begin Phase King protocol");
    	
		/*
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
        */
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
    
    private void sendMyMessage(String myMessage) {
    	byte[] message = myMessage.getBytes();
    	DatagramPacket messageOut = new DatagramPacket(message, message.length, group, port);
        try {
            multicastSocket.send(messageOut);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            //publicKeys.set(pid, publicKey);
        } catch (Exception e) {
            System.out.println("Exception found when generating key pair: " + e.getMessage());
        }
    }
    
    private void warmUp() {
    	/*
    	 * _publicKeys: guarda todas as chaves de processos conhecidos pela tread
    	 * hasKeys: Guarda quais chaves a thread ja conseguiu
    	 * threadsFinished: Quando uma thread consegue todas as chaves, manda um sinal para as demais
    	 * */
    	
    	// Encoder
    	Base64.Encoder encoder = Base64.getEncoder();
    	
    	// Messages
    	String terminated = "terminated";
    	String end = "end";
    	
    	String myKeyMessage = pid+ "-" + encoder.encodeToString(publicKey.getEncoded());//publicKey;
    	String myFinishMessage = pid +"-"+terminated;
    	String myEndMessage = pid +"-"+end;
    	
    	// Keys
    	String[] _publicKeys = new String[numberOfProcesses];
    	_publicKeys[pid] = encoder.encodeToString(publicKey.getEncoded());
    	
    	// Control
    	boolean[] threadsFinished = new boolean[numberOfProcesses];
        Arrays.fill(threadsFinished, false);

    	boolean[] hasKeys = new boolean[numberOfProcesses];
    	Arrays.fill(hasKeys, false);
    	hasKeys[pid] = true;

    	int threadsFinishedCounter = 0;
    	int hasKeysCounter = 1;
    	
    	 // To control the loop
    	boolean terminate = false;
    	boolean canEndEarly = false;
    	boolean endWarmUp = false;
    	boolean timeOut = true;
    	
    	// Send the first message
    	sendMyMessage(myKeyMessage);
    	
    	while(!endWarmUp && !canEndEarly) {
        	byte[] buffer = new byte[1200];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
            	timeOut = true;
            	multicastSocket.setSoTimeout(100);
                multicastSocket.receive(messageIn);
                timeOut = false;
                int senderPid = Integer.parseInt(new String(messageIn.getData()).trim().split("-")[0]);
                
                if(pid != senderPid) {
                	String messageContent = new String(messageIn.getData()).trim().split("-")[1];
                    if(messageContent.equals(terminated)) {
                    	if(!threadsFinished[senderPid]) {
                    		threadsFinished[senderPid] = true;
                    		threadsFinishedCounter++;
                    		if(threadsFinishedCounter == numberOfProcesses) {
                    			endWarmUp=true;	
                    			canEndEarly = true;
                    		}
                    	}
                    } else if(messageContent.equals(end)) {
                    	System.out.println(pid + " got the message to end from " + senderPid);
                    	canEndEarly=true;
                    } else {
                    	sendMyMessage(myKeyMessage); 
                    	if(!hasKeys[senderPid]) {
                    		String newKey = new String(messageIn.getData()).trim().split("-")[1];
                    		_publicKeys[senderPid] = newKey;
                    		hasKeys[senderPid] = true;
                    		hasKeysCounter++;
                    		if(hasKeysCounter == numberOfProcesses) {
                    			terminate = true;
                    			
                    			if(!threadsFinished[pid]) {
                    				threadsFinished[pid] = true;
                        			threadsFinishedCounter++;
                        			
    								if(threadsFinishedCounter == numberOfProcesses) {
    									endWarmUp=true;
    								} 
                    			}
                            }
                    	}
                    }
                    if(terminate) {
                    	sendMyMessage(myFinishMessage);
                    }
                    if(canEndEarly) {
                    	sendMyMessage(myEndMessage);
                    }
                }
            } 
            catch (IOException e) {
                if(timeOut && terminate) {
                	endWarmUp = true;
                }
            }  
        }

        publicKeysStrings.addAll(Arrays.asList(_publicKeys).subList(0, numberOfProcesses));
        
        System.out.println("--------------Processo " + pid + " terminou warmUp ");
        warmUpEnded = true;
    }
}


