package market;
import java.util.Comparator;

public class Order {
	
	public enum Type {
		MARKET,
		LIMIT;
	}
	
	public enum Buysell {
		BUY,
		SELL;
	}
	
	private long id;
	private String agentID;
	private int arriveTime;
	private int length;
	private Type type;			// limit, market
	private Buysell buysell;	// buy, sell
	private double price;
	private int volume;
	private static long count = 1;
	
	public Order(String agentID, int length, Type type, Buysell buysell, double price, int volume) {
		this.id = count;
		this.agentID = agentID;
		this.length = length;
		this.type = type;
		this.buysell = buysell;
		this.price = price;
		this.volume = volume;
		Order.count++;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getAgentID() { return this.agentID; }
	
	public void setAgentID(String id) { this.agentID = id; }
	
	public int getArriveTime() {
		return this.arriveTime;
	}
	
	public void setArriveTime(int t) {
		this.arriveTime = t;
	}
	
	public int getLength() { return this.length; }
	
	public void setLength(int t) { this.length = t; }
	
	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Buysell getBuysell() {
		return this.buysell;
	}
	
	public void setBuysell(Buysell buysell) {
		this.buysell = buysell;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public String toString() {
		String str = "Odr(" + id + ")";
		str += agentID + ",";
		str += "[" + arriveTime + "," + length + "]";
		
		if (type == Type.LIMIT)
			str += "LIMIT";
		else
			str += "MARKET";		
		if (buysell == Buysell.BUY)
			str += " BUY";
		else
			str += " SELL";
		
		str += " " + price + "x" + volume;
		return str;
	}
}

class OrderComparator implements Comparator<Order> {
	
	@Override
	public int compare(Order o1, Order o2) {
		
		if (o1.getBuysell() == Order.Buysell.BUY) {
			
			if (o1.getPrice() < o2.getPrice())
				return 1;
			else if (o1.getPrice() > o2.getPrice())
				return 0;
			else {
				if (o1.getArriveTime() < o2.getArriveTime()) return 0;
				else return 1;
			}
		} else {
			
			if (o1.getPrice() < o2.getPrice())
				return 0;
			else if (o1.getPrice() > o2.getPrice())
				return 1;
			else {
				if (o1.getArriveTime() < o2.getArriveTime()) return 0;
				else return 1;
			}
		}
	}	
}
