package modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Mutacao {
	
	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, ArrayList<Cliente> clientes,
				ArrayList<Veiculo> veiculos, Veiculo v1) {
		
		Random rnd = new Random();
		double m = rnd.nextDouble();

		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

			if(m <= cMutacao) {

				if(v1.getCargaMaxima() - rotaClonada.listaClientes.get(j).getDemanda() + rotaClonada.listaClientes.get(i).getDemanda()
						<= v1.getCargaMaxima())
					Collections.swap(rotaClonada.listaClientes, i, j);

				// calcula os custos da nova rota (função de avaliação ou aptidão)
				rotaClonada.resetCustoTotalRota();
				v1.resetCustoVeiculo();
				v1.calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
				rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

				for(int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
					Veiculo v3 = rotaClonada.listaVeiculos.get(l);
					if(v3.ordemDeVisitacao != v1.ordemDeVisitacao)
						rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
				}
			}
		}
	}

}
