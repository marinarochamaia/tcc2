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

	public void calculaCustos(double[][] matrizDeDistancias, int multa) {
		
		setCustoVeiculo(0);
		setTempoVeiculo(0);

		double tempoVeiculo = 0;
		double custoVeiculo = 0;
		
		// percorre a rota de um veículo em específico
		for (int row = 1; row < ordemDeVisitacao.size(); row++) {

			tempoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];
			custoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];

			// se o veículo chega dentro da janela
			if (tempoVeiculo >= ordemDeVisitacao.get(row).getInicioJanela()
					&& tempoVeiculo <= ordemDeVisitacao.get(row).getFimJanela()) {
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
			}
			// se o veículo chega antes da janela adiciona-se o tempo de espera e a multa
			else if (tempoVeiculo < ordemDeVisitacao.get(row).getInicioJanela()) {
				tempoVeiculo += ordemDeVisitacao.get(row).getInicioJanela() - tempoVeiculo;
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
				custoVeiculo += ordemDeVisitacao.get(row).getInicioJanela() - tempoVeiculo;
				custoVeiculo += multa;
			}
			// se o veículo chega depois da janela o cliente é atendido mas paga a multa
			else if (tempoVeiculo > ordemDeVisitacao.get(row).getFimJanela()) {
				tempoVeiculo += ordemDeVisitacao.get(row).getDuracaoServico();
				custoVeiculo += multa;
			} // fecha if 

			
		} // fecha for
		
		setTempoVeiculo(tempoVeiculo);
		setCustoVeiculo(custoVeiculo);
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
