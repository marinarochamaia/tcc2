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
		for (int l = 1; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//o custo do veículo analisado é calculado
			rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa);
			
			//é somado o custo deste veículo ao custo total da rota 
			rotaClonada.setCustoTotalRota(rotaClonada.listaVeiculos.get(l).getCustoVeiculo());

		}
	}

	//função para atualizar as posições dos clientes após a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {
		
		giantTour.clear();
		giantTour.add(deposito);

		//a lista de veículos é percorrida
		for(int i = 0; i < veiculosUtilizados; i++) {

			//a lista de clientes deste veículo é percorrida
			for(int j = 0; j < listaVeiculos.get(i).ordemDeVisitacao.size(); j++) {

				Cliente clienteAtual = listaVeiculos.get(i).ordemDeVisitacao.get(j);

				//verificação se o cliente atual não é o depósito
				if(clienteAtual.getNumero() != 0) {
					//o cliente atual é adicionado ao giant tour
					giantTour.add(clienteAtual);

				}
			}


		}

		
	}
}
		
