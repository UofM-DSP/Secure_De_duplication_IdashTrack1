/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset_Preprocessing;

import static Utilities.Utils.N;
import static Utilities.Utils.private_exp;
import static Utilities.Utils.r_i;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.codec.digest.DigestUtils;
import task_1_idash.BloomFilter;

/**
 *
 * @author wasif
 */
public class BFCreate {
     public static  double falsePositiveProbability = 0.001;  //bloom filter intialization
    public static int expectedSize = 10000 * 2;

    private static List<String> data_records = new ArrayList<>();

    private static void read_data(int file_id) throws FileNotFoundException {
        file_id = file_id - 1;
        String file_name = "file" + file_id + ".txt";
        System.out.println("file name: " + file_name);

        data_records.clear();

        String directory = "center_rep1" + File.separator + file_name;  //center_rep contains stored has values  after dataset pre-processing
        File fl = new File(directory);
        Scanner sc = new Scanner(fl);

        while (sc.hasNext()) {
          
           String s = sc.nextLine();
           if (!data_records.contains(s)) {
               data_records.add(s);
           }

     

            }
        }
    
     // String next = sc.nextLine();

//                System.out.println(next);
           /* String[] parts = next.split(",");
            if (!next.contains("YOB") && parts.length > 7) {
//                    if () {
//                        continue;
//                    }
                //String tmp = parts[4] + parts[5] + parts[6].substring(2);
                HashTest.Data data = new HashTest.Data(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[0]);
                data_records.add(data.toString());*/
       //Scanner sc = new Scanner(fl);
       //while (sc.hasNext()) {       
   

    public static void main(String[] args) throws IOException {
       // double falsePositiveProbability = 0.001;  //bloom filter intialization
      //  int expectedSize = 1000000;
        for (int C_Id = 345; C_Id <= 999; C_Id++) {

            BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
            read_data(C_Id); //read records for centre 

            for (int j = 0; j < data_records.size(); j++) //  for (int j = 0; j < rec.length; j++) 
            {
                String st = data_records.get(j);
                // String st = rec[j];
                String message = DigestUtils.sha1Hex(st); //calculate SHA1 hash over message
                byte[] msg = message.getBytes("UTF8"); //get the bytes of the hashed message
                BigInteger m = new BigInteger(msg);  //create a BigInteger object based on the extracted bytes of the message
                BigInteger val = m.multiply(r_i).mod(N); //msg multiplied with random

                BigInteger mu = private_exp(val);
                bloomFilter.add(mu.toString());  //adding values to the bloom filter
              

            }

            BitSet b1 = bloomFilter.getBitSet();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("BFilters" + File.separator + C_Id + "_filter.txt"));
            objectOutputStream.writeObject(b1);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
    }
}
