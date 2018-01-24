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

		double menorCusto = 0; // menor custo encontrado na popula��o inicial
		double menorCustoDescendente = 0; // menor custo encontrado nas novas gera��es
		double menorCustoTotal = 0; // menor custo Final
		int numeroDeRotas = 30; // mu tamanho da popula��o inicial
		int gmax = 10;// n�mero de gera��es
		int descendentes = 150; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.6; // coeficiente de muta��o
		double cBuscaLocal = 0.6; // coeficiente de busca local

		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local
		//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
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
		while (geracoes < gmax) {
			// para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {
				// a rota � clonada
				Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
				rotaClonada = (Rota) r.getClone(rotaClonada);
				for(int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {
					// s�o selecionados n�meros aleat�rios que ser�o utilizados para pegar os ve�culos
					Random rnd = new Random();
					int k = rnd.nextInt(rotaClonada.getVeiculosUtilizados() - 1);
					// os ve�culos s�o selecionados
					Veiculo v1 = rotaClonada.listaVeiculos.get(k);
					//gerar (lambda/mu) w filhos
					for(int i = 0; i < (descendentes/numeroDeRotas); i++) {

						double m = rnd.nextDouble();

						for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

							if(m <= cMutacao) {

								if(v1.getCargaMaxima() - rotaClonada.listaClientes.get(j).getDemanda() + rotaClonada.listaClientes.get(i).getDemanda()
										<= v1.getCargaMaxima())
									Collections.swap(rotaClonada.listaClientes, i, j);

								// calcula os custos da nova rota (fun��o de avalia��o ou aptid�o)
								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada.listaClientes,
										rotaClonada.listaVeiculos, r, rotaClonada);
							}
						}

						double b = rnd.nextDouble();
						//a busca local s� � feita se o fator pl for atendido
						if (b <= cBuscaLocal){
							int count = 0;
							while(count <= (clientes.size()/2)) {	
								count++;


								//1) remover u e inserir ap�s v;
								//2) remover u e x e inserir u e x ap�s v;
								//3) remover u e x e inserir x e u ap�s v; (posi��es invertidas)
								//4) trocar u e v; // SWAP
								//5) troca u e x com v;
								//6) troca u e x com v e y;
								//7) troca u e v em ve�culos diferentes
								//8) troca u e x com v e y em ve�culos diferentes
								ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
								Collections.shuffle(operacoes);	

								for(Integer o : operacoes) {

									switch (o) {
									case 1: {

										double custoAntesBuscaLocal = v1.getCustoVeiculo();

										//n�o parte de zero e termina uma posi��o antes por causa do dep�sito
										for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);

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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
													break;

												if(x >= v1.ordemDeVisitacao.size())
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												Cliente clienteX = v1.ordemDeVisitacao.get(x);
												Cliente clienteV = v1.ordemDeVisitacao.get(v);

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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
													break;

												if(x >= v1.ordemDeVisitacao.size())
													break;

												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												Cliente clienteX = v1.ordemDeVisitacao.get(x);
												Cliente clienteV = v1.ordemDeVisitacao.get(v);

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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
													break;

												if(x >= v1.ordemDeVisitacao.size())
													break;

												Cliente clienteX = v1.ordemDeVisitacao.get(x);

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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
											int x = u + 1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

												int y = v+1;

												if(v >= v1.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size())
													break;

												if(x >= v1.ordemDeVisitacao.size())
													break;

												if(y >= v1.ordemDeVisitacao.size())
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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
											for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

												if(v >= r.getVeiculosUtilizados())
													break;

												Veiculo v2 = r.listaVeiculos.get(v);

												if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size())
													break;

												if(u == v1.ordemDeVisitacao.size()  || u == v2.ordemDeVisitacao.size())
													break;

												if(v == v1.ordemDeVisitacao.size()  || v == v2.ordemDeVisitacao.size())
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

										for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
											int x = u+1;
											for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
												
												int y = v + 1;
												
												if(v >= r.getVeiculosUtilizados())
													break;

												Veiculo v2 = r.listaVeiculos.get(v);
												
												if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size())
													break;

												if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size())
													break;

												if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size())
													break;

												if(y >= v1.ordemDeVisitacao.size() || y >= v2.ordemDeVisitacao.size())
													break;

												if(u == v1.ordemDeVisitacao.size()  || u == v2.ordemDeVisitacao.size())
													break;

												if(v == v1.ordemDeVisitacao.size()  || v == v2.ordemDeVisitacao.size())
													break;
												
												if(x == v1.ordemDeVisitacao.size()  || x == v2.ordemDeVisitacao.size())
													break;

												if(y == v1.ordemDeVisitacao.size()  || y == v2.ordemDeVisitacao.size())
													break;


												Cliente clienteU = v1.ordemDeVisitacao.get(u);
												Cliente clienteV = v1.ordemDeVisitacao.get(v);
												Cliente clienteX = v2.ordemDeVisitacao.get(x);
												Cliente clienteY = v2.ordemDeVisitacao.get(y);

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
													v2.ordemDeVisitacao.remove(y);
													
													v2.ordemDeVisitacao.add(x, clienteU);
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

									// as novas rotas s�o adicionadas em um array auxiliar
									aux.add(rotaClonada);
								}

							}

						} // fecha for

						// � feito um merge da nova popula��o e da popula��o inicial
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
							if (menorCustoDescendente > r.getCustoTotalRota()) {
								menorCustoDescendente = r.getCustoTotalRota();
							}
						}

						geracoes++;
						BigDecimal bd = new BigDecimal(menorCustoDescendente).setScale(2,RoundingMode.HALF_EVEN);
						System.out.println(geracoes + " " + bd.doubleValue());
					} // fecha while

					// menor custo final � encontrado
					if (menorCusto < menorCustoDescendente)
						menorCustoTotal = menorCusto;
					else
						menorCustoTotal = menorCustoDescendente;

					BigDecimal bd1 = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
					System.out.println("Menor custo antes da aplica��o do algoritmo evolutivo: " + bd1.doubleValue());

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