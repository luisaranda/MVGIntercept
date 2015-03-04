package com.ballydev.sds.slipsui.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLClientUtil {

public static File stringToFile(String str,String name) throws IOException {
		File infile = new File(name+".xml");
		FileOutputStream fos = new FileOutputStream(infile);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(str);
		bw.close();
		osw.close();
		fos.close();
		return infile;
	}

	
}
