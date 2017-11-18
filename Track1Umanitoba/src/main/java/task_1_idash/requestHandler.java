/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import static dataset_Preprocessing.BFCreate.expectedSize;
import static dataset_Preprocessing.BFCreate.falsePositiveProbability;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import static task_1_idash.main.center_1_port;
import static task_1_idash.main.center_Ip;

/**
 *
 * @author wasif
 */
public class requestHandler extends Thread {

    SSLSocket client;
    static int centre_num;
    static String centre_total; //total num ber of centers
    //  static int centre_total=50;
    static int[] dup_records = new int[100];
    static int len = 0;
    String duplicate;
    static ArrayList<String> list = new ArrayList<String>();

    static BigInteger[] server_val, centre_val;

    public requestHandler(int n) {
        centre_num = n;
        centre_total = "c" + centre_num;

    }

    requestHandler(SSLSocket ss) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        this.client = ss;
        this.client.setEnabledCipherSuites(client.getSupportedCipherSuites());
    }

    @Override
    public void run() {
        try { 
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream os;
            os = new ObjectOutputStream(client.getOutputStream());

            create_Record m = (create_Record) is.readObject();

            String s1 = "c";  //data came from centre to match with server data

            //if(m.type.equals(s1))
            if (m.type.contains(s1)) {
                
                //  System.out.println("BitSet recieved from Centre: "+m.type+" "+ m.bs);
                BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedSize);
                /**
                 * test block
                 *
                 *
                 */
                bloomFilter.setBitSet(m.bs);
               // double falsePositiveProbability = 0.001;
              //  int expectedSize = 1000000;

                //  duplicate="dup";
                
                for (int i = 0; i < server_val.length; i++) {
                  //  BloomFilter<String> bloomFilter = new BloomFilter<String>(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) / Math.log(2), expectedSize, 1);
                   // bloomFilter.add(requestHandler.server_val[i].toString());  //generating bloom filter bitset for server record (from center 1)
                   // BitSet b1 = bloomFilter.getBitSet();
                    // System.out.println("BitSet recieved from server s1"+ b1);
                    
                     boolean chk = bloomFilter.contains(this.server_val[i].toString());

                 //   boolean chk = b1.intersects(m.bs);  //check if record ecista in bloom filter recieved from center 2 
                    //   System.out.println("check is"+ chk+"for element"+i);
                    if (chk == true) {
                        
                        String st = "" + i;
                        if (!list.contains(st)) {
                            list.add(st);
                        }
                    }
                }
                //System.out.println("BitSet recieved from server s1"+ b1);

                
                if (m.type.equals(centre_total)&& list!=null) {
                    duplicate = list.toString();
                    m.set_duplicates(duplicate);
                    send_response(m);  //send matching response to data centre c1

                }

                //   }
            } else {
                requestHandler.server_val = m.get_Records();
                //System.out.println("in server value"+  Arrays.toString(requestHandler.server_val));

            }

            os.writeObject(m);

        } catch (IOException | ClassNotFoundException | KeyManagementException | NoSuchAlgorithmException e) {
            System.err.println("Exception caught in handler: client disconnected");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
    }

    public void send_response(create_Record m) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket s = (SSLSocket) factory.createSocket(center_Ip,center_1_port);  //port for sending response center1
        s.setEnabledCipherSuites(s.getSupportedCipherSuites());
        m.type = "c1";
        request_Sender sender = new request_Sender(s, m); //sending response to centre c1
        sender.start();

    }

}
