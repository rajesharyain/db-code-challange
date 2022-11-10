package com.db.tardestore.store;

import com.db.tardestore.model.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TradeStore {
    private static List<Trade> tradeStore = new ArrayList<>();

    public boolean isStoreEmpty() {return tradeStore.isEmpty();}
    public int size() {return tradeStore.size();}
    public void addTrade(Trade trade){
        tradeStore.add(trade);
    }

    public List<Trade> getAllTrades(){
       return tradeStore;
    }

    public Optional<Trade> getTradeById(String id) {
        return tradeStore.stream().filter(item -> item.getTradeId().equals(id)).findFirst();
    }
    public void updateTrade(Trade oldTrade, Trade newTrade){
        int index = tradeStore.indexOf(oldTrade);
        tradeStore.set(index, newTrade);
    }

    public void print() {
        tradeStore.stream().forEach(trade -> log.info(trade.toString()));
    }

    public List<Trade> getTradesById(String tradeId) {
        return tradeStore.stream().filter(item -> item.getTradeId().equals(tradeId)).collect(Collectors.toList());
    }

    public void updateTradesStatus(Trade trade) {
        int index = tradeStore.indexOf(trade);
        trade.setIsExpired("Y");
        tradeStore.set(index, trade);
    }
}
