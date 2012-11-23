package data;

import market.Order;
import market.Order.Buysell;

public class OrderRecord extends Record {
	
	public long orderID;
	public String agentID;
	public String agentType;
	public int length;
	public Buysell buysell;
	public double price;
	public int volume;
	
	public OrderRecord(Order o) {
		this.t = o.getArrivedTime();
		this.lt = o.getArrivedTimeLT();
		this.orderID = o.getId();
		this.agentID = o.getAgentID();
		this.length = o.getLength();
		this.buysell = o.getBuysell();
		this.price = o.getPrice();
		this.volume = o.getVolume();
		if (agentID.contains("H"))
			this.agentType = "H";
		else if (agentID.contains("L"))
			this.agentType = "L";
		
	}

	public static String getHeader() {
		return new String("t,lt,agentType,length,buysell,price,volume");
	}

	public String toString() {
		
		String format = "%d,%d,%s,%d,%s,%.4f,%d\n";
		return String.format(format,
				t,
				lt,
				agentType,
				length,
				buysell,
				price,
				volume);
	}
	
}
