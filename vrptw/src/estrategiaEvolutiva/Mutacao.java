package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, Cliente deposito) {
		
		Random rnd = new Random();
		double m = rnd.nextDouble();


		//o giant tour � percorrido
		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {
			
			//a muta��o s� � feita se o fator de muta��o for atendido
			if(m <= cMutacao) {
				
				//se os clientes visitados forem o dep�sito ou iguais a muta��o n�o � feita
				if(rotaClonada.listaClientes.get(i).getNumero() == 0 || rotaClonada.listaClientes.get(j).getNumero() == 0 || i == j)
					continue;

					Collections.swap(rotaClonada.listaClientes, i, j);
					
			}
		}

		//o giant tour � subdividido em ordens de visita��o de cada ve�culo
		rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
					matrizDeDistancias , multa);

	}
}



