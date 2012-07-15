package data;

import market.Order;
import market.Order.Buysell;

public class OrderRecord extends Record {
	
	public long orderID;
	public String agentID;
	public int length;
	public Buysell buysell;
	public double price;
	public int volume;
	
	public OrderRecord(Order o) {
		this.t = o.getArriveTime();
		this.orderID = o.getId();
		this.agentID = o.getAgentID();
		this.length = o.getLength();
		this.buysell = o.getBuysell();
		this.price = o.getPrice();
		this.volume = o.getVolume();
	}

	public static String getHeader() {
		return new String("t,agentID,length,buysell,price,volume");
	}

	public String toString() {
		
		String format = "%d,%s,%d,%s,%.4f,%d";
		return String.format(format,
				t,
				agentID,
				length,
				buysell,
				price,
				volume);
	}
	
}
