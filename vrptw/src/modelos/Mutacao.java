package modelos;

import java.util.Collections;
import java.util.Random;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, Veiculo v1, Cliente deposito) {

		Random rnd = new Random();
		double m = rnd.nextDouble();

		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

			if(m <= cMutacao) {

				Collections.swap(rotaClonada.listaClientes, i, j);
				Collections.swap(rotaClonada.listaClientes, i+5, j);

			}
		}

		rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
				matrizDeDistancias);

	}
}



