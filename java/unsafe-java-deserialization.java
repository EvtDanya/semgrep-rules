// Клиент, подключающийся к RMI-серверу атакующего
public class VulnerableRMIClient {
    public void connectToServer(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            // Это подключение запускает десериализацию DGC
            // ruleid: unsafe-java-deserialization
            RemoteInterface stub = (RemoteInterface) registry.lookup("service");
        } catch (Exception e) {
            // Десериализация DGC происходит во время обработки исключения
        }
    }
}