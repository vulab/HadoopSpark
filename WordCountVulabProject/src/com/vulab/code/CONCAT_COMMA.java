package com.vulab.code;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class CONCAT_COMMA extends EvalFunc<String>{

	@Override
	public String exec(Tuple arg0) throws IOException {
		String first = arg0.get(0).toString().trim();
		String second = arg0.get(1).toString().trim();
		return first + " - " +second;
	}

}
