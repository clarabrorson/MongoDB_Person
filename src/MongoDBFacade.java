import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
public class MongoDBFacade {
    MongoClient client; //Instans av MongoDB-klienten som används för att ansluta till en databas.
    MongoDatabase db; //Instansen av en databas som används för att kommunicera med databasen.
    MongoCollection<Document> collection; //MongoCollection innehåller dokument som lagras i databasen.
    KeyReader keyReader=new KeyReader("MongoDB"); // SKapa en instans av KeyReader

    String connString = keyReader.getKey(); // MongoDB-anslutningssträngen som definierar plats för databasen.
    String collectionName = "Person"; // Namnet på den samling/dokument som innehåller alla personer
    String databaseName = "PersonDB"; // Namnet på databasen

    // En konstruktor som tar emot anslutningssträngen, databasnamnet och samlingens namn som argument.
    public MongoDBFacade(String connString, String databaseName, String collectionName) {
        this.connString = connString;
        this.collectionName = collectionName;
        this.databaseName = databaseName;
        Connect();
    }
    // En standardkonstruktor som använder standardinställningarna.
    public MongoDBFacade() {

        Connect();
    }

    // Metod som ansluter till databasen och hämtar instanser av databasen och samlingen.
    public void Connect() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build(); // Skapar en instans av MongoDB Server API.

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .serverApi(serverApi)
                .build(); // Skapar inställningarna för MongoDB-klienten.

        try {
            client = MongoClients.create(settings); // Skapar en instans av MongoDB-klienten.
            db = client.getDatabase(databaseName); // Hämtar instansen av databasen.
            collection = db.getCollection(collectionName); // Hämtar instansen av samlingen.
        } catch (Exception ex) {
            System.out.println("Misslyckad anslutning!"); // Skriver ut ett meddelande om anslutningen misslyckades.
            System.out.println(ex.getMessage()); // Skriver ut felet som orsakade anslutningsmisslyckandet.
        }
    }

    // Skapar ett index för att öka sökhastigheten och säkerställa att namnen är unika.
    public void createIndex() {
        collection.createIndex(new Document("name", 1),
                new IndexOptions().unique(true));
    }
    
    // METODER FÖR PERSONER
    // Metod som lägger till en person i samlingen
    public void insertOnePerson(Person person) {
        Document doc = person.toDoc(); // Konverterar personen till ett dokument.
        doc.remove("id"); // Tar bort dokument-ID:t, eftersom det kommer att tilldelas av databasen automatiskt.
        doc.remove("age");

        var find = collection.find(doc); // Söker efter dokumentet i samlingen.
        if (find.first() == null) { // Om dokumentet inte finns i samlingen.
            collection.insertOne(doc); // Lägg till dokumentet i samlingen.
        }
    }

    // Metod som hämtar en person från samlingen baserat på ID.

    public Person FindPerson(Person find) {
        Document doc = find.toDoc();
        doc.remove("_id");
        Document search = collection.find(doc).first(); // Söker efter personen baserat på data
        if(search != null){
            return Person.fromDoc(search); // Konverterar dokumentet till en person.
        }
        return null; // Returnerar null om personen inte finns i databasen.
    }

    public Employee FindEmployee(Employee find) {
        Document doc = find.toDoc();
        doc.remove("_id");
        Document search = collection.find(doc).first(); // Söker efter personen baserat på data
        if (search != null){
        return Employee.fromDoc(search); // Konverterar dokumentet till en person.
             }
            return null; // Returnerar null om personen inte finns i databasen.
    }

    public Person UpdatePerson(Person person){
        Document doc = person.toDoc(); // Konverterar personen till ett dokument.
        Document search = collection.find(doc).first(); // Söker efter personen baserat på dokument-ID:t.
        if(search !=null){ //Kontrollerar om personen finns i databasen
            collection.replaceOne(new Document("id", search.getObjectId("_id")), doc);
        }
        return person;
    }

    // Tar bort en person från samlingen baserat på ID.
    public void DeletePerson(String id) {
        Document doc = new Document("id", id); // Skapar ett dokument med ID:t för att söka efter personen.
        collection.deleteOne(doc); // Tar bort personen från samlingen.
    }

    // Söker efter alla personer som matchar namnet och returnerar en lista med personer.
    public ArrayList<Person> Find(String name) {
        Document doc = new Document("name", name); // Skapar ett dokument med personens namn för att söka efter alla dokument som matchar namnet.
        FindIterable<Document> result = collection.find(doc); // Söker efter alla dokument som matchar namnet.
        ArrayList<Person> people = new ArrayList<>(); // Skapar en tom lista med personer.
        // Itererar över alla dokument som matchar namnet och konverterar varje dokument till en person.
        result.forEach(persDoc -> people.add(Person.fromDoc(persDoc)));

        return people; // Returnerar listan med personer.
    }
    // METODER FÖR EMPLOYEES
    public void insertOneEmployee(Employee employee) {
        //Document doc = employee.toDoc(); // Konverterar employeen till ett dokument.
        Document doc = new Document("name", employee.getName());
        //doc.remove("_id"); // Tar bort dokument-ID:t, eftersom det kommer att tilldelas av databasen automatiskt.

        var find = collection.find(doc); // Söker efter dokumentet i samlingen.
        if (find.first() == null) { // Om dokumentet inte finns i samlingen.
            doc = employee.toDoc();
            doc.remove("_id");
            collection.insertOne(doc); // Lägg till dokumentet i samlingen.
        }
    }
    public Employee findOneEmployee(String employeeId){
        // Skapa en sökning med sökvillkoret att dokumentet måste ha rätt employeeId.
        var query = new Document("employeeId", employeeId);
        // Sök efter dokumentet i samlingen och konvertera resultatet till en Employee-objekt.
        var doc = collection.find(query).first();
        return doc != null ? new Employee(doc) : null;
    }
    public Employee updateOneEmployee(Employee employee) {
        // Skapa en sökning med sökvillkoret att dokumentet måste ha rätt employeeId.
        var query = collection.find(employee.toDoc()).first();
            if(query==null){
                System.out.println("Employee not found");
                return null;
            }
        // Uppdatera dokumentet med nya värden.
        collection.replaceOne(new Document("_id", query.getObjectId("_id")), employee.toDoc()); // Uppdaterar personen i samlingen.
        return employee;
    }
    public void deleteOneEmployee(int employeeId) {
        // Skapa en sökning med sökvillkoret att dokumentet måste ha rätt employeeId.
        var query = new Document("employeeId", employeeId);
        // Ta bort dokumentet från samlingen.
        collection.deleteOne(query);
    }
    // Hjälpt av chatGPT. Metod som hämtar alla anställda från databasen.
    public ArrayList<Employee> FindAllEmployees() {
        // Skapa en sökning som matchar alla dokument som har "employeeId" fältet.
        var query = new Document("employeeId", new Document("$exists", true));

        // Hämta alla dokument som matchar sökningen och konvertera varje dokument till en Employee-objekt.
        FindIterable<Employee> result = collection.find(query, Employee.class);
        ArrayList<Employee> employees = new ArrayList<>();
        result.forEach(employees::add);

        return employees;
    }

    // METODER MED SÖKINGSKRITERIER. Hjälpt av ChatGPT.
    // Söker efter en person med ett visst ID och returnerar en person.
    public Person searchPersonById(String id) {
        Document query = new Document("id", id); // Skapar ett sökkriterium baserat på ID:t.
        Document result = collection.find(query).first(); // Söker efter personen baserat på ID:t.

        if (result == null) {
            return null; // Om ingen person hittades, returnera null.
        } else {
            return Person.fromDoc(result); // Konverterar dokumentet till en person och returnerar personen.
        }
    }

    // Söker efter personer med ett visst namn och returnerar en lista med personer.
    public ArrayList<Person> searchPersonByName(String name) {
        Document query = new Document("name", name); // Skapar ett sökkriterium baserat på namnet.
        FindIterable<Document> results = collection.find(query); // Söker efter personer baserat på namnet.
        ArrayList<Person> people = new ArrayList<>(); // Skapar en tom lista med personer.

        for (Document result : results) {
            people.add(Person.fromDoc(result)); // Konverterar dokumentet till en person och lägger till personen i listan.
        }

        return people; // Returnerar listan med personer.
    }

    // Söker efter personer med en viss ålder och returnerar en lista med personer.
    public ArrayList<Person> searchPersonByAge(int age) {
        Document query = new Document("age", age); // Skapar ett sökkriterium baserat på åldern.
        FindIterable<Document> results = collection.find(query); // Söker efter personer baserat på åldern.
        ArrayList<Person> people = new ArrayList<>(); // Skapar en tom lista med personer.

        for (Document result : results) {
            people.add(Person.fromDoc(result)); // Konverterar dokumentet till en person och lägger till personen i listan.
        }

        return people; // Returnerar listan med personer.
    }

    // Söker efter personer med en viss adress och returnerar en lista med personer.
    public ArrayList<Person> searchPersonByAddress(String address) {
        Document query = new Document("address", address); // Skapar ett sökkriterium baserat på adressen.
        FindIterable<Document> results = collection.find(query); // Söker efter personer baserat på adressen.
        ArrayList<Person> people = new ArrayList<>(); // Skapar en tom lista med personer.

        for (Document result : results) {
            people.add(Person.fromDoc(result)); // Konverterar dokumentet till en person och lägger till personen i listan.
        }

        return people; // Returnerar listan med personer.
    }

    // Söker efter personer med ett visst kundnummer och returnerar en lista med personer.
    public ArrayList<Person> searchPersonByCustomerNumber(String customerNumber) {
        Document query = new Document("customerNumber", customerNumber); // Skapar ett sökkriterium baserat på kundnumret.
        FindIterable<Document> results = collection.find(query); // Söker efter personer baserat på kundnumret.
        ArrayList<Person> people = new ArrayList<>(); // Skapar en tom lista med personer.

        for (Document result : results) {
            people.add(Person.fromDoc(result)); // Konverterar dokumentet till en person och lägger till personen i listan.
        }

        return people; // Returnerar listan med personer.
    }
}


