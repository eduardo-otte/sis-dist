import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class PhaseKingProcess extends Thread {
    private int pid;
    private int numberOfProcesses;
    private int numberOfFaults;
    private ArrayList<Integer> knownProcesses = new ArrayList<>();
    private VoteTally voteTally;

    // Connection
    private int port;
    private MulticastSocket multicastSocket;
    private InetAddress group;

    // Message
    private String value;
        
    // Encryption
    private ArrayList<PublicKey> publicKeys = new ArrayList<>();
    private ArrayList<String> publicKeysStrings =  new ArrayList<>();
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PrivateKey newPrivateKey;

    PhaseKingProcess(int _pid, String _value, int _port, String inetAddress,int _numberOfProcesses) {
        pid = _pid;
        value = _value;
        port = _port;
        numberOfProcesses = _numberOfProcesses;
        numberOfFaults = numberOfProcesses/4;
        
        generateKeyPair();

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

        decodePublicKeys();

		System.out.println(pid+ " sleeping...");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch(InterruptedException e){
			System.out.print(e.getMessage()); 
		}
		
		System.out.println(pid + " may begin Phase King protocol");

    	int numberOfFaults = numberOfProcesses / 4;
        for(int phase = 0; phase < numberOfFaults + 1; phase++) {
        	cleanBuffer();
            voteTally = new VoteTally();

            // First round
            System.out.println(pid + ": Begin first round");
            for(int i : knownProcesses) {
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
            
            cleanBuffer();
            
            String mostVoted = voteTally.getMostVoted();
            System.out.println(pid + ": Voting finished");
            System.out.println(pid + ": Most voted: " + mostVoted + " : " + voteTally.getVotesFor(mostVoted));

            // Second round
            System.out.println(pid + ": Begin second round");
            if(phase == pid) {
                sendPhaseKingDecisionAux();
            } else {
                receivePhaseKingDecisionAux(phase);
            }
            cleanBuffer();
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
    	//testPrivateKey();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Phase king sending exception: " + e.getMessage());
        }
        
        String mostVoted = voteTally.getMostVoted();
        int mostVotes = voteTally.getVotesFor(mostVoted);

        if(mostVotes > numberOfProcesses/2 + numberOfFaults) {
            value = mostVoted;
        }

        try {
        	Signature signature = Signature.getInstance("SHA256withDSA");
            signature.initSign(privateKey);
            signature.update(value.getBytes());
            byte[] messageSignature = signature.sign();

            String message = value + ":" + new String(messageSignature);
            sendMessage(message);
            System.out.println(pid+": Phase King decision signed with sucess");
        } catch (Exception e) {
            System.out.println("Exception thrown when signing Phase King decision: " + e.getMessage());
        }
    }

    private void receivePhaseKingDecision(int phaseKingPid) {
        byte[] buffer = new byte[10000];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

        try {
            multicastSocket.receive(messageIn);

            int senderPid = Integer.parseInt(new String(messageIn.getData()).trim().split(":")[0]);
            String messageContent = new String(messageIn.getData()).trim().split(":")[1];
            byte[] messageSignature = new String(messageIn.getData()).trim().split(":")[2].getBytes();
           
            if(phaseKingPid != senderPid) {
                throw new Exception("Process " + pid + " received Phase King decision with unexpected pid");
            }

            Signature signature = Signature.getInstance("SHA256withDSA");
            signature.initVerify(publicKeys.get(phaseKingPid));
            signature.update(messageContent.getBytes());
            if(signature.verify(messageSignature)) {
                System.out.println("Process " + pid + " verified signature for received Phase King decision");
            } else {
                throw new Exception("Process " + pid + " failed to verify signature for received Phase King decision");
            }
            value = messageContent;
        } catch (Exception e) {
            System.out.println("Exception while receiving phase king decision: " + e.getMessage());
        }
    }
    
    private void sendPhaseKingDecisionAux() {
    	//testPrivateKey();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Phase king sending exception: " + e.getMessage());
        }
        
        String mostVoted = voteTally.getMostVoted();
        int mostVotes = voteTally.getVotesFor(mostVoted);

        if(mostVotes > numberOfProcesses/2 + numberOfFaults) {
            value = mostVoted;
        }

        try {
        	String message = value;
            sendMessage(message);
            System.out.println(pid+": Phase King decision signed with sucess");
        } catch (Exception e) {
            System.out.println("Exception thrown when signing Phase King decision: " + e.getMessage());
        }
    }

    private void receivePhaseKingDecisionAux(int phaseKingPid) {
        byte[] buffer = new byte[10000];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

        try {
            multicastSocket.receive(messageIn);

            int senderPid = Integer.parseInt(new String(messageIn.getData()).trim().split(":")[0]);
            String messageContent = new String(messageIn.getData()).trim().split(":")[1];
            
            if(phaseKingPid == senderPid) {
            	value = messageContent;
            }
            
            else {
                throw new Exception("Process " + pid + " received Phase King decision with unexpected pid");
            }
        } catch (Exception e) {
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

    
    private void decodePublicKeys() {
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("DSA", "SUN");

            for(int i = 0; i < publicKeysStrings.size(); i++) {
                String key = publicKeysStrings.get(i);
                byte[] decodedBytes = Base64.getDecoder().decode(key);

                X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
                publicKeys.add(i, keyFactory.generatePublic(spec));
            }
        } catch (Exception e) {
            System.out.println("Exception when decoding public keys: " + e.getMessage());
        }
    }
    
    
    private void receivePublicKey(int senderPid) {
        try {
            boolean correctMessageReceived = false;
            byte[] buffer = new byte[1000];

            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(messageIn);

            while(!correctMessageReceived) {
                String senderProcess = new String(messageIn.getData()).trim().split(":")[0];
                String messageContent = new String(messageIn.getData()).trim().split(":")[1];

                if(Integer.parseInt(senderProcess) == senderPid) {
                    knownProcesses.add(senderPid);
                    publicKeysStrings.add(senderPid, messageContent);
                    correctMessageReceived = true;
                } else {
                    multicastSocket.receive(messageIn);
                }
            }
        } catch (Exception e) {
            System.out.println("Receiving public key exception: " + e.getMessage());
        }
    }

    
    private void warmUp() {
        System.out.println("Process " + pid + " beginning warm up");
        
        try {
            for(int i = 0; i < numberOfProcesses; i++) {
                if(i == pid) {
                    TimeUnit.SECONDS.sleep(1);
                    knownProcesses.add(pid);
                    String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                    publicKeysStrings.add(pid, encodedKey);
                    System.out.println("Process " + pid + " sending public key");
                    sendMessage(publicKeysStrings.get(pid));
                } else {
                    receivePublicKey(i);
                }
            }
        } catch (Exception e) {
            System.out.println("Warm up exception: " + e.getMessage());
        }
        cleanBuffer();
        System.out.println("Process " + pid + " finished warm up");
    }
    
    
    private void cleanBuffer() {
    	
    	System.out.println(pid+ ": cleaning buffer...");
    	
    	try {
			byte[] buffer = new byte[1000];
			multicastSocket.setSoTimeout(100);
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
			multicastSocket.receive(messageIn);	
		}catch (Exception e) {
		    //System.out.println("Exception while receiving key decision: " + e.getMessage());
		}
    	try {
    		multicastSocket.setSoTimeout(0);
    		TimeUnit.SECONDS.sleep(1);
		}catch (Exception e) {
		    //System.out.println("Exception while receiving key decision: " + e.getMessage());
		}
    	
	}
}
