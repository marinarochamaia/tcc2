package principal;

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
		int numeroDeRotas = 100; // mu tamanho da popula��o inicial
		int gmax = 1000;// n�mero de gera��es
		int descendentes = 1000; // lamba, numero de descendentes
		int multa = 1000;// multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.8; // coeficiente de muta��o
		double cBuscaLocal = 0.3; // coeficiente de busca local

		ArrayList<Rota> aux = new ArrayList<>(); // array auxiliar para guardar todas os ind�viduos criados atrav�s da
													// muta��o (antes de aplicar o fator PL)
		ArrayList<Rota> novaPopulacao = new ArrayList<>();// salva a popula��o com o fator PL j� aplicado
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
		
		System.out.println("Menor custo antes da aplica��o do algoritmo evolutivo: " + menorCusto);

		// n�mero de gera��es que ser�o criadas
		int geracoes = 0;

		// la�o para fazer a muta��o em todas as gera��es criadas
		while (geracoes < gmax) {
			// para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {
				//gerar (lambda/mu) w filhos
				for(int i = 0; i < (descendentes/numeroDeRotas); i++) {
					
					Random rnd = new Random();
					double m = rnd.nextDouble();
					
					if(m <= cMutacao) {
							// a rota � clonada
							Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(),matrizDeDistancias);
							rotaClonada = (Rota) r.getClone(rotaClonada);
							rotaClonada.custoTotalRota = 0;
	
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
							v1.resetCustoVeiculo();
							v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
							rotaClonada.custoTotalRota += v1.getCustoVeiculo();
							
							for(int v = 0; v < r.getVeiculosUtilizados(); v++) {
								if(v != j)
									rotaClonada.custoTotalRota += v1.getCustoVeiculo();
							}
							
							double custoInicial = rotaClonada.custoTotalRota;
							
							double b = rnd.nextDouble();
							//a busca local s� � feita se o fator pl for atendido
							if (b <= cBuscaLocal){
								int count = 0;
								while(count <= (clientes.size()/2)) {	

									
									int bl1;
									do {
										bl1 = rnd.nextInt(v1.ordemDeVisitacao.size());
									} while (v1.ordemDeVisitacao.get(bl1).getNumero() == 0 || v1.ordemDeVisitacao.size() == 0);
		
									// uma segunda posi��o do ve�culo � selecionada
									// esta n�o deve ser o dep�sito e nem igual a primeira posi��o
									int bl2;
									do {
										bl2 = rnd.nextInt(v1.ordemDeVisitacao.size());
									} while (v1.ordemDeVisitacao.get(bl2).getNumero() == 0 || bl2 == bl1);
									
									Collections.swap(v1.ordemDeVisitacao, bl1, bl2);
									v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
									rotaClonada.custoTotalRota += v1.getCustoVeiculo();
									
									for(int v = 0; v < r.getVeiculosUtilizados(); v++) {
										if(v != j)
											rotaClonada.custoTotalRota += v1.getCustoVeiculo();
									}
									
									double custoBuscaLocal = rotaClonada.custoTotalRota;
									
									if(custoBuscaLocal > custoInicial) {
										Collections.swap(v1.ordemDeVisitacao, bl1, bl2);
									}else
										break;
									
									count++;
								}	
							}//fim da buscalocal
							
							// as novas rotas s�o adicionadas em um array auxiliar
							aux.add(rotaClonada);
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


		// menor custo final � encontrado
		if (menorCusto < menorCustoDescendente)
			menorCustoTotal = menorCusto;
		else
			menorCustoTotal = menorCustoDescendente;

		System.out.println("Menor custo encontrado: " + menorCustoTotal);

	}// fecha a main

}// fecha a classe