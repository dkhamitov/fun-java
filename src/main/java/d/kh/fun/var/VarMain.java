package d.kh.fun.var;

public class VarMain {
    public static void main(String[] args) {
        var foo = new Object() {
            void bar() {
                System.out.println("bar");
            }
        };
        foo.bar();
        System.out.println(foo.getClass());
    }
}
