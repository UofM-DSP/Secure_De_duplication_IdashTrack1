/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author aziz
 */
public class HashTest {

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static void main(String[] args)
            throws FileNotFoundException, UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
//        List<Integer> hashes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        Map<Integer, List<String>> dataMap = new HashMap<>();
        double falsePositiveProbability = 0.001;
        int expectedNumberOfElements = 1 * 10000;
//        List<Data> data_list = new ArrayList<>();
        //BloomFilter<Integer> bloomFilter = new BloomFilter<>(falsePositiveProbability, expectedNumberOfElements);
//        BloomFilter<Integer> bloomFilter = new FilterBuilder(expectedNumberOfElements, falsePositiveProbability).buildBloomFilter();
        //EncryptDecrypt encryptDecrypt = new EncryptDecrypt();

        for (int i = 0; i < 10; i++) {
            Scanner sc = new Scanner(new File("random-batch-1per/file" + i));

            while (sc.hasNext()) {
                String next = sc.nextLine();
                
//                System.out.println(next);
                    String[] parts = next.split(",");
                    if (!next.contains("YOB")&&parts.length > 7) {
//                    if () {
//                        continue;
//                    }
                    //String tmp = parts[4] + parts[5] + parts[6].substring(2);
                    Data data = new Data(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[0]);

                    MessageDigest sha = MessageDigest.getInstance("SHA1");
                    sha.reset();
                    sha.update((data.hashCode() + "").getBytes());
                    String hash = byteToHex(sha.digest());
                    if (!names.contains(hash)) {

                        names.add(hash);

//                    System.out.println("hash size "+(hash+"").length()+ " AES size "+ aes_encrypted.length());
                        int hashcodeSmall = (data.dob + data.mob + data.yob + (data.fname.toCharArray()[0] + "") + (data.lname.toCharArray()[0] + "")).hashCode();
                        if (dataMap.containsKey(hashcodeSmall)) {
                            dataMap.get(hashcodeSmall).add(hash);
                        } else {
                            List<String> adding = new ArrayList<>();
                            adding.add(hash);
                            dataMap.put(hashcodeSmall, adding);
                        }
                    }
//                    bloomFilter.add(hash);
                }
            }
            //break;
        }
        System.out.println("Map size "+dataMap.size()+" total records "+names.size());
        int cnt = 0;
        for (Map.Entry<Integer, List<String>> entry : dataMap.entrySet()) {
            //System.out.println(entry.getKey()+": "+entry.getValue().size());
            if(entry.getValue().size()==1)cnt++;
        }
        System.out.println(cnt);
//        System.out.println(hashes.get(0));
//        PrintWriter printWriter = new PrintWriter(new File("second200_aes.txt"));
//        PrintWriter printWriter2 = new PrintWriter(new File("second200_hash.txt"));
//
//        Collections.sort(hashes);
//        for (int i = 0; i < names.size(); i++) {
//            printWriter.println(names.get(i));
//            printWriter2.println(hashes.get(i));
//        }
//
//        printWriter2.flush();
//        printWriter2.close();
//        printWriter.flush();
//        printWriter.close();

//        System.out.println(hashes.size() + "," + names.size() + "," + bloomFilter.getBitSet().size());
//        System.out.println("Probability of a false positive: " + bloomFilter.getEstimatedFalsePositiveProbability());
//        int fp=0;
//        for (int i=hashes.get(0);i<hashes.get(hashes.size()-1);i++) {
//            
//            if(bloomFilter.contains(i) &&!hashes.contains(i))
//            {
//                fp++;
//                //System.out.print(i+",");
//                if(fp%100==0)
//                    System.out.println(i);
//            }
//            
//            
//        }
//        System.out.println(fp);
//        BitSet bs1 = bloomFilter.getBitSet();
//        for (int i = 0; i < bs1.length(); i+=1) {
//            System.out.print(bloomFilter.getBit(i));
//        }
//        File file = new File("first202.txt");
//        //file.getParentFile().mkdirs();
//        PrintWriter printWriter = new PrintWriter(file);
//        for (Integer hash : hashes) {
//            printWriter.println(hash + "");
//
//        }
    }

    private static String toString(BitSet bs) {

        return Long.toString(bs.toLongArray()[0], 2);
    }

    public static class Data {

        String ID;
        public String fname, lname, mname;
        public int yob, mob, dob;

        public String getID() {
            return ID;
        }

        public Data(String fname, String mname, String lname, String mob, String dob, String yob, String ID) {
            try {
                this.ID = ID;
                this.fname = fname;
                this.lname = lname;
                this.mname = mname;
                this.yob = !yob.isEmpty() ? Integer.parseInt(yob) : 0;
                this.mob = !mob.isEmpty() ? Integer.parseInt(mob) : 0;
                this.dob = !dob.isEmpty() ? Integer.parseInt(dob) : 0;
            } catch (Exception ex) {
                System.out.println(yob + " " + " " + mob + " " + dob);
            }
        }

        @Override
        public String toString() {
            return fname + "," + mname + "," + lname + "," + yob + "," + mob + "," + dob;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.fname);
            hash = 29 * hash + Objects.hashCode(this.lname);
            hash = 29 * hash + this.yob;
            hash = 29 * hash + this.mob;
            hash = 29 * hash + this.dob;

            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Data other = (Data) obj;
            if (!Objects.equals(this.ID, other.ID)) {
                return false;
            }
            return true;
        }

    }
}
