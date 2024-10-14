import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorDeMoedas {
    private static final String API_KEY = "96002890f4750ceecd765ec8";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. USD para EUR");
            System.out.println("2. EUR para USD");
            System.out.println("3. USD para BRL");
            System.out.println("4. BRL para USD");
            System.out.println("5. EUR para BRL");
            System.out.println("6. BRL para EUR");
            System.out.println("7. Sair");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    converterMoeda(scanner, option);
                    break;
                case 7:
                    System.out.println("Obrigado por usar o conversor de moedas!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        }
    }

    private static void converterMoeda(Scanner scanner, int option) {
        System.out.print("Digite o valor a ser convertido: ");
        double value = scanner.nextDouble();

        String fromCurrency = "";
        String toCurrency = "";

        switch (option) {
            case 1:
                fromCurrency = "USD";
                toCurrency = "EUR";
                break;
            case 2:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            case 3:
                fromCurrency = "USD";
                toCurrency = "BRL";
                break;
            case 4:
                fromCurrency = "BRL";
                toCurrency = "USD";
                break;
            case 5:
                fromCurrency = "EUR";
                toCurrency = "BRL";
                break;
            case 6:
                fromCurrency = "BRL";
                toCurrency = "EUR";
                break;
        }

        double exchangeRate = getExchangeRate(fromCurrency, toCurrency);

        if (exchangeRate > 0) {
            double convertedValue = value * exchangeRate;
            System.out.printf("O valor convertido é: %.2f %s%n", convertedValue, toCurrency);
        } else {
            System.out.println("Erro ao obter a taxa de câmbio.");
        }
    }

    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + fromCurrency))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();


            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONObject("conversion_rates").getDouble(toCurrency);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao buscar a taxa de câmbio: " + e.getMessage());
            return 0.0;
        }
    }
}