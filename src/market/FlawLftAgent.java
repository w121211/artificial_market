package market;

import java.util.ArrayList;
import java.util.List;

public class FlawLftAgent extends LftAgent {

	FlawLftAgent(String id) {
		super(id);
	}
	
	public List<Order> action() {
		List<Order> odrList = new ArrayList<Order>();
		
		int vol = 10;
		int length = 10;
		double p_sell = Market.b_q_t - tick;
		
		odrList.add(new Order(id, Order.TimeStep.LFT, length, Order.Type.LIMIT, Order.Buysell.SELL, p_sell, vol));
		
		return odrList;
	}

}
