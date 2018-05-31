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

		double menorCusto = 0; // menor custo encontrado na popula��o inicial
		double menorCustoDescendente = 0;// menor custo encontrado nas novas gera��es
		double menorCustoFinal = 0; // menor custo Final
		int numeroDeRotas = 5; // mu, tamanho da popula��o inicial
		int gmax = 5000;// n�mero de gera��es
		int descendentes = 250; // lamba, n�mero de descendentes
		int multa = 0;// multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de muta��o
		double cBuscaLocal = 0.3; // coeficiente de busca local

		//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
		ArrayList<Rota> aux = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de ve�culos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)

		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser�
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		// matriz que salva as dist�ncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		//array para salvar a melhor rota encontrada
		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		melhorRota.setCustoTotalRota(Double.MAX_VALUE);

		// as dist�ncias entre os clientes s�o calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// cria��o da popula��o inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			//a rota � instanciada
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			//a fun��o para mudar as rotas � chamada
			r.criaRotas();

			//as rotas criadas s�o clonadas
			Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			rotaInicial = (Rota) r.getClone(rotaInicial);

			//verifica��o se os ve�culos utilizados n�o s�o maiores do que os dispon�veis
			if (rotaInicial.getVeiculosUtilizados() <= veiculos.size()) {
				//se os ve�culos utilizados s�o menores ou iguais aos dispon�veis, a rota � inclu�da na popula��o
				populacao.add(rotaInicial);
			} else {
				//se n�o a rota � infact�vel
				System.out.println("Rota infact�vel");
			}
		}

		// busca pelo menor custo da popula��o inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}

		//contador para o n�mero de gera��es que ser�o criadas
		int geracoes = 0;

		// la�o para fazer a muta��o em todas as gera��es criadas
		while (geracoes < gmax) {
			// para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {

				//gerar (lambda/mu) filhos
				for (int i = 0; i < (descendentes / numeroDeRotas); i++) {

					// a rota � clonada
					Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
					rotaClonada = (Rota) r.getClone(rotaClonada);

					//o dep�sito � instanciado
					Cliente deposito = rotaClonada.getDeposito();

					//a muta��o � feita
					Mutacao mut = new Mutacao();
					mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, deposito);

					//a busca local � feita
					BuscaLocal bl = new BuscaLocal();
					bl.fazBuscaLocal(rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito);

					// as novas rotas s�o adicionadas em um array auxiliar
					aux.add(rotaClonada);
					
				}
			}

			//populacao = populacao + aux
			populacao.addAll(aux);

			// as rotas s�o ordenadas por valor de custos
			Collections.sort(populacao);

			// � feito um corte para mu indiv�duos
			populacao.subList(numeroDeRotas, populacao.size()).clear();

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (int i = 0; i < populacao.size(); i++) {

				if (menorCustoDescendente >= populacao.get(i).getCustoTotalRota()) {
					menorCustoDescendente = populacao.get(i).getCustoTotalRota();
				}
			}

			//a lista auxiliar � limpa
			aux.clear();

			//a melhor rota � selecionada
			melhorRota = (Rota) populacao.get(0).getClone(melhorRota);

			//� impresso o custo encontrado nessa gera��o
			BigDecimal bd3 = new BigDecimal(menorCustoDescendente).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println(geracoes + " " + bd3);

			geracoes++;
			
			if(geracoes == 500) {
				cMutacao = 0.6;
				cBuscaLocal = 0.6;
			}
			if(geracoes == 1000) {
				cMutacao = 0.4;
				cBuscaLocal = 0.8;
			}
			if(geracoes == 2000)
				cMutacao = 0.3;

		}

		// menor custo final � encontrado
		if (menorCusto < menorCustoDescendente) {

			menorCustoFinal = menorCusto;

		} else {

			menorCustoFinal = menorCustoDescendente;

		}

		//� impressa a melhor rota
		for (int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {

			System.out.println((i + 1) + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);

		}

		//� impresso o menor custo antes da estrat�gia evolutiva
		BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Custo antes da estrat�gia evolutiva: " + bd1);

		//� impresso o menor custo depois da estrat�gia evolutiva
		BigDecimal bd2 = new BigDecimal(menorCustoFinal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

	}
}