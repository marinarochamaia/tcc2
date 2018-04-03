package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, Veiculo v1, Cliente deposito) {
		
		Random rnd = new Random();
		double m = rnd.nextDouble();

		
		
		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {
			
			if(m <= cMutacao) {
				
				if(rotaClonada.listaClientes.get(i) == deposito || rotaClonada.listaClientes.get(j) == deposito)
					continue;

					Collections.swap(rotaClonada.listaClientes, i, j);

					
			}
		}
		
		rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
					matrizDeDistancias , multa);

	}
}



