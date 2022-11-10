# db-code-challange
Trades transmission in tradestore

# Api
  GET http://localhost:8080/api/trades
  
  Response:
  [
    {
    "tradeId": "T1",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2020-05-20",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    },
    {
    "tradeId": "T2",
    "version": 2,
    "counterPartyId": "CP-2",
    "bookId": "B1",
    "maturityDate": "2021-05-20",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    },
    {
    "tradeId": "T2",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2015-03-14",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    },
    {
    "tradeId": "T3",
    "version": 3,
    "counterPartyId": "CP-3",
    "bookId": "B2",
    "maturityDate": "2014-05-20",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    }
    ]
  
  GET http://localhost:8080/api/trades/T2
  
  Response
  [
    {
    "tradeId": "T2",
    "version": 2,
    "counterPartyId": "CP-2",
    "bookId": "B1",
    "maturityDate": "2021-05-20",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    },
    {
    "tradeId": "T2",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2015-03-14",
    "createdDate": "2022-11-10",
    "isExpired": "Y"
    }
    ]
    
# Scheduler - It runs every 5 seconds on tradestore, update the expired flag to "Y" if any trade with maturity date crosses the Today's date.

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
