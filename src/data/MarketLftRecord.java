package data;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import market.HftAgent;
import market.LftAgent;
import market.Market;

public class MarketLftRecord extends Record {
	
//	public int lt;
	public double p_f_t;
	public double p_lt;
	public double spread;
	public DescriptiveStatistics hftWealth;
	public DescriptiveStatistics hftFreq;
	public DescriptiveStatistics hftPos;		// HFT position
	public DescriptiveStatistics lftPos;		// LFT position
	
	public MarketLftRecord(Market market) {
		this.t = market.t - 1;
		this.lt = market.lt;
		this.p_f_t = market.p_f_t;
		this.p_lt = market.p_lt[market.lt];
		this.spread = market.a_q_t - market.b_q_t;
		
		this.hftWealth = new DescriptiveStatistics();
		this.hftFreq = new DescriptiveStatistics();
		this.hftPos = new DescriptiveStatistics();
		this.lftPos = new DescriptiveStatistics();
		for (HftAgent agent : market.marketMakers) {
			hftWealth.addValue(agent.getWealth());
			hftFreq.addValue(agent.lambda_m);
			hftPos.addValue(agent.getS_i_t());
		}
		for (LftAgent agent : market.stylizedTraders) {
			lftPos.addValue(agent.getS_i_t());
		}
	}
	
	public static String getHeader() {
		return new String("t,lt,p_f_t,p_lt,spread," +
				"hft_wealth_mu,hft_wealth_sd," +
				"hft_freq_mu,hft_freq_sd," +
				"hft_pos,lft_pos");
	}
	
	public String toString() {
		String format = "%d,%d,%.4f,%.4f,%.4f," +
				"%.4f,%.4f," +
				"%.4f,%.4f," +
				"%.4f,%.4f\n";
		return String.format(format,
				t, lt, p_f_t, p_lt, spread,
				hftWealth.getMean(), hftWealth.getStandardDeviation(), 
				hftFreq.getMean(), hftFreq.getStandardDeviation(),
				hftPos.getSum(), lftPos.getSum()
				);
	}
}
