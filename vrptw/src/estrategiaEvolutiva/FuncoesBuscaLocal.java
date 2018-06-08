package estrategiaEvolutiva;

import java.util.ArrayList;

import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class FuncoesBuscaLocal {

	//fun��o para calcular distancia e tempo da fun��o objetivo
	public boolean calculaFuncaoObjetivo(double[][] matrizDeDistancias, int multa, Rota rotaClonada) {

		//o tempo e o custo totais da rota s�o resetados
		rotaClonada.resetDistanciaTotalRota();
		rotaClonada.resetTempoTotalRota();
		
		boolean semMulta = true;

		//a lista de ve�culos utilizados � percorrida
		for (int l = 1; l < rotaClonada.getVeiculosUtilizados(); l++) {

			//o custo do ve�culo analisado � calculado
			semMulta = rotaClonada.listaVeiculos.get(l).calculaCustos(matrizDeDistancias, multa);
			
			//� somado o custo deste ve�culo ao custo total da rota 
			rotaClonada.setDistanciaTotalRota(rotaClonada.listaVeiculos.get(l).getDistanciaPercorridaVeiculo());
			rotaClonada.setTempoTotalRota(rotaClonada.listaVeiculos.get(l).getTempoVeiculo());

		}
		
		return semMulta;
	}

	//fun��o para atualizar as posi��es dos clientes ap�s a busca local
	public void atualizaGiantTour(ArrayList<Cliente> giantTour, ArrayList<Veiculo> listaVeiculos, int veiculosUtilizados, Cliente deposito) {
		
		//o giant tour � limpo para receber a nova configura��o
		giantTour.clear();
		
		//� adicionado o dep�sito
		giantTour.add(deposito);

		//a lista de ve�culos � percorrida
		for(int i = 0; i < veiculosUtilizados; i++) {

			//a lista de clientes deste ve�culo � percorrida
			for(int j = 0; j < listaVeiculos.get(i).ordemDeVisitacao.size(); j++) {

				Cliente clienteAtual = listaVeiculos.get(i).ordemDeVisitacao.get(j);

				//verifica��o se o cliente atual n�o � o dep�sito
				if(clienteAtual.getNumero() != 0) {
					//o cliente atual � adicionado ao giant tour
					giantTour.add(clienteAtual);

				}
			}
		}
	}
}
		
