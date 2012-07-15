package data;

import market.Agent;

public class AgentRecord extends Record {
	
	String id;				// agent id
	String property; 
	int position;			// position
	double cash;			// cash
	double wealth;
	
	public AgentRecord(int t, Agent agent) {
		this.t = t;
		this.id = agent.getId();
		this.property = agent.getProperty();
		this.position = agent.getS_i_t();
		this.cash = agent.getC_i_t();
		this.wealth = agent.getWealth();
	}
	
	public static String getHeader() {
		return new String("t,id,type,stock,cash,wealth");
	}
	
	public String toString() {
		String format = "%d,%s,%s,%d,%.4f,%.4f\n";
		return String.format(format,
				t,
				id,
				property,
				position,
				cash,
				wealth);
	}
}
