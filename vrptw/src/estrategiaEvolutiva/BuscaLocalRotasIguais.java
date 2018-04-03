package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Collections;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocalRotasIguais {
	
	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	
	//visita-se dois clientes U e V, o cliente U � inserido ap�s o cliente V	
	public void inserirApos(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
		
		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {	

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito 
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v1.ordemDeVisitacao.get(v);

				//� feita a remo��o do cliente U que mudar� de posi��o
				v1.ordemDeVisitacao.remove(u);

				//a posi��o do cliente V � encontrada
				int posV = v1.ordemDeVisitacao.indexOf(clienteV);

				//o cliente U � inserido ap�s o cliente V
				v1.ordemDeVisitacao.add(posV + 1, clienteU);

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
					v1.ordemDeVisitacao.remove(posV + 1);
					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}
		
		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se tr�s clientes, U, X e V, os clientes U e X s�o inseridos ap�s o cliente V
	public void inserirDoisApos(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
		
		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);
				Cliente clienteV = v1.ordemDeVisitacao.get(v);

				//os clientes que mudar�o de posi�ao s�o removidos
				v1.ordemDeVisitacao.remove(u);
				v1.ordemDeVisitacao.remove(x);

				//� encontrada a posi��o do cliente V
				int posV = v1.ordemDeVisitacao.indexOf(clienteV);

				//os clientes U e X s�o inseridos ap�s o cliente V
				v1.ordemDeVisitacao.add(posV + 1, clienteU);
				v1.ordemDeVisitacao.add(posV + 2, clienteX);

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
					int pos = v1.ordemDeVisitacao.indexOf(clienteU);
					v1.ordemDeVisitacao.remove(pos);
					
					pos = v1.ordemDeVisitacao.indexOf(clienteX);
					v1.ordemDeVisitacao.remove(pos);

					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}
		
		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se tr�s clientes, U, X e V, os clientes X e U s�o inseridos ap�s o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v1.ordemDeVisitacao.get(v);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				//os clientes que mudar�o de posi�ao s�o removidos
				v1.ordemDeVisitacao.remove(u);
				//como o cliente U j� foi removido, a posi��o de X muda 
				v1.ordemDeVisitacao.remove(x - 1);

				//� encontrada a posi��o do cliente V
				int posV = v1.ordemDeVisitacao.indexOf(clienteV);

				// os clientes X e U s�o inseridos ap�s V
				v1.ordemDeVisitacao.add(posV + 1, clienteX);
				v1.ordemDeVisitacao.add(posV + 2, clienteU);

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
					int pos = v1.ordemDeVisitacao.indexOf(clienteU);
					v1.ordemDeVisitacao.remove(pos);
					
					pos = v1.ordemDeVisitacao.indexOf(clienteX);
					v1.ordemDeVisitacao.remove(pos);

					v1.ordemDeVisitacao.add(u, clienteU);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}
		
		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}

	//� feito o SWAP (troca de posi��es) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//� feito o swap(troca de posi��es)
				Collections.swap(v1.ordemDeVisitacao, u, v);

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados());
					continue;
				}
				//se n�o hover melhora, a troca � desfeita
				else
					Collections.swap(v1.ordemDeVisitacao, u, v);
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se tr�s clientes, U, X e V, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//o cliente que ser� adicionado � selecionado
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				Collections.swap(v1.ordemDeVisitacao, u, v);
				v1.ordemDeVisitacao.remove(x);
				v1.ordemDeVisitacao.add(v+1, clienteX);

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
					Collections.swap(v1.ordemDeVisitacao, u, v);
					
					int pos = v1.ordemDeVisitacao.indexOf(clienteX);
					
					v1.ordemDeVisitacao.remove(pos);
					v1.ordemDeVisitacao.add(x, clienteX);
				}
			}
		}

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se quatro clientes, U, X, V e Y, e ent�o troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa) {
	
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				int y = v + 1;

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito							
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(y >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//� feito o swap (troca de posi��es) entre os clientes visitados
				Collections.swap(v1.ordemDeVisitacao, u, v);
				Collections.swap(v1.ordemDeVisitacao, x, y);

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
					Collections.swap(v1.ordemDeVisitacao, u, v);
					Collections.swap(v1.ordemDeVisitacao, x, y);
				}	
			}
		}	

		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	}
	
	//s�o visitados dois clientes, X e Y, e � feita a invers�o das posi��es dos clientes que est�o entre estes
	//os clientes X e Y n�o mudam de posi��o, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, int k) throws CloneNotSupportedException {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
		
		//o array de ordem de visita��o � clonado para que possa ser usado caso a troca deva ser desfeita
		ArrayList<Cliente> old = new ArrayList<>();
		
		for(int c = 0; c < v1.ordemDeVisitacao.size(); c++) {
			Cliente clienteClonado = v1.ordemDeVisitacao.get(c);
			
			clienteClonado = (Cliente) v1.ordemDeVisitacao.get(c).clone();
			
			old.add(clienteClonado);
		}

		//percorre o array da ordem de visita��o at� a metade
		for(int i = 1; i < v1.ordemDeVisitacao.size() / 2; i++) {
			//percorre todo o array de ordem de visita��o
			for(int j = 0; j < v1.ordemDeVisitacao.size() - 1; j++) {

				//o primeiro cliente visitado � o da posi��o i
				Cliente x = v1.ordemDeVisitacao.get(i);
				//o segundo cliente visitado � o da posi��o i + k (uma var�avel qualquer) mod tamanho do vetor
				Cliente y = v1.ordemDeVisitacao.get((i+k)%v1.ordemDeVisitacao.size());

				//as posi��es dos clientes que sofrer�o mudan�as nas posi��es s�o encontradas
				int posX = v1.ordemDeVisitacao.indexOf(x)+1;
				int posY = v1.ordemDeVisitacao.indexOf(y)-1;
				
				//verifica-se se as posi��es visitados fazem parte do array de ordem de visita��o
				if(posX >= v1.ordemDeVisitacao.size() || posY >= v1.ordemDeVisitacao.size())
					continue;

				//� criado um arrayList auxiliar para salvar as posi��es invertidas
				ArrayList<Cliente> aux = new ArrayList<>();
				
				//inverte-se as posi��es
				for(int l = posY; l >= posX; l--) {
					aux.add(v1.ordemDeVisitacao.get(l));
				}
				
				//o array de ordem de visita��o pe percorrido
				for(int m = 0; m < v1.ordemDeVisitacao.size(); m++) {
					//verifica-se se a posi��o atual est� dentro das posi��es que foram invertidas
					if(m >= posX && m <= posY) {
						//o cliente da atual posi��o � removido
						v1.ordemDeVisitacao.remove(m);
						//� inserido em seu lugar o cliente refente � primeira posi��o do array auxiliar que est� invertido
						v1.ordemDeVisitacao.add(m, aux.get(0));
						//� removida no array auxiliar a posi��o que foi inserida no array de ordem de viisita��o 
						aux.remove(0);
					}
				}

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
					//limpa-se a ordem de visita��o
					v1.ordemDeVisitacao.clear();
					//adiciona-se os clientes nas antigas posi��es
					v1.ordemDeVisitacao.addAll(old);
				}	
			}
		}	
		//e � calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
}
