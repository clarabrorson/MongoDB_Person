import org.bson.Document;

public class Person {
    private String name;
    private String age;
    private String address;

    public Person(String name, String age, String address){
        this.name = name;
        this.age = age;
        this.address = address;

    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getAge(){
        return age;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setAge(String age){
        this.age = age;
    }

    @Override
    public String toString(){
        return "Name: " + name + "\nAddress: " + address + "\nAge: " + age;
    }

    public static Person fromDoc(Document doc) // En statisk metod som tar emot en Document och returnerar ett objekt av klassen Person
    {
        if (doc==null) { // Om Document-objektet är null
            return new Person("","",""); // Returnera ett nytt objekt av klassen Person med tomma strängar som argument
        }

        return new Person( // Returnera ett nytt objekt av klassen Person med värden från Document-objektet
                doc.getString("name"),
                doc.getString("age"),
                doc.getString("address")

        );
    }
    public static Person fromJson(String json) // En statisk metod som tar emot en JSON-sträng och returnerar ett objekt av klassen Person
    {
        Document doc = Document.parse(json); // Konverterar JSON-strängen till ett Document-objekt
        return fromDoc(doc); // Returnerar ett nytt objekt av klassen Hero med värden från Document-objektet
    }

    public Document toDoc(){
        return new Document("name", name)
                .append("age", age)
                .append("address", address);

    }
    public String toJson() // Skapa en offentlig metod toJson() som returnerar en json-sträng som representerar objektet
    {
        return toDoc().toJson(); // Returnera toDoc()-metodens dokument som en json-sträng
    }

    public Object getId() {
        return null;
    }
}

