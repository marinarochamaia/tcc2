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
		double menorCustoTotal = 0; // menor custo Final
		int numeroDeRotas = 50; // mu tamanho da popula��o inicial
		int gmax = 10000;// n�mero de gera��es
		int descendentes = 250; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de muta��o
		double cBuscaLocal = 0.3; // coeficiente de busca local

		//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
		ArrayList<Rota> aux = new ArrayList<>();
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de ve�culos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)
		// matriz que salva as dist�ncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()]; 

		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser�
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		// as dist�ncias entre os clientes s�o calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// cria��o da popula��o inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			r.criaRotas();
			if(r.getVeiculosUtilizados() <= veiculos.size())
				populacao.add(r);	
		}

		// busca pelo menor custo da popula��o inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}

		// n�mero de gera��es que ser�o criadas
		int geracoes = 0;

		// la�o para fazer a muta��o em todas as gera��es criadas
		while (geracoes < gmax) {
			// para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {

				// a rota � clonada
				Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
				rotaClonada = (Rota) r.getClone(rotaClonada);

				for(int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {

					// s�o selecionados n�meros aleat�rios que ser�o utilizados para pegar os ve�culos
					Random rnd = new Random();
					int k = rnd.nextInt(rotaClonada.getVeiculosUtilizados());

					// os ve�culos s�o selecionados
					Veiculo v1 = rotaClonada.listaVeiculos.get(k);

					//gerar (lambda/mu) filhos
					for(int i = 0; i < (descendentes/numeroDeRotas); i++) {

						Mutacao mut = new Mutacao ();
						mut.fazMutacao(rotaClonada, cMutacao, i, matrizDeDistancias, multa, v1, rotaClonada.getDeposito());

						BuscaLocal bl = new BuscaLocal();
						bl.fazBuscaLocal(v1, rotaClonada, matrizDeDistancias, multa, k, cBuscaLocal);

					}

					// as novas rotas s�o adicionadas em um array auxiliar
					aux.add(rotaClonada);

				}
			}		

			novaPopulacao.addAll(populacao);
			novaPopulacao.addAll(aux);

			// as rotas s�o ordenadas por valor de custos
			Collections.sort(novaPopulacao);

			// � feito um corte para mu indiv�duos
			for (int p = (novaPopulacao.size() - 1); p >= numeroDeRotas; p--) {
				novaPopulacao.remove(p);
			}

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (Rota r : novaPopulacao) {

				if (menorCustoDescendente >	r.getCustoTotalRota()) {
					menorCustoDescendente = r.getCustoTotalRota();
					melhorRota = r;
				}
			}

			System.out.println(geracoes + " " + melhorRota.getCustoTotalRota());
			geracoes++;

		} // fecha while

		// menor custo final � encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		for(int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {
			System.out.println((i+1) + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);
		}

		BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Custo antes da estrat�gia evolutiva: " + bd1);

		BigDecimal bd2 = new BigDecimal(menorCustoTotal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

	}// fecha a main
}// fecha a classe