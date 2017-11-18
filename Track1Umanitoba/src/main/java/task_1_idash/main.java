/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import Utilities.ConfigParser;
import static Utilities.Utils.N;
import static Utilities.Utils.e;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author wasif
 */
public class main {
    // public static BigInteger temp;
//test
   // static BigInteger e = new BigInteger("3");
   // static BigInteger N = new BigInteger("9020987491511327311928694450078718187974394849729463211276011130552134522319282901755329263390895517338128475852745476298912507087747638863777404670563999"); // Key pair's modulus

    static BigInteger r_s = generate_Random.generate_Random(N);
    static BigInteger[] value;
    public static boolean verbose= false;
    //static int num_of_servers = 5;
    static int num_of_servers_conf;
    static List<Integer> server_port = new ArrayList<>();
    static List<String> server_Ip = new ArrayList<>();
    static int num_of_center;
    static int centre_id = 1; //staring
    static int count = 0;   //number of centre
    static int batch_size;  //input batch size in center1
    static int input_file_id;  //file id for checking duplication
    static int main_port;  //server1 port(main server)
     static int center_1_port;  //server1 port(main server)
    static String center_Ip;
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, NoSuchAlgorithmException, KeyManagementException {

        ConfigParser configParser = new ConfigParser("Config.conf");
        num_of_servers_conf = configParser.getInt("Num_Server");
        num_of_center = configParser.getInt("Num_Center");
        batch_size = configParser.getInt("DataSizeCenter");
        input_file_id = configParser.getInt("InputFileId");
        String[] server_Ips=configParser.getString("ServerIPs").split(",");
        center_Ip=configParser.getString("Center_Ip");
        main_port = configParser.getInt("Server1Port");
        center_1_port = configParser.getInt("Center1Port");
        String[] ports = configParser.getString("ServerPorts").split(",");
        for (String port : ports) {
            server_port.add(Integer.parseInt(port.trim()));
        }
        for (String Ips : server_Ips) {
            server_Ip.add(Ips.trim());
        }
        //starting server 1 in socket
        start_Centers cn = new start_Centers();  //starting centres
        start_Servers sv = new start_Servers(); //starting servers
        Centre_handler ch = new Centre_handler(num_of_servers_conf, 0);
        requestHandler rq = new requestHandler(num_of_center); //setting number of centres
        request_Sender rs = new request_Sender(num_of_servers_conf);
        if(verbose)
            System.out.println("The records are multiplied with random number in Centre1 and passed to server1");

        try {
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket echoServer = null;
            SSLSocket clientSocket = null;
            try {
                SSLContext context = SSLContext.getInstance("TLSv1.2");
                context.init(null, null, null);
                echoServer = (SSLServerSocket) ssf.createServerSocket(main_port);
                echoServer.setEnabledCipherSuites(echoServer.getSupportedCipherSuites());

            } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
                System.out.print("server can't be created");
            }
            /**
             * init ports for the data centers in gap of 50, can change later on
             */
            for (int i = 1281; i <=52281; i = i + 50) //i presents port num of centres
            {
                count++;
                if (count == num_of_center) {
                    break;
                }

                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            Center_1 c1 = new Center_1();  //receiving input for centre 1
                            c1.read_input(batch_size, input_file_id);
                        } catch (ClassNotFoundException | NoSuchAlgorithmException | KeyManagementException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();

                clientSocket = (SSLSocket) echoServer.accept();   //recieving input in server1
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream os;
                os = new ObjectOutputStream(clientSocket.getOutputStream());
                create_Record m = (create_Record) is.readObject();
                os.writeObject(m);

                //server1 passing values to other centres
                centre_id += 1;
                connect(m, i, centre_id);

            }

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public static void connect(create_Record C2, int port_num, int center_id) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket s = (SSLSocket) factory.createSocket(server_Ip.get(0), port_num); //connecting with other centers
        s.setEnabledCipherSuites(s.getSupportedCipherSuites());
        BigInteger[] number = C2.get_Records();

        for (int i = 0; i < C2.records.length; i++) {
            number[i] = ((r_s.modPow(e, N)).multiply(C2.records[i])).mod(N); //server1 computes mu = H(msg) * r^e mod N
            //  System.out.println("value in server1 "+number[i]);

        }

        String st = "c" + center_id;
        if(verbose)System.out.println("The server connecting. centre: " + center_id + "..for checking");
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        create_Record Center2 = new create_Record(st, number);
        os.writeObject(Center2);
        //  System.out.println("object created ");

        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
        create_Record returnMessage = (create_Record) is.readObject();

        BigInteger[] val = returnMessage.get_Records();
        if(verbose)System.out.println("records signed by centre " + center_id);
        for (int j = 0; j < val.length; j++) {
            val[j] = r_s.modInverse(N).multiply(val[j]).mod(N);

        }
        //dividing by the random value multiplied before sending to the servers

        //BigInteger val=returnMessage.value;
        if(verbose)System.out.println("The server s1 passing signed values to other servers: ");

        for (int i = 0; i < num_of_servers_conf; i++) {
            pass_value_to_servers(i + 2, val, server_port.get(i));

        }

        pass_value_to_Centre(center_id);

    }

    public static void pass_value_to_servers(int i, BigInteger[] n, int port_num) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket server1 = (SSLSocket) factory.createSocket(server_Ip.get(i-1), port_num);
        server1.setEnabledCipherSuites(server1.getSupportedCipherSuites());

        String st = "s" + i;
        create_Record S1 = new create_Record(st, n, false);

        request_Sender sender = new request_Sender(server1, S1); //sending value for matching to other servers
        sender.start();

    }

    public static void pass_value_to_Centre(int center_id) throws IOException, NoSuchAlgorithmException, KeyManagementException, ClassNotFoundException {
        Centre_handler c = new Centre_handler(center_id);
        c.pass_value_to_servers();
    }

}
