package com.dbms.dbrp.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class IDGenerator {
	static int paperCounter;
	static int keywordCounter;
	public static int getPaperCounter(){
		int res = -1;
		try{
			BufferedReader brIn = new BufferedReader(new FileReader("data/paperCounter"));
			paperCounter = Integer.parseInt(brIn.readLine().toString());
			BufferedWriter brOut = new BufferedWriter(new FileWriter("data/paperCounter"));
			res = paperCounter;
			paperCounter++;
			brOut.write(paperCounter + "");
			brIn.close();
			brOut.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	public static int getKeywordCounter(){
		int res = -1;
		try{
			BufferedReader brIn = new BufferedReader(new FileReader("data/keywordCounter"));
			keywordCounter = Integer.parseInt(brIn.readLine().toString());
			BufferedWriter brOut = new BufferedWriter(new FileWriter("data/keywordCounter"));
			res = keywordCounter;
			keywordCounter++;
			brOut.write(keywordCounter + "");
			brIn.close();
			brOut.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
