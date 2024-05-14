package br.coinConverter.main;

import br.coinConverter.api.ApiConnection;
import br.coinConverter.api.ApiResponse;
import br.coinConverter.models.Coin;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    private final Scanner reader = new Scanner(System.in);

    private void showOptions() {
        System.out.println("""
                -------------------------------------
                           MENU DE OPÇÕES
                -------------------------------------
                [1] Converter valores de EUR para BRL
                [2] Converter valores de USD para BRL
                [3] Converter valores de BRL para ARS
                [4] Converter valores de BRL para CLP
                [5] Converter valores de BRL para JPY
                [6] Definir câmbio e converter valores
                [7] Redefinir chave de API
                [0] Encerrar programa
                -------------------------------------""");
    }

    public String getInput() {
        return reader.nextLine();
    }

    private double inputValue(){
        while (true) {
            System.out.print("Insira o valor da conversão: ");
            String input = getInput();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, digite novamente. \n");
            }
        }
    }

    private void getCurrency(String base, String target, ApiConnection connection){
        System.out.printf("""
                *************************************
                     Convertendo de %s para %s
                *************************************
                """, base, target);

        try {
            ApiResponse response = connection.connect(base, target);

            if (response.result().equals("success")) {
                Coin coin = new Coin(response);
                System.out.println(coin.getLastUpdated());
                System.out.println(" --> Digite qualquer valor negativo para sair <-- \n");

                while (true){
                    double value = inputValue();
                    if (value < 0){
                        showOptions();
                        break;
                    } else {
                        System.out.println(coin.getConversion(value) + "\n");
                    }
                }
            } else {
                requestErrorHandler(response.errorType(), connection);
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    public void choiceOptions(ApiConnection connection){
        showOptions();
        boolean running = true;
        while (running) {
            System.out.print("\nEscolha uma opção: ");
            String option = getInput();

            switch (option) {
                case "0":
                    System.out.println("Execução encerrada.");
                    running = false;
                    break;
                case "1":
                    getCurrency("EUR", "BRL", connection);
                    break;
                case "2":
                    getCurrency("USD", "BRL", connection);
                    break;
                case "3":
                    getCurrency("BRL", "ARS", connection);
                    break;
                case "4":
                    getCurrency("BRL", "CLP", connection);
                    break;
                case "5":
                    getCurrency("BRL", "JPY", connection);
                    break;
                case "6":
                    Pattern pattern = Pattern.compile("^[A-Z0-9]+$");
                    System.out.println("Digite o código de moeda base da conversão:");
                    String base = getInput().toUpperCase();
                    System.out.println("Digite o código de moeda alvo da conversão:");
                    String target = getInput().toUpperCase();

                    if (base.length() != 3 || target.length() != 3 || (!pattern.matcher(base).matches() && !pattern.matcher(target).matches())) {
                        System.out.println("Código de moeda inválido.");
                    } else {
                        getCurrency(base, target, connection);
                    }
                    break;
                case "7":
                    connection.setApiKey("");
                    invalidKeyHandler(connection);
                    showOptions();
                    break;
                default:
                    System.out.println("Esse comando não é válido.");
                    break;
            }
        }
    }

    private void requestErrorHandler(String error, ApiConnection connection){
        switch (error){
            case "invalid-key":
                System.out.println("Sua chave de API atual é inválida");
                connection.setApiKey("");
                invalidKeyHandler(connection);
                showOptions();
                break;
            case "quota-reached":
                System.out.println("Você atingiu o limite de requisições permitidos no seu plano");
                System.out.println("Saiba mais em https://app.exchangerate-api.com/dashboard");
                break;
            case "inactive-account":
                System.out.println("A requisição não pode ser concluída porque seu endereço de email ainda não foi confirmado.");
                break;
            case "unsupported-code":
                System.out.println("Requisição inválida, o código de moeda inserido não é suportado.");
                break;
            default:
                System.out.println("Ocorreu um erro inesperado na requisição.");
                break;
        }
    }

    public void invalidKeyHandler(ApiConnection connection){
        while (connection.getApiKey().isEmpty()){
            System.out.println("Você precisa de uma chave da API para fazer conversões, adquira uma acessando https://app.exchangerate-api.com");
            System.out.println("Insira sua chave abaixo:");
            connection.setApiKey(getInput());
            System.out.println("Sua chave de API é: \"" + connection.getApiKey() + "\"? Confirme com \"S\" para continuar");
            String answer = getInput();

            if (answer.equalsIgnoreCase("S")){
                break;
            } else {
                connection.setApiKey("");
            }
        }
    }

    public void showTitle(){
        System.out.println("""
                
                 $$$$$$\\          $$\\                 $$$$$$\\                                               $$\\                      \s
                $$  __$$\\         \\__|               $$  __$$\\                                              $$ |                     \s
                $$ /  \\__|$$$$$$\\ $$\\$$$$$$$\\        $$ /  \\__|$$$$$$\\ $$$$$$$\\$$\\    $$\\ $$$$$$\\  $$$$$$\\$$$$$$\\   $$$$$$\\  $$$$$$\\ \s
                $$ |     $$  __$$\\$$ $$  __$$\\       $$ |     $$  __$$\\$$  __$$\\$$\\  $$  $$  __$$\\$$  __$$\\_$$  _| $$  __$$\\$$  __$$\\\s
                $$ |     $$ /  $$ $$ $$ |  $$ |      $$ |     $$ /  $$ $$ |  $$ \\$$\\$$  /$$$$$$$$ $$ |  \\__|$$ |   $$$$$$$$ $$ |  \\__|
                $$ |  $$\\$$ |  $$ $$ $$ |  $$ |      $$ |  $$\\$$ |  $$ $$ |  $$ |\\$$$  / $$   ____$$ |      $$ |$$\\$$   ____$$ |     \s
                \\$$$$$$  \\$$$$$$  $$ $$ |  $$ |      \\$$$$$$  \\$$$$$$  $$ |  $$ | \\$  /  \\$$$$$$$\\$$ |      \\$$$$  \\$$$$$$$\\$$ |     \s
                 \\______/ \\______/\\__\\__|  \\__|       \\______/ \\______/\\__|  \\__|  \\_/    \\_______\\__|       \\____/ \\_______\\__|     \s
                                                                                                                                                                                                                                                        \s
                """);
    }
}

