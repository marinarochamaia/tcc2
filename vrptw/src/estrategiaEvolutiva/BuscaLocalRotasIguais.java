package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocalRotasIguais {

	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();

	//visita-se dois clientes U e V, o cliente U é inserido após o cliente V	
	public void inserirApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v =  1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verificação se as posições não são iguais
				if(u == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito 
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se a posição onde será inserido o cliente não é a posição do depósito 
				if(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v + 1) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);

				//é feita a remoção do cliente U que mudará de posição
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);

				//o cliente U é inserido após o cliente V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v + 1, clienteU);


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
					
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se três clientes, U, X e V, os clientes U e X são inseridos após o cliente V
	public void inserirDoisApos(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;

				//verificação se as posições analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//o cliente U é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

				//o cliente X é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteX);

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

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se três clientes, U, X e V, os clientes X e U são inseridos após o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;
				
				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u);
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);					

				//o cliente X é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteX);

				//o cliente U é removido de sua posição e inserido após a posição de v
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v, clienteU);

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

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(u, clienteU);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//é feito o SWAP (troca de posições) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			for(int v = 1; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				
				//verificação se as posições não são iguais
				if(u == v)
					continue;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//é feito o swap(troca de posições)
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

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
					
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
					
				}
			}
		}
	}

	//visita-se três clientes, U, X e V, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posições não são iguais
				if(u == x || u == v || x == v)
					continue;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;

				//verificação se a posição onde será inserido o cliente não é a posição do depósito
				if(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v + 1) == deposito)
					continue;

				//o cliente que será adicionado é selecionado
				Cliente clienteX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x);

				//o swap das posições U e V é feito
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

				//o cliente X é removido e inserido após a posição de V
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
				rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(v + 1, clienteX);

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

					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);

					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(clienteX);
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(x, clienteX);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}
			}
		}
	}

	//visita-se quatro clientes, U, X, V e Y, e então troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; u++) {
			int x = 1;
			for(int v = 2; v < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; v++) {
				int y = 3;
				
				//verificação se as posições não são iguais
				if(u == x || u == v || u == y || x == v || x == y || v == y)
					continue;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito							
				if(u >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(u) == deposito)
					continue;
				if(x >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(x) == deposito)
					continue;
				if(v >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(v) == deposito)
					continue;
				if(y >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1 || rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(y) == deposito)
					continue;

				//é feito o swap (troca de posições) entre os clientes visitados
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);
				Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, x, y);

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

					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, u, v);
					Collections.swap(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao, x, y);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}	
			}
		}	
	}

	//são visitados dois clientes, X e Y, e é feita a inversão das posições dos clientes que estão entre estes
	//os clientes X e Y não mudam de posição, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, int k, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//um número aleatório é selecionado para saber a segunda posição
		Random rnd = new Random();
		int d = rnd.nextInt(rotaClonada.listaClientes.size());

		//o array de ordem de visitação é clonado para que possa ser usado caso a troca deva ser desfeita
		ArrayList<Cliente> old = new ArrayList<>();

		for(int c = 0; c < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); c++) {

			Cliente clienteClonado = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c);

			clienteClonado = (Cliente) rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(c).clone();

			old.add(clienteClonado);
		}

		//percorre o array da ordem de visitação até a metade
		for(int i = 0; i < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() / 2; i++) {
			//percorre todo o array de ordem de visitação 
			for(int j = 0; j < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() - 1; j++) {

				//o primeiro cliente visitado é o da posição i
				Cliente x = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(i);
				//o segundo cliente visitado é o da posição i + k (uma varíavel qualquer) mod tamanho do vetor
				Cliente y = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get((i+d)%rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size());

				//se os clientes selecionados forem o depósito não é feita a troca
				if(x == deposito || y == deposito)
					continue;

				//as posições dos clientes que sofrerão mudanças nas posições são encontradas
				int posX = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(x) + 1;
				int posY = rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.indexOf(y) - 1;

				//verifica-se se as posições visitados fazem parte do array de ordem de visitação
				if(posX >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size() || posY >= rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size())
					continue;

				//é criado um arrayList auxiliar para salvar as posições invertidas
				ArrayList<Cliente> aux = new ArrayList<>();

				//inverte-se as posições
				for(int l = posY; l >= posX; l--) {
					aux.add(rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.get(l));
				}

				//o array de ordem de visitação pe percorrido
				for(int m = 0; m < rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.size(); m++) {
					//verifica-se se a posição atual está dentro das posições que foram invertidas
					if(m >= posX && m <= posY) {
						//o cliente da atual posição é removido
						rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.remove(m);
						//é inserido em seu lugar o cliente refente à primeira posição do array auxiliar que está invertido
						rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.add(m, aux.get(0));
						//é removida no array auxiliar a posição que foi inserida no array de ordem de viisitação 
						aux.remove(0);
					}
				}

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

					//limpa-se a ordem de visitação
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.clear();
					//adiciona-se os clientes nas antigas posições
					rotaClonada.listaVeiculos.get(k).ordemDeVisitacao.addAll(old);

					//e é calculado novamente o custo (pois foi defeita a troca)
					fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				}	
			}
		}	
	}
}
