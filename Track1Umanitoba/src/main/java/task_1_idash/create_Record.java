/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task_1_idash;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.BitSet;

/**
 *
 * @author wasif
 */
public class create_Record implements Serializable {

    String type;
    boolean match_status;
    BitSet bs;
    BigInteger[] records;
    String duplicated_rec;

    public create_Record(String t, BigInteger[] val, boolean match_status, BitSet b) {
        this.type = t;
        this.records = val;
        this.match_status = match_status;
        this.bs = b;

    }

    public create_Record(String t, BigInteger[] val) {
        this.type = t;
        // this.value=(BigInteger)val;   
        this.match_status = false;
        //System.arraycopy(val, 0, records, 0, val.length);
        this.records = val;
    }

    public create_Record(String t, BigInteger[] val, boolean match_status) {
        this.type = t;
        // this.value=(BigInteger)val;   
        this.match_status = false;
        this.records = val;
        this.match_status = match_status;
    }

    public BigInteger[] get_Records() {
        return records;
    }

    public void set_duplicates(String dup) {

        this.duplicated_rec = dup;
    }

    public String get_duplicates() {
        return duplicated_rec;
    }
}
