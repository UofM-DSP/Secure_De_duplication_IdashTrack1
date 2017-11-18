/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import static Utilities.Utils.private_exp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import static task_1_idash.main.server_Ip;
import static task_1_idash.main.server_port;

/**
 *
 * @author wasif
 */
public class Centre_handler extends Thread {

    public static int value;

    private static List<String> data_records = new ArrayList<>();
   // static RSAPrivateCrtKey alicePrivate; // alice private key d
 //   static RSAPublicKey alicePublic; // alice public key e
//    static BigInteger e = new BigInteger("3");
//    static BigInteger N = new BigInteger("9020987491511327311928694450078718187974394849729463211276011130552134522319282901755329263390895517338128475852745476298912507087747638863777404670563999"); // Key pair's modulus
//    static BigInteger P = new BigInteger("103522117865058531141827498655873961305741198324808896378196647358963675084553"); // Key pair's modulus
//    static BigInteger Q = new BigInteger("87140677543616508473683598214048461918713084166703408215432825663674976903783"); // Key pair's modulus
//    static BigInteger d = new BigInteger("6013991661007551541285796300052478791982929899819642140850674087034756348212728159306613725567520004160839035619680681344280663188769339593836510679050443");

    static BigInteger val;
    static BigInteger mu_centre;

    static int num_of_servers;
   // static int[] server_port = {2187, 2387, 2587, 2987};  //port numbers of other servers s2,s3
    static BigInteger r_i = new BigInteger("231832831741666642979337");
    SSLSocket client;
    static int C_Id;  //centre id

    public Centre_handler(int n, int tem) {
        num_of_servers = n;

    }

//    private static void read_data(int file_id) throws FileNotFoundException {
//        file_id = file_id - 1;
//        String file_name = "file" + file_id + ".txt";
//        System.out.println("file name is" + file_name);
//        data_records.clear();
//        String directory = "center_rep1/" + file_name;
//        File fl = new File(directory);
//
//        Scanner sc = new Scanner(fl);
//        while (sc.hasNext()) {
//            String s = sc.nextLine();
//            if (!data_records.contains(s)) {
//                data_records.add(s);
//
//            }
//
//        }
//
//    }

    Centre_handler(SSLSocket ss) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        this.client = ss;
        this.client.setEnabledCipherSuites(client.getSupportedCipherSuites());
    }

    Centre_handler(int c_id) {
        C_Id = c_id;

    }

    @Override
    public void run() {
        try {
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream os;
            os = new ObjectOutputStream(client.getOutputStream());
            create_Record m = (create_Record) is.readObject();
            String center_id = m.type;
            //  System.out.println("operating for "+center_id);
            for (int i = 0; i < m.records.length; i++) {
                BigInteger mu = private_exp(m.records[i]);
                m.records[i] = mu;
            }
            os.writeObject(m);

            //  Centre_handler.pass_value_to_servers(center_id);
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Exception caught in handler: client disconnected");
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
            }
        }
    }

    

    public static void pass_value_to_servers() throws IOException, NoSuchAlgorithmException, KeyManagementException, ClassNotFoundException {
//        double falsePositiveProbability = 0.001;  //bloom filter intialization
//        int expectedSize = 300000;
//        BloomFilter<String> bloomFilter = new BloomFilter<String>(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) / Math.log(2), expectedSize, 1);
//        read_data(C_Id); //read records for centre 
//
//        for (int j = 0; j < data_records.size(); j++) //  for (int j = 0; j < rec.length; j++) 
//        {
//            String st = data_records.get(j);
//            // String st = rec[j];
//            String message = DigestUtils.sha1Hex(st); //calculate SHA1 hash over message
//            byte[] msg = message.getBytes("UTF8"); //get the bytes of the hashed message
//            BigInteger m = new BigInteger(msg);  //create a BigInteger object based on the extracted bytes of the message
//            val = m.multiply(r_i).mod(N); //msg multiplied with random
//
//            BigInteger mu = private_exp(val);
//            bloomFilter.add(mu.toString());  //adding values to the bloom filter
//            bloomFilter.add(st);
//
//        }
//
//        BitSet b1 = bloomFilter.getBitSet();
//        
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(C_Id+"_filter.txt"));
//        objectOutputStream.writeObject(b1);
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("BFilters"+File.separator+C_Id+"_filter.txt"));
        BitSet b1 = (BitSet) (objectInputStream.readObject());
//        BitSet b3 = new BitSet(10);
        //System.out.println(b1.equals(b3));
        for (int i = 0; i < num_of_servers; i++) {
            int port_num = server_port.get(i);

            //passing value to the servers
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket s = (SSLSocket) factory.createSocket(server_Ip.get(i+1), port_num);
            s.setEnabledCipherSuites(s.getSupportedCipherSuites());
            int server_num = i + 2;
            String c = "c" + C_Id;

         //   System.out.println("Centre: " + C_Id + " passing bloom filter to the server: " + server_num);
            request_Sender sender = new request_Sender(s, b1, c); //sending value for matching
            sender.start();

        }

    }

}
