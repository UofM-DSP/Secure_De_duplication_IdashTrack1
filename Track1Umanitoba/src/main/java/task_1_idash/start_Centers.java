/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wasif
 */
public class start_Centers extends Thread {
    // static int num_of_servers=3;
    // static int num_of_centers=100;

    //  static int[] center_port={1281,1381,1481,1581,1681,1781,1881,1981,2081};  //port numbers of other servers s2,s3
    private int Portnumber;
    static int count = 0;

    public start_Centers(int portNumber) {
        Portnumber = portNumber;
        setDaemon(false);
    }
    //  public static void main(String[] args)

    public start_Centers() {
        //create three threads
        //  start_Servers.start_server();
        for (int i = 1281; i <=52281; i = i + 50) {
            // Thread first = new start_Centers(center_port[i]);
            // count++;
            // if(count==num_center+1)break;
            Thread first = new start_Centers(i);
            first.start();

        }

        //  System.out.println("Centers  Started");
    }

    @Override
    public void run() {

        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket s1 = (SSLServerSocket) ssf.createServerSocket(Portnumber);  //port for server3 
            s1.setEnabledCipherSuites(s1.getSupportedCipherSuites());

            while (true) {
                SSLSocket ss = (SSLSocket) s1.accept();
                Centre_handler handler = new Centre_handler(ss);
                handler.start();

            }

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException ex) {
            Logger.getLogger(start_Centers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
