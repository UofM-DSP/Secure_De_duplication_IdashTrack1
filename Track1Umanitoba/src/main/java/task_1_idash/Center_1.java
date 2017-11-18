/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import static Utilities.Utils.N;
import static Utilities.Utils.r_i;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.codec.digest.DigestUtils;
import static task_1_idash.main.main_port;
import static task_1_idash.main.server_Ip;
import static task_1_idash.main.verbose;

/**
 *
 * @author wasif
 */
public class Center_1 {

    //static BigInteger r_i = new BigInteger("231832831741666642979337");
    static BigInteger m;
    private List<String> input_records = new ArrayList<>();
    //setting values for RSA Blind signature  
    //static BigInteger e = new BigInteger("3");
    //  static BigInteger N = new BigInteger("9020987491511327311928694450078718187974394849729463211276011130552134522319282901755329263390895517338128475852745476298912507087747638863777404670563999");

    public void read_input(int batch_size, int file_id) throws ClassNotFoundException, NoSuchAlgorithmException, KeyManagementException {

        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket s = (SSLSocket) factory.createSocket(server_Ip.get(1), main_port);  //port number to connect with server1
            s.setEnabledCipherSuites(s.getSupportedCipherSuites());

            String file_name = "file" + file_id + ".txt";

            String directory = "center_rep1" + File.separator + file_name;
            File fl = new File(directory);

            int count = 1;
            Scanner sc = new Scanner(fl);
            // int j=0;
            while (sc.hasNext()) {
                String in = sc.nextLine();
                if (!input_records.contains(in)) {
                    input_records.add(in);

                }
                // input_records.add(sc.nextLine());
                //  st[j]=sc.nextLine();
                if (count == batch_size) {
                    break;
                }
                //  j++;
                count++;

            }
            String[] st = new String[input_records.size()];
            for (int j = 0; j < input_records.size(); j++) {
                st[j] = input_records.get(j);
            }

            // produce_key_pair();  //passing random value r_i to center2 
            //     System.out.println("The input in Centre 1(Pre-set)");
            //  String[] st={"-3436380041872942655","-6574237559446998988","-104240044479446998988","121212121212121","2323232323232"};
            // String[] st={"1300","14000","1500"};
            // BigInteger[] records = new BigInteger[input_records.size()];
            BigInteger[] records = new BigInteger[st.length];
            // System.out.println("input size"+input_records.size());

            for (int i = 0; i < st.length; i++) {
                String message = DigestUtils.sha1Hex(st[i]); //calculate SHA1 hash over message
                byte[] msg = message.getBytes("UTF8"); //get the bytes of the hashed message

                BigInteger m = new BigInteger(msg);  //create a BigInteger object based on the extracted bytes of the message

                records[i] = m.multiply(r_i).mod(N); //input multiplied with random  r_i*x  pass to the server1
                //  System.out.println("Records: "+ records[i]);

            }

            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

            create_Record C1 = new create_Record("c1", records);
            os.writeObject(C1);

            ObjectInputStream is = new ObjectInputStream(s.getInputStream());
            create_Record returnMessage = (create_Record) is.readObject();
            //  System.out.println("In server 1 the value is=" + Arrays.toString(returnMessage.get_Records()));
            if (verbose) {
                System.out.println("Records are passed to server1 to check duplication");
            }

            s.close();

        } catch (IOException ex) {
            System.err.println("Exception caught in Centre1...socket closed");
        }

    }

}
