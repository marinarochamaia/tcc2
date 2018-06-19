package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//função para calcular distancia e tempo da função objetivo
	public void calculaFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o tempo e o custo totais da rota são resetados
		rotaClonada.resetCustoTotalRota();
		rotaClonada.resetTempoTotalRota();
		
		double auxCusto = 0;
		double auxTempo = 0;
		
		//a lista de veículos utilizados é percorrida
		for (int l = 0; l < rotaClonada.listaVeiculos.size(); l++) {

			//o custo do veículo analisado é calculado
			rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa, rotaClonada);
			
			auxCusto += rotaClonada.listaVeiculos.get(l).getCustoVeiculo();
			auxTempo += rotaClonada.listaVeiculos.get(l).getTempoVeiculo();		
		}

		rotaClonada.setCustoTotalRota(auxCusto); 
		rotaClonada.setTempoTotalRota(auxTempo);
	}

	//função para atualizar as posições dos clientes no giant tour após a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {

		//o giant tour é limpo para receber a nova configuração
		giantTour.clear();

		//é adicionado o depósito
		giantTour.add(deposito);

		//a lista de veículos é percorrida
		for(int i = 0; i < listaVeiculos.size(); i++) {

			//a lista de clientes deste veículo é percorrida
			for(int j = 0; j < listaVeiculos.get(i).ordemDeVisitacao.size(); j++) {

				//o cliente da posição i é selecionado
				Cliente clienteAtual = listaVeiculos.get(i).ordemDeVisitacao.get(j);

				//verificação se o cliente atual não é o depósito
				if(clienteAtual.getNumero() != 0) {
					//o cliente atual é adicionado ao giant tour
					giantTour.add(clienteAtual);
				}
			}
		}
	}

	//função para calcular a carga ocupada dos veículos após as mudanças da busca local
	public void calculaCargaOcupada(Veiculo veiculo) {

		//a carga do veículo é resetada
		veiculo.resetCargaOcupada();

		//percorre-se a ordem de visitação deste veículo
		for(int i = 0; i < veiculo.ordemDeVisitacao.size(); i++) {

			//a demanda do cliente da posição i é adicionada à carga ocupada deste veículo
			veiculo.setCargaOcupada(veiculo.ordemDeVisitacao.get(i).getDemanda());
		}		
	}
	
}