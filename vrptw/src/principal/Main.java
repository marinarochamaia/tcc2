package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {

		double menorCusto = 0; // menor custo encontrado na popula��o inicial
		double menorCustoDescendente = 0; // menor custo encontrado nas novas gera��es
		double menorCustoTotal = 0; // menor custo Final
		int numeroDeRotas = 30; // mu tamanho da popula��o inicial
		int gmax = 100;// n�mero de gera��es
		int descendentes = 150; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de muta��o
		double cBuscaLocal = 0.3; // coeficiente de busca local

		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local
		//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
		ArrayList<Rota> novaPopulacao = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); // lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); // lista de ve�culos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); // array das rotas iniciais (pais)
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()]; // matriz que salva as dist�ncias
		// de todos os clientes para os
		// outros

		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser�
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		// as dist�ncias entre os clientes s�o calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		// cria��o da popula��o inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			r.criaRotas();
			populacao.add(r);
		}

		// busca pelo menor custo da popula��o inicial
		menorCusto = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorCusto > r.getCustoTotalRota()) {
				menorCusto = r.getCustoTotalRota();
			}
		}

		BigDecimal bd = new BigDecimal(menorCusto).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo antes da aplica��o do algoritmo evolutivo: " + bd.doubleValue());

		// n�mero de gera��es que ser�o criadas
		int geracoes = 0;

		// la�o para fazer a muta��o em todas as gera��es criadas
		while (geracoes < gmax) {
			// para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {
				for(int lv = 0; lv < r.getVeiculosUtilizados(); lv++) {
					//gerar (lambda/mu) w filhos
					for(int i = 0; i < (descendentes/numeroDeRotas); i++) {

						Random rnd = new Random();
						double m = rnd.nextDouble();

						if(m <= cMutacao) {
							// a rota � clonada
							Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
							rotaClonada = (Rota) r.getClone(rotaClonada);

							// s�o selecionados n�meros aleat�rios que ser�o utilizados para pegar os ve�culos
							int j = rnd.nextInt(rotaClonada.getVeiculosUtilizados() - 1);

							// os ve�culos s�o selecionados
							Veiculo v1 = rotaClonada.listaVeiculos.get(j);

							// uma posi��o de cada ve�culo � selecionada
							// esta deve ser diferente do dep�sito, enquanto n�o for, outra posi��o �
							// selecionada
							int pv1;

							do {
								pv1 = rnd.nextInt(v1.ordemDeVisitacao.size());
							} while (v1.ordemDeVisitacao.get(pv1).getNumero() == 0 || v1.ordemDeVisitacao.size() == 0);

							// uma segunda posi��o do ve�culo � selecionada
							// esta n�o deve ser o dep�sito e nem igual a primeira posi��o
							int pv2;
							do {
								pv2 = rnd.nextInt(v1.ordemDeVisitacao.size());
							} while (v1.ordemDeVisitacao.get(pv2).getNumero() == 0 || pv2 == pv1);

							// muta��o
							Collections.swap(v1.ordemDeVisitacao, pv1, pv2);
							Collections.swap(v1.ordemDeVisitacao, (pv1 / 2), (pv2 / 3));
							Collections.swap(v1.ordemDeVisitacao, (pv1 / 4), (pv2 / 5));

							// calcula os custos da nova rota (fun��o de avalia��o ou aptid�o)
							calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

							double b = rnd.nextDouble();
							//a busca local s� � feita se o fator pl for atendido
							if (b <= cBuscaLocal){
								int count = 0;
								while(count <= (clientes.size()/2)) {	
									count++;
									

									//1) remover u e inserir ap�s v;
									//2) remover u e x e inserir u e x ap�s v;
									//3) remover u e x e inserir x e u ap�s v; (posi��es invertidas)
									//4) trocar u e v; // SWAP
									//5) troca u e x com v;
									//6) troca u e x com v e y;
									//7) 
									ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
									Collections.shuffle(operacoes);	

									for(Integer k : operacoes) {
										switch (k) {
										case 1: {
											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();
											//n�o parte de zero e termina uma posi��o antes por causa do dep�sito
											for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
												for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.add(v, clienteU);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else {
														v1.ordemDeVisitacao.remove(v);
														v1.ordemDeVisitacao.add(u, clienteU);
													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 2: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);
													Cliente clienteX = v1.ordemDeVisitacao.get(x);
													Cliente clienteV = v1.ordemDeVisitacao.get(v);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.remove(x - 1);

													int posV = v1.ordemDeVisitacao.indexOf(clienteV);

													v1.ordemDeVisitacao.add(posV + 1, clienteU);
													v1.ordemDeVisitacao.add(posV + 2, clienteX);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
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

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}
										case 3: {
 
											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteU = v1.ordemDeVisitacao.get(u);
													Cliente clienteX = v1.ordemDeVisitacao.get(x);
													Cliente clienteV = v1.ordemDeVisitacao.get(v);

													v1.ordemDeVisitacao.remove(u);
													v1.ordemDeVisitacao.remove(x - 1);

													int posV = v1.ordemDeVisitacao.indexOf(clienteV);

													v1.ordemDeVisitacao.add(posV + 1, clienteX);
													v1.ordemDeVisitacao.add(posV + 2, clienteU);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else {
														int pos = v1.ordemDeVisitacao.indexOf(clienteU);
														v1.ordemDeVisitacao.remove(pos);
														pos = v1.ordemDeVisitacao.indexOf(clienteX);
														v1.ordemDeVisitacao.remove(pos);

														v1.ordemDeVisitacao.add(u, clienteU);
														v1.ordemDeVisitacao.add(u + 1, clienteX);

													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 4: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 3; u++) {
												for(int v = u + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Collections.swap(rotaClonada.listaVeiculos.get(j).ordemDeVisitacao, u, v);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else 
														Collections.swap(v1.ordemDeVisitacao, v, u);
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 5: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													Cliente clienteX = v1.ordemDeVisitacao.get(x);

													Collections.swap(v1.ordemDeVisitacao, u, v);
													v1.ordemDeVisitacao.remove(x);
													v1.ordemDeVisitacao.add(v, clienteX);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else { 
														Collections.swap(v1.ordemDeVisitacao, u, v);
														int pos = v1.ordemDeVisitacao.indexOf(clienteX);
														v1.ordemDeVisitacao.remove(pos);
														v1.ordemDeVisitacao.add(x, clienteX);
													}
												}
											}

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}

										case 6: {

											double custoAntesBuscaLocal = rotaClonada.getCustoTotalRota();

											for(int u = 1; u < v1.ordemDeVisitacao.size() - 4; u++) {
												int x = u + 1;
												for(int v = x + 1; v < v1.ordemDeVisitacao.size(); v++) {

													int y = v+1;

													if(x == v1.ordemDeVisitacao.size() || y == v1.ordemDeVisitacao.size() )
														break;

													Collections.swap(v1.ordemDeVisitacao, u, v);
													Collections.swap(v1.ordemDeVisitacao, x, y);

													calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

													if(rotaClonada.getCustoTotalRota() < custoAntesBuscaLocal)
														break;
													else { 
														Collections.swap(v1.ordemDeVisitacao, u, v);
														Collections.swap(v1.ordemDeVisitacao, x, y);
													}	
												}
											}	

											calculaCustoFuncaoObjetivo(v1, matrizDeDistancias, multa, clientes, veiculos, r, rotaClonada);

											break;
										}
										
										}//fecha switch
									}//fecha for
								}	//fecha while
							}//fim da buscalocal

							// as novas rotas s�o adicionadas em um array auxiliar
							aux.add(rotaClonada);
						}
					}
				}
			} // fecha for

			// � feito um merge da nova popula��o e da popula��o inicial
			novaPopulacao.addAll(populacao);
			novaPopulacao.addAll(aux);

			// as rotas s�o ordenadas por valor de custos
			Collections.sort(novaPopulacao);

			// � feito um corte para mu indiv�duos
			for (int j = (novaPopulacao.size() - 1); j >= numeroDeRotas; j--) {
				novaPopulacao.remove(j);
			}

			// busca pelo menor custo da nova populacao
			menorCustoDescendente = Double.MAX_VALUE;
			for (Rota r : novaPopulacao) {
				if (menorCustoDescendente > r.getCustoTotalRota()) {
					menorCustoDescendente = r.getCustoTotalRota();
				}
			}

			geracoes++;
			//System.out.println(geracoes + " " + menorCustoDescendente);
		} // fecha while

		BigDecimal bd3 = new BigDecimal(menorCustoDescendente).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo dos descendentes: " + bd3.doubleValue());

		// menor custo final � encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		BigDecimal bd2 = new BigDecimal(menorCustoTotal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor custo encontrado: " + bd2.doubleValue());

	}// fecha a main

	public static void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, ArrayList <Cliente> clientes,
																				ArrayList <Veiculo> veiculos, Rota r, Rota rotaClonada) {
		rotaClonada.resetCustoTotalRota();
		v1.resetCustoVeiculo();
		v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for(int l = 0; l < r.getVeiculosUtilizados(); l++) {
			Veiculo v2 = rotaClonada.listaVeiculos.get(l);
			if(v2.ordemDeVisitacao != v1.ordemDeVisitacao)
				rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());
		}
	}
	
}// fecha a classe