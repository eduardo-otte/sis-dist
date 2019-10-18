import models.JobOffering;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ApplicantClientInterface extends Remote {
    void subscriptionCallback(JobOffering jobOffering) throws RemoteException;
}
