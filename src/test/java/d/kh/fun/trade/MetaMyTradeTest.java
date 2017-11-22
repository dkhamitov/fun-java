package d.kh.fun.trade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static d.kh.fun.trade.MetaMyTrade.AMOUNT;
import static d.kh.fun.trade.MetaMyTrade.NOTIONAL1;
import static d.kh.fun.trade.MetaMyTrade.NOTIONAL2;
import static d.kh.fun.trade.MetaMyTrade.SOURCE_SYSTEM;
import static d.kh.fun.trade.MetaMyTrade.TRADE_ID;
import static d.kh.fun.trade.MetaMyTrade.TYPE;

public class MetaMyTradeTest {
    @Test
    public void testWithEnum() {
        //given:
        EnumMap<MetaMyTrade, Object> records = new EnumMap<>(MetaMyTrade.class);
        records.put(SOURCE_SYSTEM, expectedTrade.getSourceSystem());
        records.put(TRADE_ID, expectedTrade.getId());
        records.put(TYPE, expectedTrade.getType());
        records.put(AMOUNT, expectedTrade.getAmount());
        records.put(NOTIONAL1, expectedTrade.getNotional1());
        records.put(NOTIONAL2, expectedTrade.getNotional2());

        //when:
        MyTrade trade = new MyTrade();
        Arrays.stream(MetaMyTrade.values()).forEach(meta -> meta.populate(trade, records));

        //then:
        Assert.assertEquals(expectedTrade, trade);
    }

    @Test
    public void testWithString() {
        //given::
        Map<String, Object> records = new HashMap<>();
        records.put(SOURCE_SYSTEM.sourceColumn(), expectedTrade.getSourceSystem());
        records.put(TRADE_ID.sourceColumn(), expectedTrade.getId());
        records.put(TYPE.sourceColumn(), expectedTrade.getType());
        records.put(AMOUNT.sourceColumn(), expectedTrade.getAmount());
        records.put(NOTIONAL1.sourceColumn(), expectedTrade.getNotional1());
        records.put(NOTIONAL2.sourceColumn(), expectedTrade.getNotional2());

        //when:
        MyTrade trade = new MyTrade();
        Arrays.stream(MetaMyTrade.values()).forEach(meta -> meta.populate(trade, records));

        //then:
        Assert.assertEquals(expectedTrade, trade);
    }

    private MyTrade expectedTrade;

    @Before
    public void before() {
        expectedTrade = new MyTrade();
        expectedTrade.setSourceSystem("SS");
        expectedTrade.setId("T1");
        expectedTrade.setType("Swap");
        expectedTrade.setNotional1(1);
        expectedTrade.setNotional2(-1);
    }
}
