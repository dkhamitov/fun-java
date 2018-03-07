package d.kh.fun.trade

import spock.lang.Specification

class MetaMyTradeSpec extends Specification {
    def "populate trade from records"() {
        given:
        def givenTrade = new MyTrade(
                sourceSystem: "SS",
                id: "T1",
                type: "Swap",
                notional1: 1,
                notional2: -1
        )
        def records = new EnumMap<>(MetaMyTrade.class)
        givenTrade.with {
            records[MetaMyTrade.SOURCE_SYSTEM] = sourceSystem
            records[MetaMyTrade.TRADE_ID] = id
            records[MetaMyTrade.TYPE] = type
            records[MetaMyTrade.AMOUNT] = amount
            records[MetaMyTrade.NOTIONAL1] = notional1
            records[MetaMyTrade.NOTIONAL2] = notional2
        }

        when:
        def trade = new MyTrade()
        MetaMyTrade.values().each({ meta -> meta.populate(trade, records) })
        then:
        trade == givenTrade
    }
}
