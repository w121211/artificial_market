package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import umontreal.iro.lecuyer.stochprocess.GeometricBrownianMotion;
import umontreal.iro.lecuyer.rng.RandomStream;
import umontreal.iro.lecuyer.rng.MRG32k3a;

import data.*;
import simulation.Global;

public class Market implements Global {
	
	protected static Log log;
	protected static Orderbook orderbook;
	public static List<LftAgent> stylizedTraders;
	public static List<HftAgent> marketMakers;
	public static HashMap<String, Agent> agents;
	
	public static int t;					// time t
	public static int lt;
	public static double a_q_t;				// ask price quoted at time t
	public static double b_q_t;				// bid price quoted at time t
	public static double p_f_t;
	public static double p_trade_t;			// transaction price at time t
	public static double[] p_t;				// market price at time t
	public static double[] p_lt;			// market price at time lft_t
	
	public Market(Log log) {
		this.log = log;
	}
	
	public void init() {
		orderbook = new Orderbook();
//		stylizedTraders = new LftAgent[LFT_AGENT_NUMBER];
		stylizedTraders = new ArrayList<LftAgent>();
		marketMakers = new ArrayList<HftAgent>();
		agents = new HashMap<String, Agent>();
		t = 0;
		lt = 0;
		p_t = new double[10000000];
		p_t[t] = p_f_0;
		p_lt = new double[10000000];
		p_lt[lt] = p_f_0;
		p_f_t = p_f_0;
		p_trade_t = 0d;
		
		int id = 0;
		for (int i = 0; i < LFT_AGENT_NUMBER; i++) {
			LftAgent agent = new LftAgent("L" + id);
			stylizedTraders.add(agent);
			agents.put(agent.getId(), agent);
			log.add(new AgentRecord(t, agent));
			id++;
		}
		for (int i = 0; i < HFT_AGENT_NUMBER; i++) {
			HftAgent agent = new HftAgent("H" + id);
			marketMakers.add(agent);
			agents.put(agent.getId(), agent);
			log.add(new AgentRecord(t, agent));
			id++;
		}
		
		log.add(new MarketRecord(this));
	}
	
	public void start() {
		switch(SCENARIO) {
		case NORMAL:
			normalScenarioStart();
			break;
		case CRASH:
			crashScenarioStart();
			break;
		}
	}
	
	public void crashScenarioStart() {
		Random random = new Random();
		t = 1;
		lt = 1;
		p_t[t] = p_f_0;
		p_lt[lt] = p_f_0;
		List<Agent> tradeWaitingList = new ArrayList<Agent>();
		
		for (lt = 1; lt < SIMULATION_LFT_TIME; lt++) {
			System.out.println("[RUN]lt: " + lt);
			tradeWaitingList.clear();
			
			// determine p_f_t
			int t1 = 1000;
			double p1 = 90;
			if (lt > t1) {
				if (FALL_SPEED > 0) {
					if (p_f_t <= p1) p_f_t = p1;
					else p_f_t = p_f_0 + FALL_SPEED * (lt - t1);
				} else {
					p_f_t = p1;
				}
			} else {
				p_f_t = p_f_0;
			}
			
			// select one stylized trader randomly, then put to trade waiting list
			if (LFT_AGENT_NUMBER > 0) {
				int i = (int)(random.nextDouble() * LFT_AGENT_NUMBER);
				tradeWaitingList.add(agents.get("L" + i));
			}
			
			// check each market maker's willingness to trade, then put to trade waiting list
			for (HftAgent agent : marketMakers) {
				if (agent.lambda_m > random.nextDouble())
					tradeWaitingList.add(agent);
			}
			
			// process waiting list, randomly choose one trader to trade then remove from list, loop
			boolean isLftTrade = false;
			while (!tradeWaitingList.isEmpty()) {
				int i;
				if (!isLftTrade) {
					i = 0;
					isLftTrade = true;
				} else
					i = random.nextInt(tradeWaitingList.size());
				
				Agent agent = tradeWaitingList.get(i);
				List<Order> odrs = agent.action();
				if (!odrs.isEmpty()) {
					for (Order odr : odrs) {
						odr.setArrivedTimes(t, lt);
						//log.add(new OrderRecord(odr));
						orderbook.executeOrder(odr);
						this.update();
						t++;
					}
				}
				tradeWaitingList.remove(i);
			}
			
			// update lt
			p_lt[lt+1] = p_t[t];
			log.add(new MarketLftRecord(this));
			log.add(new OrderbookRecord(lt, p_t[t], orderbook));
		}
	}
	
	public void normalScenarioStart() {
		Random random = new Random();
		t = 1;
		lt = 1;
		p_t[t] = p_f_0;
		p_lt[lt] = p_f_0;
		
		GeometricBrownianMotion gbm = new GeometricBrownianMotion(
				p_f_0, p_f_mu, p_f_sigma, new MRG32k3a());
		gbm.setObservationTimes(1, SIMULATION_LFT_TIME);
		
		List<Agent> tradeWaitingList = new ArrayList<Agent>();
		for (lt = 1; lt < SIMULATION_LFT_TIME; lt++) {
			System.out.println("[RUN]lt: " + lt);
			
			tradeWaitingList.clear();
			
			// determine p_f_t
			if (IS_RANDOM_WALK) {
				p_f_t = gbm.nextObservation();
			} else {
				p_f_t = p_f_0;
			}
			
			// select one stylized trader randomly, then put to trade waiting list
			if (LFT_AGENT_NUMBER > 0) {
				int i = (int)(random.nextDouble() * LFT_AGENT_NUMBER);
				tradeWaitingList.add(agents.get("L" + i));
			}
			
			// check each market maker's willingness to trade, then put to trade waiting list
			for (HftAgent agent : marketMakers) {
				if (agent.lambda_m > random.nextDouble())
					tradeWaitingList.add(agent);
			}
			
			// process waiting list, randomly choose one trader to trade then remove from list, loop
			boolean isLftTrade = false;
			while (!tradeWaitingList.isEmpty()) {
				int i;
				if (!isLftTrade) {
					i = 0;
					isLftTrade = true;
				} else
					i = random.nextInt(tradeWaitingList.size());
				
				Agent agent = tradeWaitingList.get(i);
				List<Order> odrs = agent.action();
				if (!odrs.isEmpty()) {
					for (Order odr : odrs) {
						if (odr.getLength() < ORDER_MIN_LENGTH)
							odr.setLength(ORDER_MIN_LENGTH);
						odr.setArrivedTimes(t, lt);
						log.add(new OrderRecord(odr));
						orderbook.executeOrder(odr);
						this.update();
						t++;
					}
				}
				tradeWaitingList.remove(i);
			}
			
			// learning
			if (IS_LEARNING) {
				if (lt > TRAINING_INITIAL_LFT_T && 
						lt % TRAINING_LFT_T == 0) {
					this.learning();
				}
			}
			
			// update lt
			p_lt[lt+1] = p_t[t];
			log.add(new MarketLftRecord(this));
			log.add(new OrderbookRecord(lt, p_t[t], orderbook));
		}
	}
	
	public void learning() {
		// ranking, separated into top, middle, bottom classes
		Collections.sort(marketMakers, new AgentComparator());
		int n = marketMakers.size();
		List<HftAgent> bots = 
				marketMakers.subList(0, n / 3);
		List<HftAgent> mids = 
				marketMakers.subList(n / 3, n * 2 / 3);
		List<HftAgent> tops = 
				marketMakers.subList(n * 2 / 3, n);
		
		// bottom class learning
		Random random = new Random();
		n = tops.size();
		for (HftAgent agent : bots) {
			int i = (int)(random.nextDouble() * n);
			agent.learn(tops.get(i));
		}
						
		// middle class learning
		for (HftAgent agent : mids) {
			agent.learn();
		}
		
	}
	
	public void update() {
//		orderbook.printOrderbook();
//		orderbook.removeExpiredOrders();
		
		// update prices
		if (p_trade_t > 0.0) {
			p_t[t+1] = p_trade_t;
		} else {							
			if (orderbook.getMidPrice() != 0)
				p_t[t+1] = orderbook.getMidPrice();
			else
				p_t[t+1] = p_t[t];
		}
		p_trade_t = 0d;
		//p_delta_t = p_t[t] - p_t[t-1];
		a_q_t = orderbook.getBestAskPrice();
		b_q_t = orderbook.getBestBidPrice();
		
		// update hft
		for (HftAgent agent : marketMakers) {
			agent.setWealth(p_t[t]);
		}
		
		log.add(new MarketRecord(this));		// add logs
	}
	
	public Log getLog() {
		return log;
	}
}
