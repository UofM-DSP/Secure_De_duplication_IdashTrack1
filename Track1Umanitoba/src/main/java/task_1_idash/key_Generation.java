package task_1_idash;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * The class AliceRSA represents Alice who can create an RSA keypair and can
 * issue digital signatures
 */
public class key_Generation {

    /**
     * Produces and returns an RSA keypair (N,e,d) N: Modulus, e: Public
     * exponent, d: Private exponent The public exponent value is set to 3 and
     * the keylength to 2048
     *
     * @return RSA keypair
     */
    //static BigInteger P = new BigInteger("103522117865058531141827498655873961305741198324808896378196647358963675084553"); 
    // static BigInteger Q = new BigInteger("87140677543616508473683598214048461918713084166703408215432825663674976903783"); 
    // static BigInteger d = new BigInteger("6013991661007551541285796300052478791982929899819642140850674087034756348212728159306613725567520004160839035619680681344280663188769339593836510679050443");
    //static BigInteger r_i=generate_Random.generate_Random(N);

    // public static void produce_key_pair()
    //  {
    //**key generation for RSA**
    /* alicePair = key_Generation.produceKeyPair(); // call key generation function to produce a key pair (N, e ,d), and save it in alicePair variable
        alicePrivate = (RSAPrivateCrtKey) alicePair.getPrivate(); //get the private key d out of the key pair Alice produced
        alicePublic = (RSAPublicKey) alicePair.getPublic(); //get  the public key e out of the key pair Alice produced
        BigInteger  N = alicePublic.getModulus(); //get the modulus of the key pair produced by Alice
        System.out.println("value of N is:"+N);
        BigInteger P = alicePrivate.getPrimeP(); //get the prime number p used to produce the key pair
        System.out.println("value of p is:"+P);
        BigInteger   Q = alicePrivate.getPrimeQ(); //get the prime number q used to produce the key pair
        System.out.println("value of Q is:"+Q);
        BigInteger  d = alicePrivate.getPrivateExponent(); //get private exponent d
        System.out.println("value of d is:"+d);
        BigInteger e = alicePublic.getPublicExponent();
        System.out.println("value of e is:"+e);*/
    //  r_i = generate_Random.generate_Random(N);
    // Centre_handler Centre_handler = new  Centre_handler(r_i);
    // Centre_handler.set_key_pair(r_i);
    // Center_3.set_key_pair(r_i);
    //  BigInteger r=generate_Random.generate_Random(N);
    //  System.out.println("value of r is:"+r);
    //}
    public static KeyPair produceKeyPair() {
        try {
            KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");  //get rsa key generator

            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(512, BigInteger.valueOf(3)); //set the parameters for they key, key length=2048, public exponent=3

            rsaKeyPairGenerator.initialize(spec); //initialise generator with the above parameters

            KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair(); //generate the key pair, N:modulus, d:private exponent

            return (keyPair);  //return the key pair produced (N,e,d)

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
