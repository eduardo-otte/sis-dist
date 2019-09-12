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
    private PublicKey publicKey;
    private ArrayList<String> newPublicKeys =  new ArrayList<String>();
    
    private boolean warmUpEnded = false;

    PhaseKingProcess(int _pid, String _value, int _port, String inetAddress, ArrayList<PublicKey> _publicKeys, PrivateKey _privateKey) {
        pid = _pid;
        value = _value;
        port = _port;
        publicKeys = _publicKeys;
        privateKey = _privateKey;
        publicKey = _publicKeys.get(_pid);
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
    	//System.out.println(pid+ ": size of my public key: " + publicKey.toString().length());
    	//System.out.println(pid+ ": my public key: " + publicKey.toString()+"--------------------");
    	
    	//WarmUp
    	warmUp();
    	while(!warmUpEnded) {}
    	System.out.println(">>>>>>>>Processo " + pid + " terminou warmUp com as chaves ");
    	//for (int i=0; i<numberOfProcesses; i++) {
    		//System.out.println("\t"+i+"----->"+newPublicKeys.get(i) );
    	//}
    	//System.out.println("***********************");
    	
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
    
    private void warmUp() {
    	/*
    	 * _publicKeys: guarda todas as chaves de processos conhecidos pela tread
    	 * hasKeys: Guarda quais chaves a thread ja conseguiu
    	 * threadsFinished: Quando uma thread consegue todas as chaves, manda um sinal para as demais
    	 * */
    	
    	//messagesto end the function
    	String terminated = "terminated";
    	String end = "end";
    	String myKeyMessage = pid+ "-" + publicKey;
    	String myFinishMessage = pid +"-"+terminated;
    	String myEndMessage = pid +"-"+end;
    	
    	//keys
    	String[] _publicKeys = new String[numberOfProcesses];
    	_publicKeys[pid] = publicKey.toString();
    	
    	//controll
    	boolean[] threadsFinished = new boolean[numberOfProcesses]; //if a thread has all keys, send a signal to the others
    	boolean[] hasKeys = new boolean[numberOfProcesses]; 
    	for (int i=0; i<numberOfProcesses; i++) {
    		threadsFinished[i] = false;
    		hasKeys[i] = false;
    	}
    	hasKeys[pid] = true;
    	int threadsFinishedCounter = 0; 	//how many thread has all keys
    	int hasKeysCounter = 1; 			//how many keys the thread has
    	
    	 // to controll the loop
    	boolean terminate = false;
    	boolean canEndEarly = false;
    	boolean endWarmUp = false;
    	//Send the first message
    	sendMyMessage(myKeyMessage);
    	
        while(!endWarmUp && !canEndEarly) { 		//leave just when every thread has all keys
        	byte[] buffer = new byte[1200];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
            	
                multicastSocket.receive(messageIn);
                int _pid = Integer.parseInt(new String(messageIn.getData()).trim().split("-")[0]);
                //System.out.println(pid+ " is waiting");
                if(pid!=_pid) { 			//just verify the message if it's not from the current thread
                	String rest = new String(messageIn.getData()).trim().split("-")[1]; //terminate, end or key
                    if(rest.equals(terminated)) { 				//When some thread has all the keys
                    	if(threadsFinished[_pid] == false) {	//update list of finished threads
                    		threadsFinished[_pid] = true;
                    		threadsFinishedCounter++;
                    		if(threadsFinishedCounter == numberOfProcesses) {	//every task has all keys
                    			System.out.println(pid+ " got all signals and will send the message to end");
                    			endWarmUp=true;	
                    			canEndEarly = true;
                    		}
                    	}
                    }
                    else if(rest.equals(end)) { 	//Got the signal that all threads have all keys
                    	System.out.println(pid+ " got the message to end from "+_pid);
                    	canEndEarly=true;
                    }
                    else {	//some thread sent its key
                    	sendMyMessage(myKeyMessage); 
                    	if(hasKeys[_pid] == false) {	//if this thread doesn't has the key, it will save it and increment the counter 
                    		String newkey = new String(messageIn.getData()).trim().split("-")[1];
                    		_publicKeys[_pid] = newkey;
                    		hasKeys[_pid] = true;
                    		hasKeysCounter++;
                    		if(hasKeysCounter == numberOfProcesses) {
                    			System.out.println(pid +" terminated");
                    			terminate = true;
                    			threadsFinished[pid] = true;
                    			threadsFinishedCounter++;
                    			
								/*
								 * if(threadsFinishedCounter == numberOfProcesses) { //every task has all keys
								 * canEndEarly=true; }
								 */
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
                System.out.println("Exception while receiving key decision: " + e.getMessage());
            }  
        }
        
        for (int i=0; i<numberOfProcesses; i++) {
        	newPublicKeys.add(_publicKeys[i]);
    	}
        int limite = 100;
        for (int i=0; i<limite; i++) {
        	sendMyMessage(myEndMessage); //so p garantir...
    	}
        
        warmUpEnded = true;
        
    	//System.out.println("--------------Processo " + pid + " terminou warmUp ");

    }
}


