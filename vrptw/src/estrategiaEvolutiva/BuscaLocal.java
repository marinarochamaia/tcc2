package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocal {

	public void fazBuscaLocal(Rota rotaClonada, double [][] matrizDeDistancias, int multa, 
			double cBuscaLocal, Cliente deposito, int criterioParada) throws CloneNotSupportedException{

		//um n�mero aleat�rio entre 0 e 1 � selecionado para ser o coeficiente que deve ser atendido para a busca local ser feita
		Random rnd = new Random();
		double bl = rnd.nextDouble();

		//a busca local s� � feita se o coeficiente (fator pl) for atendido
		if (bl <= cBuscaLocal){

			int count = 0;

			//crit�rio de parada
			while(count < criterioParada) {	
				count++;			

				//a lista de ve�culos utilizados � percorrida para que todos possam passar pela busca local 
				for(int k = 0; k < rotaClonada.getVeiculosUtilizados(); k++) {
					for(int n = 0; n < rotaClonada.getVeiculosUtilizados(); n++) {

						//se os ve�culos forem iguais, as buscas s�o feitas na mesma rota
						if(k == n) {

							//1) remove u e insere ap�s v;
							//2) remove u e x e insere u e x ap�s v;
							//3) remove u e x e insere x e u ap�s v; (posi��es invertidas)
							//4) troca u e v; (SWAP)
							//5) troca u e x com v;
							//6) troca u e x com v e y;
							//7) opera��o 2opt

							BuscaLocalRotasIguais blri = new BuscaLocalRotasIguais();

							ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6,  7));
							Collections.shuffle(operacoes);	

							for(Integer o : operacoes) {

								switch (o) {

								case 1: {

									blri.inserirApos(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 2: {

									blri.inserirDoisApos(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 3: {

									blri.inserirDoisAposInvertido(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 4: {

									blri.swap(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 5: {

									blri.trocaDuasPosicoesComUmaPosicao(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 6: {

									blri.trocaDuasPosicoesComDuasPosicoes(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}

								case 7: {

									blri.doisopt(rotaClonada, k, matrizDeDistancias, multa, deposito);

									break;

								}
								}
								
	                            FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	                            double custo = rotaClonada.getCustoTotalRota();
	                            fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	                            
	                           if (custo != rotaClonada.getCustoTotalRota()) {
	                               System.err.println(custo);
	                               System.err.println(rotaClonada.getCustoTotalRota());
	                               System.exit(0);
	                           }
							}
						}
						else {
							BuscaLocalRotasDiferentes blrd = new BuscaLocalRotasDiferentes();

							//7) remove u e insere ap�s v em ve�culos diferentes;
							//8) remove u e x e insere u e x ap�s v;
							//9) remove u e x e insere x e u ap�s v em ve�culos diferentes; (posi��es invertidas)
							//10) troca u e v em ve�culos diferentes;
							//11) troca u e x com v em ve�culos diferentes;
							//12) troca u e x com v e y em ve�culos diferentes;

							ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
							Collections.shuffle(operacoes);	

							for(Integer o : operacoes) {

								switch (o) {

								case 1: {

									blrd.insereApos(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;

								}

								case 2: {

									blrd.insereDuasPosicoesAposUma(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;

								}

								case 3: {

									blrd.insereDuasPosicoesAposUmaInvertido(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;

								}

								case 4: {

									blrd.trocaPosicoes(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;
								}

								case 5: {

									blrd.trocaDuasPosicoesComUmaPosicao(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;

								}

								case 6: {

									blrd.trocaDuasPosicoesComDuasPosicoes(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

									break;

								}
								}
								
	                            FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
	                            double custo = rotaClonada.getCustoTotalRota();
	                            fbl.calculaCustoFuncaoObjetivo(matrizDeDistancias, multa, rotaClonada);
	                            
	                           if (custo != rotaClonada.getCustoTotalRota()) {
	                               System.err.println(custo);
	                               System.err.println(rotaClonada.getCustoTotalRota());
	                               System.exit(0);
	                           }

							}
						}
					}
				}
			}
		}
	}
}

