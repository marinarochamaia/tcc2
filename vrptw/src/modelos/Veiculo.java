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

	public void calculaCustos(double[][] matrizDeDistancias, int multa, int numeroDeClientes, int numeroDeVeiculos) {
		
		resetCustoVeiculo();
		resetTempoVeiculo();
		
		// percorre a rota de um veículo em específico
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

			// se o cliente chega antes da janela não é incluso na rota
			else if (getTempoVeiculo() < ordemDeVisitacao.get(row).getInicioJanela()) {
				setTempoVeiculo(ordemDeVisitacao.get(row).getInicioJanela() - getTempoVeiculo());
				setTempoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
				setCustoVeiculo(ordemDeVisitacao.get(row).getInicioJanela() - getTempoVeiculo());
				setCustoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
				setCustoVeiculo(multa);
			}
			// se o cliente chega depois da janela o cliente é atendido mas é paga a multa
			else if (getCustoVeiculo() > ordemDeVisitacao.get(row).getInicioJanela()) {
				setTempoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
				setCustoVeiculo(ordemDeVisitacao.get(row).getDuracaoServico());
				setCustoVeiculo(multa);
			} // fecha if


			
		} // fecha for
		

	} // fecha calculaCustos

	@Override
	public String toString() {
		return "Veículo: [carga máxima = " + cargaMaxima + " ,carga ocupada = " + cargaOcupada
				+ " ,custo do veículo = " + custoVeiculo + "]\n";
	}

}// fecha classe
