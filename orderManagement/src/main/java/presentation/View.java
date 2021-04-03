//package presentation;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.w3c.dom.Document;
//
//import java.awt.*;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//public class View {
//    /**
//     * clasa in care formam bonul sau mesajul de "eroaore" pentru comanda si afisam elementele in format pdf
//     * @param list
//     * @param name
//     */
//
//        public void report(List<String> list, String name) {
//            /**
//             * afisam elementele listei deja formate din formatul String al elementeleor, avand titlui Report ( name + number ) si data curenta
//             * name= numele Clasei (Product, Order, Client)
//             *
//             */
//            try {
//                Document document = new Document();
//                PdfWriter.getInstance(document, new FileOutputStream("Report "+name+".pdf"));
//                document.open();
//                Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
//                Chunk chunk = new Chunk("Report"+name, font);
//                document.add(chunk);
//
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                document.add(new Paragraph("\n"+formatter.format(new Date(System.currentTimeMillis()))));
//
//                for (int i=0; i<list.size();i++) {
//                    document.add(new Paragraph("\n" + list.get(i)));
//                }
//                document.close();
//
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//    public void getReceipt(List<String> list) {
//        /**
//         * cazul in care trebuie doar sa afisam un mesaj este cand list==2  "Fail Receipt + message - in functie de "eroare"  "
//         * intai afisa un titlu " Receipt Order + number ", data si dupa elementele bonului fiscal (client, produs,cantitate si pret)
//         */
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(list.get(0)+".pdf"));
//            document.open();
//            Font font = new Font(Font.FontFamily.HELVETICA,18,Font.BOLD);
//            if (list.size()>2){
//                Chunk chunk = new Chunk("Receipt "+list.get(0), font);
//                document.add(chunk);
//                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                document.add(new Paragraph("\n"+formatter.format(new Date(System.currentTimeMillis()))));
//                document.add(new Paragraph("\n Client: "+list.get(1)));
//                document.add(new Paragraph("\n Product: "+list.get(2)+"\n"));
//                document.add(new Paragraph("\n Quantity: "+list.get(3)+"\n"));
//                document.add(new Paragraph("\n Price: "+list.get(4)+"\n"));
//            }
//            else{
//                Chunk chunk = new Chunk("Fail receipt", font);
//                document.add(chunk);
//                document.add(new Paragraph(list.get(1)));
//            }
//                document.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }
//}
