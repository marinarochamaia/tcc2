package modelos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class BuscaLocal {

	public void fazBuscaLocal(Veiculo v1, Rota rotaClonada, double [][] matrizDeDistancias, int multa, 
			int k, double cBuscaLocal){

		Random rnd = new Random();
		double b = rnd.nextDouble();

		//a busca local s� � feita se o fator pl for atendido
		if (b <= cBuscaLocal){
			int count = 0;
			while(count <= rotaClonada.listaClientes.size()/2) {	
				count++;

				//1) remover u e inserir ap�s v;
				//2) remover u e x e inserir u e x ap�s v;
				//3) remover u e x e inserir x e u ap�s v; (posi��es invertidas)
				//4) trocar u e v; // SWAP
				//5) troca u e x com v;
				//6) troca u e x com v e y;
				//7) troca u e v em ve�culos diferentes
				//8) troca u e v com x e y em ve�culos diferentes
				//9) troca u e v com x em ve�culos diferentes


				ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
				Collections.shuffle(operacoes);	

				for(Integer o : operacoes) {

					switch (o) {
					case 1: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
							for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {	

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);

								v1.ordemDeVisitacao.remove(u);
								v1.ordemDeVisitacao.add(v, clienteU);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else {
									v1.ordemDeVisitacao.remove(v);
									v1.ordemDeVisitacao.add(u, clienteU);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;
					}

					case 2: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteX = v1.ordemDeVisitacao.get(x);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);

								v1.ordemDeVisitacao.remove(x);
								v1.ordemDeVisitacao.remove(u);					

								int posV = v1.ordemDeVisitacao.indexOf(clienteV);

								v1.ordemDeVisitacao.add(posV + 1, clienteU);
								v1.ordemDeVisitacao.add(posV + 2, clienteX);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
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

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}
					case 3: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(x >= v1.ordemDeVisitacao.size()|| v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);
								Cliente clienteX = v1.ordemDeVisitacao.get(x);

								v1.ordemDeVisitacao.remove(u);
								v1.ordemDeVisitacao.remove(x - 1);

								int posV = v1.ordemDeVisitacao.indexOf(clienteV);

								v1.ordemDeVisitacao.add(posV + 1, clienteX);
								v1.ordemDeVisitacao.add(posV + 2, clienteU);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
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

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 4: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
							for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;

								Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else
									Collections.swap(v1.ordemDeVisitacao, u, v);
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}


					case 5: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
									continue;


								Cliente clienteX = v1.ordemDeVisitacao.get(x);

								Collections.swap(v1.ordemDeVisitacao, u, v);
								v1.ordemDeVisitacao.remove(x);
								v1.ordemDeVisitacao.add(v, clienteX);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
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

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 6: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
								int y = v+1;

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o								
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(x >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(x).getNumero() == 0)
									continue;
								if(y >= v1.ordemDeVisitacao.size()  || v1.ordemDeVisitacao.get(y).getNumero() == 0)
									continue;

								Collections.swap(v1.ordemDeVisitacao, u, v);
								Collections.swap(v1.ordemDeVisitacao, x, y);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else {
									Collections.swap(v1.ordemDeVisitacao, u, v);
									Collections.swap(v1.ordemDeVisitacao, x, y);
								}	
							}
						}	

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 7: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
							for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if(v >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(v);

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v2.ordemDeVisitacao.get(v);

								//verifica-se se a carga m�xima de cada ve�culo ser� respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

								//se for respeitada a troca � feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteV);
									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteU);
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, v);

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else {
									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteV);

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteU);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;
					}

					case 8: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u+1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								int y = v + 1;

								if(v >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(v);
								if(v2.ordemDeVisitacao == v1.ordemDeVisitacao)
									continue;

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(y >= v1.ordemDeVisitacao.size() || y >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);
								Cliente clienteX = v2.ordemDeVisitacao.get(x);
								Cliente clienteY = v2.ordemDeVisitacao.get(y);

								//verifica-se se a carga m�xima de cada ve�culo ser� respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteV.getDemanda() +
										clienteX.getDemanda() + clienteY.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteX.getDemanda() - clienteY.getDemanda() +
										clienteU.getDemanda() + clienteV.getDemanda();

								//se for respeitada a troca � feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteX);

									v1.ordemDeVisitacao.remove(v);
									v1.ordemDeVisitacao.add(v, clienteY);

									v2.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(x, clienteU);

									v2.ordemDeVisitacao.remove(y);
									v2.ordemDeVisitacao.add(y, clienteV);										
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, v);;

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else {
									v2.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(x, clienteX);

									v2.ordemDeVisitacao.remove(y);
									v2.ordemDeVisitacao.add(y, clienteY);

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteU);

									v1.ordemDeVisitacao.remove(v);											
									v1.ordemDeVisitacao.add(v, clienteV);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 9: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u+1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if(v >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(v);
								if(v2.ordemDeVisitacao == v1.ordemDeVisitacao)
									continue;

								//verifica��o se os clientes analisados n�o s�o o dep�sito que s�o a primeira e a �ltima posi��o de cada ordem de visita��o
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);
								Cliente clienteX = v2.ordemDeVisitacao.get(x);

								//verifica-se se a carga m�xima de cada ve�culo ser� respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteV.getDemanda() +
										clienteX.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteX.getDemanda() +
										clienteU.getDemanda() + clienteV.getDemanda();

								//se for respeitada a troca � feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteX);

									v1.ordemDeVisitacao.remove(v);

									v2.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(x, clienteU);
									v2.ordemDeVisitacao.add(x, clienteV);

									calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, v);

								}else
									continue;

								//se acha um custo menor, p�ra, se n�o desfaz as trocas continua a percorrer a ordem de visita��o
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
									break;
								else {
									v2.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(x, clienteX);

									v2.ordemDeVisitacao.remove(x+1);

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteU);

									v1.ordemDeVisitacao.add(v, clienteV);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					}//fecha switch
				}//fecha for
			} //fecha while
		}	//fecha if
	}//fim da buscalocal


	public void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		rotaClonada.resetCustoTotalRota();
		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for(int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if(v3 != v1) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}

	public void calculaCustoFuncaoObjetivoDoisVeiculos(Veiculo v1, double[][] matrizDeDistancias, int multa, Rota rotaClonada, int v) {

		rotaClonada.resetCustoTotalRota();
		Veiculo v2 = rotaClonada.listaVeiculos.get(v);

		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		v2.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());

		for(int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if(v3 != v1 && v3 != v2) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}
}
