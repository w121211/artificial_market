package data;

import market.Order;

public class TradeRecord extends Record {

	public String demandAgentId;
	public String supplyAgentId;
	public Order.Buysell buysell;
	public int volume;
	public double price;
	
	public TradeRecord( int t,
						String demandAgentId,
						String supplyAgentId,
						Order.Buysell buysell,
						int volume,
						double price) {
		
		this.t = t;
		this.demandAgentId = demandAgentId;
		this.supplyAgentId = supplyAgentId;
		this.buysell = buysell;
		this.volume = volume;
		this.price = price;
		
	}
	
	public static String getHeader() {
		return new String(
				"t," +
				"demandAgentId," +
				"supplyAgentId," +
				"buysell," +
				"volume," +
				"price");
	}

	public String toString() {
		
		String format = "%d,%s,%s,%s,%d,%.4f\n";
		return String.format(format,
				t,
				demandAgentId,
				supplyAgentId,
				buysell,
				volume,
				price);
	}
	

}
