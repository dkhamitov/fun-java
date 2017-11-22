package d.kh.fun.trade;

import java.util.Objects;

public class MyTrade {
    private String sourceSystem;
    private String id;
    private String type;
    private int amount;
    private int notional1;
    private int notional2;

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getNotional1() {
        return notional1;
    }

    public void setNotional1(int notional1) {
        this.notional1 = notional1;
    }

    public int getNotional2() {
        return notional2;
    }

    public void setNotional2(int notional2) {
        this.notional2 = notional2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyTrade myTrade = (MyTrade) o;
        return Double.compare(myTrade.amount, amount) == 0 &&
                Double.compare(myTrade.notional1, notional1) == 0 &&
                Double.compare(myTrade.notional2, notional2) == 0 &&
                Objects.equals(sourceSystem, myTrade.sourceSystem) &&
                Objects.equals(id, myTrade.id) &&
                Objects.equals(type, myTrade.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceSystem, id, type, amount, notional1, notional2);
    }

    @Override
    public String toString() {
        return "MyTrade{" +
                "sourceSystem='" + sourceSystem + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", notional1=" + notional1 +
                ", notional2=" + notional2 +
                '}';
    }
}
