package modelos;

import java.util.Collections;
import java.util.Random;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, Veiculo v1, Cliente deposito) {
		
		
		Random rnd = new Random();
		double m = rnd.nextDouble();
		int controle = 0;
		do {
		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

			if(m <= cMutacao) {

					Collections.swap(rotaClonada.listaClientes, i, j);
					
			}
			
			controle = rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
					matrizDeDistancias);
		
			if(controle == -1) {
				Collections.shuffle(rotaClonada.listaClientes);
			}	

		}
		}while(controle == -1);
		
			

	}
}



