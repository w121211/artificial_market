package simulation;

import data.Log;
import market.Market;

public class Main implements Global {
	
	public static void main(String args[]) {
		
		// Run simulations
		for (int i = 0; i < Global.SIMULATION_RUN; i++) {
			String path = System.getProperty("user.dir");
			path += "/log/";
			if (IS_RANDOM_WALK)
				path += "rf-";		// random walk fundamental price
			else
				path += "sf-";		// static fundamental price
			if (HFT_AGENT_NUMBER == 0)
				path += "nm-";		// no market makers
			else if (IS_LEARNING)
				path += "ll-";		// learning
			else
				path += "nl-";		// non-learning
			path += String.format("%.1f-%.1f", lambda_mu, lambda_sd);
			Log log = new Log(path, i);
			
			Market market = new Market(log);
			market.init();
			market.start();
			
			log.closeWriters();
			System.out.println("[RUN]Simulation run " + i + " completed");
		}
		System.out.println("[RUN]All simulations completed");
		
		// Analysing log
		//MultiLogAnalyser analyser = new MultiLogAnalyser(Global.logs);
		//analyser.getResult().out();
	}
}
