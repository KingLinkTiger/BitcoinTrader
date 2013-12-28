/*
 * This file is the Main/Runner file of the entire program, it handles
 * connecting to the exchange and getting new values. When it finds a 
 * new value it runs the run method in the Strategy class.
 */

package net.tigerclan.KingLinkTiger.BitcoinTrader;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.dto.marketdata.OrderBookUpdate;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.mtgox.v2.MtGoxExchange;
import com.xeiam.xchange.mtgox.v2.service.streaming.MtGoxStreamingConfiguration;
import com.xeiam.xchange.service.streaming.ExchangeEvent;
import com.xeiam.xchange.service.streaming.ExchangeStreamingConfiguration;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;

public class Main {

	public static void main(String[] args) throws ExecutionException,
			InterruptedException {

		Main streamingTickerDemo = new Main();
		
		
		streamingTickerDemo.start();
	}

	public void start() throws ExecutionException, InterruptedException {

		// Use the default MtGox settings
		Exchange mtGoxExchange = ExchangeFactory.INSTANCE
				.createExchange(MtGoxExchange.class.getName());

		// Configure BTC/EUR ticker stream for MtGox
		// see https://mtgox.com/api/2/stream/list_public, for a list of
		// channels to choose from
		ExchangeStreamingConfiguration mtGoxStreamingConfiguration = new MtGoxStreamingConfiguration(
				10, 10000, 60000, true, "ticker.BTCUSD");

		// Interested in the public streaming market data feed (no
		// authentication)
		StreamingExchangeService streamingMarketDataService = mtGoxExchange
				.getStreamingExchangeService(mtGoxStreamingConfiguration);

		// Open the connections to the exchange
		streamingMarketDataService.connect();

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<?> mtGoxMarketDataFuture = executorService
				.submit(new MarketDataRunnable(streamingMarketDataService));

		// the thread waits here until the Runnable is done.
		mtGoxMarketDataFuture.get();

		executorService.shutdown();

		// Disconnect and exit
		System.out.println(Thread.currentThread().getName()
				+ ": Disconnecting...");
		streamingMarketDataService.disconnect();
	}

	/**
	 * Encapsulates some market data monitoring behavior
	 */
	class MarketDataRunnable implements Runnable {
		
		Strategy strategy = new Strategy();
		
		
		private final StreamingExchangeService streamingExchangeService;

		/**
		 * Constructor
		 * 
		 * @param streamingExchangeService
		 */
		public MarketDataRunnable(
				StreamingExchangeService streamingExchangeService) {

			this.streamingExchangeService = streamingExchangeService;
		}

		@Override
		public void run() {

			try {

				while (true) {

					ExchangeEvent exchangeEvent = streamingExchangeService
							.getNextEvent();

					switch (exchangeEvent.getEventType()) {

					case CONNECT:
						System.out.println("Connected!");
						break;

					case TICKER:
						Ticker ticker = (Ticker) exchangeEvent.getPayload();
						String[] parts = ticker.getLast().toBigMoney().toString().split(" ");
						strategy.run(Float.parseFloat(parts[1]));
						break;

					case TRADE:
						Trade trade = (Trade) exchangeEvent.getPayload();
						System.out.println(trade.toString());
						break;

					case DEPTH:
						OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent
								.getPayload();
						System.out.println(orderBookUpdate.toString());
						break;

					default:
						break;
					}

				}
			} catch (InterruptedException e) {
				System.out.println("ERROR in Runnable!!!");
			}

		}
	}
}
