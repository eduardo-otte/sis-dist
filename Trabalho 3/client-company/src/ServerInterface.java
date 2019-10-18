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

    void registerJobOffering(JobOffering jobOffering) throws RemoteException;

    // Alter registration
    void alterCurriculum(Curriculum newCurriculum) throws RemoteException;

    void alterJobOffering(JobOffering newJobOffering) throws RemoteException;

    // Subscriptions
    void subscribeForJobApplicants(CompanyClientInterface clientRef, String jobArea) throws RemoteException;
}
