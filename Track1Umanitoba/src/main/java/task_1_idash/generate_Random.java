/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wasif
 */
public class generate_Random {

    static BigInteger r;

    static BigInteger m;

    public static BigInteger generate_Random(BigInteger N) {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            byte[] randomBytes = new byte[10]; //create byte array to store the r

            BigInteger one = new BigInteger("1"); // make BigInteger object equal to 1, so we can compare it later with the r produced to verify r>1

            BigInteger gcd = null; // initialise variable gcd to null

            do {
                random.nextBytes(randomBytes); //generate random bytes using the SecureRandom function

                r = new BigInteger(randomBytes); //make a BigInteger object based on the generated random bytes representing the number r

                gcd = r.gcd(N); //calculate the gcd for random number r and the  modulus of the keypair

            } while (!gcd.equals(one) || r.compareTo(N) >= 0 || r.compareTo(one) <= 0); //repeat until getting an r that satisfies all the conditions and belongs to Z*n and >1
            return r;

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(generate_Random.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(generate_Random.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;

    }

}
