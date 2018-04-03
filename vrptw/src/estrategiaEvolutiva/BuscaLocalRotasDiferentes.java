package estrategiaEvolutiva;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocalRotasDiferentes {
	
	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	
	//visita-se dois clientes, o cliente U em um ve�culo e o cliente V em outro ve�culo, insere-se o cliente U ap�s o cliente V, ambos no mesmo ve�culo
	public void insereApos(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//a carga ocupado do ve�culo que ter� um cliente a mais � atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda();

				//a posi��o do cliente V � encontrada
				int posV = v2.ordemDeVisitacao.indexOf(clienteV);
				
				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o do cliente U � feita no segundo ve�culo
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//remove-se o cliente U no primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					//insere-se o cliente U ap�s o cliente V no segundo ve�culo
					v2.ordemDeVisitacao.add(posV + 1, clienteU);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(posV + 1);

					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

	}

	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente U e X ap�s o cliente V, ambos no mesmo ve�culo
	public void insereDuasPosicoesAposUma(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//a carga ocupado do ve�culo que ter� dois clientes a mais � atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//a posi��o do cliente V � encontrada
				int posV = v2.ordemDeVisitacao.indexOf(clienteV);
				
				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��es dos clientes U e X s�o feitas
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//os clientes U e X s�o removidos do primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.remove(x);
					//os cliente U e X s�o inseridos ap�s o cliente V
					v2.ordemDeVisitacao.add(posV + 1, clienteU);
					v2.ordemDeVisitacao.add(posV + 2, clienteX);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);


				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(posV + 1);
					v1.ordemDeVisitacao.add(u, clienteU);
					
					v2.ordemDeVisitacao.remove(posV + 1);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}
		
		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	
	}
	
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro ve�culo, insere-se os cliente X e U ap�s o cliente V, ambos no mesmo ve�culo (inverso da anterior)
	public void insereDuasPosicoesAposUmaInvertido(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if( k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um novo ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k+1);

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				
				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//a carga ocupado do ve�culo que ter� dois clientes a mais � atualizada
				double cargaOcupadaV2 = v2.getCargaOcupada() + clienteU.getDemanda() + clienteX.getDemanda();

				//a posi��o do cliente V � encontrada
				int posV = v2.ordemDeVisitacao.indexOf(clienteV);
				
				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a inser��o dos clientes X e U � feita
				if(cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//remove-se os clientes do primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.remove(x);
					//s�o inseridos X e U no segundo ve�culo
					v2.ordemDeVisitacao.add(posV + 1, clienteX);
					v2.ordemDeVisitacao.add(posV + 2, clienteU);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;
				
				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(posV + 1);
					v2.ordemDeVisitacao.remove(posV + 1);
					
					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se dois clientes, U e V, cada um em um ve�culo, e troca-se a posi��o dos dois
	public void trocaPosicoes(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;
				
				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//as cargas ocupadas dos ve�culos que sofrer�o as trocas s�o atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca do cliente U com o cliente V � feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
					//remove-se a posi��o u no primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					//insere-se nesta posi��o o cliente V
					v1.ordemDeVisitacao.add(u, clienteV);
					
					//remove-se a posi��o v no segundo ve�culo
					v2.ordemDeVisitacao.remove(v);
					//insere-se nesta posi��o o cliente U
					v2.ordemDeVisitacao.add(v, clienteU);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(v);
					v2.ordemDeVisitacao.add(v, clienteV);

					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	
	}
	
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
		
		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//as cargas ocupadas dos ve�culos s�o atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca dos clientes U e X com V � feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {
					
					//remove-se a posi��o u no primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					//o cliente V � inserido nesta posi��o
					v1.ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					v1.ordemDeVisitacao.remove(x);

					//remove-se a posi��o v do segundo ve�culo
					v2.ordemDeVisitacao.remove(v);
					//insere-se o cliente U na posi��o v
					v2.ordemDeVisitacao.add(v, clienteU);
					//insere-se o cliente X ap�s a posi��o v 
					v2.ordemDeVisitacao.add(v + 1, clienteX);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;
				
				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(v);
					v2.ordemDeVisitacao.add(v, clienteV);
					v2.ordemDeVisitacao.remove(v + 1);

					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

	}
	
	//visita-se tr�s clientes, U e X em um ve�culo e V em outro, troca-se as posi��es de X e U com a posi��o de V (inverso da anterior)
	public void trocaDuasPosicoesComUmaPosicaoInvertido(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if(k + 1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);

				//as cargas ocupadas dos ve�culos s�o atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() + clienteV.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() + clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca dos clientes X e U com V � feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

					//remove-se a posi��o u no primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					//o cliente V � inserido nesta posi��o
					v1.ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					v1.ordemDeVisitacao.remove(x);

					//remove-se a posi��o v do segundo ve�culo
					v2.ordemDeVisitacao.remove(v);
					//insere-se o cliente X na posi��o v
					v2.ordemDeVisitacao.add(v, clienteX);
					//insere-se o cliente U ap�s a posi��o v 
					v2.ordemDeVisitacao.add(v + 1, clienteU);
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(v);
					v2.ordemDeVisitacao.add(v, clienteV);
					v2.ordemDeVisitacao.remove(v + 1);

					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se quatro clientes, U, X, V e Y, e ent�o troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, Veiculo v1, int k, int multa, double [][] matrizDeDistancias) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();		

		//percorre o array da ordem de visita��o
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				int y = v + 1;

				//verifica��o se o ve�culo que ser� analisado foi utilizado
				if(k+1 >= rotaClonada.getVeiculosUtilizados())
					continue;

				//um outro ve�culo � selecionado dentro da lista dos ve�culos
				Veiculo v2 = rotaClonada.listaVeiculos.get(k + 1);

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(y >= v2.ordemDeVisitacao.size() || v2.ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v2.ordemDeVisitacao.get(v);
				Cliente clienteY = v2.ordemDeVisitacao.get(y);

				//as cargas ocupadas dos ve�culos s�o atualizadas
				double cargaOcupadaV1 = v1.getCargaOcupada() - clienteU.getDemanda() - clienteX.getDemanda() +
						clienteV.getDemanda() + clienteY.getDemanda();
				double cargaOcupadaV2 = v2.getCargaOcupada() - clienteV.getDemanda() - clienteY.getDemanda() +
						clienteU.getDemanda() + clienteX.getDemanda();

				//verifica��o se a carga m�xima do ve�culo � respeitada
				//se for, a troca dos clientes U e X com V e Y � feita
				if(cargaOcupadaV1 <= v1.getCargaMaxima() && cargaOcupadaV2 <= v2.getCargaMaxima()) {

					//remove-se a posi��o u do primeiro ve�culo
					v1.ordemDeVisitacao.remove(u);
					//insere-se o cliente V nesta posi��o
					v1.ordemDeVisitacao.add(u, clienteV);

					//remove-se a posi��o x do primeiro ve�culo
					v1.ordemDeVisitacao.remove(x);
					//insere-se o cliente Y nesta posi��o
					v1.ordemDeVisitacao.add(x, clienteY);

					//remove-se a posi��o v do segundo ve�culo
					v2.ordemDeVisitacao.remove(v);
					//insere-se o cliente U nesta posi��o
					v2.ordemDeVisitacao.add(v, clienteU);

					//remove-se a posi��o y do segundo ve�culo
					v2.ordemDeVisitacao.remove(y);
					//insere-se o cliente X nesta poosi��o
					v2.ordemDeVisitacao.add(y, clienteX);										
				}
				//se a carga m�xima n�o for respeitada continua-se percorrendo o array de ordem de visita��o
				else
					continue;

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else {
					v2.ordemDeVisitacao.remove(v);
					v2.ordemDeVisitacao.add(v, clienteV);

					v2.ordemDeVisitacao.remove(y);
					v2.ordemDeVisitacao.add(y, clienteY);

					v1.ordemDeVisitacao.remove(u);
					v1.ordemDeVisitacao.add(u, clienteU);

					v1.ordemDeVisitacao.remove(x);											
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
}
