package validator;

import DAO.ClientD;
import DAO.OrderD;
import DAO.ProductD;
import Model.Client;
import Model.Order1;
import Model.Products;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderValidator implements Validator<Order1> {


    public int valid(String client, String product, int quantity){
        /**
         * returneaza -1 daca clientul sau produsul nu exista,  -2 daca cantitatea ceruta e prea mare in rest returneaza 1
         */
        ProductD prod=new ProductD(Products.class);
        ClientD cli=new ClientD(Client.class);
        int c=cli.getIdOfName(client,"idClient", "nameClient");
        int p=prod.getIdOfName(product,"idProducts", "nameProduct");
        int q=prod.quantityProd(product);
        if (c==-1 || p==-1)
            return -1;
        if (q<quantity)
            return -2;
        return 1;
    }


    @Override
    public int validate(Order1 order) {
        return 0;
    }

    @Override
    public int validateString(String command) {
        /**
         * verifica daca comanda are standardul necesar pentru Order:...(returneaza 1)  si Report order (returneaza 3)  altfel -1
         */
        Pattern pattern=Pattern.compile("[O]{1}+[r]{1}+[d]{1}+[e]{1}+[r]{1}+[:]{1}+[\\s]{1}+[A-Z]{1}+[a-z]+[\\s]{1}+[A-Z]{1}+[a-z]+[,]{1}+[\\s]{1}+[a-z]+[,]{1}+[\\s]{1}+[0-9]+$");
        Matcher matcher=pattern.matcher(command);
        if (matcher.matches()==true)
            return 1;

        pattern=Pattern.compile("[R]{1}+[e]{1}+[p]{1}+[o]{1}+[r]{1}+[t]{1}+[\\s]+[o,O]{1}+[r]{1}+[d]{1}+[e]{1}+[r]{1}+$");
        matcher=pattern.matcher(command);
        if (matcher.matches()==true)
            return 3;

        return -1;
    }
}
