package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {

		double menorCusto = 0; // menor custo encontrado na população inicial
		double menorCustoDescendente = 0; // menor custo encontrado nas novas gerações
		double menorCustoTotal = 0; // menor custo Final
		int numeroDeRotas = 30; // mu tamanho da população inicial
		int gmax = 100;// número de gerações
		int descendentes = 150; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada às rotas que não chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de mutação
		double cBuscaLocal = 0.3; // coeficiente de busca local

		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os indíviduos criados através da busca local
		//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de veículos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()]; // matriz que salva as distâncias
		// de todos os clientes para os
		// outros

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		// as distâncias entre os clientes são calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// criação da população inicial (pais)
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

		BigDecimal bd = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo antes da aplicação do algoritmo evolutivo: " + bd.doubleValue());

		// número de gerações que serão criadas
		int geracoes = 0;

		// laço para fazer a mutação em todas as gerações criadas
		while (geracoes < gmax) {
			// para cada indivíduo da população (pai) 
			for (Rota r : populacao) {
				for(int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {
					//gerar (lambda/mu) w filhos
					for(int i = 0; i < (descendentes/numeroDeRotas); i++) {

						Random rnd = new Random();
						double m = rnd.nextDouble();

						if(m <= cMutacao) {
							// a rota é clonada
							Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
							rotaClonada = (Rota) r.getClone(rotaClonada);

							// são selecionados números aleatórios que serão utilizados para pegar os veículos
							int j = rnd.nextInt(rotaClonada.getVeiculosUtilizados() - 1);

							// os veículos são selecionados
							Veiculo v1 = rotaClonada.listaVeiculos.get(j);

							// uma posição de cada veículo é selecionada
							// esta deve ser diferente do depósito, enquanto não for, outra posição é
							// selecionada
							int pv1;

							do {
								pv1 = rnd.nextInt(v1.ordemDeVisitacao.size());
							} while (v1.ordemDeVisitacao.get(pv1).getNumero() == 0 || v1.ordemDeVisitacao.size() == 0);

							// uma segunda posição do veículo é selecionada
							// esta não deve ser o depósito e nem igual a primeira posição
							int pv2;
							do {
								pv2 = rnd.nextInt(v1.ordemDeVisitacao.size());
							} while (v1.ordemDeVisitacao.get(pv2).getNumero() == 0 || pv2 == pv1);

							// mutação
							Collections.swap(v1.ordemDeVisitacao, pv1, pv2);
							Collections.swap(v1.ordemDeVisitacao, (pv1 / 2), (pv2 / 3));
							Collections.swap(v1.ordemDeVisitacao, (pv1 / 4), (pv2 / 5));

							// calcula os custos da nova rota (função de avaliação ou aptidão)
							calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

							double b = rnd.nextDouble();
							//a busca local só é feita se o fator pl for atendido
							if (b <= cBuscaLocal){
								int count = 0;
								while(count <= (clientes.size()/2)) {	
									count++;
									

									//1) remover u e inserir após v;
									//2) remover u e x e inserir u e x após v;
									//3) remover u e x e inserir x e u após v; (posições invertidas)
									//4) trocar u e v; // SWAP
									//5) troca u e x com v;
									//6) troca u e x com v e y;
									//7) 
									ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
									Collections.shuffle(operacoes);	

									for(Integer k : operacoes) {
										switch (k) {
										case 1: {
											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
											//não parte de zero e termina uma posição antes por causa do depósito
											for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
												for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.add(v, clienteU);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else {
														v1.ordemDeVisitacao.remove(v);
														v1.ordemDeVisitacao.add(u, clienteU);
													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 2: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);
													Cliente clienteX = v1.ordemDeVisitacao.get(x);
													Cliente clienteV = v1.ordemDeVisitacao.get(v);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.remove(x - 1);

													int posV = v1.ordemDeVisitacao.indexOf(clienteV);

													v1.ordemDeVisitacao.add(posV + 1, clienteU);
													v1.ordemDeVisitacao.add(posV + 2, clienteX);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else {
														int pos = v1.ordemDeVisitacao.indexOf(clienteU);
														v1.ordemDeVisitacao.remove(pos);
														pos = v1.ordemDeVisitacao.indexOf(clienteX);
														v1.ordemDeVisitacao.remove(pos);

														v1.ordemDeVisitacao.add(u, clienteU);
														v1.ordemDeVisitacao.add(x, clienteX);
													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}
										case 3: {
 
											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);
													Cliente clienteX = v1.ordemDeVisitacao.get(x);
													Cliente clienteV = v1.ordemDeVisitacao.get(v);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.remove(x - 1);

													int posV = v1.ordemDeVisitacao.indexOf(clienteV);

													v1.ordemDeVisitacao.add(posV + 1, clienteX);
													v1.ordemDeVisitacao.add(posV + 2, clienteU);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else {
														int pos = v1.ordemDeVisitacao.indexOf(clienteU);
														v1.ordemDeVisitacao.remove(pos);
														pos = v1.ordemDeVisitacao.indexOf(clienteX);
														v1.ordemDeVisitacao.remove(pos);

														v1.ordemDeVisitacao.add(u, clienteU);
														v1.ordemDeVisitacao.add(u + 1, clienteX);

													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 4: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
												for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Collections.swap(rotaClonada.listaVeiculos.get(j).ordemDeVisitacao, u, v);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else 
														Collections.swap(v1.ordemDeVisitacao, v, u);
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 5: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteX = v1.ordemDeVisitacao.get(x);

													Collections.swap(v1.ordemDeVisitacao, u, v);
													v1.ordemDeVisitacao.remove(x);
													v1.ordemDeVisitacao.add(v, clienteX);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else { 
														Collections.swap(v1.ordemDeVisitacao, u, v);
														int pos = v1.ordemDeVisitacao.indexOf(clienteX);
														v1.ordemDeVisitacao.remove(pos);
														v1.ordemDeVisitacao.add(x, clienteX);
													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 6: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													int y = v+1;

													if(x == v1.ordemDeVisitacao.size() || y == v1.ordemDeVisitacao.size() )
														break;

													Collections.swap(v1.ordemDeVisitacao, u, v);
													Collections.swap(v1.ordemDeVisitacao, x, y);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else { 
														Collections.swap(v1.ordemDeVisitacao, u, v);
														Collections.swap(v1.ordemDeVisitacao, x, y);
													}	
												}
											}	

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}
										
										}//fecha switch
									}//fecha for
								}	//fecha while
							}//fim da buscalocal

							// as novas rotas são adicionadas em um array auxiliar
							aux.add(rotaClonada);
						}
					}
				}
			} // fecha for

			// é feito um merge da nova população e da população inicial
			novaPopulacao.addAll(populacao);
			novaPopulacao.addAll(aux);

			// as rotas são ordenadas por valor de custos
			Collections.sort(novaPopulacao);

			// é feito um corte para mu indivíduos
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
			//System.out.println(geracoes + " " + menorCustoDescendente);
		} // fecha while

		// menor custo final é encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		BigDecimal bd2 = new BigDecimal(menorCustoTotal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

	}// fecha a main

	public static void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, ArrayList <Cliente> clientes,
																				ArrayList <Veiculo> veiculos, Rota r, Rota rotaClonada) {
		rotaClonada.resetCustoTotalRota();
		v1.resetCustoVeiculo();
		v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for(int l = 0; l < r.getVeiculosUtilizados(); l++) {
			Veiculo v2 = rotaClonada.listaVeiculos.get(l);
			if(v2.ordemDeVisitacao != v1.ordemDeVisitacao)
				rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());
		}
	}
	
}// fecha a classe
