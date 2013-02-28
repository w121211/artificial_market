package market;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simulation.Global;

public class HftAgent extends Agent implements Global {
	
	public double theta_b;
	public double theta_s;
	public double lambda_m;
	public int tau_m;
	
	HftAgent(String id) {
		this.id = id;
		this.S_i_t = 0;
		this.C_i_t = 0d;
		
		Random random = new Random();
		//theta_b = Math.abs( random.nextGaussian() * theta_sigma + theta_mu );
		//theta_s = Math.abs( random.nextGaussian() * theta_sigma + theta_mu );
		theta_b = theta;
		theta_s = theta;
		this.setLambda(Math.abs( random.nextGaussian() * lambda_sd + lambda_mu ));
		if (odr_hft_length > 0)
			tau_m = odr_hft_length;
		else
			tau_m = (int)(1 / lambda_m);
	}
	
	public List<Order> action() {
		switch(HFT_STRATEGY) {
		case POSITION_MARKET_MAKING:
			return positionMarketMakerAction();
		case RANDOM:
			return randomTraderAction();
		case SIMPLE_MARKET_MAKING:
			return simpleMarketMakerAction();
		}
		return null;
	}
	
	private List<Order> simpleMarketMakerAction() {
		// determine to buy or to sell
		Random random = new Random();
		double probToSell = this.rho * S_i_t + 0.5;
		
		List<Order> odrList = new ArrayList<Order>();
		if (random.nextDouble() < probToSell) {
			double p_sell;
			//p_sell = Market.p_t[Market.t] * (1 + theta_s);
			if (!Double.isNaN(Market.a_q_t)) {
				p_sell = Market.a_q_t - theta;
				odrList.add(new Order(id, Order.TimeStep.LFT, tau_m, Order.Type.LIMIT, Order.Buysell.SELL, p_sell, s_hft));
			}
		} else {
			double p_buy;
			//p_buy = Market.p_t[Market.t] * (1 - theta_b);
			if (!Double.isNaN(Market.b_q_t)) {
				p_buy = Market.b_q_t + theta;
				odrList.add(new Order(id, Order.TimeStep.LFT, tau_m, Order.Type.LIMIT, Order.Buysell.BUY, p_buy, s_hft));
			}
		}
		
		return odrList;
	}
	
	private List<Order> positionMarketMakerAction() {
		Random random = new Random();
		double probToSell = this.rho * S_i_t + 0.5;
		
		int t = Market.t;
		double ls;	// lower spread
		double us;	// upper spread
		
//		ls = w_3 * Math.abs(Math.pow(S_i_t, 3)) + w_1 * Math.pow(S_i_t, 3) + w_2;
//		us = w_3 * Math.abs(Math.pow(S_i_t, 3)) - w_1 * Math.pow(S_i_t, 3) + w_2;
		ls = w_3 * Math.abs(Math.pow(S_i_t, 3)) + w_1 * Math.pow(S_i_t, 3);
		us = w_3 * Math.abs(Math.pow(S_i_t, 3)) - w_1 * Math.pow(S_i_t, 3);
		
		List<Order> odrList = new ArrayList<Order>();
		if (random.nextDouble() < probToSell) {
			// submit a sell order
			double p_sell;
			if (!Double.isNaN(Market.a_q_t))
//				p_sell = Market.a_q_t * (1 + us);
				p_sell = Market.a_q_t + us;
			else
//				p_sell = Market.p_t[t] * (1 + us);
				p_sell = Market.p_t[t] + us;
			odrList.add(new Order(id, Order.TimeStep.LFT, tau_m, Order.Type.LIMIT, Order.Buysell.SELL, p_sell, s_hft));
		} else {
			// submit a buy order
			double p_buy;
			if (!Double.isNaN(Market.b_q_t))
//				p_buy = Market.b_q_t * (1 - ls);
				p_buy = Market.b_q_t - ls;
			else
//				p_buy = Market.p_t[t] * (1 - ls);
				p_buy = Market.p_t[t] - ls;
			odrList.add(new Order(id, Order.TimeStep.LFT  , tau_m, Order.Type.LIMIT, Order.Buysell.BUY, p_buy, s_hft));
		}
		
		
//		if (!Double.isNaN(Market.a_q_t) && !Double.isNaN(Market.b_q_t)) {
//			// if mid price exist
//			double p = (Market.a_q_t + Market.b_q_t) / 2; 	
//			p_sell = (1 + us) * p;
//			p_buy = (1 + ls) * p;
//		} else {	
//			// use market price instead
//			p_sell = (1 + us) * Market.p_t[t];
//			p_buy = (1 + ls) * Market.p_t[t];
//		}
//		
//		if (p_sell < 0 || p_buy < 0) {
//			System.out.println("sell:" + p_sell + " buy" + p_buy);
//		}
		
		
//		if (random.nextDouble() < probToSell && p_sell > Market.b_q_t) {
//			odrList.add(new Order(id, Order.TimeStep.LFT, tau_m, Order.Type.LIMIT, Order.Buysell.SELL, p_sell, s_hft));
//		} else if (p_buy < Market.a_q_t) {
//			odrList.add(new Order(id, Order.TimeStep.LFT  , tau_m, Order.Type.LIMIT, Order.Buysell.BUY, p_buy, s_hft));
//		}
		
		
		return odrList;
	}
	
	private List<Order> randomTraderAction() {
		Random random = new Random();
		double epsilon = 0.01;
		double p_t = Market.p_t[Market.t];
		double p = p_t + (random.nextDouble() - 0.5) * epsilon;
		
		List<Order> odrList = new ArrayList<Order>();
		if (random.nextDouble() > 0.5)
			odrList.add(new Order(id, Order.TimeStep.TICK, tau_m, Order.Type.LIMIT, Order.Buysell.BUY, p, 1));
		else
			odrList.add(new Order(id, Order.TimeStep.TICK, tau_m, Order.Type.LIMIT, Order.Buysell.SELL, p, 1));
		// System.out.println(odrList);
		return odrList;
	}
	
	public void learn() {
		Random random = new Random();
		this.setLambda(lambda_m + (random.nextDouble() * 2d - 1d) * lambda_delta);
		this.tau_m = (int)(tau_h / lambda_m + 5);
	}
	
	public void learn(HftAgent agent) {
		//this.theta_b = agent.theta_b;
		//this.theta_s = agent.theta_s;
		this.lambda_m = agent.lambda_m;
		this.learn();
	}
	
	@Override
	public String getProperty() {
		return String.format("{%.4f %.4f %.4f %d}",
				theta_b,
				theta_s,
				lambda_m,
				tau_m);
	}
	
	public void setLambda(double new_lambda_m) {
		if (new_lambda_m > 1d)
			this.lambda_m = 1d;
		else if (new_lambda_m < 0)
			this.lambda_m = 0d;
		else
			lambda_m = new_lambda_m;
	}
}
