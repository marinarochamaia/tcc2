package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasDiferentes {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//1
	//visita-se dois clientes, o cliente U em um ve�culo e o cliente V em outro ve�culo, insere-se o cliente U ap�s o cliente V, ambos no mesmo ve�culo
	public void insereApos(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a distancia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenar as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() -  1; v++) {
				
				if(u == v)
					continue;

				//o cliente que ser� visitado � selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				
				//a demanda do cliente que ser� inserido � somada � carga ocupada do segundo ve�culo
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o do cliente U � feita no segundo ve�culo
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//o novo valor da carga ocupada � setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se o cliente U no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);

					//insere-se o cliente U ap�s o cliente V no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);

				}
				//se a carga m�xima n�o for respeitada, continua-se percorrendo o array de ordem de visita��o
				else
					continue;				

				//atualiza-se a dist�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois a trocar ser desfeita
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {
					
					//a dist�ncia de antes da busca local � atualizado
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//a melhor ordem de visita��o � atualizada
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				}//sen�o, continua-se a percorrer o array da ordem de visita��o
				else 									
					continue;

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

			}
		}

		//os arrays das ordens de visita��es s�o atualizados com as melhores configura��es encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		
		//atualiza-se a dist�ncia e o tempo
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

	}

	//2
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente U e X ap�s o cliente V, ambos no mesmo ve�culo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a dist�ncia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenar as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 2; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a demanda dos clientes que ser�o inseridos s�o somadas � carga ocupada do segundo ve�culo
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, as inser��es dos clientes U e X s�o feitas
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					
					//o novo valor da carga ocupada � setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//os clientes U e X s�o removidos do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//os cliente U e X s�o inseridos ap�s o cliente V
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteX);

				} //se a carga m�xima n�o for respeitada, continua-se percorrendo o array de ordem de visita��o
				else 
					continue;

				//atualiza-se a dist�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//sen�o, salva-se as melhores ordens para depois a troca ser desfeita
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {

					//a dist�ncia de antes da busca local � atualizado
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//os arrays das melhores ordens de visita��o s�o atualizados
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //sen�o, continua-se a percorrer o array da ordem de visita��o
				else
					continue;

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

			}
		}

		//as ordens de visita��es s�o atualizadas com as melhores configura��es encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		//atualiza-se a dist�ncia e o tempo
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}

	//3
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente X e U ap�s o cliente V, ambos no mesmo ve�culo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a distancia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenar as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 2; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 0; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				
				//verifica��o se a posi��o analisada n�o est� fora do array de ordem de visita��o ou se o cliente analisado n�o � o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//as posi��es visitadas s�o selecionadas
				int posU =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteU);
				int posX =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteX);

				//as demandas dos clientes que ser�o inseridos s�o somadas � carga ocupada do segundo ve�culo
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o dos clientes X e U � feita
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//o novo valor da carga ocupada � setado
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se os clientes do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//insere-se X e U no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteU);

				} //se a carga m�xima n�o for respeitada, continua-se percorrendo o array de ordem de visita��o
				else 
					continue;

				//atualiza-se a disnt�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//se for melhor salva a melhor ordem para depois destrocar
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {

					//o custo de antes da busca local � atualizado
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//os arrays das melhores ordens de visita��es s�o atualizados
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //sen�o, continua-se a percorrer o array da ordem de visita��o
				else 					
					continue;

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posU, clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posX, clienteX);

			}
		}

		//as ordens de visita��es s�o atualizadas com os melhores valores encontrados
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		//atualiza-se a dist�ncia e o tempo
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

	}

	//4
	//visita-se dois clientes, U e V, cada um em um ve�culo, e troca-se a posi��o dos dois
	public void trocaPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a distancia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenarem as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//a carga ocupado dos ve�culos que sofrer�o mudan�as s�o calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verifica��o se as cargas m�ximas dos ve�culos s�o respeitadas
				//se forem, a troca do cliente U com o cliente V � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {


					//os novos valores das cargas ocupadas s�o setados
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posi��o u no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se nesta posi��o o cliente V
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o v no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se nesta posi��o o cliente U
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);

				} //se as cargas m�ximas n�o forem respeitadas, continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//atualiza-se a dist�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//se for melhor, salvam-se as melhores ordens para depois desfazer a troca
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {

					//a dist�ncia de antes da busca local � atualizado
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//as melhores ordens de visita��es s�o atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);


				} //sen�o, continua-se a percorrer o array da ordem de visita��o
				else
					continue;

				//as trocas s�o desfeitas
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(v);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(u);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

			}
		}

		//as ordens de visita��es s�o atualizadas com as melhores configura��es encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		//atualiza-se a dist�ncia e o custo
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

	}

	//5
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a distancia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenarem as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 ; u++) {
			int x = 1;
			//verifica��o se as posi��es n�o s�o iguais
			if(u == x) {
				x++;
			}
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se a posi��o analisada n�o est� fora do array de ordem de visita��o e se o cliente analisado n�o � o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0) 
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//a carga ocupado dos ve�culos que sofrer�o mudan�as s�o calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se as cargas m�ximas dos ve�culos s�o respeitadas
				//se forem, a troca dos clientes U e X com V � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//os novos valores das cargas ocupadas s�o setados
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posi��o u no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente V nesta posi��o
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//insere-se o cliente U na posi��o v
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X ap�s a posi��o v 
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);

					//remove-se a posi��o v do segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);

				}
				//se a carga m�xima n�o for respeitada, continua-se percorrendo o array da ordem de visita��o
				else
					continue;

				//atualiza-se a dist�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//se for melhor salvam-se as melhores ordens para depois desfazer a troca
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {

					//a distancia de antes da busca local � atualizado 
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//as melhores ordens de visita��es s�o atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //sen�o, continua-se a percorrer o array da ordem de visita��o
				else 
					continue;

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

			}
		}

		//as ordens de visita��es s�o atualizadas com as melhores configura��es encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		//atualiza-se a dist�ncia e o tempo
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

	}

	//6
	//visita-se quatro clientes, U, X, V e Y, e ent�o troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//guarda-se a dist�ncia antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

		//arrays para armazenarem as melhores ordens de visita��es geradas
		ArrayList<Cliente> melhorOrdemDeVisitacaoV1 = new ArrayList<>();
		ArrayList<Cliente> melhorOrdemDeVisitacaoV2 = new ArrayList<>();

		//as configura��es atuais s�o salvas nos arrays de melhores visita��es
		melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
		melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

		//percorrem-se os arrays das ordens de visita��es
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				int x = 1;
				int y = 1;
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == x)
					x++;
				if(v == y)
					y++;

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y);
				
				//a carga ocupado dos ve�culos que sofrer�o mudan�as s�o calculadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se as carga m�ximas dos ve�culos s�o respeitadas
				//se forem, as trocas dos clientes U e X com V e Y � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//os novos valores das cargas ocupadas s�o setados
					rotaClonada.listaVeiculos.get(k).setCargaOcupada(cargaOcupadaV1);
					rotaClonada.listaVeiculos.get(n).setCargaOcupada(cargaOcupadaV2);

					//remove-se a posi��o u do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente V nesta posi��o
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					//insere-se o cliente Y nesta posi��o
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteY);

					//remove-se a posi��o v do segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U nesta posi��o
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);

					//remove-se a posi��o y do segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteY);
					//insere-se o cliente X nesta posi��o
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteX);		

				} //se a carga m�xima n�o for respeitada, continua-se percorrendo o array de ordem de visita��o
				else 
					continue;

				//atualiza-se a dist�ncia da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se a nova dist�ncia com a anterior para saber se houve melhora ou n�o
				//se for melhor, salvam-se a melhores ordens para depois desfazer as trocas
				if(rotaClonada.getDistanciaTotalRota() < distanciaAntesBuscaLocal) {

					//a dist�ncia de antes da busca local � atualizado
					distanciaAntesBuscaLocal = rotaClonada.getDistanciaTotalRota();

					//as melhores ordens de visita��es s�o atualizadas
					melhorOrdemDeVisitacaoV1.clear();
					melhorOrdemDeVisitacaoV2.clear();
					melhorOrdemDeVisitacaoV1.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					melhorOrdemDeVisitacaoV2.addAll(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao);

				} //sen�o, continua-se a percorrer o array da ordem de visita��o
				else 
					continue;

				//as trocas s�o desfeitas
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteY);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteY);											
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);
				
			}
		}

		//as ordens de visita��es s�o atualizadas com as melhores configura��es encontradas
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV1);
		rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.addAll(melhorOrdemDeVisitacaoV2);
		
		//a dist�ncia e o tempo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

	}
}