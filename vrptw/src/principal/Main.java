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

		while(contador < 1) {
			contador++;
			int gmax = 1000; //número de gerações
			int numeroDeRotas = 5; //mu, tamanho da população
			int numeroDeDescendentes = 250; //lamba, número de descendentes
			int criterioParadaBL = 10; //critério de parada da busca local
			int multa = 1000; //multa aplicada às rotas que não chegarem dentro da janela
			double cMutacao = 0.6; //coeficiente de mutação
			double cBuscaLocal = 0.8; //coeficiente de busca local
			long tempoInicio = System.currentTimeMillis();
			long tempoDeExecucao;

			//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
			ArrayList<Rota> descendentes = new ArrayList<>();
			ArrayList<Cliente> clientes = new ArrayList<>(); //lista de clientes passados pelo arquivo
			ArrayList<Veiculo> veiculos = new ArrayList<>(); //lista de veículos passados pelo arquivo
			ArrayList<Rota> populacao = new ArrayList<>(); //array das rotas criadas inicialmente e das novas gerações

//			Random rnd = new Random();
//			int parametro;
//
//			int [] cont = new int[11];
//			for (int j = 0; j < cont.length; j++) {
//				cont[j] = 0;
//			}
//
//			do {
//
//				parametro = rnd.nextInt(11);
//
//				switch(parametro) {
//				case 0:
//					cont[0]++;
//					break;
//				case 1:
//					cont[1]++;
//					break;
//				case 2:
//					cont[2]++;
//					break;
//				case 3:
//					cont[3]++;
//					break;
//				case 4:
//					cont[4]++;
//					break;
//				case 5:
//					cont[5]++;
//					break;
//				case 6:
//					cont[6]++;
//					break;
//				case 7:
//					cont[7]++;
//					break;
//				case 8:
//					cont[8]++;
//					break;
//				case 9:
//					cont[9]++;
//					break;
//				case 10:
//					cont[10]++;
//					break;
//				case 11:
//					cont[11]++;
//					break;			
//				}
//			}while(cont[parametro] >= 15);	

			//args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
			Conversor conversor = new Conversor(args[0]);
			conversor.converterArquivo(clientes, veiculos);

			System.out.println(conversor.getNomeDoArquivo());
			
			//matriz que salva as distâncias de todos os clientes para os outros
			double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

			//as distâncias entre os clientes são calculadas
			matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

			Rota melhorRota = new Rota(clientes, veiculos,clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			melhorRota.setCustoTotalRota(Double.MAX_VALUE);

			//criação da população inicial (pais)
			for (int i = 0; i < numeroDeRotas; i++) {
				//a rota é instanciada
				Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				//a função para mudar as rotas é chamada
				r.criaRotas(r);

				//as rotas criadas são clonadas
				Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				rotaInicial = (Rota) r.getClone(rotaInicial);

				// a rota é incluída na população
				populacao.add(rotaInicial);
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

						if(melhorRota.getCustoTotalRota() > rotaClonada.getCustoTotalRota()) {
							melhorRota = (Rota) rotaClonada.getClone(melhorRota);
						}

						//as novas rotas são adicionadas em um array auxiliar
						descendentes.add(rotaClonada);
					}	
				}

				//populacao = populacao + descendentes
				populacao.addAll(descendentes);

				Collections.sort(populacao);

				// é feito um corte para mu (numeroDeRotas) indivíduos
				populacao.subList(numeroDeRotas, populacao.size()).clear();

				//a lista auxiliar é limpa
				descendentes.clear();

				//é impressa a distância encontrada nessa geração
				BigDecimal bd1 = new BigDecimal(melhorRota.getCustoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);
				System.out.println((geracoes+1) + " " + bd1);

				geracoes++;	

				tempoDeExecucao = (System.currentTimeMillis()-tempoInicio)/60000;

			}


			//é impresso o menor custo encontrado
			BigDecimal bd2 = new BigDecimal(melhorRota.getCustoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println("Menor custo encontrado: " + bd2.doubleValue());

			for(int i = 0; i < melhorRota.listaVeiculos.size(); i++) {
				if(melhorRota.listaVeiculos.get(i).getCargaOcupada() != 0) {
					BigDecimal bd3 = new BigDecimal(melhorRota.listaVeiculos.get(i).getTempoVeiculo()).setScale(2, RoundingMode.HALF_EVEN);
					BigDecimal bd6 = new BigDecimal(melhorRota.listaVeiculos.get(i).getCustoVeiculo()).setScale(2, RoundingMode.HALF_EVEN);
					System.out.println((i + 1)  + "\t\t" + bd6 + "\t\t" + bd3 + "\t\t" +
							melhorRota.listaVeiculos.get(i).getCargaOcupada()
							+ "     " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);
				}
				
			}

			Saida criaArquivo = new Saida(args[1]);

			BigDecimal bd4 = new BigDecimal(melhorRota.getTempoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);

			melhorRota.atualizaVeiculosUtilizados(melhorRota);

			tempoDeExecucao = System.currentTimeMillis() - tempoInicio;

			BigDecimal bd5 = new BigDecimal(tempoDeExecucao / 60000).setScale(2, RoundingMode.HALF_EVEN);

			criaArquivo.solucoes(bd2, bd5, bd4, melhorRota.getVeiculosUtilizados(), geracoes, melhorRota.isFactivel(), conversor.getNomeDoArquivo());

		}

	}
}
