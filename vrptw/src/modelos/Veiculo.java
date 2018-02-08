package modelos;

import java.util.ArrayList;

public class Veiculo {
	private int cargaMaxima;
	private double cargaOcupada, custoVeiculo = 0, tempoVeiculo = 0;

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
		this.custoVeiculo += custoVeiculo;
	}

	public double getCustoVeiculo() {
		return custoVeiculo;
	}

	public void resetCustoVeiculo() {
		this.custoVeiculo = 0;
	}

	public void setTempoVeiculo(double tempoVeiculo) {
		this.tempoVeiculo += tempoVeiculo;
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

	public int calculaCustos(double[][] matrizDeDistancias) {

		resetCustoVeiculo();
		resetTempoVeiculo();

		ArrayList<Cliente> restante = new ArrayList<>();
		restante.addAll(ordemDeVisitacao);
		int controle = 0;

		do {
			// percorre a rota de um veÃ­culo em especÃ­fico
			for (int row = 1; row < ordemDeVisitacao.size(); row++) {

				setCustoVeiculo(matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row)
				                                                                              .getNumero()]);
				setTempoVeiculo(matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row)

				                                                                              .getNumero()]);

				// se o cliente chega dentro da janela
				if (getTempoVeiculo() >= ordemDeVisitacao.get(row).getInicioJanela()
						&& getTempoVeiculo() <= ordemDeVisitacao.get(row).getFimJanela()) {
					setTempoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
					setCustoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
				}
			} // fecha for
			controle++;
			
			if(controle > ordemDeVisitacao.size())
				return -1;
				
		}while (restante.size() !=0);

		return 1;


	} // fecha calculaCustos

	@Override
	public String toString() {
		return "Veículo: [carga máxima = " + cargaMaxima + " ,carga ocupada = " + cargaOcupada
				+ " ,custo do veículo = " + custoVeiculo + "]\n";
	}

}// fecha classe
