import models.JobOffering;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CompanyClient {
    public static void main(String[] args) {
        System.out.println("Inicializando cliente de companhia");

        try {
            Registry registry = LocateRegistry.getRegistry(1092);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("Server");

            CompanyClientImpl client = new CompanyClientImpl();

            System.out.println("Cliente inicializado");

            JobOffering jobOffering = new JobOffering("Blabla", "blablabla", "casa do caralho", 10, 10000);
            serverInterface.registerJobOffering(jobOffering);
        } catch (Exception e) {
            System.out.println("Exceção ao iniciar o cliente: " + e.getMessage());
        }
    }
}
