import models.Curriculum;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CompanyClientInterface extends Remote {
    void subscriptionCallback(Curriculum curriculum) throws RemoteException;
}
