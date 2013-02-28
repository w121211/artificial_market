package simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import market.Agent;

public class Tester {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// input to agent array
		int nlft = 10000;
		double price = 0;
		
		double[] cash = new double[nlft];
		int[] stock = new int[nlft];
		double[] asset = new double[nlft];
		
		String path = System.getProperty("user.dir");
//		path += "/log/EXP-01/rf-pm-nn-10-1.0-0.00/";
//		path += "/log/EXP-01/rf-nn-nn-00-0.0-0.00/";
//		path += "/log/EXP-02/rf-nn-nn-00-0.0-0.00/";
		path += "/log/EXP-02/rf-pm-nn-10-1.0-0.00/";
		
		BufferedReader br = new BufferedReader(
				new FileReader(path+"agent0"));
		
		String str;
		br.readLine();
		while ((str = br.readLine()) != null) {
			String[] s = str.split(",");
			if (s[1].contains("L")) {
				int id = Integer.parseInt(s[1].replace("L", ""));
				cash[id] = Double.parseDouble(s[4]);
				stock[id] = Integer.parseInt(s[3]);
			}
		}
		
		br = new BufferedReader(
				new FileReader(path+"marketlt0"));
		while((str = br.readLine()) != null) {
			String[] s = str.split(",");
			System.out.println(str);
			
			if (s[1].equals("9999"))
				price = Double.parseDouble(s[3]);
		}
		System.out.println(price);
		
		PrintWriter writer = new PrintWriter(new BufferedWriter(
				new FileWriter(path+"lft0")));
		for (int i = 0; i < nlft; i++) {
			writer.println((cash[i] + stock[i]*price));
			writer.flush();
		}
	}
	
}
