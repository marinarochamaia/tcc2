package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, Cliente deposito) {
		
		Random rnd = new Random();
		double m = rnd.nextDouble();

		
		//o giant tour é percorrido
		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {
			
			//a mutação só é feita se o fator de mutação for atendido
			if(m <= cMutacao) {
				
				//se os clientes visitados forem o depósito ou iguais a mutação não é feita
				if(rotaClonada.listaClientes.get(i) == deposito || rotaClonada.listaClientes.get(j) == deposito || i == j)
					continue;

					Collections.swap(rotaClonada.listaClientes, i, j);
					Collections.swap(rotaClonada.listaClientes, i + 5, j);
					
			}
		}
		
		//o giant tour é subdividido em ordens de visitação de cada veículo
		rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
					matrizDeDistancias , multa);

	}
}



