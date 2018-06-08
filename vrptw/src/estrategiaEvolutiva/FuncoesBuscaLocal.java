package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//função para calcular distancia e tempo da função objetivo
	public boolean calculaFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o tempo e o custo totais da rota são resetados
		rotaClonada.resetDistanciaTotalRota();
		rotaClonada.resetTempoTotalRota();
		
		boolean semMulta = true;

		//a lista de veículos utilizados é percorrida
		for (int l = 1; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//o custo do veículo analisado é calculado
			semMulta = rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa);
			
			//é somado o custo deste veículo ao custo total da rota 
			rotaClonada.setDistanciaTotalRota(rotaClonada.listaVeiculos.get(l).getDistanciaPercorridaVeiculo());
			rotaClonada.setTempoTotalRota(rotaClonada.listaVeiculos.get(l).getTempoVeiculo());

		}
		
		return semMulta;
	}

	//função para atualizar as posições dos clientes após a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {
		
		//o giant tour é limpo para receber a nova configuração
		giantTour.clear();
		
		//é adicionado o depósito
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
		
