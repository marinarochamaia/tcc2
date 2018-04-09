package estrategiaEvolutiva;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocalRotasDiferentes {
	
	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	
	//visita-se dois clientes, o cliente U em um veículo e o cliente V em outro veículo, insere-se o cliente U após o cliente V, ambos no mesmo veículo
	public void insereApos(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);

				//a carga ocupado do veículo que terá um cliente a mais é atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda();
				
				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserção do cliente U é feita no segundo veículo
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//remove-se o cliente U no primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente U após o cliente V no segundo veículo
					v2.ordemDeVisitacao.add(v, clienteU);
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;				
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {

					v2.ordemDeVisitacao.remove(clienteU);

					v1.ordemDeVisitacao.add(u, clienteU);

				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

	}

	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente U e X após o cliente V, ambos no mesmo veículo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				//a carga ocupado do veículo que terá dois clientes a mais é atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserções dos clientes U e X são feitas
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {

					//os clientes U e X são removidos do primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					v1.ordemDeVisitacao.remove(clienteX);
					
					//os cliente U e X são inseridos após o cliente V
					v2.ordemDeVisitacao.add(v + 1, clienteU);
					v2.ordemDeVisitacao.add(v + 2, clienteX);
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					v2.ordemDeVisitacao.remove(clienteU);
					v1.ordemDeVisitacao.add(u, clienteU);
					
					v2.ordemDeVisitacao.remove(clienteX);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}
		
		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	
	}
	
	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente X e U após o cliente V, ambos no mesmo veículo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
			
				
				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);


				//a carga ocupado do veículo que terá dois clientes a mais é atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserção dos clientes X e U é feita
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {

					//remove-se os clientes do primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					v1.ordemDeVisitacao.remove(clienteX);
					
					//são inseridos X e U no segundo veículo
					v2.ordemDeVisitacao.add(v + 1, clienteX);
					v2.ordemDeVisitacao.add(v + 2, clienteU);
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;
				
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					v2.ordemDeVisitacao.remove(clienteU);
					v2.ordemDeVisitacao.remove(clienteX);
					
					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se dois clientes, U e V, cada um em um veículo, e troca-se a posição dos dois
	public void trocaPosicoes(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//as cargas ocupadas dos veículos que sofrerão as trocas são atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca do cliente U com o cliente V é feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//remove-se a posição u no primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					//insere-se nesta posição o cliente V
					v1.ordemDeVisitacao.add(u, clienteV);
					
					//remove-se a posição v no segundo veículo
					v2.ordemDeVisitacao.remove(clienteV);
					//insere-se nesta posição o cliente U
					v2.ordemDeVisitacao.add(v, clienteU);
					
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					v2.ordemDeVisitacao.remove(v);
					v2.ordemDeVisitacao.add(v, clienteV);

					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	
	}
	
	//visita-se três clientes, U e X em um veículo e V em outro, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
		
		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
		
				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//as cargas ocupadas dos veículos são atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca dos clientes U e X com V é feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
					
					//remove-se a posição u no primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					//o cliente V é inserido nesta posição
					v1.ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição x do primeiro veículo
					v1.ordemDeVisitacao.remove(clienteX);

					//remove-se a posição v do segundo veículo
					v2.ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U na posição v
					v2.ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X após a posição v 
					v2.ordemDeVisitacao.add(v + 1, clienteX);
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
					
				}
				//se não hover melhora, a troca é desfeita
				else {
					v2.ordemDeVisitacao.remove(clienteU);
					v2.ordemDeVisitacao.add(v, clienteV);
					v2.ordemDeVisitacao.remove(clienteX);

					v1.ordemDeVisitacao.remove(clienteV);
					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

	}
	
	
	//visita-se quatro clientes, U, X, V e Y, e então troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, Veiculo v1, Veiculo v2, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				int y = v + 1;
				
				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(y >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);
				Cliente clienteY = v2.ordemDeVisitacao.get(y);

				//as cargas ocupadas dos veículos são atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca dos clientes U e X com V e Y é feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

					//remove-se a posição u do primeiro veículo
					v1.ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente V nesta posição
					v1.ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição x do primeiro veículo
					v1.ordemDeVisitacao.remove(clienteX);
					//insere-se o cliente Y nesta posição
					v1.ordemDeVisitacao.add(x, clienteY);

					//remove-se a posição v do segundo veículo
					v2.ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U nesta posição
					v2.ordemDeVisitacao.add(v, clienteU);

					//remove-se a posição y do segundo veículo
					v2.ordemDeVisitacao.remove(clienteY);
					//insere-se o cliente X nesta poosição
					v2.ordemDeVisitacao.add(y, clienteX);										
				}
				//se a carga máxima não for respeitada continua-se percorrendo o array de ordem de visitação
				else
					continue;
			
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					v2.ordemDeVisitacao.remove(clienteU);
					v2.ordemDeVisitacao.add(v, clienteV);

					v2.ordemDeVisitacao.remove(clienteX);
					v2.ordemDeVisitacao.add(y, clienteY);

					v1.ordemDeVisitacao.remove(clienteV);
					v1.ordemDeVisitacao.add(u, clienteU);

					v1.ordemDeVisitacao.remove(clienteY);											
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
}
