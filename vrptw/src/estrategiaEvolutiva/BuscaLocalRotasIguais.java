package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocalRotasIguais {
	
	FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	
	//visita-se dois clientes U e V, o cliente U é inserido após o cliente V	
	public void inserirApos(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {	

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito 
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				
				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);

				//é feita a remoção do cliente U que mudará de posição
				v1.ordemDeVisitacao.remove(clienteU);

				//o cliente U é inserido após o cliente V
				v1.ordemDeVisitacao.add(v, clienteU);
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					v1.ordemDeVisitacao.remove(clienteU);
					v1.ordemDeVisitacao.add(u, clienteU);
				}
			}
		}
		
		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se três clientes, U, X e V, os clientes U e X são inseridos após o cliente V
	public void inserirDoisApos(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//guarda-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 1; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				
				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() - 1 || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				
				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				//os clientes que mudarão de posiçao são removidos
				v1.ordemDeVisitacao.remove(clienteU);
				v1.ordemDeVisitacao.add(v, clienteU);
				
				v1.ordemDeVisitacao.remove(clienteX);
				v1.ordemDeVisitacao.add(v, clienteX);

				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
				
				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
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
		
		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se três clientes, U, X e V, os clientes X e U são inseridos após o cliente V (inverso da anterior)
	public void inserirDoisAposInvertido(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//os clientes que serão visitados são selecionados
				Cliente clienteU = v1.ordemDeVisitacao.get(u);
				Cliente clienteV = v1.ordemDeVisitacao.get(v);
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				//os clientes que mudarão de posiçao são removidos
				v1.ordemDeVisitacao.remove(u);
				//como o cliente U já foi removido, a posição de X muda 
				v1.ordemDeVisitacao.remove(x - 1);

				//é encontrada a posição do cliente V
				int posV = v1.ordemDeVisitacao.indexOf(clienteV);

				// os clientes X e U são inseridos após V
				v1.ordemDeVisitacao.add(posV + 1, clienteX);
				v1.ordemDeVisitacao.add(posV + 2, clienteU);
				
				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
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
		
		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}

	//é feito o SWAP (troca de posições) entre os dois clientes visitados, U e V
	public void swap(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 1; u++) {
			for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//é feito o swap(troca de posições)
				Collections.swap(v1.ordemDeVisitacao, u, v);

				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else
					Collections.swap(v1.ordemDeVisitacao, u, v);
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se três clientes, U, X e V, troca-se as posições de U e X com a posição de V
	public void trocaDuasPosicoesComUmaPosicao(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size() - 1; v++) {

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito
				if(u >= v1.ordemDeVisitacao.size() - 1 || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() - 1 || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() - 1 || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;

				//o cliente que será adicionado é selecionado
				Cliente clienteX = v1.ordemDeVisitacao.get(x);

				Collections.swap(v1.ordemDeVisitacao, u, v);
				v1.ordemDeVisitacao.remove(clienteX);
				v1.ordemDeVisitacao.add(v, clienteX);
				


				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {

					Collections.swap(v1.ordemDeVisitacao, u, v);
					
					v1.ordemDeVisitacao.remove(clienteX);
					v1.ordemDeVisitacao.add(x, clienteX);

				}
			}
		}

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
	//visita-se quatro clientes, U, X, V e Y, e então troca-se as posições de U e X com as posições de V e Y
	public void trocaDuasPosicoesComDuasPosicoes(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

		//percorre o array da ordem de visitação
		for(int u = 0; u < v1.ordemDeVisitacao.size() - 2; u++) {
			int x = u + 1;
			for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {
				int y = v + 1;

				//verificação se as posiçoes analisadas não estão fora do array de ordem de visitação e se os clientes analisados não são o depósito							
				if(u >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(u).getNumero() == 0)
					continue;
				if(x >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(x).getNumero() == 0)
					continue;
				if(v >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(v).getNumero() == 0)
					continue;
				if(y >= v1.ordemDeVisitacao.size() || v1.ordemDeVisitacao.get(y).getNumero() == 0)
					continue;

				//é feito o swap (troca de posições) entre os clientes visitados
				Collections.swap(v1.ordemDeVisitacao, u, v);
				Collections.swap(v1.ordemDeVisitacao, x, y);

				//calcula-se a nova função objetivo (custo) para comparar se houve melhora ou não
				fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);

				//compara-se o novo custo com o anterior para saber se houve melhora ou não
				//se houver o GiantTour é atualizado e a busca local continua a partir do próximo cliente
				if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal) {
					fbl.atualizaGiantTour(rotaClonada.listaClientes, rotaClonada.listaVeiculos, rotaClonada.getVeiculosUtilizados(), deposito);
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					Collections.swap(v1.ordemDeVisitacao, u, v);
					Collections.swap(v1.ordemDeVisitacao, x, y);
				}	
			}
		}	

		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	}
	
	//são visitados dois clientes, X e Y, e é feita a inversão das posições dos clientes que estão entre estes
	//os clientes X e Y não mudam de posição, apenas os clientes entre eles
	public void doisopt(Rota rotaClonada, Veiculo v1, double [][] matrizDeDistancias, int multa, Cliente deposito) throws CloneNotSupportedException {

		//calcula-se o custo antes de ser realizada a busca local para comparar se houve ou não melhora
		double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
		
		Random rnd = new Random();
		int k = rnd.nextInt(v1.ordemDeVisitacao.size());
		
		//o array de ordem de visitação é clonado para que possa ser usado caso a troca deva ser desfeita
		ArrayList<Cliente> old = new ArrayList<>();
		
		for(int c = 0; c < v1.ordemDeVisitacao.size(); c++) {
			Cliente clienteClonado = v1.ordemDeVisitacao.get(c);
			
			clienteClonado = (Cliente) v1.ordemDeVisitacao.get(c).clone();
			
			old.add(clienteClonado);
		}

		//percorre o array da ordem de visitação até a metade
		for(int i = 0; i < v1.ordemDeVisitacao.size() / 2; i++) {
			//percorre todo o array de ordem de visitação
			for(int j = 0; j < v1.ordemDeVisitacao.size() - 1; j++) {

				//o primeiro cliente visitado é o da posição i
				Cliente x = v1.ordemDeVisitacao.get(i);
				//o segundo cliente visitado é o da posição i + k (uma varíavel qualquer) mod tamanho do vetor
				Cliente y = v1.ordemDeVisitacao.get((i+k)%v1.ordemDeVisitacao.size());

				//as posições dos clientes que sofrerão mudanças nas posições são encontradas
				int posX = v1.ordemDeVisitacao.indexOf(x)+1;
				int posY = v1.ordemDeVisitacao.indexOf(y)-1;
				
				//verifica-se se as posições visitados fazem parte do array de ordem de visitação
				if(posX >= v1.ordemDeVisitacao.size() || posY >= v1.ordemDeVisitacao.size())
					continue;

				//é criado um arrayList auxiliar para salvar as posições invertidas
				ArrayList<Cliente> aux = new ArrayList<>();
				
				//inverte-se as posições
				for(int l = posY; l >= posX; l--) {
					aux.add(v1.ordemDeVisitacao.get(l));
				}
				
				//o array de ordem de visitação pe percorrido
				for(int m = 0; m < v1.ordemDeVisitacao.size(); m++) {
					//verifica-se se a posição atual está dentro das posições que foram invertidas
					if(m >= posX && m <= posY) {
						//o cliente da atual posição é removido
						v1.ordemDeVisitacao.remove(m);
						//é inserido em seu lugar o cliente refente à primeira posição do array auxiliar que está invertido
						v1.ordemDeVisitacao.add(m, aux.get(0));
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
					continue;
				}
				//se não hover melhora, a troca é desfeita
				else {
					//limpa-se a ordem de visitação
					v1.ordemDeVisitacao.clear();
					//adiciona-se os clientes nas antigas posições
					v1.ordemDeVisitacao.addAll(old);

				}	
			}
		}	
		//e é calculado novamente o custo (pois foi defeita a troca)
		fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
		
	}
	
}
