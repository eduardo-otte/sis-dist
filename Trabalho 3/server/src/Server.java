import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        System.out.println("Inicializando servidor");

        try {
            Registry nameServiceRegistry = LocateRegistry.createRegistry(1099);
            nameServiceRegistry.list();

            ServerImpl server = new ServerImpl();
            nameServiceRegistry.bind("server", server);

            System.out.println("Servidor inicializado");
            System.out.println("----------------");
        } catch (RemoteException e) {
            System.out.println("RemoteException ao iniciar o servidor: " + e.getMessage());
        } catch (AlreadyBoundException e) {
            System.out.println("AlreadyBoundException ao inicar o servidor " + e.getMessage());
        }
    }
}
