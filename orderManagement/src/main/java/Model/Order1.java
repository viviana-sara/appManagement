package Model;

public class Order1 {
    private int idOrder;
    private int idOrderClient;
    private int idOrderProduct;
    private int quantityOrder;

    public Order1(int idOrder, int idOrderClient, int idOrderProduct, int quantityOrder){
        this.idOrder=idOrder;
        this.idOrderClient=idOrderClient;
        this.idOrderProduct=idOrderProduct;
        this.quantityOrder=quantityOrder;

    }


    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdOrderClient() {
        return idOrderClient;
    }

    public void setIdOrderClient(int idOrderClient) {
        this.idOrderClient = idOrderClient;
    }

    public int getIdOrderProduct() {
        return idOrderProduct;
    }

    public void setIdOrderProduct(int idOrderProduct) {
        this.idOrderProduct = idOrderProduct;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

}
