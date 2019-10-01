import java.io.IOException;
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

    PhaseKingProcess(int _pid, String _value, int _port, String inetAddress,int _numberOfProcesses) {
        pid = _pid;
        value = _value;
        port = _port;
        numberOfProcesses = _numberOfProcesses;
        numberOfFaults = numberOfProcesses / 4;
        
        generateKeyPair();

        try {
            group = InetAddress.getByName(inetAddress);
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        
    }

    /**
     * Inicia a thread. Contém implementação do protocolo Phase King
     */
    public void run() {
    	warmUp();
        decodePublicKeys();

		// Intervalo para manter sincronização entre as threads
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch(InterruptedException e){
			System.out.print(e.getMessage()); 
		}
		
		System.out.println(pid + " may begin Phase King protocol");

		// Inicio do Phase King
        for(int phase = 0; phase < numberOfFaults + 1; phase++) {
        	cleanBuffer();
            voteTally = new VoteTally();

            // First round
            System.out.println(pid + ": Begin first round");
            for(int i : knownProcesses) {
                if(i == pid) {
                    try {
                        System.out.println("Process " + pid + " sending vote: " + value);
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
                sendPhaseKingDecision();
            } else {
                receivePhaseKingDecision(phase);
            }
            cleanBuffer();
        }

        System.out.println("Decision for process with id " + pid + " : " + value);
    }

    /**
     * Faz a recepção da mensagem multicast que contém o voto de primeira rodada do processo especificado
     * @param processId ID do processo que espera-se ser autor da mensagem
     */
    private void receiveFirstRoundVote(int processId) {
        try {
            boolean correctMessageReceived = false;
            byte[] buffer = new byte[100];

            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(messageIn);

            // Laço que garante que a mensagem recebida veio do processo correto
            while(!correctMessageReceived) {
                String senderProcess = new String(messageIn.getData()).trim().split(":")[0];
                String receivedMessage = new String(messageIn.getData()).trim().split(":")[1];
                int senderProcessId = Integer.parseInt(senderProcess);

                if(senderProcessId == processId) {
                    System.out.println(pid + ": Voting for " + receivedMessage);
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

    /**
     * Envia a decisão tomada pelo Phase King, assinando a mensagem
     */
    private void sendPhaseKingDecision() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println("Phase king sending exception: " + e.getMessage());
        }

        // Decide qual valor enviar de acordo com as regras do protocolo
        String mostVoted = voteTally.getMostVoted();
        int mostVotes = voteTally.getVotesFor(mostVoted);

        if(mostVotes > numberOfProcesses/2 + numberOfFaults) {
            value = mostVoted;
        }

        try {
            // Gera assinatura
        	Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(value.getBytes());
            byte[] messageSignature = signature.sign();

            // Envia mensagem no formato pid:valor:assinatura
            String messageSignatureString = Base64.getEncoder().encodeToString(messageSignature);
            String message = value + ":" + messageSignatureString;
            sendMessage(message);
            System.out.println(pid + ": Phase King decision signed successfully");
        } catch (Exception e) {
            System.out.println("Exception thrown when signing Phase King decision: " + e.getMessage());
        }
    }

    /**
     * Recebe a decisão tomada pelo Phase King, e verifica sua assinatura
     * @param phaseKingPid ID do processo que é o atual Phase King
     */
    private void receivePhaseKingDecision(int phaseKingPid) {
        byte[] buffer = new byte[10000];
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

        try {
            multicastSocket.receive(messageIn);

            int senderPid = Integer.parseInt(new String(messageIn.getData()).trim().split(":")[0]);
            String messageContent = new String(messageIn.getData()).trim().split(":")[1];
            String messageSignatureString = new String(messageIn.getData()).trim().split(":")[2];

            byte[] messageSignature = Base64.getDecoder().decode(messageSignatureString);
            String mostVoted = voteTally.getMostVoted();
           
            if(phaseKingPid != senderPid) {
                throw new Exception("Process " + pid + " received Phase King decision with unexpected pid");
            }

            // Verifica assinatura
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKeys.get(phaseKingPid));
            signature.update(messageContent.getBytes());
            if(signature.verify(messageSignature)) {
                System.out.println("Process " + pid + " verified signature for received Phase King decision");
                if(voteTally.getVotesFor(mostVoted) > (numberOfProcesses / 2) + numberOfFaults) {
                    value = mostVoted;
                } else {
                    value = messageContent;
                }
            } else {
                throw new Exception("Process " + pid + " failed to verify signature for received Phase King decision");
            }
        } catch (Exception e) {
            System.out.println("Exception while receiving phase king decision: " + e.getMessage());
        }
    }

    /**
     * Encapsulamento do envio da mensagem. Envia uma mensagem por comunicação multicast, inserindo o ID do processo
     * no início
     * @param messageValue Mensagem a ser enviada
     */
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

    /**
     * Gera par de chaves público/privado para o processo
     */
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            System.out.println("Exception found when generating key pair: " + e.getMessage());
        }
    }

    /**
     * Transforma as chaves públicas dos outros processos recebidas no formato Base64 e as instancia
     */
    private void decodePublicKeys() {
        KeyFactory keyFactory;

        try {
            keyFactory = KeyFactory.getInstance("RSA");

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

    /**
     * Recebe a chave pública de outro processo, no formato Base64, e salva em uma lista
     * @param senderPid ID do processo que espera-se enviar sua chave
     */
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

    /**
     * Realiza o processo de conhecimento e troca de chaves públicas entre os processos
     */
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

    /**
     * Limpa o buffer de recepção de mensagens multicast. Evita que as threads leiam suas próprias mensagens.
     */
    private void cleanBuffer() {
    	try {
            byte[] buffer = new byte[1000];
            multicastSocket.setSoTimeout(300);
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            multicastSocket.receive(messageIn);
        } catch (Exception e) {
    	    if(e.getClass().equals(IOException.class)) {
                System.out.println("Exception while cleaning buffer, part 1: " + e.getMessage());
            }
		}
    	try {
    		multicastSocket.setSoTimeout(0);
    		TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
		    System.out.println("Exception while cleaning buffer, resetting timeout: " + e.getMessage());
		}
    	
	}
}
