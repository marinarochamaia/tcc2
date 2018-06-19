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

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();		

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

		//percorre-se o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v)
					continue;

				//um cliente que ser� visitado � selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//� feita a remo��o do cliente U que mudar� de posi��o
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);

				//o cliente U � inserido ap�s o cliente V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o  novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois a troca ser desfeita
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o custo de antes da busca local � atualizado
					custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visita��o � atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//a troca � desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}

	//2
	//visita-se tr�s clientes, U, X e V, os clientes U e X s�o inseridos ap�s o cliente V
	public void inserirDoisApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//array para salvar a ordem de visita��o antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();

		//o array de ordem de visita��o � clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
		}

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(antigaOrdemDeVisitacao);

		//percorre-se o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x)
				x++;
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v || x == v)
					continue;

				//verifica��o se a posi��o visitada n�o � maior que o array ou se n�o � o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//a posi��o do cliente V � encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o cliente U � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteU);

				//o cliente X � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//a troca � desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}

	//3
	//visita-se tr�s clientes, U, X e V, os clientes X e U s�o inseridos ap�s o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//array para salvar a ordem de visita��o antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();

		//o array de ordem de visita��o � clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
		}

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(antigaOrdemDeVisitacao);

		//percorre-se o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x)
				x++;
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v || x == v)
					continue;

				//verifica��o se a posi��o visitada n�o � maior que o array ou se n�o � o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//a posi��o do cliente V � encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o cliente X � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//o cliente U � removido de sua posi��o e inserido ap�s a posi��o de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteU);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o  novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//a troca � desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);	
	}

	//4
	//� feito o SWAP (troca de posi��es) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

		//percorre-se o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v)
					continue;			

				//� feito o swap(troca de posi��es)
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//a troca � desfeita
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}

	//5
	//visita-se tr�s clientes, U, X e V, troca-se as posi��es de U e X com a posi��o de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();

		//array para salvar a ordem de visita��o antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();

		//o array de ordem de visita��o � clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
		}

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(antigaOrdemDeVisitacao);

		//percorre-se o array da ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x)
				x++;
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v || x == v)
					continue;

				//verifica��o se a posi��o visitada n�o � maior que o array ou se n�o � o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;

				//clientes que ser�o visitados s�o selecionados
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//a posi��o do cliente V � encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o swap das posi��es U e V � feito
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//o cliente X � removido e inserido ap�s a posi��o de V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//a troca � desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}

	//6
	//visita-se quatro clientes, U, X, V e Y, e, ent�o, troca-se as posi��es de U e X com as posi��es de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

		//percorre-se o array de ordem de visita��o
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				int x = 1;
				int y = 1;	

				//verifica��o se as posi��es n�o s�o iguais
				if(u == v)
					continue;
				if(x == u)
					x++;
				if(x == v)
					x++;
				if(x == u)
					x++;
				if(y == u)
					y++;
				if(y == v)
					y++;
				if(y == x)
					y++;
				if(u == y || v == y)
					y++;
				if(y == x)
					y++;

				//verifica��o se as posi��es visitadas n�o s�o maiores que o array ou se n�o s�o o dep�sito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//os clientes que ser�o visitados s�o selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y);

				//as posi��es dos clientes s�o encontradas
				int posU =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteU);
				int posX =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteX);
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);
				int posY =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteY);

				//� feito o swap (troca de posi��es) entre os clientes visitados
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posU, posV);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posX, posY);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o  novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {

					//as trocas s�o desfeitas
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posX, posY);
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posU, posV);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posX, posY);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posU, posV);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
			}
		}	

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}

	//7
	//s�o visitados dois clientes, X e Y, e � feita a invers�o das posi��es dos clientes que est�o entre estes
	//os clientes X e Y n�o mudam de posi��o, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou n�o melhora
		double custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

		//um n�mero aleat�rio � selecionado para ajudar a calcular a posi��o de Y
		Random rnd = new Random();
		int d = rnd.nextInt(rotaClonada.listaClientes.size() * 2);

		//array para salvar a ordem de visita��o antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();

		//o array de ordem de visita��o � clonado para que possa ser usado caso a troca deva ser desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
		}

		//array para armazenar a melhor ordem de visita��o gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();

		//a melhor ordem de visita��o recebe a configura��o atual
		melhorOrdemDeVisitacao.addAll(antigaOrdemDeVisitacao);

		//o array da ordem de visita��o � percorrido at� a metade
		for(int i = 0; i < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size()/2; i++) {
			//percorre-se todo o array da ordem de visita��o 
			for(int j = 0; j < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); j++) {

				//o primeiro cliente visitado � o da posi��o i
				Cliente x = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(i);
				//o segundo cliente visitado � o da posi��o j + d (uma var�avel aleat�ria) e encontra-se o m�dulo com tamanho do vetor
				Cliente y = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get((j + d) % rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size());

				//verifica��o se as posi��es n�o s�o iguais
				if(x == y)
					continue;

				//as posi��es dos clientes que sofrer�o mudan�as nas posi��es s�o encontradas
				int posX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(x);
				int posY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(y);

				//verifica��o se as posi��es n�o s�o iguais ou se a posi��o X n�o � maior que a poci��o Y
				if(posX == posY || posX > posY)
					continue;

				//verifica-se se as posi��es visitados fazem parte do array de ordem de visita��o
				if(posX > rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || posY > rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1)
					continue;

				//� criado um arrayList auxiliar para salvar as posi��es invertidas
				ArrayList<Cliente> aux = new ArrayList<>();

				//inverte-se as posi��es
				aux.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.subList(posX + 1, posY));
				Collections.reverse(aux);

				//o array auxiliar com as posi��es invertidas � adicionado novamente � ordem de visita��o
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.removeAll(aux);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(posX + 1, aux);

				//atualiza-se o custo da fun��o objetivo para comparar se houve melhora ou n�o
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou n�o
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {;

				//o custo de antes da busca local � atualizado
				custoAntesBuscaLocal= rotaClonada.getCustoTotalRota();

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				} //sen�o, desfaz a troca e atualiza-se o custo e o tempo
				else {				

					//a troca � desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//o tempo e o custo s�o atualizados
					fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					continue;
				}

				//a troca � desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//o tempo e o custo s�o atualizados
				fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//o array da melhor ordem de visita��o � atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
			}			
		}	

		//a ordem de visita��o � atualizada com a melhor configura��o encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

		//o tempo e o custo s�o atualizados
		fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

		//o giant tour � atualizado
		fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
	}
}