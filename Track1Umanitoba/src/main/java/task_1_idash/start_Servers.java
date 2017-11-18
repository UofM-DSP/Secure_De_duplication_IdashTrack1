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
import static task_1_idash.main.center_1_port;
import static task_1_idash.main.num_of_servers_conf;
import static task_1_idash.main.server_port;

/**
 *
 * @author wasif
 */
public class start_Servers extends Thread {

    private int Portnumber;
    static int num_of_servers = 4;
   // static int[] server_port = {2787, 2187, 2387, 2587, 2987};  //port numbers of other servers s2,s3
    //first port 2787, centre1 port

    public start_Servers(int portNumber) {
        Portnumber = portNumber;
        setDaemon(false);
    }

    public start_Servers() {
        //create three threads
        for (int i = 0; i < num_of_servers_conf; i++) {
            Thread first = new start_Servers(server_port.get(i));
            first.start();
            if(i==0)
            {
                  Thread second = new start_Servers(center_1_port);  //starting center_1
                   second.start();
                
            }

        }

        System.out.println("The servers Started");

        //  Thread second = new start_Servers(centre_1_port);
        //  second.start();
        // System.out.println("Centre1  Started");
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
                requestHandler handler = new requestHandler(ss);
                handler.start();

            }

        } catch (IOException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(start_Servers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
