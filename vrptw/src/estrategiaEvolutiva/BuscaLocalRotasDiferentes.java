package estrategiaEvolutiva;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocalRotasDiferentes {
	
	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	
	public void insereApos(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

				//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
					continue;
				}
				else {
					v2.ordemDeVisitacao.remove(posV + 1);

					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

	}

	public void insereDuasPosicoesAposUma(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

				//se acha um custo menor, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);
	}
	
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

				//se acha um custo menor, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);
	}
	
	public void trocaPosicoes(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k+1);

				//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);
	}
	
	
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
		
		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

					fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k+1);

				}else
					continue;

				//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);

	}
	
	public void trocaDuasPosicoesComUmaPosicaoInvertido(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

					fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k+1);

				}else
					continue;

				//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);
		
	}
	
	public void trocaDuasPosicoes(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				int y = v + 1;

				if(k+1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verificação se as posiçoes analisadas não estão fora da ordem de visitação e se os clientes analisados não são o depósito
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

				fbl.calculaCustoFuncaoObjetivoDoisVeiculos(v1, matrizDeDistancias, multa, rotaClonada, k + 1);

				//se acha um custo menor, pára, se não desfaz as trocas continua a percorrer a ordem de visitação
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos);
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

		fbl.calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, rotaClonada);
		
	}
		
	
	
}
