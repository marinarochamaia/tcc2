package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//fun��o para calcular distancia e tempo da fun��o objetivo
	public boolean calculaFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o tempo e o custo totais da rota s�o resetados
		rotaClonada.resetCustoTotalRota();
		rotaClonada.resetTempoTotalRota();

		//vari�vel para indicar se a rota atende ou n�o � restri��o de tempo
		boolean semMulta = true;

		//a lista de ve�culos utilizados � percorrida
		for (int l = 1; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//o custo do ve�culo analisado � calculado
			semMulta = rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa);

			//� somado o custo deste ve�culo ao custo total da rota 
			rotaClonada.setCustoTotalRota(rotaClonada.listaVeiculos.get(l).getCustoVeiculo());

			//� somado o tempo deste ve�culo ao tempo total da rota 
			rotaClonada.setTempoTotalRota(rotaClonada.listaVeiculos.get(l).getTempoVeiculo());
		}

		return semMulta;
	}

	//fun��o para atualizar as posi��es dos clientes no giant tour ap�s a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {

		//o giant tour � limpo para receber a nova configura��o
		giantTour.clear();

		//� adicionado o dep�sito
		giantTour.add(deposito);

		//a lista de ve�culos � percorrida
		for(int i = 0; i < veiculosUtilizados; i++) {

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
	public void calculaCargaOcupada(Cliente deposito, Veiculo veiculo) {

		//a carga do ve�culo � resetada
		veiculo.resetCargaOcupada();

		//percorre-se a ordem de visita��o deste ve�culo
		for(int i = 0; i < veiculo.ordemDeVisitacao.size(); i++) {

			//a demanda do cliente da posi��o i � adicionada � carga ocupada deste ve�culo
			veiculo.setCargaOcupada(veiculo.ordemDeVisitacao.get(i).getDemanda());
		}		
	}
}