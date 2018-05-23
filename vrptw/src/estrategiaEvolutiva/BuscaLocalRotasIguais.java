package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasIguais {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//1
	//visita-se dois clientes U e V, o cliente U � inserido ap�s o cliente V	
	public void inserirApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == v)
					continue;
				
				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito 
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//� feita a remo��o do cliente U que mudar� de posi��o
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);

				//o cliente U � inserido ap�s o cliente V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

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
					
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//2
	//visita-se tr�s clientes, U, X e V, os clientes U e X s�o inseridos ap�s o cliente V
	public void inserirDoisApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 2;
			for(int v = 3; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == x || u == v || x == v) {
					x++;
					continue;
				}

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito) {
					x++;
					continue;
				}
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito) {
					x++;
					continue;
				}
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito) {
					x++;
					continue;
				}
				
				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				
				//o cliente U � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

				//o cliente X � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteX);

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				x++;
				
				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//3
	//visita-se tr�s clientes, U, X e V, os clientes X e U s�o inseridos ap�s o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 2;
			for(int v = 3; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == x || u == v || x == v) {
					x++;
					continue;
				}

				//verifica��o se as posi��es analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito) {
					x++;
					continue;
				}
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito) {
					x++;
					continue;
				}
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito) {
					x++;
					continue;
				}

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//o cliente X � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteX);
				
				//o cliente U � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				x++;
				
				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);
					
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}
	//4
	//� feito o SWAP (troca de posi��es) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == v)
					continue;

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//� feito o swap(troca de posi��es)
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

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
					
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
					
				}
			}
		}
	}

	//5
	//visita-se tr�s clientes, U, X e V, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {
		
		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 2;
			for(int v = 3; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 2; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == x || u == v || x == v) {
					x++;
					continue;
				}

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito) {
					x++;
					continue;
				}
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito) {
					x++;
					continue;
				}
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito) {
					x++;
					continue;
				}

				//o cliente que ser� adicionado � selecionado
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//o swap das posi��es U e V � feito
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//o cliente X � removido e inserido ap�s a posi��o de V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteX);

				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
				x++;

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}
	
	//6
	//visita-se quatro clientes, U, X, V e Y, e ent�o troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 2;
			for(int v = 3; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				int y = 4;
				
				//verifica��o se as posi��es n�o s�o iguais
				if(u == x || u == v || u == y || x == v || x == y || v == y) {
					x++;
					y++;
					continue;
				}

				//verifica��o se as posi�oes analisadas n�o est�o fora do array de ordem de visita��o e se os clientes analisados n�o s�o o dep�sito							
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito) {
					x++;
					y++;
					continue;
				}
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito) {
					x++;
					y++;
					continue;
				}
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito) {
					x++;
					y++;
					continue;
				}
				if(y >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y) == deposito) {
					x++;
					y++;
					continue;
				}

				//� feito o swap (troca de posi��es) entre os clientes visitados
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, x, y);
				
				//calcula-se a nova fun��o objetivo (custo) para comparar se houve melhora ou n�o
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
				x++;
				y++;

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se houver o GiantTour � atualizado e a busca local continua a partir do pr�ximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					continue;

				}
				//se n�o hover melhora, a troca � desfeita
				else {

					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, x, y);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}	
			}
		}	
	}

	//13
	//s�o visitados dois clientes, X e Y, e � feita a invers�o das posi��es dos clientes que est�o entre estes
	//os clientes X e Y n�o mudam de posi��o, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//um n�mero aleat�rio � selecionado para saber a segunda posi��o
		Random rnd = new Random();
		int d = rnd.nextInt(rotaClonada.listaClientes.size());

		//o array de ordem de visita��o � clonado para que possa ser usado caso a troca deva ser desfeita
		ArrayList<Cliente> old = new ArrayList<>();

		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			old.add(clienteClonado);
		}

		//percorre o array da ordem de visita��o at� a metade
		for(int i = 1; i < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() / 2; i++) {
			//percorre todo o array de ordem de visita��o 
			for(int j = 1; j < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; j++) {

				//o primeiro cliente visitado � o da posi��o i
				Cliente x = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(i);
				//o segundo cliente visitado � o da posi��o i + k (uma var�avel qualquer) mod tamanho do vetor
				Cliente y = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get((i+d)%rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size());

				//se os clientes selecionados forem o dep�sito n�o � feita a troca
				if(x == deposito || y == deposito)
					continue;

				//as posi��es dos clientes que sofrer�o mudan�as nas posi��es s�o encontradas
				int posX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(x) + 1;
				int posY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(y) - 1;

				//verifica-se se as posi��es visitados fazem parte do array de ordem de visita��o
				if(posX >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || posY >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size())
					continue;

				//� criado um arrayList auxiliar para salvar as posi��es invertidas
				ArrayList<Cliente> aux = new ArrayList<>();

				//inverte-se as posi��es
				for(int l = posY; l >= posX; l--) {
					aux.add(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(l));
				}

				//o array de ordem de visita��o pe percorrido
				for(int m = 0; m < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); m++) {
					//verifica-se se a posi��o atual est� dentro das posi��es que foram invertidas
					if(m >= posX && m <= posY) {
						//o cliente da atual posi��o � removido
						rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(m);
						//� inserido em seu lugar o cliente refente � primeira posi��o do array auxiliar que est� invertido
						rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(m, aux.get(0));
						//� removida no array auxiliar a posi��o que foi inserida no array de ordem de viisita��o 
						aux.remove(0);
					}
				}

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

					//limpa-se a ordem de visita��o
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					//adiciona-se os clientes nas antigas posi��es
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(old);

					//e � calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}	
			}
		}	
	}
}
