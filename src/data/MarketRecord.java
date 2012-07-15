package data;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import market.HftAgent;
import market.Market;

public class MarketRecord extends Record {
	
	//public int lft_t;
	public double p_f_t;
	public double p_t;
	public double p_lt;
	public double r_log_t;
	public double r_abs_t;
	public double spread;
	
	public DescriptiveStatistics hftWealth;
	public DescriptiveStatistics hftFreq;
	
	public MarketRecord(Market market) {
		
		this.t = market.t;
		this.p_f_t = market.p_f_t;
		this.p_t = market.p_t[market.t];
		this.spread = market.a_q_t - market.b_q_t;
		this.p_lt = market.p_lt[market.lt];
		
		this.hftWealth = new DescriptiveStatistics();
		this.hftFreq = new DescriptiveStatistics();
		for (HftAgent agent : market.marketMakers) {
			hftWealth.addValue(agent.getWealth());
			hftFreq.addValue(agent.lambda_m);
		}
	}
	
	public static String getHeader() {
		return new String("t,p_f_t,p_t,p_lt," +
				"r_log_t,r_abs_t,spread," +
				"hft_wealth_avg, hft_wealth_var, " +
				"hft_freq_avg, hft_freq_var");
	}
	
	public String toString() {
		String format = "%d,%.4f,%.4f,%.4f," +
				"%.6f,%.6f,%.4f," +
				"%.4f,%.4f," +
				"%.4f,%.4f\n";
		return String.format(format,
				t, p_f_t, p_t, p_lt,
				r_log_t, r_abs_t, spread,
				hftWealth.getMean(), hftWealth.getVariance(), 
				hftFreq.getMean(), hftFreq.getVariance()
				);
	}


}
