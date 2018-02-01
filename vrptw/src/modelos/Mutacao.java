package modelos;

import java.util.Collections;
import java.util.Random;

public class Mutacao {

	public void fazMutacao(Rota rotaClonada, double cMutacao, int i, double[][] matrizDeDistancias, int multa, Veiculo v1) {
		
		Cliente deposito = rotaClonada.listaClientes.get(0);

		Random rnd = new Random();
		double m = rnd.nextDouble();

		for(int j = 0; j < rotaClonada.listaClientes.size(); j++) {

			if(m <= cMutacao) {

					Collections.swap(rotaClonada.listaClientes, i, j);
					
					rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
							matrizDeDistancias , multa);

				// calcula os custos da nova rota (função de avaliação ou aptidão)
				rotaClonada.resetCustoTotalRota();
				v1.resetCustoVeiculo();
				v1.resetTempoVeiculo();
				v1.calculaCustos(matrizDeDistancias, multa, rotaClonada.listaClientes.size(), rotaClonada.listaVeiculos.size());
				rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());
				
				for(int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
					Veiculo v3 = rotaClonada.listaVeiculos.get(l);
					v3.resetCustoVeiculo();
					v3.resetTempoVeiculo();
					if(v3 != v1) {
						v3.calculaCustos(matrizDeDistancias, multa, rotaClonada.listaClientes.size(), rotaClonada.listaVeiculos.size());
						rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
					}
					
				}
						

			}
		}

	}
}



