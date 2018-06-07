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

		//um número aleatório entre 0 e 1 é selecionado para ser o coeficiente que deve ser atendido para a busca local ser feita
		Random rnd = new Random();
		double bl = rnd.nextDouble();

		//a busca local só é feita se o coeficiente (fator pl) for atendido
		if (bl <= cBuscaLocal){

			int count = 0;

			//critério de parada
			while(count < criterioParada) {	
				count++;			

				//a lista de veículos utilizados é percorrida para que todos possam passar pela busca local 
				for(int k = 0; k < rotaClonada.getVeiculosUtilizados(); k++) {
					for(int n = 0; n < rotaClonada.getVeiculosUtilizados(); n++) {

						//se os veículos forem iguais, as buscas são feitas na mesma rota
						if(k == n) {

							//1) remove u e insere após v;
							//2) remove u e x e insere u e x após v;
							//3) remove u e x e insere x e u após v; (posições invertidas)
							//4) troca u e v; (SWAP)
							//5) troca u e x com v;
							//6) troca u e x com v e y;
							//7) operação 2opt

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
							}
						}
						else {
							BuscaLocalRotasDiferentes blrd = new BuscaLocalRotasDiferentes();

							//7) remove u e insere após v em veículos diferentes;
							//8) remove u e x e insere u e x após v;
							//9) remove u e x e insere x e u após v em veículos diferentes; (posições invertidas)
							//10) troca u e v em veículos diferentes;
							//11) troca u e x com v em veículos diferentes;
							//12) troca u e x com v e y em veículos diferentes;

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
							}
						}
					}
				}
			}
		}
	}
}

