package net.tigerclan.KingLinkTiger.BitcoinTrader;

public class Strategy{
	History history = new History();
	Trader trader = new Trader(0f, 0.17f, history);
	
	public void run(float lastTrade){
		history.add(lastTrade); //DO NOT REMOVE THIS LINE!

		/*
		 * Insert your trading strategy here.
		 */
	
			/*
			 * START Example Trading Strategy
			 */
			if(trader.getUSDBalance() > 0.0f){
				if(lastTrade > history.getTrough()){
					//The price has gone up and "is" going up so buy
					trader.buy(trader.getUSDBalance());
					
				}
			}
			
			if(trader.getBTCBalance() > 0.0f){
				if(lastTrade < history.getPeak()){
					//The price has gone down and "is" going down so sell
					trader.sell(trader.getBTCBalance());
				}
			}

			/*
			 * END Example Trading Strategy
			 */
	}
}
