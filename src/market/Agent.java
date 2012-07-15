package market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public abstract class Agent {
	
	public String id;			// agent id
	public int S_i_t;			// hold stock amount
	public double C_i_t;		// hold cash amount
	public double wealth;		// wealth = cash + stock_price * stock_amount
	public abstract List<Order> action();
	
	protected double castDoubleToTick(double num, double tick) {
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
	
	public void setWealth(double stockPrice) { wealth = C_i_t + stockPrice * S_i_t; }
	
	public double getWealth() { return wealth; }
	
	public String getId() { return id; }
	
	public abstract String getProperty();
	
	public double getC_i_t() { return C_i_t; }
	
	public void setC_i_t(double C_i_t) { this.C_i_t = C_i_t; }
	
	public int getS_i_t() { return S_i_t; }
	
	public void setS_i_t(int S_i_t) { this.S_i_t = S_i_t; }
	
	public String toString() {
		String format = "%s,%s,%.4f";
		return String.format(format,
				id,
				this.getProperty(),
				wealth);
	}
}

class AgentComparator implements Comparator<Agent> {
	
	@Override
	public int compare(Agent a1, Agent a2) {
		
		if (a1.wealth > a2.wealth)
			return 1;
		else
			return 0;
	}
}

