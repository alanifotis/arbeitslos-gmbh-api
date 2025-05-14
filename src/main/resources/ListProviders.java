import java.security.Provider;
import java.security.Security;

public class ListProviders {
    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider.getName() + ": " + provider.getInfo());
        }
    }
}