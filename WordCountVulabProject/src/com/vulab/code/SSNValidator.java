package com.vulab.code;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
 
public final class SSNValidator extends UDF {
	
  public Text evaluate(final Text s) {
    if (s == null) { return null; }
    
    if(s.getLength()==9){
    	 return new Text("VALID");
    }else {
    	 return new Text("INVALID");
    }
    
   
  }
}
