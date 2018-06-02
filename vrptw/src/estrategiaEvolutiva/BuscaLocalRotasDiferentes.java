package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasDiferentes {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//8
	//visita-se dois clientes, o cliente U em um veículo e o cliente V em outro veículo, insere-se o cliente U após o cliente V, ambos no mesmo veículo
	public void insereApos(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenar as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() -  1; v++) {
				
				if(u == v)
					continue;

				//o cliente que será visitado é selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				
				//a demanda do cliente que será inserido é somada à carga ocupada do segundo veículo
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
				//se a carga máxima não for respeitada, continua-se percorrendo o array de ordem de visitação
				else
					continue;				

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois a trocar ser desfeita
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//a melhor ordem de visitação é atualizada
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				}//senão, continua-se a percorrer o array da ordem de visitação
				else 									
					continue;

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//os arrays das ordens de visitações são atualizados com as melhores configurações encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}

	//9
	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente U e X após o cliente V, ambos no mesmo veículo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenar as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a demanda dos clientes que serão inseridos são somadas à carga ocupada do segundo veículo
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, as inserções dos clientes U e X são feitas
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//os clientes U e X são removidos do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//os cliente U e X são inseridos após o cliente V
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteX);

				} //se a carga máxima não for respeitada, continua-se percorrendo o array de ordem de visitação
				else 
					continue;

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//senão, salva-se as melhores ordens para depois a troca ser desfeita
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//os arrays das melhores ordens de visitação são atualizados
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //senão, continua-se a percorrer o array da ordem de visitação
				else
					continue;

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//as ordens de visitações são atualizadas com as melhores configurações encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}

	//10
	//visita-se três clientes, U e X em um veículo e V em outro veículo, insere-se os cliente X e U após o cliente V, ambos no mesmo veículo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenar as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size(); v++) {
				
				//verificação se a posição analisada não está fora do array de ordem de visitação ou se o cliente analisado não é o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//as posições visitadas são selecionadas
				int posU =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteU);
				int posX =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteX);

				//as demandas dos clientes que serão inseridos são somadas à carga ocupada do segundo veículo
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se a carga máxima do veículo é respeitada
				//se for, a inserção dos clientes X e U é feita
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//o novo valor da carga ocupada é setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se os clientes do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//insere-se X e U no segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteU);

				} //se a carga máxima não for respeitada, continua-se percorrendo o array de ordem de visitação
				else 
					continue;

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor salva a melhor ordem para depois destrocar
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					// o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//os arrays das melhores ordens de visitações são atualizados
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //senão, continua-se a percorrer o array da ordem de visitação
				else 					
					continue;

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posU, clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posX, clienteX);

				//System.out.println("Depois TROCA V1: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
				//System.out.println("Depois TROCA V2: " + rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//as ordens de visitações são atualizadas com os melhores valores encontrados
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}

	//11
	//visita-se dois clientes, U e V, cada um em um veículo, e troca-se a posição dos dois
	public void trocaPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenarem as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//a carga ocupado dos veículos que sofrerão mudanças são calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verificação se as cargas máximas dos veículos são respeitadas
				//se forem, a troca do cliente U com o cliente V é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {


					//os novos valores das cargas ocupadas são setados
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

				} //se as cargas máximas não forem respeitadas, continua-se percorrendo o array de ordem de visitação
				else
					continue;

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salvam-se as melhores ordens para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//as melhores ordens de visitações são atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);


				} //senão, continua-se a percorrer o array da ordem de visitação
				else
					continue;

				//as trocas são desfeitas
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(v);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(u);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//as ordens de visitações são atualizadas com as melhores configurações encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}

	//12
	//visita-se três clientes, U e X em um veículo e V em outro, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenarem as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 ; u++) {
			int x = 1;
			//verificação se as posições não são iguais
			if(u == x) {
				x++;
			}
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verificação se a posição analisada não está fora do array de ordem de visitação e se o cliente analisado não é o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0) 
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//a carga ocupado dos veículos que sofrerão mudanças são calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se as cargas máximas dos veículos são respeitadas
				//se forem, a troca dos clientes U e X com V é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//os novos valores das cargas ocupadas são setados
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posição u no primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente V nesta posição
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posição x do primeiro veículo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//insere-se o cliente U na posição v
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X após a posição v 
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);

					//remove-se a posição v do segundo veículo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);

				}
				//se a carga máxima não for respeitada, continua-se percorrendo o array da ordem de visitação
				else
					continue;

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor salvam-se as melhores ordens para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado 
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//as melhores ordens de visitações são atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //senão, continua-se a percorrer o array da ordem de visitação
				else 
					continue;

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//as ordens de visitações são atualizadas com as melhores configurações encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}

	//13
	//visita-se quatro clientes, U, X, V e Y, e então troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//arrays para armazenarem as melhores ordens de visitações geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configurações atuais são salvas nos arrays de melhores visitações
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visitações
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				int x = 1;
				int y = 1;
				
				//verificação se as posições não são iguais
				if(u == x)
					x++;
				if(v == y)
					y++;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y);
				
				//a carga ocupado dos veículos que sofrerão mudanças são calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verificação se as carga máximas dos veículos são respeitadas
				//se forem, as trocas dos clientes U e X com V e Y é feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//os novos valores das cargas ocupadas são setados
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
					//insere-se o cliente X nesta posição
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteX);		

				} //se a carga máxima não for respeitada, continua-se percorrendo o array de ordem de visitação
				else 
					continue;

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salvam-se a melhores ordens para depois desfazer as trocas
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//as melhores ordens de visitações são atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //senão, continua-se a percorrer o array da ordem de visitação
				else 
					continue;

				//as trocas são desfeitas
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteY);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteY);											
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);
				
				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
			}
		}

		//as ordens de visitações são atualizadas com as melhores configurações encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);

	}
}