package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, double[][] matrizDeDistancias, int multa, Cliente deposito) {

		Random rnd = new Random();

		//um inteiro é criado e será instanciado aleatoriamente para determinar a posição que será utilizada para aplicar a mutação
		int k;

		//o giant tour é percorrido
		for (int j = 0; j < rotaClonada.listaClientes.size(); j++) {

			//um número aleatório entre 0 e 1 é selecionado para ser o coeficiente que deve ser atendido para a mutação ser feita
			double m = rnd.nextDouble();

			//a mutação só é feita se o coeficiente de mutação for atendido
			if (m <= cMutacao) {

				//enquanto k for igual a j é selecionado um novo valor
				do {
					k = rnd.nextInt(rotaClonada.listaClientes.size());
				} while (j == k);

				//se os clientes visitados forem o depósito a mutação não é feita
				if (rotaClonada.listaClientes.get(j).getNumero() == 0 || rotaClonada.listaClientes.get(k).getNumero() == 0) {
					continue;
				}

				Collections.swap(rotaClonada.listaClientes, j, k);

			}
		}

		//o giant tour é subdividido em ordens de visitação de cada veículo
		rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
				matrizDeDistancias, multa, rotaClonada);	
	}
}