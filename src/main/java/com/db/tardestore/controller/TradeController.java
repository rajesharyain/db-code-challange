package com.db.tardestore.controller;

import com.db.tardestore.model.Trade;
import com.db.tardestore.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    /**
     * GET /trades
     * @return list of trades
     */
    @GetMapping("/trades")
    public List<Trade> findAllTrades() {
        return tradeService.findAll();
    }

    /**
     * GET /trades/T1
     * @param tradeId
     * @return list of trades by trade id
     */
    @GetMapping("/trades/{tradeId}")
    public List<Trade> findTradesById(@PathVariable("tradeId") String tradeId) {
        return tradeService.findById(tradeId);
    }

    /**
     * Add valid trade to the store
     * @param trade
     * @return
     */
    @PostMapping("/trade")
    public ResponseEntity<String> storeTrade(@RequestBody Trade trade) {
        if(isValidTrade(trade)) {
            log.info("Adding trade", trade.getTradeId());
            tradeService.save(trade);
            tradeService.print(trade);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Validate trade
     * @param trade
     * @return
     */
    private boolean isValidTrade(Trade trade) {
        validateMaturityDate(trade); //if valid proceed ahead
        List<Trade> existingTrades = tradeService.findById(trade.getTradeId());
        if(existingTrades.size() > 0) {
            //check if any received trade version is less than any existing trade version
            Optional<Trade> t = existingTrades.stream().filter(existingTrade -> (existingTrade.getVersion() > trade.getVersion())).findFirst();
            if(t.isPresent()) {
                throw new RuntimeException("Received version is lesser than the existing trade.");
            }
        }
        return true;
    }

    /**
     * Validate Trade's maturity date
     * @param trade
     */
    private void validateMaturityDate(Trade trade) {
        if(trade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Maturity Date should not be less than today's date!");
        }
    }

    /**
     * It's a scheduling task to run at every 5 seconds
     * which should update the status of Trade if maturity date crosses the today's date.
     * Status="Y" if Maturity Data < Today's Date
     */
    @Scheduled(cron = "*/5 * * * * *")
    public void updateTradeStatus() {
        log.info("The time is now {}", LocalTime.now());
        List<Trade> trades  = tradeService.findAll();
        if(trades.size() > 0) tradeService.updateTradesStatus(trades);
        else log.info("No trades available");
    }
}
