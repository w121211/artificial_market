package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import data.AgentRecord;
import data.TradeRecord;


public class Orderbook {
	
	private static ArrayList<Order> orderBuyList;
	private static ArrayList<Order> orderSellList;
	
	public Orderbook() {
		orderBuyList = new ArrayList<Order>();
		orderSellList = new ArrayList<Order>();
	}
	
	public void executeOrder(Order odr) {
		
		if (odr.getType() == Order.Type.MARKET ) {
			System.out.println("Market Order!");
		} else {
			if (odr.getBuysell() == Order.Buysell.BUY)
				executeLimitBuyOrder(odr);
			else
				executeLimitSellOrder(odr);
		}
	}
	
	private void executeLimitBuyOrder(Order odr) {
		while (true) {
			if (orderSellList.isEmpty()) {
				addLimitOrder(odr);
				break;
			} else {
				if (odr.getPrice() >= this.getBestAskPrice()) {		// execute order
					Order sellOdr = orderSellList.get(0); 
					int tmpVol = sellOdr.getVolume() - odr.getVolume();
					
					if (tmpVol > 0) {				// sell order volume > buy order volume 
						this.transaction(
								odr.getAgentID(),
								sellOdr.getAgentID(),
								Order.Buysell.BUY,
								odr.getVolume(),
								sellOdr.getPrice());
						sellOdr.setVolume(tmpVol);
						break;
					} else if (tmpVol == 0) {		// sell order volume = buy order volume
						this.transaction(
								odr.getAgentID(),
								sellOdr.getAgentID(),
								Order.Buysell.BUY,
								odr.getVolume(),
								sellOdr.getPrice());
						this.removeLimitOrder(sellOdr);
						break;
					} else {						// sell order volume < buy order volume
						this.transaction(
								odr.getAgentID(),
								sellOdr.getAgentID(),
								Order.Buysell.BUY,
								sellOdr.getVolume(),
								sellOdr.getPrice());
						this.removeLimitOrder(sellOdr);
						odr.setVolume(-tmpVol);
					}
				} else {							// add to buy orderbook
					addLimitOrder(odr);
					break;
				}
			}
		}
	}
	
	private void executeLimitSellOrder(Order odr) {
		while (true) {
			if (orderBuyList.isEmpty()) {
				addLimitOrder(odr);
				break;
			} else {				
				if (odr.getPrice() <= this.getBestBidPrice()) {		// execute order
					Order buyOdr = orderBuyList.get(0); 
					int tmpVol = buyOdr.getVolume() - odr.getVolume();
					
					if (tmpVol > 0) {				// sell order volume > buy order volume 
						this.transaction(
								odr.getAgentID(),
								buyOdr.getAgentID(),
								Order.Buysell.SELL,
								odr.getVolume(),
								buyOdr.getPrice());
						buyOdr.setVolume(tmpVol);
						break;
					} else if (tmpVol == 0) {		// sell order volume = buy order volume
						this.transaction(
								odr.getAgentID(),
								buyOdr.getAgentID(),
								Order.Buysell.SELL,
								odr.getVolume(),
								buyOdr.getPrice());
						this.removeLimitOrder(buyOdr);
						break;
					} else {						// sell order volume < buy order volume
						this.transaction(
								odr.getAgentID(),
								buyOdr.getAgentID(),
								Order.Buysell.SELL,
								buyOdr.getVolume(),
								buyOdr.getPrice());
						this.removeLimitOrder(buyOdr);
						odr.setVolume(-tmpVol);
					}
				} else {							// add to buy orderbook
					addLimitOrder(odr);
					break;
				}
			}
		}
	}
	
	private boolean transaction(String demanderId,
								String providerId,
								Order.Buysell buysell,
								int volume,
								double price) {
		
		String buyerId, sellerId;
		if (buysell == Order.Buysell.BUY) {
			buyerId = demanderId;
			sellerId = providerId;
		} else {
			buyerId = providerId;
			sellerId = demanderId;
		}
		
		Agent buyer = Market.agents.get(buyerId);
		Agent seller = Market.agents.get(sellerId);
		
		double buyerCash = buyer.getC_i_t();	// get agent status
		int buyerStock = buyer.getS_i_t();
		double sellerCash = seller.getC_i_t();
		int sellerStock = seller.getS_i_t();
		
		buyer.setC_i_t(buyerCash - (price * volume));		// update buyer status
		buyer.setS_i_t(buyerStock + volume);
		seller.setC_i_t(sellerCash + (price * volume));	// update seller status
		seller.setS_i_t(sellerStock - volume);
		Market.p_trade_t = price;		// update transaction price
		
		/*
		double buyerCash = Market.agents.get(buyerId).getC_i_t();	// get agent status
		int buyerStock = Market.agents.get(buyerId).getS_i_t();
		double sellerCash = Market.agents.get(sellerId).getC_i_t();
		int sellerStock = Market.agents.get(sellerId).getS_i_t();
		
		Market.agents.get(buyerId).setC_i_t(buyerCash - (price * volume));		// update buyer status
		Market.agents.get(buyerId).setS_i_t(buyerStock + volume);
		Market.agents.get(sellerId).setC_i_t(sellerCash + (price * volume));	// update seller status
		Market.agents[sellerId].setS_i_t(sellerStock - volume);
		Market.p_trade_t = price;		// update transaction price
		*/
		
		Market.log.add(new TradeRecord(
				Market.t,
				demanderId,
				providerId,
				buysell,
				volume,
				price));
		Market.log.add(new AgentRecord(
				Market.t,
				Market.agents.get(demanderId)));
		Market.log.add(new AgentRecord(
				Market.t,
				Market.agents.get(providerId)));
		
		return true;
	}
	
	private void addLimitOrder(Order odr) {		
		if (odr.getType() == Order.Type.MARKET)
			return;	
		if (odr.getBuysell() == Order.Buysell.BUY) {
			orderBuyList.add(odr);
			Collections.sort(orderBuyList, new OrderComparator());
		} else {
			orderSellList.add(odr);
			Collections.sort(orderSellList, new OrderComparator());
		}
	}
	
	public void removeLimitOrder(Order odr) {		
		if (odr.getType() == Order.Type.MARKET)
			return;		
		if (odr.getBuysell() == Order.Buysell.BUY) {
			orderBuyList.remove(odr);
			Collections.sort(orderBuyList, new OrderComparator());
		} else {
			orderSellList.remove(odr);
			Collections.sort(orderSellList, new OrderComparator());
		}
	}
	
	public void removeExpiredOrders() {		
		Order odr;
		Iterator<Order> itr = orderBuyList.iterator();
		while (itr.hasNext()) {
	    	odr = itr.next();
	    	if (odr.getArriveTime() + odr.getLength() < Market.t) {
	    		orderBuyList.remove(odr);
	    		itr = orderBuyList.iterator();
	    	}
	    }
		Collections.sort(orderBuyList, new OrderComparator());
		
		itr = orderSellList.iterator();
	    while (itr.hasNext()) {
	    	odr = itr.next();
	    	if (odr.getArriveTime() + odr.getLength() < Market.t) {
	    		orderSellList.remove(odr);
    			itr = orderSellList.iterator();
	    	}
	    }
		Collections.sort(orderSellList, new OrderComparator());
	}
	
	public void printOrderbook() {
		System.out.println("=== t:" + Market.t + ", p:" + Market.p_t[Market.t] + " ===");
		System.out.println("= Sell Orders =");
		Iterator<Order> itr;
		for (int i = orderSellList.size() - 1; i >= 0; i--)
			System.out.println(orderSellList.get(i));

	    itr = orderBuyList.iterator();
	    System.out.println("= Buy Orders =");
		while (itr.hasNext()) {
	    	System.out.println(itr.next());
	    }
	    System.out.println("");
	}
	
	public double getBestBidPrice() {
		if (!orderBuyList.isEmpty())
			return orderBuyList.get(0).getPrice();
		else
			return Double.NaN;
	}
	
	public double getBestAskPrice() {
		if (!orderSellList.isEmpty())
			return orderSellList.get(0).getPrice();
		else
			return Double.NaN;
	}
	
	public double getMidPrice() {
		double ask = this.getBestAskPrice();
		double bid = this.getBestBidPrice();
		if (Double.isNaN(ask) || Double.isNaN(bid))
			return 0d;
		else
			return (ask + bid) / 2;	
	}
	
	
	public int getBuyOrderDepth(double limitPercentage) {
		double p = this.getMidPrice();
		if (p <= 0d)
			p = Market.p_t[Market.t];
		p = p * (1d - limitPercentage);
		
		int vol = 0;
		Order odr;
		Iterator<Order> itr = orderBuyList.iterator();
		while (itr.hasNext()) {
	    	odr = itr.next();
	    	if (odr.getPrice() >= p)
	    		vol += odr.getVolume();
	    }
		return vol;
	}
	
	public int getSellOrderDepth(double limitPercentage) {
		double p = this.getMidPrice();
		if (p <= 0d)
			p = Market.p_t[Market.t];
		p = p * (1d + limitPercentage);
		
		int vol = 0;
		Order odr;
		Iterator<Order> itr = orderSellList.iterator();
		while (itr.hasNext()) {
	    	odr = itr.next();
	    	if (odr.getPrice() <= p)
	    		vol += odr.getVolume();
	    }
		return vol;
	}
	
	public ArrayList<Order> getOrderBuyList() { return orderBuyList; }
	
	public ArrayList<Order> getOrderSellList() { return orderSellList; }
}
