package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import estrategiaEvolutiva.BuscaLocal;
import estrategiaEvolutiva.Mutacao;
import io.Conversor;
import io.Saida;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {

		int contador = 0;

		while(contador < 12) {
			contador++;
			double menorCusto = 0; //menor custo encontrado na população inicial
			double menorCustoFinal = 0; //menor custo final
			int gmax = 1000; //número de gerações
			int numeroDeRotas = 5; //mu, tamanho da população
			int numeroDeDescendentes = 25; //lamba, número de descendentes
			int criterioParadaBL = 10; //critério de parada da busca local
			int multa = 1000; //multa aplicada às rotas que não chegarem dentro da janela
			double cMutacao = 0.8; //coeficiente de mutação
			double cBuscaLocal = 0.6; //coeficiente de busca local
			long tempoInicio = System.currentTimeMillis();
			long tempoDeExecucao;

			//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
			ArrayList<Rota> descendentes = new ArrayList<>();
			ArrayList<Cliente> clientes = new ArrayList<>(); //lista de clientes passados pelo arquivo
			ArrayList<Veiculo> veiculos = new ArrayList<>(); //lista de veículos passados pelo arquivo
			ArrayList<Rota> populacao = new ArrayList<>(); //array das rotas criadas inicialmente e das novas gerações

			//args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
			Conversor conversor = new Conversor(args[0]);
			conversor.converterArquivo(clientes, veiculos);

			//matriz que salva as distâncias de todos os clientes para os outros
			double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

			//as distâncias entre os clientes são calculadas
			matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

			//criação da população inicial (pais)
			for (int i = 0; i < numeroDeRotas; i++) {
				//a rota é instanciada
				Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				//a função para mudar as rotas é chamada
				r.criaRotas();

				//as rotas criadas são clonadas
				Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				rotaInicial = (Rota) r.getClone(rotaInicial);

				// a rota é incluída na população
				populacao.add(rotaInicial);

			}

			//busca pela menor distância da população inicial
			menorCusto = Double.MAX_VALUE;
			for (Rota r : populacao) {
				if (menorCusto > r.getCustoTotalRota()) {
					menorCusto = r.getCustoTotalRota();
				}
			}

			//contador para o número de gerações que serão criadas
			int geracoes = 0;

			//laço para fazer a mutação em todas as gerações criadas
			while (geracoes < gmax) {
				//para cada indivíduo da população (pai) 
				for (Rota r : populacao) {

					//gerar (lambda/mu) filhos
					for (int i = 0; i < (numeroDeDescendentes / numeroDeRotas); i++) {

						//a rota é clonada
						Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
						rotaClonada = (Rota) r.getClone(rotaClonada);

						//o depósito é instanciado
						Cliente deposito = rotaClonada.getDeposito();

						//a mutação é feita
						Mutacao mut = new Mutacao();
						mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, deposito);

						//a busca local é feita
						BuscaLocal bl = new BuscaLocal();
						bl.fazBuscaLocal(rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito, criterioParadaBL);

						//as novas rotas são adicionadas em um array auxiliar
						descendentes.add(rotaClonada);
					}
				}

				//populacao = populacao + descendentes
				populacao.addAll(descendentes);

				// as rotas são ordenadas por valor de custos
				Collections.sort(populacao);

				// é feito um corte para mu (numeroDeRotas) indivíduos
				populacao.subList(numeroDeRotas, populacao.size()).clear();

				//a lista auxiliar é limpa
				descendentes.clear();

				//é impressa a distância encontrada nessa geração
				BigDecimal bd3 = new BigDecimal(populacao.get(0).getCustoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);
				System.out.println((geracoes+1) + " " + bd3);

				geracoes++;	

				tempoDeExecucao = (System.currentTimeMillis()-tempoInicio)/60000;

				if(tempoDeExecucao >= 30)
					break;	
			}

			// menor custo final é encontrado
			if (menorCusto < populacao.get(0).getCustoTotalRota())
				menorCustoFinal = menorCusto;
			else
				menorCustoFinal = populacao.get(0).getCustoTotalRota();

			//é impresso o menor custo encontrado
			BigDecimal bd2 = new BigDecimal(menorCustoFinal).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println("Menor custo encontrado: " + bd2.doubleValue());

			BigDecimal bd3 = new BigDecimal(populacao.get(0).getTempoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);

			tempoDeExecucao = System.currentTimeMillis()-tempoInicio;
			Saida criaArquivo = new Saida(args[1]);

			BigDecimal bd5 = new BigDecimal(tempoDeExecucao / 60000).setScale(2, RoundingMode.HALF_EVEN);

			populacao.get(0).atualizaVeiculosUtilizados(populacao.get(0));
			
			criaArquivo.solucoes(bd2, bd5, bd3, populacao.get(0).getVeiculosUtilizados(), geracoes);
			}

	}
}