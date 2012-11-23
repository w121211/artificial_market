package market;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

import simulation.Global;

public class Order implements Global {
	
	public enum Type {
		MARKET,
		LIMIT;
	}
	
	public enum Buysell {
		BUY,
		SELL;
	}
	
	public enum TimeStep {
		LFT,
		TICK;
	}
	
	private long id;
	private String agentID;
	private int arrivedTime;
	private int arrivedTimeLT;
	private TimeStep timeStep;
	private int length;			// time length of the order
	private Type type;			// limit, market
	private Buysell buysell;	// buy, sell
	private double price;
	private int volume;
	private static long count = 1;
	
	public Order(String agentID, TimeStep timeStep, int length, Type type, Buysell buysell, double price, int volume) {
		this.id = count;
		this.agentID = agentID;
		this.timeStep = timeStep;
		this.length = length;
		this.type = type;
		this.buysell = buysell;
		this.price = castDoubleToTick(price, tick);
		this.volume = volume;
		Order.count++;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getAgentID() { return this.agentID; }
	
	public void setAgentID(String id) { this.agentID = id; }
	
	public int getArrivedTime() { return this.arrivedTime; }
	
	public int getArrivedTimeLT() { return this.arrivedTimeLT; }
	
	public void setArrivedTimes(int t, int lt) {
		this.arrivedTime = t;
		if (this.timeStep == TimeStep.LFT)
			this.arrivedTimeLT = lt;
	}
	
	public int getLength() { return this.length; }
	
	public void setLength(int t) { this.length = t; }
	
	public Type getType() { return this.type; }
	
	public void setType(Type type) { this.type = type; }
	
	public Buysell getBuysell() { return this.buysell; }
	
	public void setBuysell(Buysell buysell) { this.buysell = buysell; }
	
	public double getPrice() { return this.price; }
	
	public void setPrice(double price) { this.price = price; }
	
	public int getVolume() { return this.volume; }
	
	public void setVolume(int volume) { this.volume = volume; }
	
	public TimeStep getTimeStep() { return timeStep; }
	
	public String toString() {
		String str = "Odr(" + id + ")";
		str += agentID + ",";
		str += "[" + arrivedTimeLT + "," + length + "]";
		
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
	
	public double castDoubleToTick(double num, double tick) {
		if (Double.isNaN(num))
			return num;
		
		BigDecimal a = new BigDecimal(Double.toString(num));
		BigDecimal b = new BigDecimal(Double.toString(tick));
		try {
			a = a.divide(b).setScale(0, RoundingMode.DOWN).multiply(b);
		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
		return a.doubleValue(); 
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
				if (o1.getArrivedTime() < o2.getArrivedTime()) return 0;
				else return 1;
			}
		} else {
			
			if (o1.getPrice() < o2.getPrice())
				return 0;
			else if (o1.getPrice() > o2.getPrice())
				return 1;
			else {
				if (o1.getArrivedTime() < o2.getArrivedTime()) return 0;
				else return 1;
			}
		}
	}	
}
