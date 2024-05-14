package br.coinConverter.main;

import br.coinConverter.api.ApiConnection;

public class Main {
    public static void main(String[] args){
        Menu menu = new Menu();
        ApiConnection connection = new ApiConnection("");
        menu.invalidKeyHandler(connection);
        menu.showTitle();
        menu.choiceOptions(connection);
    }
}
