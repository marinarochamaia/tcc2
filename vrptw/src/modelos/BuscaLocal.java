package modelos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class BuscaLocal {

	public void fazBuscaLocal(Veiculo v1, Rota rotaClonada, double [][] matrizDeDistancias, int multa, 
			int k, double cBuscaLocal){

		Random rnd = new Random();
		double bl = rnd.nextDouble();

		//a busca local só é feita se o fator pl for atendido
		if (bl <= cBuscaLocal){
			int count = 0;
			while(count <= rotaClonada.listaClientes.size()/2) {	
				count++;

				//1) remover u e inserir após v;
				//2) remover u e x e inserir u e x após v;
				//3) remover u e x e inserir x e u após v; (posições invertidas)
				//4) trocar u e v; // SWAP
				//5) troca u e x com v;
				//6) troca u e x com v e y;
				//7) troca u e v em veículos diferentes
				//8) troca u e v com x e y em veículos diferentes
				//9) troca u e v com x em veículos diferentes


				ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
				Collections.shuffle(operacoes);	

				for(Integer o : operacoes) {

					switch (o) {
					case 1: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
							for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {	

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);

								v1.ordemDeVisitacao.remove(u);

								int posV = v1.ordemDeVisitacao.indexOf(clienteV);

								v1.ordemDeVisitacao.add(posV+1, clienteU);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v1.ordemDeVisitacao.remove(posV + 1);
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

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
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

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;
								if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
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

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
									continue;
								if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
									continue;

								Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
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

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação								
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

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

								if( k + 1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(u).getNumero() || 0 == v2.ordemDeVisitacao.get(u).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v2.ordemDeVisitacao.get(v);

								//verifica-se se a carga máxima do veículo será respeitada com a troca
								double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda();

								int posV = v2.ordemDeVisitacao.indexOf(clienteV);
								//se for respeitada a troca é feita
								if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
									v1.ordemDeVisitacao.remove(u);
									v2.ordemDeVisitacao.add(posV + 1, clienteU);
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v2.ordemDeVisitacao.remove(posV + 1);

									v1.ordemDeVisitacao.add(u, clienteU);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 8: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if( k + 1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(u).getNumero() || 0 == v2.ordemDeVisitacao.get(u).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(x).getNumero() || 0 == v2.ordemDeVisitacao.get(x).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteX = v1.ordemDeVisitacao.get(x);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);

								//verifica-se se a carga máxima do veículo será respeitada com a troca
								double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

								int posV = v2.ordemDeVisitacao.indexOf(clienteV);
								//se for respeitada a troca é feita
								if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(posV + 1, clienteU);
									v2.ordemDeVisitacao.add(posV + 2, clienteX);
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

								//se acha um custo menor, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v2.ordemDeVisitacao.remove(posV + 1);
									v1.ordemDeVisitacao.add(u, clienteU);
									v2.ordemDeVisitacao.remove(posV + 1);
									v1.ordemDeVisitacao.add(x, clienteX);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 9: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if( k + 1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(u).getNumero() || 0 == v2.ordemDeVisitacao.get(u).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(x).getNumero() || 0 == v2.ordemDeVisitacao.get(x).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteX = v1.ordemDeVisitacao.get(x);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);

								//verifica-se se a carga máxima do veículo será respeitada com a troca
								double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

								int posV = v2.ordemDeVisitacao.indexOf(clienteV);
								//se for respeitada a troca é feita
								if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.remove(x);
									v2.ordemDeVisitacao.add(posV + 1, clienteX);
									v2.ordemDeVisitacao.add(posV + 2, clienteU);
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

								//se acha um custo menor, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v2.ordemDeVisitacao.remove(posV + 1);
									v2.ordemDeVisitacao.remove(posV + 1);
									v1.ordemDeVisitacao.add(u, clienteU);
									v1.ordemDeVisitacao.add(x, clienteX);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 10: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
							for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if(k + 1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size() 
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v2.ordemDeVisitacao.get(v);

								//verifica-se se a carga máxima de cada veículo será respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

								//se for respeitada a troca é feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteV);
									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteU);
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k+1);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
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

					case 11: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

								if(k + 1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(x).getNumero() || 0 == v2.ordemDeVisitacao.get(x).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);
								Cliente clienteX = v2.ordemDeVisitacao.get(x);

								//verifica-se se a carga máxima de cada veículo será respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteV.getDemanda() +
										clienteX.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteX.getDemanda() +
										clienteU.getDemanda() + clienteV.getDemanda();

								//se for respeitada a troca é feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteV);

									v1.ordemDeVisitacao.remove(x);

									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteU);
									v2.ordemDeVisitacao.add(v+1, clienteX);

									calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k+1);

								}else
									continue;

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteV);
									v2.ordemDeVisitacao.remove(v+1);

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteU);
									v1.ordemDeVisitacao.add(x, clienteX);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 12: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

						for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
							int x = u + 1;
							for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
								int y = v + 1;

								if(k+1 >= rotaClonada.getVeiculosUtilizados())
									continue;

								Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

								//verificação se os clientes analisados não são o depósito que são a primeira e a última posição de cada ordem de visitação
								if(v >= v1.ordemDeVisitacao.size() || v >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(v).getNumero() || 0 == v2.ordemDeVisitacao.get(v).getNumero())
									continue;
								if(u >= v1.ordemDeVisitacao.size() || u >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(u).getNumero() || 0 == v2.ordemDeVisitacao.get(u).getNumero())
									continue;
								if(x >= v1.ordemDeVisitacao.size() || x >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(x).getNumero() || 0 == v2.ordemDeVisitacao.get(x).getNumero())
									continue;
								if(y >= v1.ordemDeVisitacao.size() || y >= v2.ordemDeVisitacao.size()
										|| 0 == v1.ordemDeVisitacao.get(y).getNumero() || 0 == v2.ordemDeVisitacao.get(y).getNumero())
									continue;

								Cliente clienteU = v1.ordemDeVisitacao.get(u);
								Cliente clienteV = v1.ordemDeVisitacao.get(v);
								Cliente clienteX = v2.ordemDeVisitacao.get(x);
								Cliente clienteY = v2.ordemDeVisitacao.get(y);

								//verifica-se se a carga máxima de cada veículo será respeitada com a troca
								double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
										clienteV.getDemanda() + clienteY.getDemanda();
								double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
										clienteU.getDemanda() + clienteX.getDemanda();

								//se for respeitada a troca é feita
								if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteV);

									v1.ordemDeVisitacao.remove(x);
									v1.ordemDeVisitacao.add(x, clienteY);

									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteU);

									v2.ordemDeVisitacao.remove(y);
									v2.ordemDeVisitacao.add(y, clienteX);										
								}else
									continue;

								calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v2.ordemDeVisitacao.remove(v);
									v2.ordemDeVisitacao.add(v, clienteU);

									v2.ordemDeVisitacao.remove(y);
									v2.ordemDeVisitacao.add(y, clienteX);

									v1.ordemDeVisitacao.remove(u);
									v1.ordemDeVisitacao.add(u, clienteV);

									v1.ordemDeVisitacao.remove(x);											
									v1.ordemDeVisitacao.add(x, clienteY);
								}
							}
						}

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}

					case 13: {

						double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
						ArrayList<Cliente> old = new ArrayList<>();
						old.addAll(v1.ordemDeVisitacao); 

						for(int i = 1; i < v1.ordemDeVisitacao.size()/2; i++) {
							for(int j = 0; j < v1.ordemDeVisitacao.size()-1; j++) {

								Cliente a = v1.ordemDeVisitacao.get(i);
								Cliente b = v1.ordemDeVisitacao.get((i+k)%v1.ordemDeVisitacao.size());

								int posX = v1.ordemDeVisitacao.indexOf(a)+1;
								int posV = v1.ordemDeVisitacao.indexOf(b)-1;
								
								if(posV >= v1.ordemDeVisitacao.size() || posX >= v1.ordemDeVisitacao.size())
									continue;

								ArrayList<Cliente> aux = new ArrayList<>();
								
								for(int l = posV; l >= posX; l--) {
									aux.add(v1.ordemDeVisitacao.get(l));
								}

								for(int m = 0; m < v1.ordemDeVisitacao.size(); m++) {
									if(m >= posX && m <= posV) {
										v1.ordemDeVisitacao.remove(m);
										v1.ordemDeVisitacao.add(m, aux.get(0));
										aux.remove(0);
									}
								}

								calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

								//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
								if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
									atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
									continue;
								}
								else {
									v1.ordemDeVisitacao.clear();
									v1.ordemDeVisitacao.addAll(old);
								}	
							}
						}	

						calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

						break;

					}


					}//fecha switch
				}//fecha for
			}

		} // fecha if
	}// fim da buscalocal

	public void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		rotaClonada.resetCustoTotalRota();
		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if (l != rotaClonada.listaVeiculos.indexOf(v1)) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}

	public void calculaCustoFuncaoObjetivoDoisVeiculos(Veiculo v1, double[][] matrizDeDistancias, int multa,
			Rota rotaClonada, int v) {

		rotaClonada.resetCustoTotalRota();
		Veiculo v2 = rotaClonada.listaVeiculos.get(v);

		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		v2.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());

		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if (l != rotaClonada.listaVeiculos.indexOf(v1) && l != v) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}

	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos) {

		for (int i = 0; i > listaVeiculos.size(); i++) {
			Veiculo v = listaVeiculos.get(i);
			for (int j = 0; j < v.ordemDeVisitacao.size(); j++) {
				giantTour.add(i, v.ordemDeVisitacao.get(j));

			}
		}

	}
}
