package Model;

public class Products {

    private int idProducts;
    private String nameProduct;
    private int quantity;
    private double price;

    public Products(int idProduct, String nameProducts, int quantity, double price){
        this.idProducts=idProduct;
        this.nameProduct=nameProducts;
        this.quantity=quantity;
        this.price=price;
    }

    public int getIdProduct() {
        return idProducts;
    }

    public void setIdProduct(int idProduct) {
        this.idProducts = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString(){
        return this.idProducts+": "+this.nameProduct+", "+this.quantity+", "+this.price;
    }
}
