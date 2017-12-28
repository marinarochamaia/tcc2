package principal;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {

		double menorCusto = 0; // menor custo encontrado na popula��o inicial
		double menorCustoDescendente = 0; // menor custo encontrado nas novas gera��es
		double menorCustoTotal = 0; // menor custo Final
		int n = 7;// n�mero de gera��es
		int cont = 0; // contador auxiliar
		int numeroDeRotas = 1000; // mu tamanho da popula��o inicial
		int multa = 1000;// multa aplicada �s rotas que n�o chegarem dentro da janela
		int tamanhoNovaPopulacao = (n * numeroDeRotas) * 30 / 100; // Fator PL = 30%

		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os ind�viduos criados atrav�s da
													// muta��o (antes de aplicar o fator PL)
		ArrayList<Rota> novaPopulacao = new ArrayList<>();// salva a popula��o com o fator PL j� aplicado
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de ve�culos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()]; // matriz que salva as dist�ncias
																						// de todos os clientes para os
																						// outros

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
		while (geracoes < n) {

			// para cada indiv�duo da popula��o (rota)
			for (Rota r : populacao) {

				// a rota � clonada
				Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),
						matrizDeDistancias);
				rotaClonada = (Rota) r.getClone(rotaClonada);
				rotaClonada.custoTotalRota = 0;

				// s�o selecionados n�meros aleat�rios que ser�o utilizados para pegar os
				// ve�culos
				Random rnd = new Random();
				int j = rnd.nextInt(rotaClonada.getVeiculosUtilizados() - 1);

				// os ve�culos s�o selecionados
				Veiculo v1 = rotaClonada.listaVeiculos.get(j);

				// uma posi��o de cada ve�culo � selecionada
				// esta deve ser diferente do dep�sito, enquanto n�o for, outra posi��o �
				// selecionada
				int pv1;

				do {
					pv1 = rnd.nextInt(v1.ordemDeVisitacao.size());
				} while (v1.ordemDeVisitacao.get(pv1).getNumero() == 0 || v1.ordemDeVisitacao.size() == 0);

				// uma segunda posi��o do ve�culo � selecionada
				// esta n�o deve ser o dep�sito e nem igual a primeira posi��o
				int pv2;
				do {
					pv2 = rnd.nextInt(v1.ordemDeVisitacao.size());
				} while (v1.ordemDeVisitacao.get(pv2).getNumero() == 0 || pv2 == pv1);

				// muta��o
				Collections.swap(v1.ordemDeVisitacao, pv1, pv2);
				Collections.swap(v1.ordemDeVisitacao, (pv1 / 2), (pv2 / 3));
				Collections.swap(v1.ordemDeVisitacao, (pv1 / 4), (pv2 / 5));

				// calcula os custos da nova rota
				v1.resetCustoVeiculo();
				v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
				rotaClonada.custoTotalRota = v1.getCustoVeiculo();

				// as novas rotas s�o adicionadas em um array auxiliar em ordem crescente de
				// custos
				if (cont == 0)
					aux.add(rotaClonada);
				else if (rotaClonada.custoTotalRota < aux.get(cont - 1).custoTotalRota)
					aux.add((cont - 1), rotaClonada);
				else
					aux.add(rotaClonada);

				cont++;

				// se as rotas j� acabaram � feito um "corte" nos descendentes gerando a nova
				// popula��o
				if (cont == (n * numeroDeRotas)) {
					for (int i = 0; i < tamanhoNovaPopulacao; i++) {
						Rota e = aux.get(i);
						novaPopulacao.add(e);
					}
				}

			} // fecha for

			// � feito um merge da nova popula��o e da popula��o inicial
			if (n == 0) {
				for (int k = 0; k < populacao.size(); k++) {
					Rota auxiliar = populacao.get(k);
					novaPopulacao.add(auxiliar);
				}
			}

			// as rotas s�o ordenadas por valor de custos
			for (int i = 1; i < novaPopulacao.size(); i++) {
				if (novaPopulacao.get(i).custoTotalRota < novaPopulacao.get(i - 1).custoTotalRota)
					Collections.swap(novaPopulacao, i, (i - 1));
			}

			// � feito um corte para mu indiv�duos
			for (int j = (novaPopulacao.size() - 1); j >= numeroDeRotas; j--) {
				novaPopulacao.remove(j);
			}

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (Rota r : novaPopulacao) {
				if (menorCustoDescendente > r.getCustoTotalRota()) {
					menorCustoDescendente = r.getCustoTotalRota();
				}
			}

			geracoes++;

		} // fecha while

		// menor custo final � encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		System.out.println("Menor custo encontrado: " + menorCustoTotal);

	}// fecha a main

}// fecha a classe