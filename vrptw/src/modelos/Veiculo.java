package modelos;

import java.util.ArrayList;

public class Veiculo {
	private int cargaMaxima;
	private double cargaOcupada, custoVeiculo, tempoVeiculo;
	@SuppressWarnings("unused")
	private double custoTotalVeículo;

	public ArrayList<Cliente> ordemDeVisitacao = new ArrayList<>();

	public int getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(int cargaMaxima) {
		this.cargaMaxima = cargaMaxima;
	}

	public double getCargaOcupada() {
		return cargaOcupada;
	}

	public void setCargaOcupada(double cargaOcupada) {
		this.cargaOcupada += cargaOcupada;
	}

	public void resetCargaOcupada() {
		this.cargaOcupada = 0;
	}

	public void setCustoVeiculo(double custoVeiculo) {
		this.custoVeiculo = custoVeiculo;
	}

	public double getCustoVeiculo() {
		return custoVeiculo;
	}
	public void resetCustoVeiculo() {
		this.custoVeiculo = 0;
	}
	
	public void setTempoVeiculo(double tempoVeiculo) {
		this.tempoVeiculo = 0;
	}

	public double getTempoVeiculo() {
		return tempoVeiculo;
	}
	
	public void resetTempoVeiculo() {
		this.tempoVeiculo = 0;
	}

	public Veiculo(int cargaMaxima) {

		this.cargaMaxima = cargaMaxima;
	}

	public void calculaCustos(double[][] matrizDeDistancias, int multa, int numeroDeClientes,
			int numeroDeVeiculos) {
		
		// percorre a rota de um veículo em específico
		for (int row = 1; row < ordemDeVisitacao.size(); row++) {

			custoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];
			tempoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];


			// se o cliente chega dentro da janela
			if (tempoVeiculo >= ordemDeVisitacao.get(row).getInicioJanela()
					&& tempoVeiculo >= ordemDeVisitacao.get(row).getFimJanela())
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();

			// se o cliente chega antes da janela (espera a janela abrir e paga multa)
			else if (tempoVeiculo < ordemDeVisitacao.get(row).getInicioJanela()) {
				tempoVeiculo += ordemDeVisitacao.get(row).getInicioJanela() - tempoVeiculo;
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
				custoVeiculo += multa;
			}
			// se o cliente chega depois da janela o cliente é atendido mas é paga a multa
			else if (tempoVeiculo > ordemDeVisitacao.get(row).getInicioJanela()) {
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
				custoVeiculo += multa;
			}//fecha if

		} //fecha for


	} //fecha calculaCustos
	
	
	@Override
	public String toString() {
		return "Veículo: [carga máxima = " + cargaMaxima + " ,carga ocupada = " + 
					cargaOcupada + " ,custo total do veículo = " + custoTotalVeículo + "]\n";
	}
	
	
}//fecha classe