package br.com.alura.screenmatch2.principal;

import br.com.alura.screenmatch2.model.DadosEpisodio;
import br.com.alura.screenmatch2.model.DadosSerie;
import br.com.alura.screenmatch2.model.DadosTemporada;
import br.com.alura.screenmatch2.model.Episodio;
import br.com.alura.screenmatch2.service.ConsumoApi;
import br.com.alura.screenmatch2.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoApi consumo = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=88d8c2ff";

    public void exibeMenu() {

        System.out.println("Digite o nome da série para busca");

        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(        ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i
                    + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        /* Lambda
        *  Temporada((parametro) -> expressao)
        *  temporadas.forEach(t -> System.out.println(t)) = temporadas.forEach(System.out::println);
         */
        temporadas.forEach(System.out::println);

//        for (int i =0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        //temporadas.forEach(System.out::println);

        /*
         Lista unica com os temporadas e episodios em um unica lista
         *  tolist() retorna um lista imutavel
         *  collect retorna lista q pode ser modificada
         */

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episodi0s");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
               // .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //.peek(e -> System.out.println("Ordenacao  " + e))
                .limit(10)
               // .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.titulo().toUpperCase())
                //.peek(e -> System.out.println("funcao MAP " + e))
                .forEach(System.out::println);

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("Episódio ordenado por temporada");
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());


        episodios.forEach(System.out::println);


        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("   Estatistica ");

        System.out.println("Digite parte do titulo do episodio para localizar temporada");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado: " );
            System.out.println("Temporada : " + episodioBuscado.get().getTemporada() + " - Titulo: " + episodioBuscado.get().getTitulo()
            +  " - número episódio: " + episodioBuscado.get().getNumeroEpisodio());
        } else {
            System.out.println("Episódio não encontrado !!");
        }

        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");


//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1,1);
//
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/YYYY");
//
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

    }
}
