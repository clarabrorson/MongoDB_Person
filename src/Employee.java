import org.bson.Document;

public class Employee extends Person{

    private int employeeID;

    // Hjälpt av ChatGPT. Med denna konstruktur kan man skapa en Employee från ett Document-objekt
    public Employee(Document doc) { // Tar emot ett dokument och skapar ett objekt av klassen Employee
        super(doc.getString("name"), doc.getString("age"), doc.getString("address")); // Anropar superklassens konstruktor
        this.employeeID = doc.getInteger("EmployeeID"); // Sätter värdet på employeeID
    }

    public Employee(String name, String age, String address, Integer employeeID) {
      super(name, age, address);
      if (employeeID==null) employeeID=0;
      this.employeeID=employeeID;
    }

    public int getEmployeeID(){
        return employeeID;
    }

    public void setEmployeeID(int employeeID){

        this.employeeID = employeeID;
    }
    public Document toDoc() {
        Document doc = new Document();
        doc.append("name", this.getName());
        doc.append("age", this.getAge());
        doc.append("address", this.getAddress());
        doc.append("EmployeeID", this.getEmployeeID());
        return doc;
    }
    public static Employee fromDoc(Document doc) // En statisk metod som tar emot en Document och returnerar ett objekt av klassen Person
    {
        if (doc==null) { // Om Document-objektet är null
            return new Employee("","","",null); // Returnera ett nytt objekt av klassen Person med tomma strängar som argument
        }

        return new Employee( // Returnera ett nytt objekt av klassen Person med värden från Document-objektet
                doc.getString("name"),
                doc.getString("age"),
                doc.getString("address")
                ,null
        );
    }
}
