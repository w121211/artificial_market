package data;

import market.Order;

public class TradeRecord extends Record {

	public String demandAgentId;
	public String supplyAgentId;
	public Order.Buysell buysell;
	public int volume;
	public double price;
	public String demandAgentType;
	public String supplyAgentType;
	
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
		if (demandAgentId.contains("H"))
			this.demandAgentType = "H";
		else if (demandAgentId.contains("L"))
			this.demandAgentType = "L";
		if (supplyAgentId.contains("H"))
			this.supplyAgentType = "H";
		else if (supplyAgentId.contains("L"))
			this.supplyAgentType = "L";
	}
	
	public static String getHeader() {
		return new String(
				"t," +
				"demandAgentType," +
				"supplyAgentType," +
				"buysell," +
				"volume," +
				"price");
	}

	public String toString() {
		
		String format = "%d,%s,%s,%s,%d,%.4f\n";
		return String.format(format,
				t,
				demandAgentType,
				supplyAgentType,
				buysell,
				volume,
				price);
	}
	

}
