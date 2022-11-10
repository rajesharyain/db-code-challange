package com.db.tardestore;

import com.db.tardestore.model.Trade;
import com.db.tardestore.store.TradeStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class TardeStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TardeStoreApplication.class, args);
	}

	/**
	 * Add some sort of initial data to store
	 * @param store
	 * @return
	 */
	@Bean
	public CommandLineRunner loadData(TradeStore store) {
		return (args) -> {
			store.addTrade(new Trade("T1", 1, "CP-1", "B1", LocalDate.of(2020, 05, 20), LocalDate.now(), "N"));
			store.addTrade(new Trade("T2", 2, "CP-2", "B1", LocalDate.of(2021, 05, 20), LocalDate.now(), "N"));
			store.addTrade(new Trade("T2", 1, "CP-1", "B1", LocalDate.of(2015, 03, 14), LocalDate.now(), "N"));
			store.addTrade(new Trade("T3", 3, "CP-3", "B2", LocalDate.of(2014, 05, 20), LocalDate.now(), "Y"));
			store.print();
		};

	}

}
