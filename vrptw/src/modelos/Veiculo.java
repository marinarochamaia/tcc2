package modelos;

import java.util.ArrayList;

public class Veiculo implements Cloneable {

	private int cargaMaxima;
	private double cargaOcupada, custoVeiculo, tempoVeiculo;

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

	public void setTempoVeiculo(double tempoVeiculo) {
		this.tempoVeiculo = tempoVeiculo;
	}

	public double getTempoVeiculo() {
		return tempoVeiculo;
	}

	public Veiculo(int cargaMaxima) {

		this.cargaMaxima = cargaMaxima;
	}

	public int calculaCustos(double[][] matrizDeDistancias) {

		setCustoVeiculo(0);
		setTempoVeiculo(0);

		double tempoVeiculo = 0;
		double custoVeiculo = 0;

		ArrayList<Cliente> restante = new ArrayList<>();
		restante.addAll(ordemDeVisitacao);
		int controle = 0;

		do {

			// percorre a rota de um veículo em específico
			for (int row = 1; row < ordemDeVisitacao.size(); row++) {
				
				Cliente clienteAtual = ordemDeVisitacao.get(row);

				tempoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][clienteAtual.getNumero()];
				custoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][clienteAtual.getNumero()];

				// se o veículo chega dentro da janela
				if (tempoVeiculo >= clienteAtual.getInicioJanela() && tempoVeiculo <= clienteAtual.getFimJanela()) {
					tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
					restante.remove(ordemDeVisitacao.get(row));
				}else {
					tempoVeiculo -= matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][clienteAtual.getNumero()];
					custoVeiculo -= matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][clienteAtual.getNumero()];
					break;
				}

			} // fecha for

			controle++;

			if(controle < 10000)
				return -1;

		}while(restante.size() != 0);

		setTempoVeiculo(tempoVeiculo);
		setCustoVeiculo(custoVeiculo);

		return 1;

	} // fecha calculaCustos

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Veiculo v = new Veiculo(cargaMaxima);

		v.cargaOcupada = this.cargaOcupada;
		v.custoVeiculo = this.custoVeiculo;
		v.tempoVeiculo = this.tempoVeiculo;
		v.ordemDeVisitacao = new ArrayList<>(ordemDeVisitacao);

		return v;

	}

	@Override
	public String toString() {
		return "Veículo: [carga máxima = " + cargaMaxima + " ,carga ocupada = " + cargaOcupada
				+ " ,custo do veículo = " + custoVeiculo + "]\n";
	}

}// fecha classe
