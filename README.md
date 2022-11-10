# db-code-challange
Trades transmission in tradestore
Write a Java programm with all the unit cases, prefferbly TDD approach.

## Problem Statement
There is a scenario where thousands of trades are following into one store, assume any way of transmission of trades. We need to create a one trade store, which stores the trade in the following order.

#### assignment1

TradeID  |  Version |  Counter-Part ID |  Book-ID |  Maturity Date |  Created Date      |  Expired
1. T1    |   1       |    CP-1         |     B1   |    20/05/2020  |      Today Date    |   N
2. T2    |   2       |    CP-2         |     B1   |    20/05/2021  |      Today Date    |   N
3. T2    |   1       |    CP-1         |     B1   |    20/05/2021  |      14/03/2015    |   N
4. T3    |   3       |    CP-3         |     B3   |    20/05/2014  |      Today Date    |   Y


There are couples of validation, we need to provide in the above assignment1. 
1. During transmission if the lower version is being received by the store it will reject the trade and throw an exception. If the version is same it will override the existing record.
2. Store should not allow the trade which has less maturity date then today date.
3. Store should automtically update the expire flag if in a store the trade crosses the maturity date.

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

# Test (TDD)
  Main test - TradeControllerTest.java
  src/test/java/com/db/tardestore/controller/TradeControllerTest.java
  
  The test file contains all the validation rules along with scheduler test
  1. testInitialTrades_success() :  This test will simply test the initial store data entered at application startup.
  2. testAddTrade_success : TYhis will validate the incoming Trade if valid will add the trade to store.
  3. testTradeWithInvalidMaturtyDate() : This will test Trade with Invalid Maturity date.
  4. testTradeWithLesserVersion() : This will test if the incoming Trade has version lesser than already present in the store.
  5. testTradeWithSameVersion_overwrite() : This will test if the incoming Trade version is same and will overwrites the  existing trade.
  6. testTradeUpdateStatus() : This is the test which will test the scheduler for updating the tasks.
  
# Build and Run
1. Clone the code - git clone https://github.com/rajesharyain/db-code-challange.git
2. Import project in to your eclipse/Intellij IDE
3. Run the application simply go to the below file and right click and run the application 
src/main/java/com/db/tardestore/TardeStoreApplication.java
      
