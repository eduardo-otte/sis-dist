import models.Curriculum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicantClient {
    public static void main(String[] args) {
        System.out.println("Inicializando cliente de empregado");

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ServerInterface serverInterface = (ServerInterface) registry.lookup("server");

            ApplicantClientImpl client = new ApplicantClientImpl();

            System.out.println("Cliente inicializado");

            Curriculum curriculum = new Curriculum("Euzin", "Somente humano", "Todas caraio",
                    10, 10000);

            System.out.println("Currículo criado");
            serverInterface.registerCurriculum(curriculum);
        } catch (RemoteException e) {
            System.out.println("RemoteException ao iniciar o cliente: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException ao iniciar o cliente: " + e.getMessage());
        }
    }
}
