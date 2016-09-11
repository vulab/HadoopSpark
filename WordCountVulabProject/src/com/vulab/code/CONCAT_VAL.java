package com.vulab.code;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
 
public final class CONCAT_VAL extends UDF {
  public Text evaluate(final Text s, final Text s1) {
    if (s == null) { return null; }
    return new Text(s.toString().toLowerCase()+"#"+s1.toString().toLowerCase());
  }
}
