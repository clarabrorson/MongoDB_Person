import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
public class KeyReader {
    // Deklarera en variabel för att lagra Properties-objektet
    Properties prop;

    public KeyReader(String file) {
        // Skapa ett nytt Properties-objekt
        prop = new Properties();
        // Hämta sökvägen till användarens hemkatalog
        String userHome = System.getProperty("user.home");

        String filename="/Documents/MongoDB/" + file + ".txt"; // if filena does not exist, create it
        File newfile = new File("/Documents/MongoDB/" + file + ".txt");
        if (!newfile.exists()) {
            try {
                // skapa standardproperties i minnet (så Marcus slipper krångla med filen :D )
                prop.setProperty("connectionString", "mongodb://localhost:27017");
                prop.setProperty("database", "test");
                prop.setProperty("collection", "people");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // Avsluta konstruktorn
                return;
            }
        }
    }
    public String getKey() {

        return prop.getProperty("connectionString");
    }
}
