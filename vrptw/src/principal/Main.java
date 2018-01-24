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
		int numeroDeRotas = 50; // mu tamanho da população inicial
		int gmax = 100;// número de gerações
		int descendentes = 250; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada às rotas que não chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de mutação
		double cBuscaLocal = 0.3; // coeficiente de busca local


		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os indíviduos criados através da busca local
		//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de veículos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)
		// matriz que salva as distâncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()]; 

		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
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
			if(r.getVeiculosUtilizados() <= veiculos.size())
				populacao.add(r);	
		}

		// busca pelo menor custo da população inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}



		// número de gerações que serão criadas
		int geracoes = 0;

		// laço para fazer a mutação em todas as gerações criadas
		while (geracoes < gmax) {
			// para cada indivíduo da população (pai) 
			for (Rota r : populacao) {
				// a rota é clonada
				Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
				rotaClonada = (Rota) r.getClone(rotaClonada);
				for(int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {
					// são selecionados números aleatórios que serão utilizados para pegar os veículos
					Random rnd = new Random();
					int k = rnd.nextInt(rotaClonada.getVeiculosUtilizados() - 1);
					// os veículos são selecionados
					Veiculo v1 = rotaClonada.listaVeiculos.get(k);
					//gerar (lambda/mu) w filhos
					for(int i = 0; i < (descendentes/numeroDeRotas); i++) {

						double m = rnd.nextDouble();

						for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

							if(m <= cMutacao) {

								if(v1.getCargaMaxima() - rotaClonada.listaClientes.get(j).getDemanda() + rotaClonada.listaClientes.get(i).getDemanda()
										<= v1.getCargaMaxima())
									Collections.swap(rotaClonada.listaClientes, i, j);

								// calcula os custos da nova rota (função de avaliação ou aptidão)
								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
										rotaClonada.listaVeiculos, r, rotaClonada);
							}
						}

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
								//7) troca u e v em veículos diferentes
								//8) troca u e x com v e y em veículos diferentes
								ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
								Collections.shuffle(operacoes);	

								for(Integer o : operacoes) {

									switch (o) {
									case 1: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										//não parte de zero e termina uma posição antes por causa do depósito
										for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);

												if(clienteU.getNumero() == 0)
													break;

												v1.ordemDeVisitacao.remove(u);
												v1.ordemDeVisitacao.add(v, clienteU);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);


												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else {
													v1.ordemDeVisitacao.remove(v);
													v1.ordemDeVisitacao.add(u, clienteU);
												}
											}
										}

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);


										break;
									}


									case 2: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(u >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												if(v >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												if(x >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(x).getNumero() == 0)
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												if(clienteU.getNumero() == 0)
													break;
												Cliente clienteX = v1.ordemDeVisitacao.get(x);
												if(clienteX.getNumero() == 0)
													break;
												Cliente clienteV = v1.ordemDeVisitacao.get(v);
												if(clienteV.getNumero() == 0)
													break;

												v1.ordemDeVisitacao.remove(u);
												v1.ordemDeVisitacao.remove(x - 1);

												int posV = v1.ordemDeVisitacao.indexOf(clienteV);

												v1.ordemDeVisitacao.add(posV + 1, clienteU);
												v1.ordemDeVisitacao.add(posV + 2, clienteX);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);


												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
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

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);

										break;
									}
									case 3: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												if(x >= v1.ordemDeVisitacao.size()|| v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												if(clienteU.getNumero() == 0)
													break;
												Cliente clienteV = v1.ordemDeVisitacao.get(v);
												if(clienteV.getNumero() == 0)
													break;
												Cliente clienteX = v1.ordemDeVisitacao.get(x);
												if(clienteX.getNumero() == 0)
													break;


												v1.ordemDeVisitacao.remove(u);
												v1.ordemDeVisitacao.remove(x - 1);

												int posV = v1.ordemDeVisitacao.indexOf(clienteV);

												v1.ordemDeVisitacao.add(posV + 1, clienteX);
												v1.ordemDeVisitacao.add(posV + 2, clienteU);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
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

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);

										break;
									}

									case 4: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else 
													Collections.swap(v1.ordemDeVisitacao, u, v);
											}
										}

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);

										break;
									}


									case 5: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												if(u >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
													break;

												Cliente clienteX = v1.ordemDeVisitacao.get(x);
												if(clienteX.getNumero() == 0)
													break;

												Collections.swap(v1.ordemDeVisitacao, u, v);
												v1.ordemDeVisitacao.remove(x);
												v1.ordemDeVisitacao.add(v, clienteX);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else { 
													Collections.swap(v1.ordemDeVisitacao, u, v);
													int pos = v1.ordemDeVisitacao.indexOf(clienteX);
													v1.ordemDeVisitacao.remove(pos);
													v1.ordemDeVisitacao.add(x, clienteX);
												}
											}
										}

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);

										break;
									}
									case 6: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												int y = v+1;

												if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
													break;

												if(u >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(u).getNumero() == 0)
													break;

												if(x >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(x).getNumero() == 0)
													break;

												if(y >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(y).getNumero() == 0)
													break;


												Collections.swap(v1.ordemDeVisitacao, u, v);
												Collections.swap(v1.ordemDeVisitacao, x, y);

												calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
														rotaClonada.listaVeiculos, r, rotaClonada);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else { 
													Collections.swap(v1.ordemDeVisitacao, u, v);
													Collections.swap(v1.ordemDeVisitacao, x, y);
												}	
											}
										}	

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);

										break;
									}
									
									case 7: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= rotaClonada.getVeiculosUtilizados())
													break;

												Veiculo v2 = rotaClonada.listaVeiculos.get(v);

												if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
														|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
													break;

												if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size())
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												Cliente clienteV = v2.ordemDeVisitacao.get(v);

												double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
												double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();


												if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.add(u, clienteV);
													v2.ordemDeVisitacao.remove(v);
													v2.ordemDeVisitacao.add(v, clienteU);
												}

												calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, clientes, veiculos,
														r, rotaClonada, v);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else {
													v2.ordemDeVisitacao.remove(v);
													v2.ordemDeVisitacao.add(v, clienteV);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.add(u, clienteU);
												}
											}
										}

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);
										break;
									}

									case 8: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();		

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
											int x = u+1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												int y = v + 1;

												if(v >= rotaClonada.getVeiculosUtilizados())
													break;

												Veiculo v2 = rotaClonada.listaVeiculos.get(v);
												if(v2.ordemDeVisitacao == v1.ordemDeVisitacao)
													break;

												if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size()
														|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
													break;

												if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
														|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
													break;

												if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
														|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
													break;

												if(y >= v1.ordemDeVisitacao.size() || y >= v2.ordemDeVisitacao.size()
														|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												if(clienteU.getNumero() == 0)
													break;
												Cliente clienteV = v1.ordemDeVisitacao.get(v);
												if(clienteV.getNumero() == 0)
													break;
												Cliente clienteX = v2.ordemDeVisitacao.get(x);
												if(clienteX.getNumero() == 0)
													break;
												Cliente clienteY = v2.ordemDeVisitacao.get(y);
												if(clienteY.getNumero() == 0)
													break;
												
												
												
												
												double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteV.getDemanda() +
														clienteX.getDemanda() + clienteY.getDemanda();
												double cargaOcupadaV2 = v2.getCargaOcupada() - clienteX.getDemanda() - clienteY.getDemanda() +
														clienteU.getDemanda() + clienteV.getDemanda();


												if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.add(u, clienteX);

													v1.ordemDeVisitacao.remove(v);
													v1.ordemDeVisitacao.add(v, clienteY);

													v2.ordemDeVisitacao.remove(x);
													v2.ordemDeVisitacao.add(x, clienteU);

													v2.ordemDeVisitacao.remove(y);
													v2.ordemDeVisitacao.add(y, clienteV);										


												}

												calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, clientes, veiculos,
														r, rotaClonada, v);

												if(v1.getCustoVeiculo() < custoAntesBuscaLocal)
													break;
												else {
													v2.ordemDeVisitacao.remove(x);
													v2.ordemDeVisitacao.add(x, clienteU);

													v2.ordemDeVisitacao.remove(y);

													v2.ordemDeVisitacao.add(y, clienteV);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.remove(v);

													v1.ordemDeVisitacao.add(u, clienteX);
													v1.ordemDeVisitacao.add(v, clienteY);
												}
											}
										}

										calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
												rotaClonada.listaVeiculos, r, rotaClonada);
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

			} // fecha for

			// é feito um merge da nova população e da população inicial
			novaPopulacao.addAll(populacao);
			novaPopulacao.addAll(aux);

			// as rotas são ordenadas por valor de custos
			Collections.sort(novaPopulacao);

			// é feito um corte para mu indivíduos
			for (int p = (novaPopulacao.size() - 1); p >= numeroDeRotas; p--) {
				novaPopulacao.remove(p);
			}

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (Rota r : novaPopulacao) {

				if (menorCustoDescendente > r.getCustoTotalRota()) {
					menorCustoDescendente = r.getCustoTotalRota();
					melhorRota = r;
				}
			}

			geracoes++;

		} // fecha while

		// menor custo final é encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo antes da aplicação do algoritmo evolutivo: " + bd1.doubleValue());

		BigDecimal bd2 = new BigDecimal(menorCustoTotal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

		for(int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {
			System.out.println(i + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);
		}



	}// fecha a main

	public static void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, ArrayList <Cliente> clientes,
			ArrayList <Veiculo> veiculos, Rota r, Rota rotaClonada) {
		rotaClonada.resetCustoTotalRota();
		v1.resetCustoVeiculo();
		v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for(int l = 0; l < r.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if(v3.ordemDeVisitacao != v1.ordemDeVisitacao)
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
		}
	}



	public static void calculaCustoFuncaoObjetivoDoisVeiculos(Veiculo v1, double[][] matrizDeDistancias, int multa, ArrayList <Cliente> clientes,
			ArrayList <Veiculo> veiculos, Rota r, Rota rotaClonada, int v) {

		Veiculo v2 = rotaClonada.listaVeiculos.get(v);

		rotaClonada.resetCustoTotalRota();
		v1.resetCustoVeiculo();
		v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for(int l = 0; l < r.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if(v3.ordemDeVisitacao != v1.ordemDeVisitacao)
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
		}

		v2.resetCustoVeiculo();
		v2.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
		rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());

		for(int l = 0; l < r.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if(v3.ordemDeVisitacao != v1.ordemDeVisitacao)
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
		}
	}




}// fecha a classe