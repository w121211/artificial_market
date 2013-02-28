package simulation;

import java.text.DecimalFormat;

import data.Log;
import market.Market;

public class Main implements Global {
	
	public static void main(String args[]) {
		
		// Run simulations
		for (int i = 0; i < Global.SIMULATION_RUN; i++) {
			String path = System.getProperty("user.dir");
			path += "/log/";
			/*
			 * Filename: [rf,sf]-[ll,nl]-[%hft_num%]-[%lambda_mu%]-[%lambda_sd%]
			 * rf: random walk of fundamental price
			 * sf: static fundamental price
			 * ll: learning
			 * nn: no learning
			 * rr: random hft agents
			 *  
			 */
			if (IS_RANDOM_WALK)
				path += "rf-";		// random walk fundamental price
			else
				path += "sf-";		// static fundamental price
			
			switch(HFT_STRATEGY) {
			case POSITION_MARKET_MAKING:
				path += "pm-";	// position market maker
				break;
			case RANDOM:
				path += "rt-";	// random trader
				break;
			case SIMPLE_MARKET_MAKING:
				path += "sm-";	// simple market maker
				break;
			}
			
			if (IS_LEARNING)
				path += "ll-";		// learning
			else
				path += "nn-";		// non-learning
			
			path += new DecimalFormat("00").format(HFT_AGENT_NUMBER);
			path += String.format("-%.1f-%.2f-%d-%d-%.4f", 
					lambda_mu, lambda_sd, odr_hft_length, s_hft, rho);
			Log log = new Log(path, i);
			
			Market market = new Market(log);
			market.init();
			market.start();
			
			log.closeWriters();
			System.out.println("[RUN]Simulation run " + i + " completed");
		}
		System.out.println("[RUN]All simulations completed");
	}
}
