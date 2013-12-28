/*
 * TODO: Make a moving range of values to look at rather than keep ALL values.
 * TODO: Possibly store old values and be able to read them later. (This may lead to stale unusable data later)
 */
package net.tigerclan.KingLinkTiger.BitcoinTrader;

import java.util.ArrayList;

public class History {
	ArrayList<Float> history = new ArrayList<Float>();
	float lastBuyPrice = 0;
	float lastSellPrice = 0;
	
	float peak = 0;
	float trough = 0;
	
	boolean firstTrade = true;

	public void add(float lastTrade){
		history.add(lastTrade);
		
		if(firstTrade){
			setLastBuyPrice(lastTrade);
			setLastSellPrice(lastTrade);
			setPeak(lastTrade);
			setTrough(lastTrade);
			firstTrade = false;
		}
		
		//Must update this to make moving peaks
		if(lastTrade > getPeak()){
			setPeak(lastTrade);
		}
		
		//Must update this to make moving troughs
		if(lastTrade < getTrough()){
			setTrough(lastTrade);
		}
			
		if(history.size() > 1){	
			System.out.println("Adding History: " + lastTrade + " Diff: " + getDiff());
		}else{
			System.out.println("Adding History: " + lastTrade);
		}
	}
	
	public float getLastTrade(){
		return history.get(history.size()-1);
	}
	
	public float getLastBuyPrice(){
		return lastBuyPrice;
	}
	
	public float getLastSellPrice(){
		return lastSellPrice;
	}
	
	public float getPeak(){
		return peak;
	}
	
	public float getTrough(){
		return trough;
	}

	public double getDiff(){
		return (getLastTrade()/history.get(history.size()-2));
	}
	
	
	
	public void setPeak(float p){
		peak = p;
	}
	
	public void setTrough(float t){
		trough = t;
	}
	
	public void setLastBuyPrice(float lbp){
		lastBuyPrice = lbp;
	}
	
	public void setLastSellPrice(float lsp){
		lastSellPrice = lsp;
	}

}
