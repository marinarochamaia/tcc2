package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import estrategiaEvolutiva.BuscaLocal;
import estrategiaEvolutiva.Mutacao;
import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {

		double menorCusto = 0; // menor custo encontrado na população inicial
		double menorCustoDescendente = 0;// menor custo encontrado nas novas gerações
		double menorCustoTotal = 0; // menor custo Final
		int numeroDeRotas = 5; // mu, tamanho da população inicial
		int gmax = 10000;// número de gerações
		int descendentes = 25; // lamba, número de descendentes
		int multa = 0;// multa aplicada às rotas que não chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de mutação
		double cBuscaLocal = 0.3; // coeficiente de busca local

		//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
		ArrayList<Rota> aux = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de veículos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		// matriz que salva as distâncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		//array para salvar a melhor rota encontrada
		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		melhorRota.setCustoTotalRota(Double.MAX_VALUE);

		// as distâncias entre os clientes são calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// criação da população inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			//a rota é instanciada
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			//a função para mudar as rotas é chamada
			r.criaRotas();

			//as rotas criadas são clonadas
			Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			rotaInicial = (Rota) r.getClone(rotaInicial);

			//verificação se os veículos utilizados não são maiores do que os disponíveis
			if (rotaInicial.getVeiculosUtilizados() <= veiculos.size()) {
				//se os veículos utilizados são menores ou iguais aos disponíveis, a rota é incluída na população
				populacao.add(rotaInicial);
			} else {
				//se não a rota é infactível
				System.out.println("Rota infactível");
			}
		}


		// busca pelo menor custo da população inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}

		//contador para o número de gerações que serão criadas
		int geracoes = 0;

		// laço para fazer a mutação em todas as gerações criadas
		while (geracoes < gmax) {
			// para cada indivíduo da população (pai) 
			for (Rota r : populacao) {
				//os veículos utilizados são percorridos
				for (int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {

					// são selecionados números aleatórios que serão utilizados para pegar os veículos
					Random rnd = new Random();
					int k = rnd.nextInt(r.getVeiculosUtilizados());
					int n = rnd.nextInt(r.getVeiculosUtilizados());

					//enquanto forem iguais, são sorteados novamente
					while (k == n) {
						n = rnd.nextInt(r.getVeiculosUtilizados());
					}

					//gerar (lambda/mu) filhos
					for (int i = 0; i < (descendentes / numeroDeRotas); i++) {

						// a rota é clonada
						Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
						rotaClonada = (Rota) r.getClone(rotaClonada);

						//o depósito é instanciado
						Cliente deposito = rotaClonada.getDeposito();

						//a mutação é feita
						Mutacao mut = new Mutacao();
						mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, rotaClonada.getDeposito());

						//a busca local é feita
						BuscaLocal bl = new BuscaLocal();
						bl.fazBuscaLocal(k, n, rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito);

						// as novas rotas são adicionadas em um array auxiliar
						aux.add(rotaClonada);

					}
				}
			}

			//populacao = populacao + aux
			populacao.addAll(aux);

			// as rotas são ordenadas por valor de custos
			Collections.sort(populacao);

			// é feito um corte para mu indivíduos
			for (int p = (populacao.size() - 1); p >= numeroDeRotas; p--) {
				populacao.remove(p);
			}

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (int i = 0; i < populacao.size(); i++) {

				if (menorCustoDescendente >= populacao.get(i).getCustoTotalRota()) {
					menorCustoDescendente = populacao.get(i).getCustoTotalRota();
				}
			}

			//a melhor rota é selecionada
			melhorRota = (Rota) populacao.get(0).getClone(melhorRota);

			//é impresso o custo encontrado nessa geração
			BigDecimal bd3 = new BigDecimal(menorCustoDescendente).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println(geracoes + " " + bd3);
			geracoes++;

		}


		// menor custo final é encontrado
		if (menorCusto < menorCustoDescendente) {
			menorCustoTotal = menorCusto;
		} else {

			menorCustoTotal = menorCustoDescendente;

			//é impressa a melhor rota
			for (int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {
				System.out.println((i + 1) + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);

			}
		}

		//é impresso o menor custo antes da estratégia evolutiva
		BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Custo antes da estratégia evolutiva: " + bd1);

		//é impresso o menor custo depois da estratégia evolutiva
		BigDecimal bd2 = new BigDecimal(menorCustoTotal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

	}
}