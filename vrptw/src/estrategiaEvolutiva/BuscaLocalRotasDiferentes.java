package estrategiaEvolutiva;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasDiferentes {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//visita-se dois clientes, o cliente U em um ve�culo e o cliente V em outro ve�culo, insere-se o cliente U ap�s o cliente V, ambos no mesmo ve�culo
	public void insereApos(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//verifica��o se a posi��o onde ser� inserido o cliente n�o � a posi��o do dep�sito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1).getNumero() == 0)
					continue;

				//o cliente que ser� visitado � selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//a carga ocupado do ve�culo que ter� um cliente a mais � atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o do cliente U � feita no segundo ve�culo
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {
					//remove-se o cliente U no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se o cliente U ap�s o cliente V no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;				

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente U e X ap�s o cliente V, ambos no mesmo ve�culo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 2; v++) {

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//verifica��o se as posi��es onde ser�o inseridos os clientes n�o s�o posi��es do dep�sito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1).getNumero() == 0)
					continue;
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 2).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a carga ocupado do ve�culo que ter� dois clientes a mais � atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��es dos clientes U e X s�o feitas
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//os clientes U e X s�o removidos do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//os cliente U e X s�o inseridos ap�s o cliente V
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteX);

				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}

	}

	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente X e U ap�s o cliente V, ambos no mesmo ve�culo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 2; v++) {

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//verifica��o se as posi��es onde ser�o inseridos os clientes n�o s�o posi��es do dep�sito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1).getNumero() == 0)
					continue;
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 2).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//a carga ocupado do ve�culo que ter� dois clientes a mais � atualizada
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o dos clientes X e U � feita
				if(cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//remove-se os clientes do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//s�o inseridos X e U no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 2, clienteU);

				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se dois clientes, U e V, cada um em um ve�culo, e troca-se a posi��o dos dois
	public void trocaPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//as cargas ocupadas dos ve�culos que sofrer�o as trocas s�o atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca do cliente U com o cliente V � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//remove-se a posi��o u no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//insere-se nesta posi��o o cliente V
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o v no segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se nesta posi��o o cliente U
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);

				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(v);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(u);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se tr�s clientes, U e X em um ve�culo e V em outro, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//verifica��o se a posi��o onde ser� inserido o cliente n�o � posi��o do dep�sito
				if(rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v + 1).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);

				//as cargas ocupadas dos ve�culos s�o atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca dos clientes U e X com V � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

					//remove-se a posi��o u no primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					//o cliente V � inserido nesta posi��o
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					//remove-se a posi��o v do segundo ve�culo
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteV);
					//insere-se o cliente U na posi��o v
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X ap�s a posi��o v 
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v + 1, clienteX);

				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se quatro clientes, U, X, V e Y, e ent�o troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, int n, int multa, double [][] matrizDeDistancias, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() - 1; v++) {
				int y = v + 1;

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.size() || rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.get(y);

				//as cargas ocupadas dos ve�culos s�o atualizadas
				double cargaOcupadaV1 = rotaClonada.listaVeiculos.get(k).getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = rotaClonada.listaVeiculos.get(n).getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca dos clientes U e X com V e Y � feita
				if(cargaOcupadaV1 <= rotaClonada.listaVeiculos.get(k).getCargaMaxima() && cargaOcupadaV2 <= rotaClonada.listaVeiculos.get(n).getCargaMaxima()) {

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
					//insere-se o cliente X nesta poosi��o
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteX);										

				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(v, clienteV);

					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(n).ordemDeVisitacao.add(y, clienteY);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteV);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteY);											
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}
}
