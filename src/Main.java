import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello MongoDB!");

        KeyReader keyReader = new KeyReader("MongoDB");

        // Tar bort onödiga varningar i konsolen.
        Logger.getLogger("org.mongodb.driver")
                .setLevel(Level.SEVERE);

        // Skapar en instans av MongoDB
        MongoDBFacade db = new MongoDBFacade();
        Person person ;

        // Skapar en objekt av person
        Person personOne = new Person("Lasse","50","gatan 1");
        Person personTwo = new Person("Hilda","83","vägen 2");
        Person personThree = new Person("Knut","11","stigen 3");
        // Lägger till personen i databasen
        db.insertOnePerson(personOne);
        db.insertOnePerson(personTwo);
        db.insertOnePerson(personThree);

        // Skapar customer objekt
        Customer customerOne = new Customer("Sanna","35","gatan 57", 1);
        Customer customerTwo = new Customer("Peter","41","vägen 6", 2);
        Customer customerThree = new Customer("Paula","20","stigen 17", 3);
        db.insertOnePerson(customerOne);
        db.insertOnePerson(customerTwo);
        db.insertOnePerson(customerThree);

        //Skapar Employee objekt genom att skapa ett Document objekt
        // p.g.a. att Employee har en konstruktor som tar emot ett Document objekt
        Document employeeDoc = new Document()
                .append("name", "Alfred")
                .append("age", "25")
                .append("address", "gatan 7")
                .append("EmployeeID", 10);

        Employee employeeOne = new Employee(employeeDoc);
        db.insertOneEmployee(employeeOne);

        employeeDoc = new Document()
                .append("name", "Lisa")
                .append("age", "28")
                .append("address", "vägen 100")
                .append("EmployeeID", 20);

        Employee employeeTwo = new Employee(employeeDoc);
        db.insertOneEmployee(employeeTwo);

        employeeDoc = new Document()
                .append("name", "David")
                .append("age", "49")
                .append("address", "gatan 126")
                .append("EmployeeID", 30);

        Employee employeeThree = new Employee(employeeDoc);
        db.insertOneEmployee(employeeThree);

        // Uppdaterar ålder på personOne
        Person personToUpdate = db.FindPerson(personOne); // Sök upp personen så du får ett Id
        personToUpdate.setAge("99");
        // Uppdatera personen i databasen
        Person updatedPerson = db.UpdatePerson(personToUpdate);
        System.out.println("Updated person: " + updatedPerson);

        // Uppdaterar adress på employeeOne.
        Employee employeeToUpdate = db.FindEmployee(employeeOne); // Sök upp personen så du får ett Id
        employeeToUpdate.setAddress("gatan 8");
        // Uppdatera personen i databasen
        Employee updatedEmployee = db.updateOneEmployee(employeeToUpdate);
        System.out.println("Updated employee: " + updatedEmployee);

        // Tar bort en employee från databasen
        int employeeId = 30; // Ersätt med rätt anställd-ID
        db.deleteOneEmployee(employeeId); // Anropa metoden för att ta bort den anställda från databasen
        System.out.println("Employee with ID " + employeeId + " deleted.");

    }
}