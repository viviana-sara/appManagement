package start;

import BLL.BLLClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Start {
    /**
     * clasa MainClass de unde citim valorile din fisier dat ca parametru si apoi executam comenziile.
     * @param args
     */
        public static void main(String[] args) {
              List<String> listCommands=new ArrayList<>();
            try {
                File myObj = new File(args[0]);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    listCommands.add(data);
                    //System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

           BLLClass bll=new BLLClass();
            for (int i=0; i<listCommands.size(); i++){
                bll.getCommand(listCommands.get(i));
            }
   }
}
