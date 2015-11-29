import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Taha
 */
class CSV_class {
   Gson gson=new Gson();
    String json=new String();
    
       public String readObjectsFromCsv() throws IOException {
           String csvFile = "/home/taha/Documents/gitrepos/WebApplication1/src/java/master-zones.csv";

       //create BufferedReader to read csv file
      try{ BufferedReader br = new BufferedReader(new FileReader(csvFile));
       String line = "";
       StringTokenizer st = null;

       long lineNumber = 0; 
       int tokenNumber = 0;
        br.readLine();
        ArrayList al=new ArrayList();
       //read comma separated file line by line subset at 55320
       while ((line = br.readLine()) != null && lineNumber<55320) {
         lineNumber++;
         String st1[]= line.split(",");
         master_zones mz=new master_zones(st1,lineNumber);
         al.add(mz);
         /*use comma as token separator
         st = new StringTokenizer(line, ",");

         while (st.hasMoreTokens()) {
           tokenNumber++;
           String testing=st.nextToken();
           Object testtin2=st.nextElement();
           //display csv values
           System.out.print(st.nextToken() + "  ");
         }

         System.out.println();

         //reset token number
         tokenNumber = 0;
        */
    }
       String finalJSON=gson.toJson(al);
       return finalJSON;
      }
      catch(Exception e)
      {
          System.out.println("CSV_class Exception ="+e);
          return "";
      }
       }
}
