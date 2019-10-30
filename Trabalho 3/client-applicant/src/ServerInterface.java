import models.Curriculum;
import models.JobOffering;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    // Search
    ArrayList<JobOffering> searchJobOffering(JobOffering jobOfferingLookup) throws RemoteException;

    ArrayList<Curriculum> searchCurriculum(String areaOfInterest) throws RemoteException;

    // Registration
    void registerCurriculum(Curriculum curriculum) throws RemoteException;

    // Alter registration
    void alterCurriculum(Curriculum newCurriculum) throws RemoteException;

    // Subscriptions
    void subscribeForJobOfferings(ApplicantClientInterface clientRef, String areaOfInterest) throws RemoteException;
}
