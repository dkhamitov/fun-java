package d.kh.fun.jol;


import d.kh.fun.trade.MyTrade;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class MyTradeJolAnalysis {
    public static void main(String[] args) {
        System.out.println(VM.current().details());
        ClassLayout myTradeClassLayout = ClassLayout.parseClass(MyTrade.class);
        MyTrade trade = new MyTrade();

        System.out.println("= Class' layout =");
        System.out.println(myTradeClassLayout.toPrintable());

        System.out.println("= Instance's layout =");
        System.out.println(myTradeClassLayout.toPrintable(trade));

        System.out.println("= Instance's layout after System.identityHashCode invocation =");
        System.identityHashCode(trade);
        System.out.println(myTradeClassLayout.toPrintable(trade));
    }
}