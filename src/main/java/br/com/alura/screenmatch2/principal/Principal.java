package br.com.alura.screenmatch2.principal;

import br.com.alura.screenmatch2.service.ConsumoApi;

import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=88d8c2ff";

    public Principal() {

        System.out.println("Digite o nome da série para busca");

        var nomeSerie = leitura.nextLine();
        //var consumoApi = new ConsumoApi();

        var json = consumo.obterDados(        ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

    }
}
