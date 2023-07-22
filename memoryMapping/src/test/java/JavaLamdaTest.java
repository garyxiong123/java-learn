import java.util.function.*;

public class JavaLamdaTest {


    public static void main(String[] args) {


        //======
        testConsumer(msg -> System.out.println(msg), "hello gary");


        //======
        testSupplier(() -> true);
        int i = 3;
        testSupplier(() -> i > 3);
        testBooleanSupplier(() -> i > 3);


        //=========
        testFunction(input -> input, "ss");


        //=========

        testBinaryOperator((a, b) -> a + b, 3, 5);
        testBinaryOperator((a, b) -> a - b, 3, 5);

        //=========
        testBiPredicate((a, b) -> a > 3 || b != "ss", 3, "ss");

        //=========
        testSelfDefine(input -> System.out.println(input), "hello gary");

    }

    private static boolean testBiPredicate(BiPredicate<Long, String> biPredicate, long inputA, String inputB) {
        return biPredicate.test(inputA, inputB);
    }

    private static Integer testBinaryOperator(BinaryOperator<Integer> binaryOperator, int inputA, int inputB) {
        return binaryOperator.apply(inputA, inputB);
    }


    public static void testConsumer(Consumer consumer, String msg) {
        consumer.accept(msg);
    }


    public static Boolean testSupplier(Supplier<Boolean> supplier) {

        return supplier.get();
    }


    public static Boolean testBooleanSupplier(BooleanSupplier supplier) {

        return supplier.getAsBoolean();
    }

    public static String testFunction(Function<String, String> function, String input) {

        return function.apply(input);
    }


    //=========

    private static void testSelfDefine(TextConsumer<String> textConsumer, String input) {
        textConsumer.consume(input);
    }


}
