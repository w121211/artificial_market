package simulation;

public interface Global {
	enum HftStrategy {
		SIMPLE_MARKET_MAKING,
		POSITION_MARKET_MAKING,
		RANDOM;
	}
	enum Scenario {
		NORMAL,
		CRASH;
	}
	
	// Simulation Settings
	Scenario SCENARIO = Scenario.CRASH;
	int SIMULATION_LFT_TIME = 2000;		// default: 10,000
	int SIMULATION_RUN = 100;			// default: 100
	int LFT_AGENT_NUMBER = 10000;		// default: 10,000
	int HFT_AGENT_NUMBER = 10;			// default: 10
	int TRAINING_INITIAL_LFT_T = 100;	// default: 100
	int TRAINING_LFT_T = 10;			// defulat: 10
	
	
	
	// initial market setting
	boolean IS_RANDOM_WALK = true;
	double tick = 0.0005;			// default = 0.0005
	double p_f_mu = 0;				// fundamental price, geometric Brownian motion, percentage drift
	double p_f_sigma = 1E-3;		// fundamental price, geometric Brownian motion, percentage volatility
	double p_f_0 = 100d;			// fundamental price
	int ORDER_MIN_LENGTH = 0;		// default: 0
	
	// Crash scenario settings:
	double FALL_SPEED = tick * 0;	// 0: vertical fall, else: p_f falling speed per lt step
		
	
	// market maker parameters
	HftStrategy HFT_STRATEGY = HftStrategy.SIMPLE_MARKET_MAKING;
	boolean IS_LEARNING = false;
	double rho = 0.002d;			// reference probability to sell or buy, default: 0.002
	int odr_hft_length = 1;			// 0: dynamic or a fixed length
	int s_hft = 50;					// buy or sell stock amount, default: 5
	double tau_h = 1d;				// reference time horizon
	double lambda_mu = 1.0d;
	double lambda_sd = 0d;
	double lambda_delta = 0.1;		// maximum incremental value of learning
	double theta = tick;			// static theta
	
	// position market making
	double w_1 = 1E-7;				// weight a
//	double w_2 = 1E-3;				// weight b
	double w_3 = w_1 * 0.5d;		// weight c
 
	// Stylized trader parameters
	double tau_lft = 1000d;			// time scale for mean reversion to the fundamental
	//double alpha = 0.005;			// reference level of risk aversion
	double g_1_mu = 0d;				// expected value of fundamentalist weight, g_i_1
	double g_1_sigma = 6d;			// variance of fundamentalist weight, g_i_1, default: 6 
	double g_2_mu = 0d;				// expected value of chartist weight, g_2_1	
	double g_2_sigma = 13d;			// variance of chartist weight, g_i_2, default: 13
	double g_3_mu = 0d;				// expected value of noise weight, g_i_3
	double g_3_sigma = 1d;			// variance of noise weight, g_i_3, default: 1
	double epsilon_mu = 0d;			// mean of epsilon
	double epsilon_sigma = 0.1d;	// variance of epsilon, defulat: 0.1
	double k = 0.1d;				// order price variance, +k or -k, default: 0.1
	int s_lft = 10;					// coefficient of buy/sell stock volume, default: 10
}
