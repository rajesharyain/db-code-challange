package com.db.tardestore.controller;

import com.db.tardestore.model.Trade;
import org.awaitility.Duration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradeControllerTest {

    @SpyBean
    TradeController controller;

    @Order(1)
    @Test
    public void testInitialTrades_success() throws Exception {

        List<Trade> trades = controller.findAllTrades();
        Assertions.assertEquals(4,trades.size());
    }

    @Order(2)
    @Test
    public void testAddTrade_success() throws Exception {
        Trade trade = new Trade("T4", 3, "CP-4", "B4", LocalDate.of(2023, 05, 23), LocalDate.now(), "N");
        controller.storeTrade(trade);
        Trade newTrade = controller.findTradesById("T4").get(0);
        Assertions.assertEquals(trade.getTradeId(), newTrade.getTradeId());
    }

    @Order(3)
    @Test
    public void testTradeWithInvalidMaturtyDate() throws Exception {
        Trade trade = new Trade("T4", 3, "CP-4", "B4", LocalDate.of(2016, 05, 23), LocalDate.now(), "N");
        Exception ex = Assertions.assertThrows(Exception.class, () ->  controller.storeTrade(trade));
        String actualException = ex.getMessage();
        String expectedMessage = "Maturity Date should not be less than today's date!";
        Assertions.assertEquals(expectedMessage, actualException);
    }

    @Order(4)
    @Test
    public void testTradeWithLesserVersion() throws Exception {
        Trade trade = new Trade("T2", 0, "CP-4", "B4", LocalDate.of(2026, 05, 23), LocalDate.now(), "N");
        Exception ex = Assertions.assertThrows(Exception.class, () ->  controller.storeTrade(trade));
        String actualException = ex.getMessage();
        String expectedMessage = "Received version is lesser than the existing trade.";
        Assertions.assertEquals(expectedMessage, actualException);
    }

    @Order(5)
    @Test
    public void testTradeWithSameVersion_overwrite() throws Exception {
        Trade trade = new Trade("T1", 1, "CP-4", "B3", LocalDate.of(2026, 05, 23), LocalDate.now(), "N");
        controller.storeTrade(trade);
        Trade updatedTrade = controller.findTradesById(trade.getTradeId()).get(0);
        Assertions.assertEquals("B3", updatedTrade.getBookId());
    }

    @Order(6)
    @Test
    public void testTradeUpdateStatus() throws Exception {
        await()
                .atMost(Duration.ONE_MINUTE)
                .untilAsserted(() -> verify(controller,atLeast(2)).updateTradeStatus());
    }

}