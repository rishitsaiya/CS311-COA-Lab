package processor.pipeline;

public class BinaryConverter {

    public static String binaryConverter(int number) {
        String binary = "";
        if (number == 1){
            binary = "1";
            System.out.print(binary);
            return binary;
        }
        if (number == 0){
            binary = "0";
            System.out.print(binary);
            return binary;
        }
        if (number > 1) {
            String i = Integer.toString(number % 2);

            binary = binary + i;
            binaryConverter(number/2);
        }
        System.out.print(binary);
        return binary;
    }
}