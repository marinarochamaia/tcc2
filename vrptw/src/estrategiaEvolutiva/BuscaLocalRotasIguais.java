package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasIguais {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//1
	//visita-se dois clientes U e V, o cliente U é inserido após o cliente V	
	public void inserirApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();		

		//percorre-se o array da ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == v)
					continue;

				//um cliente que será visitado é selecionado
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
				//System.out.println("Cliente U: " + clienteU +  "\nCliente V: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v));

				//é feita a remoção do cliente U que mudará de posição
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);

				//o cliente U é inserido após o cliente V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois a troca ser desfeita
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca para depois salvar a melhor ordem de visitação
				else {

					//a troca é desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//System.out.println("DEPOIS TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					//atualiza-se o custo
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado 
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;
					
				}

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

	}

	//2
	//visita-se três clientes, U, X e V, os clientes U e X são inseridos após o cliente V
	public void inserirDoisApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//array para salvar a ordem de visitação antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();
		
		//o array de ordem de visitação é clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
		
		}

		//percorre-se o array da ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == v || x == v)
					continue;

				//verificação se a posição visitada não é maior que o array ou se não é o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//System.out.println("Cliente U: " + clienteU + "\nCliente X: " + clienteX + "\nCliente V: " + clienteV);

				//a posição do cliente V é encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o cliente U é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteU);

				//o cliente X é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca para depois salvar a melhor ordem de visitação
				else {

					//a troca é desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					//atualiza-se o custo
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;

				}

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

	}

	//3
	//visita-se três clientes, U, X e V, os clientes X e U são inseridos após o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//array para salvar a ordem de visitação antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();
		
		//o array de ordem de visitação é clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
			
		}

		//percorre-se o array da ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == v || x == v)
					continue;

				//verificação se a posição visitada não é maior que o array ou se não é o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//System.out.println("Cliente U: " + clienteU + "\nCliente X: " + clienteX + "\nCliente V: " + clienteV);

				//a posição do cliente V é encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o cliente X é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//o cliente U é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteU);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca para depois salvar a melhor ordem de visitação
				else {

					//a troca é desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					//atualiza-se o custo
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;

				}

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

	}

	//4
	//é feito o SWAP (troca de posições) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	

		//percorre-se o array da ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == v)
					continue;

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//System.out.println("Cliente U: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) +  "\nCliente V: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v));

				//é feito o swap(troca de posições)
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor salva a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca para depois salvar a melhor ordem de visitação
				else {

					//a troca é desfeita
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					//atualiza-se o custo
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;

				}

				//a troca é desfeita
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);


			}
		}

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

	}

	//5
	//visita-se três clientes, U, X e V, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();
		
		//array para salvar a ordem de visitação antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();
		
		//o array de ordem de visitação é clonado para que possa ser usado quando a troca for desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
			
		}

		//percorre-se o array da ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			if(u == x) {
				x++;
			}
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == v || x == v)
					continue;

				//verificação se a posição visitada não é maior que o array ou se não é o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;

				//clientes que serão visitados são selecionados
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//System.out.println("Cliente U: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) + "\nCliente X: " + clienteX + "\nCliente V: " + clienteV);

				//a posição do cliente V é encontrada
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);

				//o swap das posições U e V é feito
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//o cliente X é removido e inserido após a posição de V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(posV, clienteX);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois a troca ser desfeita=
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					// o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca para depois salvar a melhor ordem de visitação
				else {

					//a troca é desfeita
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

					//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					//o custo é atualizado
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;

				}

				//a troca é desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);

	}
	
	//6
	//visita-se quatro clientes, U, X, V e Y, e, então, troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();	
		
		//percorre-se o array de ordem de visitação
		for(int u = 1; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				int x = 1;
				int y = 1;

				//verificação se as posições não são iguais
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

				//verificação se as posições visitadas não são maiores que o array ou se não são o depósito
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);
				Cliente clienteV = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v);
				Cliente clienteY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y);

				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//System.out.println("Cliente U: " + clienteU + "\nCliente X: " + clienteX + "\nCliente V: " + clienteV + "\nCliente Y: " + clienteY);

				//as posições dos clientes são encontradas
				int posU =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteU);
				int posX =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteX);
				int posV =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteV);
				int posY =  rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(clienteY);

				//é feito o swap (troca de posições) entre os clientes visitados
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, x, y);

				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois desfazer as trocas
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					// o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, troca-se para depois salvar a melhor ordem de visitação
				else {

					//as trocas são desfeitas
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posU, posV);
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posX, posY);

					//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
					
					//o custo é atualizado
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

					continue;

				}

				//a troca é desfeita
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posU, posV);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, posX, posY);

				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

			}
		}	

		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);
		
	}

	//7
	//são visitados dois clientes, X e Y, e é feita a inversão das posições dos clientes que estão entre estes
	//os clientes X e Y não mudam de posição, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//um número aleatório é selecionado para ajudar a calcular a posição de Y
		Random rnd = new Random();
		int d = rnd.nextInt(rotaClonada.listaClientes.size() * 2);
		
		//array para salvar a ordem de visitação antes das trocas
		ArrayList<Cliente> antigaOrdemDeVisitacao = new ArrayList<>();

		//o array de ordem de visitação é clonado para que possa ser usado caso a troca deva ser desfeita
		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			antigaOrdemDeVisitacao.add(clienteClonado);
			
		}
		
		//array para armazenar a melhor ordem de visitação gerada
		ArrayList<Cliente> melhorOrdemDeVisitacao = new ArrayList<>();

		melhorOrdemDeVisitacao.addAll(antigaOrdemDeVisitacao);

		//inicialmente o array da ordem de visitação é percorrido até a metade
		for(int i = 1; i < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size()/2; i++) {
			//depois, percorre-se todo o array da ordem de visitação 
			for(int j = 1; j < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; j++) {

				//o primeiro cliente visitado é o da posição i
				Cliente x = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(i);
				//o segundo cliente visitado é o da posição j + d (uma varíavel aleatória) e encontra-se o módulo com tamanho do vetor
				Cliente y = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get((j + d) % rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size());
				
				//verificação se as posições não são iguais ou não são o depósito
				if(x == y || x.getNumero() == 0|| y.getNumero() == 0)
					continue;

				//as posições dos clientes que sofrerão mudanças nas posições são encontradas
				int posX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(x) + 1;
				int posY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(y) - 1;
				
				if(posX == posY || posX > posY)
					continue;

				//verifica-se se as posições visitados fazem parte do array de ordem de visitação
				if(posX >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || posY >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1)
					continue;
				
				//System.out.println("Antes: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
				
				//System.out.println("Pos X: " + posX + "\nPosição Y: " + posY);

				//é criado um arrayList auxiliar para salvar as posições invertidas
				ArrayList<Cliente> aux = new ArrayList<>();

				//inverte-se as posições
				aux.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.subList(posX, posY));
				Collections.reverse(aux);

				//o array auxiliar com as posições invertidas é adicionado novamente à ordem de visitação
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.removeAll(aux);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(posX, aux);
				
				//System.out.println("Depois: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
								
				//atualiza-se o custo da função objetivo para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se for melhor, salva-se a melhor ordem para depois desfazer a troca
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {

					//o giant tour é atualizado
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);

					//o custo de antes da busca local é atualizado
					custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

					//o array da melhor ordem de visitação é atualizado
					melhorOrdemDeVisitacao.clear();
					melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				} //senão, continua-se a percorr o array
				else					
					continue;
				
				//a troca é desfeita
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(antigaOrdemDeVisitacao);
				
				//System.out.println("Depois TROCA: " + rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);

				//o custo é atualizado
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
				//o array da melhor ordem de visitação é atualizado
				melhorOrdemDeVisitacao.clear();
				melhorOrdemDeVisitacao.addAll(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao);
				
			}			
		}	
		
		//a ordem de visitação é atualizada com a melhor configuração encontrada
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
		rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(melhorOrdemDeVisitacao);
	
	}
}