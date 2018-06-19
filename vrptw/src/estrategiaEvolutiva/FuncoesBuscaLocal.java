package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//fun��o para calcular distancia e tempo da fun��o objetivo
	public void calculaFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o tempo e o custo totais da rota s�o resetados
		rotaClonada.resetCustoTotalRota();
		rotaClonada.resetTempoTotalRota();
		
		double auxCusto = 0;
		double auxTempo = 0;
		
		//a lista de ve�culos utilizados � percorrida
		for (int l = 0; l < rotaClonada.listaVeiculos.size(); l++) {

			//o custo do ve�culo analisado � calculado
			rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa);
			
			auxCusto += rotaClonada.listaVeiculos.get(l).getCustoVeiculo();
			rotaClonada.listaVeiculos.get(l).getTempoVeiculo();		
		}

		rotaClonada.setCustoTotalRota(auxCusto); 
		rotaClonada.setTempoTotalRota(auxTempo);
	}

	//fun��o para atualizar as posi��es dos clientes no giant tour ap�s a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {

		//o giant tour � limpo para receber a nova configura��o
		giantTour.clear();

		//� adicionado o dep�sito
		giantTour.add(deposito);

		//a lista de ve�culos � percorrida
		for(int i = 0; i < listaVeiculos.size(); i++) {

			//a lista de clientes deste ve�culo � percorrida
			for(int j = 0; j < listaVeiculos.get(i).ordemDeVisitacao.size(); j++) {

				//o cliente da posi��o i � selecionado
				Cliente clienteAtual = listaVeiculos.get(i).ordemDeVisitacao.get(j);

				//verifica��o se o cliente atual n�o � o dep�sito
				if(clienteAtual.getNumero() != 0) {
					//o cliente atual � adicionado ao giant tour
					giantTour.add(clienteAtual);
				}
			}
		}
	}

	//fun��o para calcular a carga ocupada dos ve�culos ap�s as mudan�as da busca local
	public void calculaCargaOcupada(Veiculo veiculo) {

		//a carga do ve�culo � resetada
		veiculo.resetCargaOcupada();

		//percorre-se a ordem de visita��o deste ve�culo
		for(int i = 0; i < veiculo.ordemDeVisitacao.size(); i++) {

			//a demanda do cliente da posi��o i � adicionada � carga ocupada deste ve�culo
			veiculo.setCargaOcupada(veiculo.ordemDeVisitacao.get(i).getDemanda());
		}		
	}
	
}