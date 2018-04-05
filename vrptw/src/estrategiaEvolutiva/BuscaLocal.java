package estrategiaEvolutiva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class BuscaLocal {

	public void fazBuscaLocal(Veiculo v1, Veiculo v2, Rota rotaClonada, double [][] matrizDeDistancias, int multa, 
			int k, double cBuscaLocal, Cliente deposito) throws CloneNotSupportedException{

		Random rnd = new Random();
		double bl = rnd.nextDouble();

		//a busca local só é feita se o fator pl for atendido
		if (bl <= cBuscaLocal){
			int count = 0;
			while(count <= rotaClonada.listaClientes.size()/2) {	
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

						blri.inserirApos(rotaClonada, v1, matrizDeDistancias, multa, deposito);
						
						break;
						
					}

					case 2: {

						blri.inserirDoisApos(rotaClonada, v1, matrizDeDistancias, multa, deposito);

						break;

					}
					case 3: {

						blri.inserirDoisAposInvertido(rotaClonada, v1, matrizDeDistancias, multa, deposito);

						break;

					}

					case 4: {

						blri.swap(rotaClonada, v1, matrizDeDistancias, multa, deposito);

						break;

					}


					case 5: {

						blri.trocaDuasPosicoesComUmaPosicao(rotaClonada, v1, matrizDeDistancias, multa, deposito);

						break;

					}

					case 6: {

						blri.trocaDuasPosicoesComDuasPosicoes(rotaClonada, v1, matrizDeDistancias, multa, deposito);

						break;

					}

					case 7: {

						blrd.insereApos(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);
						
						break;

					}

					case 8: {

						blrd.insereDuasPosicoesAposUma(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);

						break;

					}

					case 9: {

						blrd.insereDuasPosicoesAposUmaInvertido(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);

						break;

					}

					case 10: {

						blrd.trocaPosicoes(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);

						break;
					}

					case 11: {

						blrd.trocaDuasPosicoesComUmaPosicao(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);

	
						break;

					}

					case 12: {

						blrd.trocaDuasPosicoesComDuasPosicoes(rotaClonada, v1, v2, multa, matrizDeDistancias, deposito);

						break;

					}

					case 13: {

						blri.doisopt(rotaClonada, v1, matrizDeDistancias, multa, k, deposito);

						break;

					}


					}//fecha switch
				}//fecha for
			}

		} // fecha if
	}// fim da buscalocal


}
