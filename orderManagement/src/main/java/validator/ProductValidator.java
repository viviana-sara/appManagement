package validator;

import DAO.AbstractDAO;
import DAO.ClientD;
import DAO.ProductD;
import Model.Client;
import Model.Products;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidator implements Validator<Products> {


    @Override
    public int validate(Products products) {
        /**
         * returneaza -1 daca cantitatea<1 si pretul<0,  -2 daca nu exista niciun produs,
         * 0 daca trebuie doar sa fie schimbata cantitatea produsului, in rest -3
         */
        if (products.getQuantity()<1)
            return -1;
        if (products.getPrice()<0)
            return -1;

        ProductD prod=new ProductD(Products.class);

        if (prod.getCount("idProducts")==0)
            return -2;   //nu contine niciun produs
        else
        {
            if (prod.findByName(products.getNameProduct(),products.getPrice())==null)
                return -2; //nu exista produsul cu acelasi nume
            else{
                Products p=null;
                p=prod.findByName(products.getNameProduct(),products.getPrice());
                if (p.getPrice()==products.getPrice())
                    return 0; // pretul este acelasi deci doar facem update;
                else
                    return -3; // pretul produselor sunt diferite;
            }

        }
    }

    @Override
    public int validateString(String command) {
        /**
         * verifica daca comanda are standardul necesar pentru Insert product:...(returneaza 1),  Delete product:....
         * (returneaza 2)  si Report product (returneaza 3)  altfel -1
         */
        //The command "Insert client"
        Pattern p1 = Pattern.compile("[I]{1}+[n]{1}+[s]{1}+[e]{1}+[r]{1}+[t]{1}+[\\s]+[p,P]{1}+[r]{1}+[o]{1}+[d]{1}+[u]{1}+[c]{1}+[t]{1}+[:]{1}+[\\s]{1}+[a-z]+[,]{1}+[\\s]{1}+[0-9]+[,]{1}+[\\s]{1}+[0-9]+$");
        Pattern p2 = Pattern.compile("[I]{1}+[n]{1}+[s]{1}+[e]{1}+[r]{1}+[t]{1}+[\\s]+[p,P]{1}+[r]{1}+[o]{1}+[d]{1}+[u]{1}+[c]{1}+[t]{1}+[:]{1}+[\\s]{1}+[a-z]+[,]{1}+[\\s]{1}+[0-9]+[,]{1}+[\\s]{1}+[0-9]+[.]{1}+[0-9]+$") ;
        Matcher m1 = p1.matcher(command);
        Matcher m2 = p2.matcher(command);
        if(m1.matches()==true && m2.matches()==false)
            return 1;
        if (m2.matches()==true && m1.matches()==false)
            return 1;

        // The command "Delete Product"
        p1=Pattern.compile("[D]{1}+[e]{1}+[l]{1}+[e]{1}+[t]{1}+[e]{1}+[\\s]+[P,p]{1}+[r]{1}+[o]{1}+[d]{1}+[u]{1}+[c]{1}+[t]{1}+[:]{1}+[\\s]{1}+[a-z]+$");
        m1=p1.matcher(command);
        if (m1.matches()==true)
            return 2;

        // The command "Report product"
        p1=Pattern.compile("[R]{1}+[e]{1}+[p]{1}+[o]{1}+[r]{1}+[t]{1}+[\\s]+[p,P]{1}+[r]{1}+[o]{1}+[d]{1}+[u]{1}+[c]{1}+[t]{1}+$");
        m1=p1.matcher(command);
        if (m1.matches()==true)
            return 3;


        return -1;
    }
}
