package d.kh.fun.trade;

import java.util.EnumMap;
import java.util.Map;

public enum MetaMyTrade {
    SOURCE_SYSTEM("SOURCE_SYSTEM_CSV", "SOURCE_SYSTEM", MyTrade::setSourceSystem, String::valueOf),
    TRADE_ID("TRADE_ID_CSV", "SOURCE_SYSTEM", MyTrade::setId, String::valueOf),
    TYPE("TYPE_CSV", "TYPE", MyTrade::setType, String::valueOf),
    AMOUNT("AMOUNT_CSV", "AMOUNT", MyTrade::setAmount, MetaMyTrade::toInt),
    NOTIONAL1("NOTIONAL1_CSV", "NOTIONAL1", MyTrade::setNotional1, MetaMyTrade::toInt),
    NOTIONAL2("NOTIONAL2_CSV", "NOTIONAL2", MyTrade::setNotional2, MetaMyTrade::toInt);

    private final String sourceColumn;
    private final String destColumn;
    private final MyTradeSetter<Object> setter;

    <T> MetaMyTrade(String sourceColumn, String destColumn, MyTradeSetter<T> setter, Caster<T> caster) {
        this.sourceColumn = sourceColumn;
        this.destColumn = destColumn;
        this.setter = (t, v) -> setter.set(t, caster.cast(v));
    }

    public MyTradeSetter<Object> setter() {
        return setter;
    }

    public String sourceColumn() {
        return sourceColumn;
    }

    public void populate(MyTrade trade, Map<String, ?> records) {
        Object value = records.get(this.sourceColumn);
        setter.set(trade, value);
    }

    public void populate(MyTrade trade, EnumMap<MetaMyTrade, ?> records) {
        Object value = records.get(this);
        setter.set(trade, value);
    }

    public String destColumn() {
        return destColumn;
    }

    private static int toInt(Object o) {
        return (int) o;
    }
}
