public class Customer extends Person{

    private int customerID;

    public Customer(String name, String age, String adress, int customerID){
        super(name, age, adress);
        this.customerID = customerID;
    }

    public int getCustomerID(){

        return customerID;
    }

    public void setCustomerID(int customerID) {

        this.customerID = customerID;
    }
}
