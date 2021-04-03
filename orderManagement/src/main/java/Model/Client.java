package Model;

public class Client {
    private int idClient;
    private String nameClient;
    private String address;

    public Client(int idClient, String nameClient, String address){
        this.address=address;
        this.nameClient=nameClient;
        this.idClient=idClient;
    }


    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
