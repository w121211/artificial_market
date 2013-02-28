package data;

import market.Order;
import market.Orderbook;

public class OrderbookRecord extends Record {

	public static double pricePercent[] = {0.01d, 0.02d, 1d};	// price under a specific percentage
	public int hftBuyDepth[];	// hft buy volumes
	public int hftSellDepth[];	// hft sell volumes
	public int lftBuyDepth[];	// lft buy volumes
	public int lftSellDepth[];	// lft sell volumes
	
	private String odrList;
	
	public OrderbookRecord(int lt, double p, Orderbook orderbook) {
		this.lt = lt;	
		hftBuyDepth = new int[pricePercent.length];
		hftSellDepth = new int[pricePercent.length];
		lftBuyDepth = new int[pricePercent.length];
		lftSellDepth = new int[pricePercent.length];
		
		for (Order odr : orderbook.getOrderBuyList()) {
			for (int i = 0; i < pricePercent.length; i++) {
				if (odr.getPrice() <= p * (1 + pricePercent[i]) &&
						odr.getPrice() >= p * (1 - pricePercent[i])) {
					if (odr.getAgentID().contains("H")) {
						hftBuyDepth[i] += odr.getVolume();
					} else if (odr.getAgentID().contains("L")) {
						lftBuyDepth[i] += odr.getVolume();					
					}
				}
			}
		}
		for (Order odr : orderbook.getOrderSellList()) {
			for (int i = 0; i < pricePercent.length; i++) {
				if (odr.getPrice() <= p * (1 + pricePercent[i]) &&
						odr.getPrice() >= p * (1 - pricePercent[i])) {
					if (odr.getAgentID().contains("H")) {
						hftSellDepth[i] += odr.getVolume();
					} else if (odr.getAgentID().contains("L")) {
						lftSellDepth[i] += odr.getVolume();					
					}
				}
			}
		}
		odrList = "";
		odrList += lt + "," + p + ",";
		this.addRecord(hftBuyDepth);
		this.addRecord(hftSellDepth);
		this.addRecord(lftBuyDepth);
		this.addRecord(lftSellDepth);
		odrList += "\n";
	}
	
	public void addRecord(int[] data) {
		String format = "%d,";
		for (int d : data)
			odrList += String.format(format, d);
	}
	
	public static String getHeader() {
		return new String("lt,p,hb1,hb2,hba,hs1,hs2,hsa,lb1,lb2,lba,ls1,ls2,lsa,");
	}
	
	public String toString() { return odrList; }
}
