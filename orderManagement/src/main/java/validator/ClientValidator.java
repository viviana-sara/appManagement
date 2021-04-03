package validator;

import DAO.AbstractDAO;
import DAO.ClientD;
import Model.Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator implements Validator<Client> {
    /**
     * implementeaza cele 2 functii
     * @param client
     * @return
     */

    @Override
    public int validate(Client client) {
        /**
         * verifica daca cilentul exista
         */
         ClientD c = new ClientD(Client.class);
         Client cl;
         cl=c.findByName(client.getNameClient());
         if (cl!=null){
             //System.out.println("Client already exists");
             return -1;
         }
        return 1;
    }

    @Override
    public int validateString(String command) {
        /**
         * verifica daca comanda are standardul necesar pentru Insert client:...(returneaza 1),  Delete client:....
         * (returneaza 2)  si Report client (returneaza 3)  altfel -1
         */

        String a=command;
        //The command "Insert client"
        Pattern p = Pattern.compile("[I]{1}+[n]{1}+[s]{1}+[e]{1}+[r]{1}+[t]{1}+[\\s]+[c,C]{1}+[l]{1}+[i]{1}+[e]{1}+[n]{1}+[t]{1}+[:]{1}+[\\s]+[A-Z]{1}+[a-z]+[\\s]{1}+[A-Z]{1}+[a-z]+[,]{1}+[\\s]{1}+[A-Z]{1}+[a-z]+[\\-]?+[a-zA-Z]+$");
        Matcher m = p.matcher(command);
        if (m.matches()==true)
            return 1;

        //The command "Delete client"
        p = Pattern.compile("[D]{1}+[e]{1}+[l]{1}+[e]{1}+[t]{1}+[e]{1}+[\\s]+[c,C]{1}+[l]{1}+[i]{1}+[e]{1}+[n]{1}+[t]{1}+[:]{1}+[\\s]+[A-Z]{1}+[a-z]+[\\s]{1}+[A-Z]{1}+[a-z]+[,]{1}+[\\s]{1}+[A-Z]{1}+[a-z]+$");
        m = p.matcher(command);
        if (m.matches()==true)
            return 2;

        //The command "Report client"
        p=Pattern.compile("[R]{1}+[e]{1}+[p]{1}+[o]{1}+[r]{1}+[t]{1}+[\\s]+[c,Cr]{1}+[l]{1}+[i]{1}+[e]{1}+[n]{1}+[t]{1}+$");
        m=p.matcher(command);

        if (m.matches()==true)
            return 3;

        return -1;


    }
}
