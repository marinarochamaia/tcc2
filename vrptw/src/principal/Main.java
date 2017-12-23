package principal;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {

		double menorCusto = 0, menorCustoDescendente = 0;
		int n = 7;//número de gerações 
		int cont = 0, numeroDeRotas = 1000, multa = 1000;
		int tamanhoNovaPopulacao = (n*numeroDeRotas)*30/100;
		

		ArrayList <Rota> aux = new ArrayList<>();
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Veiculo> veiculos = new ArrayList<>();
		ArrayList<Rota> populacao = new ArrayList<>();
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];


		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// criação da população
		for (int i = 0; i < numeroDeRotas; i++) {
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			r.criaRotas();
			populacao.add(r);
		}

		// busca pelo menor custo da população inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}

		/// número de gerações que serão criadas
		int geracoes = 0;

		// laço para fazer a mutação em todas as gerações criadas
		while (geracoes < n) {

			// para cada indivíduo da população (rota)
			for (Rota r : populacao) {

				// a rota é clonada
				Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),
						matrizDeDistancias);
				rotaClonada = (Rota) r.getClone(rotaClonada);
				rotaClonada.custoTotalRota = 0;
				
				// são selecionados números aleatórios que serão utilizados para pegar os
				// veículos
				Random rnd = new Random();
				int j = rnd.nextInt(veiculos.size());

				// os veículos são selecionados
				Veiculo v1 = rotaClonada.listaVeiculos.get(j);

				// uma posição de cada veículo é selecionada
				// esta deve ser diferente do depósito, enquanto não for, outra posição é
				// selecionada
				int pv1;

				do {
					pv1 = rnd.nextInt(v1.ordemDeVisitacao.size());
				} while (v1.ordemDeVisitacao.get(pv1).getNumero() == 0);

				// uma segunda posição do veículo é selecionada
				// esta não deve ser o depósito e nem igual a primeira posição
				int pv2;
				do {
					pv2 = rnd.nextInt(v1.ordemDeVisitacao.size());
				} while (v1.ordemDeVisitacao.get(pv2).getNumero() == 0 || pv2 == pv1);

				Collections.swap(v1.ordemDeVisitacao, pv1, pv2);
				
				//calcula os custos da npva rota
				v1.resetCustoVeiculo();
				v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
				rotaClonada.custoTotalRota = v1.getCustoVeiculo();

				//as novas rotas são adicionadas em um array auxiliar em ordem crescente de custos
				if (cont == 0)
					aux.add(rotaClonada);
				else if (rotaClonada.custoTotalRota < aux.get(cont - 1).custoTotalRota)
					aux.add((cont - 1), rotaClonada);
				else
					aux.add(rotaClonada);

				cont++;
				
				//se as rotas já acabaram é feito um "corte" nos descendentes gerando a nova população
				if(cont == (n*numeroDeRotas)) {
					for(int i = 0; i < tamanhoNovaPopulacao; i++) {
						Rota e = aux.get(i);
						novaPopulacao.add(e);
					}
				}
		

			} // fecha for

			//é feito um merge da nova população e da população inicial
			if (n == 0) {
				for (int k = 0; k < populacao.size(); k++) {
					Rota auxiliar = populacao.get(k);
					novaPopulacao.add(auxiliar);
				}
			}
			
			//as rotas são ordenadas por valor de custos
			for(int i = 1; i < novaPopulacao.size(); i++) {
				if(novaPopulacao.get(i).custoTotalRota < novaPopulacao.get(i-1).custoTotalRota)
					Collections.swap(novaPopulacao, i, (i-1));				
			}

			//é feito um corte para mu indivíduos
			for(int j = (novaPopulacao.size()-1); j >= numeroDeRotas; j--) {
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

		System.out.println(menorCusto);
		System.out.println(menorCustoDescendente);
	}// fecha a main
}// fecha a classe
