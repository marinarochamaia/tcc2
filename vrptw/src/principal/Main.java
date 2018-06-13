package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import estrategiaEvolutiva.BuscaLocal;
import estrategiaEvolutiva.FuncoesBuscaLocal;
import estrategiaEvolutiva.Mutacao;
import io.Conversor;
import io.Saida;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {

		double menorCusto = 0; //menor custo encontrado na popula��o inicial
		double menorCustoDescendente = 0; //menor custo encontrado nas novas gera��es
		double menorCustoFinal = 0; //menor custo final
		int gmax = 1000; //n�mero de gera��es
		int numeroDeRotas = 5; //mu, tamanho da popula��o inicial
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

		//args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//matriz que salva as dist�ncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		//array para salvar a melhor rota encontrada
		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);

		//as dist�ncias entre os clientes s�o calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		//cria��o da popula��o inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			//a rota � instanciada
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			//a fun��o para mudar as rotas � chamada
			r.criaRotas();

			//as rotas criadas s�o clonadas
			Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			rotaInicial = (Rota) r.getClone(rotaInicial);
			
			// a rota � inclu�da na popula��o
			populacao.add(rotaInicial);
			
		}

		//busca pela menor dist�ncia da popula��o inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
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

					//as novas rotas s�o adicionadas em um array auxiliar
					descendentes.add(rotaClonada);
				}
			}

			//populacao = populacao + descendentes
			populacao.addAll(descendentes);

			// as rotas s�o ordenadas por valor de custos
			Collections.sort(populacao);

			// � feito um corte para mu (numeroDeRotas) indiv�duos
			populacao.subList(numeroDeRotas, populacao.size()).clear();

			//busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (int i = 0; i < populacao.size(); i++) {

				if (menorCustoDescendente >= populacao.get(i).getCustoTotalRota()) {
					menorCustoDescendente = populacao.get(i).getCustoTotalRota();
				}
			}

			//a lista auxiliar � limpa
			descendentes.clear();

			//� impressa a dist�ncia encontrada nessa gera��o
			BigDecimal bd3 = new BigDecimal(menorCustoDescendente).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println((geracoes+1) + " " + bd3);

			geracoes++;	
			
			tempoDeExecucao = (System.currentTimeMillis()-tempoInicio)/60000;
			
			if(tempoDeExecucao > 10)
				break;

			
			
		}

		// menor custi final � encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoFinal = menorCusto;
		else
			menorCustoFinal = menorCustoDescendente;

		//a melhor rota � selecionada
		melhorRota = (Rota) populacao.get(0).getClone(melhorRota);		

		//� impressa a melhor rota
		for (int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {

			BigDecimal bd4 = new BigDecimal(melhorRota.listaVeiculos.get(i).getCustoVeiculo()).setScale(2, RoundingMode.HALF_EVEN);

			System.out.println((i + 1) + "   (" + bd4 + ")      " + melhorRota.listaVeiculos.get(i).getCargaOcupada()
					+ "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);
		}

		boolean semMulta;
	
		FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
		semMulta = fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, melhorRota);

		if(semMulta)
			System.out.println("Rota atende a janela de tempo!");
		else
			System.out.println("Rota n�o atende a janela de tempo!");

		BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo antes da estrat�gia: " + bd1);

		//� impresso o menor custo encontrado
		BigDecimal bd2 = new BigDecimal(menorCustoFinal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());
		
		melhorRota.atualizaVeiculosUtilizados(melhorRota);
		
		BigDecimal bd3 = new BigDecimal(melhorRota.getTempoTotalRota()).setScale(2, RoundingMode.HALF_EVEN);

		tempoDeExecucao = System.currentTimeMillis()-tempoInicio;
		Saida criaArquivo = new Saida(args[1]);
		
		BigDecimal bd5 = new BigDecimal(tempoDeExecucao / 60000).setScale(2, RoundingMode.HALF_EVEN);
		
		System.out.println(bd5);
		
		criaArquivo.solucoes(bd2, bd5, bd3, melhorRota.getVeiculosUtilizados(), geracoes, semMulta);
		
		
	}
}