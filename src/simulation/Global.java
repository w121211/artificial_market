package simulation;

public interface Global {
	
	// simulation Settings
	int LFT_AGENT_NUMBER = 10000;
	int HFT_AGENT_NUMBER = 0;
	int SIMULATION_LFT_TIME = 50000;
	int TRAINING_INITIAL_LFT_T = 100;
	int TRAINING_LFT_T = 10;
	int SIMULATION_RUN = 10;
	boolean IS_LEARNING = true;
		
	// initial market setting
	boolean IS_RANDOM_WALK = true;
	double tick = 0.0005;
	double p_f_mu = 0;				// fundamental price, geometric Brownian motion, percentage drift
	double p_f_sigma = 0.001d;		// fundamental price, geometric Brownian motion, percentage volatility
	double p_f_0 = 300d;			// fundamental price
	
	// market maker parameters
	double theta = tick;			// static theta
	//double theta_mu = 0d;			// dynamic theta, average 
	//double theta_sigma = 0.01d;	// dynamic theta, variance
	double lambda_mu = 0.3d;
	double lambda_sd = 0.3d;
	double lambda_k = 0.05;			// maximum incremental value of learning
	double tau_h = 1d;				// reference time horizon
	double rho = 0.001d;			// reference probability to sell or buy
	
	// Stylized trader parameters
	double tau_lft = 200d;			// time scale for mean reversion to the fundamental
	//double alpha = 0.005;			// reference level of risk aversion
	double g_1_mu = 0d;				// expected value of fundamentalist weight, g_i_1
	double g_1_sigma = 5d;			// variance of fundamentalist weight, g_i_1 
	double g_2_mu = 0d;				// expected value of chartist weight, g_2_1	
	double g_2_sigma = 20d;			// variance of chartist weight, g_i_2
	double g_3_mu = 0d;				// expected value of noise weight, g_i_3
	double g_3_sigma = 1d;			// variance of noise weight, g_i_3
	double epsilon_mu = 0d;			// mean of epsilon
	double epsilon_sigma = 0.1d;	// variance of epsilon
	double k = 0.1d;				// order price variance, +k or -k
	int s_i = 10;					// buy or sell stock amount
	
	// Analysis Setting
	int ana_acf_lags = 50;			// number of acf lags
	int ana_interval_1 = 5;			// time step interval
	int ana_interval_2 = 10;		// time step interval
	int ana_interval_3 = 50;		// time step interval
	int ana_interval_4 = 100;		// time step interval
	double ana_order_depth = 0.05;	// orderbook depth of mid price +- percentage
}
