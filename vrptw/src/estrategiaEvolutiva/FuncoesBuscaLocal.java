package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//função para calcular custo da função objetivo
	public void calculaCustoFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o custo total da rota é resetado
		rotaClonada.resetCustoTotalRota();

		//a lista de veículos utilizados é percorrida
		for (int l = 0; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//um veículo é selecionado no array de veículos
			Veiculo v3 = rotaClonada.listaVeiculos.get(l);

			//o custo do veículo analisado é calculado
			v3.calculaCustos(matrizDeDistancias, multa);
			//é somado o custo deste veículo ao custo total da rota 
			rotaClonada.setCustoTotalRota(v3.getCustoVeiculo());

		}
	}

	//função para atualizar as posições dos clientes após a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {

		giantTour.clear();
		giantTour.add(deposito);

		//a lista de veículos é percorrida
		for(int i = 0; i <= veiculosUtilizados; i++) {

			//um veículo é selecionado
			Veiculo v = listaVeiculos.get(i);

			//a lista de clientes deste veículo é percorrida
			for(int j = 0; j < v.ordemDeVisitacao.size(); j++) {

				Cliente clienteAtual = v.ordemDeVisitacao.get(j);

				//verificação se o cliente atual não é o depósito
				if(clienteAtual.getNumero() == 0) 				
					continue;
				else 
				{
					//o cliente atual é adicionado ao giant tour
					giantTour.add(clienteAtual);

				}
			}


		}
	}
}
		
