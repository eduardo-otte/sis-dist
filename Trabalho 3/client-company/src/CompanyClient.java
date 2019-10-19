import models.JobOffering;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CompanyClient {
    public static void main(String[] args) {
        System.out.println("Inicializando cliente de companhia");

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("server");

            CompanyClientImpl client = new CompanyClientImpl();

            System.out.println("Cliente inicializado");

            JobOffering jobOffering = new JobOffering("Blabla", "blablabla", "casa do caralho", 10, 10000);
            System.out.println("Oferta de emprego criada");
            serverInterface.registerJobOffering(jobOffering);
        } catch (RemoteException e) {
            System.out.println("RemoteException ao iniciar o cliente: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException ao iniciar o cliente: " + e.getMessage());
        }
    }
}
