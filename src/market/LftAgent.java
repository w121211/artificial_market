package market;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simulation.Global;

public class LftAgent extends Agent implements Global {

	double g_i_1;		// weight of fundamentalist
	double g_i_2;		// weight of chartist
	double g_i_3;		// weight of noise
	int tau_i;			// time horizon of agent
	//double alpha_i;		// relative risk aversion
	
	double k_i;			// bid ask quote weight
	Random random;
	
	double r_hat_i;		// expected return
	double p_hat_i;		// expected price
	
	double b_i_t;		// bid price by agent i at time t
	double a_i_t;		// ask price by agent i at time t
	
	LftAgent(String id) {
		
		this.id = id;
		random = new Random();
		
		g_i_1 = Math.abs( random.nextGaussian() * g_1_sigma + g_1_mu );
		g_i_2 = random.nextGaussian() * g_2_sigma + g_2_mu;
		g_i_3 = Math.abs( random.nextGaussian() * g_3_sigma + g_3_mu );
		tau_i = (int)(tau_lft * (1 + g_i_1) / (1 + Math.abs(g_i_2)));
		
		//System.out.println("agent:" + this.toString());
	}
	
	public List<Order> action() {
		
		List<Order> odrList = new ArrayList<Order>();
		
		int t = Market.lt;
		double[] p_t = Market.p_lt;
		//System.out.println("p_t:" + p_t[t]);
		//System.out.println("p_f_t:" + Market.p_f_t);
		
		double r_bar_i = 0d;
		for (int j = 1; j <= tau_i; j++) {
			if (t-j-1 >= 1)
				r_bar_i += Math.log(p_t[t-j] / p_t[t-j-1]);
		}
		r_bar_i = r_bar_i / tau_i;
		
		double epsilon_t = random.nextGaussian() * epsilon_sigma + epsilon_mu;	
		r_hat_i = (g_i_1 * Math.log(Market.p_f_t / p_t[t]) +				
				g_i_2 * r_bar_i + 
				g_i_3 * epsilon_t) / 
				(g_i_1 + Math.abs(g_i_2) + g_i_3);
		//System.out.println("r^:" + r_hat_i);
		
		p_hat_i = p_t[t] * Math.exp(r_hat_i);
		//System.out.println("p^:" + p_hat_i);
		
		double k_t = random.nextDouble() * k - k/2;
		double p = p_hat_i * (1 + k_t);
		if (p_hat_i < p_t[t]) {
			odrList.add(new Order(id, 10000000, Order.Type.LIMIT, Order.Buysell.SELL, p, s_i));
		}
		if (p_hat_i > p_t[t]) {
			odrList.add(new Order(id, 10000000, Order.Type.LIMIT, Order.Buysell.BUY, p, s_i));
		}
		
		return odrList;
	}

	@Override
	public String getProperty() {
		return String.format("{%.4f %.4f %.4f}", g_i_1, g_i_2, g_i_3);
	}
}