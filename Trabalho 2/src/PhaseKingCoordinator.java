import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PhaseKingCoordinator {
    private final int numberOfProcesses = 5;
    private ArrayList<PhaseKingProcess> processes = new ArrayList<>();

    private String[] values = {"1","0","1","1","1"};
    
    public void begin() {
        for(int i = 0; i < numberOfProcesses; i++) {
        	
            processes.add(new PhaseKingProcess(i, values[i], 6789, "228.5.6.7", 5));
        }

        try {
            for(PhaseKingProcess p : processes) {
                TimeUnit.MILLISECONDS.sleep(200);
                p.start();
            }
        } catch (Exception e) {
            System.out.println("Thread starting interval exception: " + e.getMessage());
        }
    }
}