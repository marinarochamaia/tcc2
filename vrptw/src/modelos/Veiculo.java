package modelos;

import java.util.ArrayList;

public class Veiculo {
	private double cargaMaxima, cargaOcupada=0;
	public double custoVeiculo;
	public static double tempoVeiculo;

	//matriz para salvar a rota feita por cada veículo
	public static ArrayList<ArrayList<Cliente>> rotasVeiculo = new ArrayList<ArrayList<Cliente>>();


	public double getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(double cargaMaxima) {
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
		Veiculo.tempoVeiculo = tempoVeiculo;
	}

	public double getTempoVeiculo() {
		return tempoVeiculo;
	}

	public Veiculo(double cargaMaxima) {
		this.cargaOcupada = 0.0;
		this.cargaMaxima = cargaMaxima;
		this.custoVeiculo = 0.0;
		Veiculo.tempoVeiculo = 0.0;
	}


	public static void calculaCustoVeiculo() {
		for(int i = 0; i < rotasVeiculo.size(); i ++) {
			System.out.println("Cheguei aqui");
			//partindo do depósito
			if(tempoVeiculo == 0) {
				@SuppressWarnings("unused")
				double aux;
				aux = Rota.matrizDeDistancias[0][i++];

				System.out.println("Cheguei aqui");

			}//fecha if
		}//fecha for
	}//fecha função
	
	
	
}//fecha classe