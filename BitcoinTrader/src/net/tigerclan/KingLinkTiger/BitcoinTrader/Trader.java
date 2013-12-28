package net.tigerclan.KingLinkTiger.BitcoinTrader;

public class Trader{
	float usd_balance = 0;
	float btc_balance = 0;
	History history;
	
	public Trader(float usdb, float btcb, History h) {
		usd_balance = usdb;
		btc_balance = btcb;
		history = h;
	}

	public void buy(float USD) {
		/*
		 * Check to see if we have a USD balance of 0 or if the amount
		 * of USD we want to buy is greater than our balance. If this is
		 * the case then you are unable to buy.
		 */
		if(usd_balance == 0f || USD > usd_balance){
			System.out.println("You don't have enough USD. USD Asked for: " + USD + " USD Balance: " + usd_balance);
			return;
		}

		/*
		 * Check if the amount of USD we want to buy is less then or equal to our
		 * USD balance. If it is then we have enough money to issue a trade.
		 */
		if(USD <= usd_balance){
			float lastTrade = history.getLastTrade();
			usd_balance -= USD;
			btc_balance += USD/lastTrade;
			history.setLastBuyPrice(lastTrade);

			printBalance();
		}
	}

	public void sell(float BTC) {
		if(btc_balance == 0f || BTC > btc_balance){
			System.out.println("You don't have enough BTC.");
			return;
		}
		
		if(BTC <= btc_balance){
			float lastTrade = history.getLastTrade();
			usd_balance += BTC*lastTrade;
			btc_balance -= BTC;
			history.setLastSellPrice(lastTrade);
			
			printBalance();
		}		
	}
	
	public float getUSDBalance(){
		return usd_balance;
	}
	
	public float getBTCBalance(){
		return btc_balance;
	}
	
	
	public void printBalance(){
		System.out.println("USD: " + usd_balance + " BTC: " + btc_balance);
	}
	
}
