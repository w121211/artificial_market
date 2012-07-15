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
		tau_m = (int)(tau_h / lambda_m + 5);
	}
	
	public List<Order> action() {
		// determine to buy or to sell
		Random random = new Random();
		double probToSell = this.rho * S_i_t + 0.5;
		
		List<Order> odrList = new ArrayList<Order>();
		if (random.nextDouble() < probToSell) {
			double p_sell;
			//p_sell = Market.p_t[Market.t] * (1 + theta_s);
			p_sell = Market.a_q_t - theta;
			odrList.add(new Order(id, tau_m, Order.Type.LIMIT, Order.Buysell.SELL, p_sell, 1));
		} else {
			double p_buy;
			//p_buy = Market.p_t[Market.t] * (1 - theta_b);
			p_buy = Market.b_q_t + theta;
			odrList.add(new Order(id, tau_m, Order.Type.LIMIT, Order.Buysell.BUY, p_buy, 1));
		}
		
		return odrList;
	}
	
	public void learn() {
		Random random = new Random();
		this.setLambda(lambda_m + (random.nextDouble() * 2d - 1d) * lambda_k);
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
