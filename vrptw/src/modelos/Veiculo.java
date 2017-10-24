package modelos;

import java.util.ArrayList;

public class Veiculo {
	private float cargaMaxima, cargaOcupada=0;
	int custoVeiculo, tempoVeiculo;

	//matriz para salvar a rota feita por cada veículo
	public ArrayList<ArrayList<Cliente>> rotasVeiculo = new ArrayList<ArrayList<Cliente>>();

	
	public float getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(float cargaMaxima) {
		this.cargaMaxima = cargaMaxima;
	}

	public float getCargaOcupada() {
		return cargaOcupada;
	}

	public void setCargaOcupada(float cargaOcupada) {
		this.cargaOcupada += cargaOcupada;
	}
	
	public void resetCargaOcupada() {
		this.cargaOcupada = 0;
	}
	
	public void setCustoVeiculo(int custoVeiculo) {
		this.custoVeiculo = custoVeiculo;
	}

	public int getCustoVeiculo() {
		return custoVeiculo;
	}
	
	public void setTempoVeiculo(int tempoVeiculo) {
		this.tempoVeiculo = tempoVeiculo;
	}

	public int getTempoVeiculo() {
		return tempoVeiculo;
	}
	
	public Veiculo(int cargaMaxima) {
		this.cargaOcupada = 0;
		this.cargaMaxima = cargaMaxima;
		this.custoVeiculo = 0;
		this.custoVeiculo = 0;
	}
	
	
	void calculaCustoVeiculo() {
		for(int i = 0; i < rotasVeiculo.size(); i ++) {
			
	}
	
	
		
	}
}