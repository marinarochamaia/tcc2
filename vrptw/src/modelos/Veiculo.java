package modelos;

import java.util.ArrayList;

public class Veiculo {
	private float cargaMaxima, cargaOcupada=0;
	//custoVeiculo=0;

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

	public Veiculo(int cargaMaxima) {
			this.cargaOcupada = 0;
			this.cargaMaxima = cargaMaxima;
	}
	
	
}
