package br.coinConverter.models;

import br.coinConverter.api.ApiResponse;

public class Coin {
    private String base;
    private String target;
    private double currency;
    private String lastUpdated;

    public Coin(ApiResponse response){
        if (response.result().equals("success")) {
            this.base = response.baseCode();
            this.target = response.targetCode();
            this.currency = response.conversionRate();
            this.lastUpdated = response.lastUpdate();
        } else {
            System.out.println("Ocorreu um erro ao instanciar a moeda.");
        }
    }

    public String getConversion(double value){
        double convertedValue = (currency * value);
        return "[" + base + "] " + value + " >>> [" + target + "] " + convertedValue;
    }

    public String getLastUpdated(){
        return "Ultima atualização: " + lastUpdated;
    }
}
