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

		while(contador < 180) {
			contador++;
			int gmax = 1000; //n�mero de gera��es
			int numeroDeRotas = 5; //mu, tamanho da popula��o
			int numeroDeDescendentes = 25; //lamba, n�mero de descendentes
			int criterioParadaBL = 10; //crit�rio de parada da busca local
			int multa = 1000; //multa aplicada �s rotas que n�o chegarem dentro da janela
			double cMutacao = 0.8; //coeficiente de muta��o
			double cBuscaLocal = 0.6; //coeficiente de busca local
			long tempoInicio = System.currentTimeMillis();
			long tempoDeExecucao;

			//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
			ArrayList<Rota> descendentes = new ArrayList<>();
			ArrayList<Cliente> clientes = new ArrayList<>(); //lista de clientes passados pelo arquivo
			ArrayList<Veiculo> veiculos = new ArrayList<>(); //lista de ve�culos passados pelo arquivo
			ArrayList<Rota> populacao = new ArrayList<>(); //array das rotas criadas inicialmente e das novas gera��es

			Random rnd = new Random();
			int parametro = rnd.nextInt(5);

			int contZero = 0, contUm = 0, contDois = 0, contTres = 0, contQuatro = 0, contCinco = 0;
			

			while(contZero == 15 || contUm == 15 || contDois == 15 || contTres == 15 || contQuatro == 15 || contCinco == 15) {
				parametro = rnd.nextInt(5);
				
				if(contZero == 15 && contUm == 15 && contDois == 15 && contTres == 15 && contQuatro == 15 && contCinco == 15)
					break;
			}

			switch(parametro) {
			case 0:{
				contZero++;
				break;
			}
			case 1:{
				contUm++;
				break;
			}			
			case 2:{
				contDois++;
				break;
			}
			case 3:{
				contTres++;
				break;
			}
			case 4:{
				contQuatro++;
				break;
			}
			case 5:{
				contCinco++;
				break;
			}
			}

			//args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
			Conversor conversor = new Conversor(args[parametro]);
			conversor.converterArquivo(clientes, veiculos);

			//matriz que salva as dist�ncias de todos os clientes para os outros
			double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

			//as dist�ncias entre os clientes s�o calculadas
			matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

			Rota melhorRota = new Rota(clientes, veiculos,clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			melhorRota.setCustoTotalRota(Double.MAX_VALUE);

			//cria��o da popula��o inicial (pais)
			for (int i = 0; i < numeroDeRotas; i++) {
				//a rota � instanciada
				Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				//a fun��o para mudar as rotas � chamada
				r.criaRotas(r);

				//as rotas criadas s�o clonadas
				Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
				rotaInicial = (Rota) r.getClone(rotaInicial);

				// a rota � inclu�da na popula��o
				populacao.add(rotaInicial);

			}

			//contador para o n�mero de gera��es que ser�o criadas
			int geracoes = 0;

			//la�o para fazer a muta��o em todas as gera��es criadas
			while (geracoes < gmax) {			

				//para cada indiv�duo da popula��o (pai) 
				for (Rota r : populacao) {

					//gerar (lambda/mu) filhos
					for (int i = 0; i < (numeroDeDescendentes / numeroDeRotas); i++) {

						//a rota � clonada
						Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
						rotaClonada = (Rota) r.getClone(rotaClonada);

						//o dep�sito � instanciado
						Cliente deposito = rotaClonada.getDeposito();

						//a muta��o � feita
						Mutacao mut = new Mutacao();
						mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, deposito);

						//a busca local � feita
						BuscaLocal bl = new BuscaLocal();
						bl.fazBuscaLocal(rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito, criterioParadaBL);		

						if(melhorRota.getCustoTotalRota() > rotaClonada.getCustoTotalRota()) {
							melhorRota = (Rota) rotaClonada.getClone(melhorRota);
						}

						//as novas rotas s�o adicionadas em um array auxiliar
						descendentes.add(rotaClonada);
					}	
				}

				//populacao = populacao + descendentes
				populacao.addAll(descendentes);

				Collections.sort(populacao);

				// � feito um corte para mu (numeroDeRotas) indiv�duos
				populacao.subList(numeroDeRotas, populacao.size()).clear();

				//a lista auxiliar � limpa
				descendentes.clear();

				//� impressa a dist�ncia encontrada nessa gera��o
				BigDecimal bd1 = new BigDecimal(melhorRota.getCustoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);
				System.out.println((geracoes+1) + " " + bd1);

				geracoes++;	

				tempoDeExecucao = (System.currentTimeMillis()-tempoInicio)/60000;

				if(tempoDeExecucao >= 30)
					break;	
			}


			//� impresso o menor custo encontrado
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

			Saida criaArquivo = new Saida(args[parametro + 6]);

			BigDecimal bd4 = new BigDecimal(melhorRota.getTempoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);

			melhorRota.atualizaVeiculosUtilizados(melhorRota);

			tempoDeExecucao = System.currentTimeMillis() - tempoInicio;

			BigDecimal bd5 = new BigDecimal(tempoDeExecucao / 60000).setScale(2, RoundingMode.HALF_EVEN);

			criaArquivo.solucoes(bd2, bd5, bd4, melhorRota.getVeiculosUtilizados(), geracoes, melhorRota.isFactivel());

		}

	}
}