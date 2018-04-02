package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	
	public void calculaCustoFuncaoObjetivo(Veiculo v1, double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		rotaClonada.resetCustoTotalRota();
		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if (l != rotaClonada.listaVeiculos.indexOf(v1)) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}

	public void calculaCustoFuncaoObjetivoDoisVeiculos(Veiculo v1, double[][] matrizDeDistancias, int multa,
			Rota rotaClonada, int v) {

		rotaClonada.resetCustoTotalRota();
		Veiculo v2 = rotaClonada.listaVeiculos.get(v);

		v1.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v1.getCustoVeiculo());

		v2.calculaCustos(matrizDeDistancias, multa);
		rotaClonada.setCustoTotalRota(v2.getCustoVeiculo());

		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);
			if (l != rotaClonada.listaVeiculos.indexOf(v1) && l != v) {
				v3.calculaCustos(matrizDeDistancias, multa);
				rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());
			}
		}
	}

	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos) {

		for (int i = 0; i > listaVeiculos.size(); i++) {
			Veiculo v = listaVeiculos.get(i);
			for (int j = 0; j < v.ordemDeVisitacao.size(); j++) {
				if(v.ordemDeVisitacao.get(j).getNumero() != 0)
					giantTour.add(i, v.ordemDeVisitacao.get(j));

			}
		}

	}
}
