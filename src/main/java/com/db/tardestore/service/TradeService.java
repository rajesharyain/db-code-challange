package com.db.tardestore.service;

import com.db.tardestore.model.Trade;
import com.db.tardestore.store.TradeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeStore store;
    public List<Trade> findAll() {
        return store.getAllTrades();
    }
    public List<Trade> findById(String tradeId) {
        return store.getTradesById(tradeId);
    }
    public void save(Trade trade) {
        List<Trade> trades = findById(trade.getTradeId());
        if(trades.size() > 0) {
            for (Trade t : trades) {
                if (t.getVersion() == trade.getVersion()) {
                    store.updateTrade(t, trade);
                }
            }
        }else {
            store.addTrade(trade);
        }
    }

    public void updateTradesStatus(List<Trade> trades) {
        for (Trade t : trades) {
            if (t.getMaturityDate().isBefore(LocalDate.now())) {
                store.updateTradesStatus(t);
            }
        }
    }

    public void print(Trade trade) {
        store.print();
    }
}
