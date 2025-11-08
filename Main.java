import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Regression regression = new Regression();
        regression.readData("data.txt");
        regression.calculate_regression();
    }
}