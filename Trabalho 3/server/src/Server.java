import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        System.out.println("Inicializando servidor");

        try {
            Registry nameServiceRegistry = LocateRegistry.createRegistry(1092);
            nameServiceRegistry.list();

            ServerImpl server = new ServerImpl();
            nameServiceRegistry.bind("Server", server);

            System.out.println("Servidor inicializado");
        } catch (Exception e) {
            System.out.println("Exceção ao iniciar o servidor: " + e.getMessage());
        }
    }
}
