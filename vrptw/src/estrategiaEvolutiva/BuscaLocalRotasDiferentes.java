package estrategiaEvolutiva;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasDiferentes {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//visita-se dois clientes, o cliente U em um veículo e o cliente V em outro veículo, insere-se o cliente U após o cliente V, ambos no mesmo veículo
	public void insereApos(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(v == u)
					continue;
				
				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1|| rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1|| rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se a posição onde será inserido o cliente não é a posição do depósito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1) == deposito)
					continue;

				//o cliente que será visitado é selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//a carga ocupado do veículo que terá um cliente a mais é atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda();
	
				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserção do cliente U é feita no segundo veículo
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);
					
					//remove-se o cliente U no primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					
					//insere-se o cliente U após o cliente V no segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
					
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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente U e X após o cliente V, ambos no mesmo veículo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 2; v++) {
				
				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1|| rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se as posições onde serão inseridos os clientes não são posições do depósito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1) == deposito)
					continue;
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 2) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a carga ocupado do veículo que terá dois clientes a mais é atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserções dos clientes U e X são feitas
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//os clientes U e X são removidos do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//os cliente U e X são inseridos após o cliente V
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteX);

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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente X e U após o cliente V, ambos no mesmo veículo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 2; v++) {
				
				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1|| rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se as posições onde serão inseridos os clientes não são posições do depósito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1) == deposito)
					continue;
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 2) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a carga ocupado do veículo que terá dois clientes a mais é atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserção dos clientes X e U é feita
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se os clientes do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//são inseridos X e U no segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteU);

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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se dois clientes, U e V, cada um em um veículo, e troca-se a posição dos dois
	public void trocaPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				
				//verificação se as posições não são iguais
				if(u == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//as cargas ocupadas dos veículos que sofrerão as trocas são atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca do cliente U com o cliente V é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posição u no primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se nesta posição o cliente V
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição v no segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se nesta posição o cliente U
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);

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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(v);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(u);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se três clientes, U e X em um veículo e V em outro, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 ; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				
				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se a posição onde será inserido o cliente não é posição do depósito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//as cargas ocupadas dos veículos são atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca dos clientes U e X com V é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posição u no primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//o cliente V é inserido nesta posição
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição x do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//remove-se a posição v do segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U na posição v
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X após a posição v 
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);

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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se quatro clientes, U, X, V e Y, e então troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				int y = 3;
				
				//verificação se as posições não são iguais
				if(u == x || u == v || u == y || x == v || x == y || v == y)
					continue;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v) == deposito)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y);

				//as cargas ocupadas dos veículos são atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a troca dos clientes U e X com V e Y é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posição u do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente V nesta posição
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição x do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					//insere-se o cliente Y nesta posição
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteY);

					//remove-se a posição v do segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U nesta posição
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);

					//remove-se a posição y do segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteY);
					//insere-se o cliente X nesta poosição
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteX);										

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
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se não hover melhora, a troca é desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteY);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteY);											
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}
}
