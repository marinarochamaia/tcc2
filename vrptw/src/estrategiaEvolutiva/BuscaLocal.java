package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class BuscaLocal {

	public void fazBuscaLocal(int k, int n, Rota rotaClonada, double [][] matrizDeDistancias, int multa, 
			double cBuscaLocal, Cliente deposito) throws CloneNotSupportedException{

		Random rnd = new Random();
		double bl = rnd.nextDouble();

		//a busca local só é feita se o fator pl for atendido
		if (bl <= cBuscaLocal){
			int count = 0;
			while(count <= 1) {	
				count++;

				//1) remove u e insere após v;
				//2) remove u e x e insere u e x após v;
				//3) remove u e x e insere x e u após v; (posições invertidas)
				//4) troca u e v; (SWAP)
				//5) troca u e x com v;
				//6) troca u e x com v e y;
				//7) remove u e insere após v em veículos diferentes;
				//8) remove u e x e insere u e x após v;
				//9) remove u e x e insere x e u após v em veículos diferentes; (posições invertidas)
				//10) troca u e v em veículos diferentes;
				//11) troca u e x com v em veículos diferentes;
				//12) troca u e x com v e y em veículos diferentes;
				//13) operação 2opt


				BuscaLocalRotasDiferentes blrd = new BuscaLocalRotasDiferentes();
				BuscaLocalRotasIguais blri = new BuscaLocalRotasIguais(); 
				
				ArrayList<Integer> operacoes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
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
						
						blrd.insereApos(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

						break;

					}

					case 8: {

						blrd.insereDuasPosicoesAposUma(rotaClonada, k, n, multa, matrizDeDistancias, deposito);
						
						break;

					}

					case 9: {

						blrd.insereDuasPosicoesAposUmaInvertido(rotaClonada, k, n, multa, matrizDeDistancias, deposito);
						
						break;

					}

					case 10: {
						
						blrd.trocaPosicoes(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

						break;
					}

					case 11: {

						blrd.trocaDuasPosicoesComUmaPosicao(rotaClonada, k, n, multa, matrizDeDistancias, deposito);
	
						break;

					}

					case 12: {

						blrd.trocaDuasPosicoesComDuasPosicoes(rotaClonada, k, n, multa, matrizDeDistancias, deposito);

						break;

					}

					case 13: {

						blri.doisopt(rotaClonada, k, matrizDeDistancias, multa, deposito);

						break;

					}
					
					}
				}
			}
		}
	}
}
