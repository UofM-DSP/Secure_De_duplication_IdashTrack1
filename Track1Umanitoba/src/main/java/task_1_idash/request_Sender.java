/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author wasif
 */
public class request_Sender extends Thread {

    SSLSocket client;
    static BitSet bs;
    String type;
    static BigInteger value;
    boolean match_status;
    public BigInteger[] val_c;
    static BigInteger[] records;
    static int count_response = 0;
    static int num_of_servers;

    request_Sender(int n) {
        num_of_servers = n;

    }

    request_Sender(SSLSocket ss, BitSet b, String type) throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        this.client = ss;
        bs = b;
        this.type = type;
        client.setEnabledCipherSuites(client.getSupportedCipherSuites());
    }



    request_Sender(SSLSocket ss, create_Record m) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        this.client = ss;
        this.type = m.type;
        this.match_status = m.match_status;
        this.records = m.records;
        client.setEnabledCipherSuites(client.getSupportedCipherSuites());
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
            create_Record C2 = new create_Record(type, records, match_status, bs);
            
            os.writeObject(C2);
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            create_Record returnMessage = (create_Record) is.readObject();
            if ("c1".equals(returnMessage.type)) {
                //System.out.println(returnMessage.type+" received response "+returnMessage.match_status);
                if(returnMessage.duplicated_rec!=null)
                {
                    System.out.println(returnMessage.type + " received response: " + returnMessage.duplicated_rec);
                    
                }
               // else  System.out.println(returnMessage.type + " received response: no duplicate");
                
                count_response++;
                if (count_response == num_of_servers) {
                    exit(0);
                }

            }
            //  else  System.out.println("center received value as input "+Arrays.toString(returnMessage.records));

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Exception caught: in sender client disconnected.");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
            }
        }
    }

}
