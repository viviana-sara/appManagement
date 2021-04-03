package BLL;

import DAO.ClientD;
import DAO.OrderD;
import DAO.ProductD;
import Model.Client;
import Model.Order1;
import Model.Products;
import validator.ClientValidator;
import validator.OrderValidator;
import validator.ProductValidator;
import validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class BLLClass {
    private Validator<Products> validProduct;
    private Validator<Client> validClient;
    private OrderValidator validOrder;
    private ProductD prod;
    private ClientD client;
    private OrderD order;
    private final String insertC="Insert client: ";
    private final String insertP="Insert product: ";
    private final String insertO="Order: ";
    private final String deleteC="Delete client: ";
    private final String deleteP="Delete Product: ";
 //   private View view;
    private int reportOrderCount;
    private int reportClientCount;
    private int reportProductCount;


    public BLLClass(){
        validProduct=new ProductValidator();
        validClient= new ClientValidator();
        validOrder= new OrderValidator();
        prod=new ProductD(Products.class);
        client=new ClientD(Client.class);
        order=new OrderD(Order1.class);
        reportClientCount=0;
        reportOrderCount=0;
        reportProductCount=0;
    }

    private List<String> getStringCommand(String insert,String command){
        /**
         * formeaza lista cu elementele din comanda pe care trebuie sa o executam
         * insert este comanda   de ex: Delete Cleint: , Order: , Insert Client: ,etc...
         * command este comanda propriu zisa
         * stergem apelul-insert si lasam doar valorile dupa care le punem in lista
         */
        List<String> list=new ArrayList<>();
        String a;
        a= command.replaceAll(insert,"");
        String[] s=a.split(",\\s");
        for (int i=0; i<s.length; i++)
            list.add(s[i]);

        return list;
    }
    public void getCommand(String command){
        /**
         * validam commanda sub forma String
         * daca comanda este in standardul normal atunci va intra pe  o ramura si va efectua operatia ceruta
         * pentru insert si delete la products si clients vom apela insertProduct/Client si deleteProduct/Client din aceasta clasa
         * pentru comanda " Order:..." vom forma bonul sau mesajul pentru "eroare"
         * iar pentru comanda "Report..." vom afisa in format pdf elementele
         */

        if (validClient.validateString(command)==1){
            insertClient(getStringCommand(insertC,command).get(0),getStringCommand(insertC,command).get(1));
            return;
        }
        if (validClient.validateString(command)==2){
            deleteClient(getStringCommand(deleteC,command).get(0));
            return;
        }
        if (validProduct.validateString(command)==1){
            insertProduct(getStringCommand(insertP,command).get(0),Integer.parseInt(getStringCommand(insertP,command).get(1)),Double.parseDouble(getStringCommand(insertP,command).get(2)));
            return;
        }
        if (validProduct.validateString(command)==2){
            deleteProduct(getStringCommand(deleteP,command).get(0));
            return;
        }
//        if (validOrder.validateString(command)==1){
//            view.getReceipt(addOrder(getStringCommand(insertO,command).get(0),getStringCommand(insertO,command).get(1),Integer.parseInt(getStringCommand(insertO,command).get(2))));
//            return;
//        }
//        if (validOrder.validateString(command)==3){
//            view.report(reportOrder(),"Order "+reportOrderCount++);
//            return;
//        }
//        if (validProduct.validateString(command)==3){
//            view.report(reportProduct(),"Product " + reportProductCount++);
//            return;
//        }
//        if (validClient.validateString(command)==3)
//            view.report(reportClient(),"Client "+ reportClientCount++);
    }


    private void insertProduct( String name, int quantity, double price){
        /**
         * insereaza produsul daca acesta nu exista sau ii schimba cantitatea produsului existent cu acelasi nume si pret, altfel nu face nimic
         *
         */
        int n=validProduct.validate(new Products(0,name,quantity,price));
        if (n==-2){
            if (prod.getCount("idProducts")==0)
                prod.add(new Products(0,name,quantity,price));
            else
                prod.add(new Products(prod.getMaxId("idProducts")+1,name,quantity,price));
        }
        if (n==0){
            Products p=prod.findByName(name,price);
            prod.update(new Products(p.getIdProduct(),name,quantity+p.getQuantity(),price));
        }
    }
    private void deleteProduct(String name){
        /**
         * sterge produsul
         */
            prod.remove(name);
    }


    private List<String> reportProduct(){
        /**
         * creea lista de stringuri pe care vom dori sa o afisa in format pdf  (id + numeProdus + cantitate + pret)
         */
        List<Products> list= prod.findAll();
        List<String> listString=new ArrayList<>();
        if (list!=null)
            for (int i=0; i<list.size(); i++)
                listString.add(list.get(i).toString());
                //System.out.println(list.get(i).getIdProduct() + "  "+ list.get(i).getNameProduct()+"  "+list.get(i).getQuantity()+"  "+ list.get(i).getPrice());
            else{
            listString.add("There is no product");
        }
            return listString;
    }

    private void insertClient(String name, String address){
        /**
         * insereaza un client daca acesta exista
         */
        int n=validClient.validate(new Client(0,name,address));
        int id=client.getMaxId("idClient");
        int count=client.getCount("idClient");
        if (n==-1)
            return;
        if (n==1){
            if (count==0)
                client.add(new Client(0,name,address));
            else
                client.add(new Client(id+1,name,address));
        }
    }

    private void deleteClient(String name){
        /**
         * sterge un clinet daca acesta exista
         */
        int n=validClient.validate(new Client(0,name,"address"));
        if (n==-1)
            client.remove(name);
    }

    private List<String> reportClient(){
        /**
         * creeaza lista cu clientii pe care vom dorii sa ii afisam in format pdf de forma  (id: + nume + adresa)
         */
        List<Client> cl=new ArrayList<>();
        List<String>listString=new ArrayList<>();
        cl=client.findAll();
        if (cl!=null)
            for (int i=0; i<cl.size(); i++){
                listString.add(cl.get(i).getIdClient()+": "+cl.get(i).getNameClient()+", "+cl.get(i).getAddress());
            }
        else
            listString.add("There is no client");
        return listString;
    }

    private List<String> addOrder(String nameClient, String nameProduct, int quantity){
        /**
         * daca nu produsul sau clientul nu exista vom afisa in format pdf mesajul " Order....  failed. The client or product no exists"
         * analog pentru cazul in care cantitatea ceruta este prea mare "Order... failed. The quantity is too big"
         * altfel
         * vom crea lista noasta cu elementele pe care vrem sa le dorim in bonul fiscal in format pdf (Order number + client + product + cantitate + pret )
         */
        List<String> list=new ArrayList<>();
        if (validOrder.valid(nameClient,nameProduct,quantity)==-1){
            list.add("Fail Order "+nameProduct);
            list.add("The order: "+nameClient+", "+nameProduct+", "+quantity+" failed." +"\nThe client or product not exists");
        }
        else
        if (validOrder.valid(nameClient,nameProduct,quantity)==-2){
            list.add("Fail Order "+nameProduct);
            list.add("The order: "+nameClient+", "+nameProduct+", "+quantity+" failed." +"\nThe quantity is too big");
        }
        else{
            int id=prod.getIdOfName(nameProduct,"idProducts", "nameProduct");
            Products p=prod.findById(id);
            if (p.getQuantity()-quantity==0)
                deleteProduct(p.getNameProduct());
            p.setQuantity(p.getQuantity()-quantity);
            prod.update(p);
            if (order.getCount("idOrder")==0)
                order.add(new Order1(0, client.getIdOfName(nameClient, "idClient", "nameClient"), id, quantity));
            else
                order.add(new Order1(order.getMaxId("idOrder") + 1, client.getIdOfName(nameClient, "idClient", "nameClient"), id, quantity));

            list.add("Order "+order.getMaxId("idOrder"));
            list.add(nameClient);
            list.add(nameProduct);
            list.add(quantity+"");
            list.add(p.getPrice()*quantity+"");
        }
        return list;
    }


    private List<String> reportOrder(){
        /**
         * creeaza lista cu comenziile in format String pentru ai putea vizualiza in fomat pdf.
         */
        List<Order1> list=new ArrayList<>();
        list=order.findAll();
        List<String>listString=new ArrayList<>();
        if (list!=null){
            for (int i=0 ; i<list.size(); i++){
                Products p=prod.findById(list.get(i).getIdOrderProduct());
                Client c=client.findById(list.get(i).getIdOrderClient());
                double price=list.get(i).getQuantityOrder()*p.getPrice();
                listString.add(list.get(i).getIdOrder()+": "+c.getNameClient()+", "+p.getNameProduct()+" "+list.get(i).getQuantityOrder()+", "+price);
            }
        }
        return listString;
    }
}
