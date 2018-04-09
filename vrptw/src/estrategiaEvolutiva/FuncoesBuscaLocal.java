package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//fun��o para calcular custo da fun��o objetivo
	public void calculaCustoFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o custo total da rota � resetado
		rotaClonada.resetCustoTotalRota();

		//a lista de ve�culos utilizados � percorrida
		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//um ve�culo � selecionado no array de ve�culos
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);

			//o custo do ve�culo analisado � calculado
			v3.calculaCustos(matrizDeDistancias, multa);
			//� somado o custo deste ve�culo ao custo total da rota 
			rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());

		}
	}

	//fun��o para atualizar as posi��es dos clientes ap�s a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {

		giantTour.clear();
		giantTour.add(deposito);

		//a lista de ve�culos � percorrida
		for(int i = 0; i <= veiculosUtilizados; i++) {

			//um ve�culo � selecionado
			Veiculo v = listaVeiculos.get(i);

			//a lista de clientes deste ve�culo � percorrida
			for(int j = 0; j < v.ordemDeVisitacao.size(); j++) {

				Cliente clienteAtual = v.ordemDeVisitacao.get(j);

				//verifica��o se o cliente atual n�o � o dep�sito
				if(clienteAtual.getNumero() == 0) 				
					continue;
				else 
				{
					//o cliente atual � adicionado ao giant tour
					giantTour.add(clienteAtual);

				}
			}


		}
	}
}
		
