package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Log {
	
	String filePath;
	int run;
	PrintWriter agtWriter;
	PrintWriter odrWriter;
	PrintWriter odrbookWriter;
	PrintWriter mktWriter;
	PrintWriter mktltWriter;
	PrintWriter trdWriter;
	
	public Log(String filePath, int run) {
		this.filePath = filePath;
		this.run = run;
		
		try {
			(new File(filePath)).mkdir();
			
			agtWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "agent" + run)));
			odrWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "order" + run)));
			odrbookWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "orderbook" + run)));
			mktWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "market" + run)));
			mktltWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "marketlt" + run)));
			trdWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath + "/" + "trade" + run)));
			
			this.printHeaders();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void printHeaders() {
		agtWriter.println(AgentRecord.getHeader());
		odrWriter.println(OrderRecord.getHeader());
		odrbookWriter.println(OrderbookRecord.getHeader());
		mktWriter.println(MarketRecord.getHeader());
		mktltWriter.println(MarketLftRecord.getHeader());
		trdWriter.println(TradeRecord.getHeader());
	}
	
	public void closeWriters() {
		agtWriter.close();
		odrWriter.close();
		odrbookWriter.close();
		mktWriter.close();
		mktltWriter.close();
		trdWriter.close();
	}
	
	public void add(AgentRecord record) { agtWriter.print(record); agtWriter.flush(); }
	
	public void add(OrderRecord record) { odrWriter.print(record); odrWriter.flush(); }
	
	public void add(OrderbookRecord record) { odrbookWriter.print(record); odrbookWriter.flush(); }
	
	public void add(MarketRecord record) { mktWriter.print(record); mktWriter.flush(); }
	
	public void add(MarketLftRecord record) { mktltWriter.print(record); mktltWriter.flush(); }
	
	public void add(TradeRecord record) { trdWriter.print(record); trdWriter.flush(); }

}
