import java.security.*;
import java.util.ArrayList;

public class PhaseKingCoordinator {
    private final int numberOfProcesses = 5;

    private ArrayList<PrivateKey> privateKeys = new ArrayList<>();
    private ArrayList<PublicKey> publicKeys = new ArrayList<>();

    private ArrayList<PhaseKingProcess> processes = new ArrayList<>();

    public void begin() {
        generateKeyPairs();

        for(int i = 0; i < numberOfProcesses; i++) {
            processes.add(new PhaseKingProcess(i, "1", 6789, "228.5.6.7",
                    publicKeys, privateKeys.get(i)));
        }

        for(PhaseKingProcess p : processes) {
            p.start();
        }
    }

    private void generateKeyPairs() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(1024, secureRandom);

            for(int i = 0; i < numberOfProcesses; i++) {
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                privateKeys.add(keyPair.getPrivate());
                publicKeys.add(keyPair.getPublic());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
