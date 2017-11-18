/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset_Preprocessing;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 *
 * @author wasif
 */
public class Dataset_preprocessing {

    /**
     * @param args the command line arguments
     */
    static String FOLDERNAME="random-batch-1per";
    public static void readFile(String filename, String outputdir) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA1");
            String firstName, lastName, MOB, DOB, YOB, COB, SEX;
            BufferedReader br = new BufferedReader(new FileReader(FOLDERNAME+File.separator+filename));
            String sCurrentLine;
            PrintWriter pw = new PrintWriter(outputdir + filename + ".txt");
            //String [] t = filename.split("/");
            //DataOutputStream out = new DataOutputStream(new FileOutputStream(outputdir+filename + ".txt"));
            int count=0;
            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                count=count+1;
                if(count==1)continue;
                String[] t = sCurrentLine.split(",");
                firstName = t[1];
                lastName = t[3];
                MOB = t[4];
                DOB = t[5];
                YOB = t[6];
                COB = t[7];
                int attribute = Math.abs((firstName + lastName + MOB + DOB + YOB).hashCode());  //generating hash for each user
                sha.update(ByteBuffer.allocate(8).putLong(attribute).array());
                long a1 = ByteBuffer.wrap(sha.digest()).getLong();
                //System.out.println(a1);
                // out.write(Long.toString(a1));
                pw.println(Long.toString(a1));//.writeBytes(Long.toString(a1) + "\n");
               // count++;

            }
            pw.flush();
            pw.close();
            System.out.println("Processed: " + filename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        FOLDERNAME = args[0];
        File fl = new File(FOLDERNAME);
        for (File fl1 : fl.listFiles()) {
            if (fl1.getName().contains("file")) {
                //String filename = fl1.getName();
                System.out.println("File " + fl1.getName());
                String outputdir = "center_rep1" + File.separator;
                
                readFile(fl1.getName(), outputdir);
                //break;
            }
        }
    }

}
