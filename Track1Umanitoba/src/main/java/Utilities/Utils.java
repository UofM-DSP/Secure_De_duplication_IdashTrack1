/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.IOException;
import java.math.BigInteger;

/**
 *
 * @author wasif
 */
public class Utils {

    public static BigInteger r_i = new BigInteger("231832831741666642979337");
    public static BigInteger e = new BigInteger("3");
    public static BigInteger N = new BigInteger("9020987491511327311928694450078718187974394849729463211276011130552134522319282901755329263390895517338128475852745476298912507087747638863777404670563999"); // Key pair's modulus
    public static BigInteger P = new BigInteger("103522117865058531141827498655873961305741198324808896378196647358963675084553"); // Key pair's modulus
    public static BigInteger Q = new BigInteger("87140677543616508473683598214048461918713084166703408215432825663674976903783"); // Key pair's modulus
    public static BigInteger d = new BigInteger("6013991661007551541285796300052478791982929899819642140850674087034756348212728159306613725567520004160839035619680681344280663188769339593836510679050443");

    public static BigInteger private_exp(BigInteger mu) throws IOException {
        try {

            BigInteger PinverseModQ = P.modInverse(Q); //calculate p inverse modulo q

            BigInteger QinverseModP = Q.modInverse(P); //calculate q inverse modulo p

            //We split the message mu in to messages m1, m2 one mod p, one mod q
            BigInteger m1 = mu.modPow(d, N).mod(P); //calculate m1=(mu^d modN)modP

            BigInteger m2 = mu.modPow(d, N).mod(Q); //calculate m2=(mu^d modN)modQ

            //We combine the calculated m1 and m2 in order to calculate muprime
            //We calculate muprime: (m1*Q*QinverseModP + m2*P*PinverseModQ) mod N where N =P*Q
            BigInteger muprime = ((m1.multiply(Q).multiply(QinverseModP)).add(m2.multiply(P).multiply(PinverseModQ))).mod(N);

            return muprime;

        } catch (Exception e) {
            System.out.println("null occured");
        }
        return null;

    }
}
