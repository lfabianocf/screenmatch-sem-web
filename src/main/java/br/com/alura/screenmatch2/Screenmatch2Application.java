package br.com.alura.screenmatch2;

import br.com.alura.screenmatch2.model.DadosEpisodio;
import br.com.alura.screenmatch2.model.DadosSerie;
import br.com.alura.screenmatch2.model.DadosTemporada;
import br.com.alura.screenmatch2.service.ConsumoApi;
import br.com.alura.screenmatch2.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Screenmatch2Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Screenmatch2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

        var consumoApi = new ConsumoApi();

		// http://www.omdbapi.com/?t=gilmore+girls&apikey=88d8c2ff
		// http://www.omdbapi.com/?t=gilmore+girls&season=1s&apikey=88d8c2ff
		// http://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=88d8c2ff
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=88d8c2ff");

		System.out.println(json);

		//json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

		System.out.println(dados);

		json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=88d8c2ff");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);

		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=" + i
					+ "&apikey=88d8c2ff");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);

	}
}
