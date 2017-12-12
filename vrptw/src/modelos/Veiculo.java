package modelos;

import java.util.ArrayList;

public class Veiculo {
	private int cargaMaxima;
	public double cargaOcupada = 0, custoVeiculo, tempoVeiculo;

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

	public void calculaCustos(double[][] matrizDeDistancias, int numeroDeRotas, int multa, int numeroDeClientes,
			int numeroDeVeiculos) {
		
		double [] custoTotalRota = new double [numeroDeRotas];


			// percorre um veículo em específico
			for (int j = 1; j <= numeroDeVeiculos; j++) {

				custoVeiculo = 0;
				tempoVeiculo = 0;
				
				// percorre uma rota em específico
				for (int row = 0; row < numeroDeClientes; row++) {

					custoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(j - 1).getNumero()][ordemDeVisitacao.get(j).getNumero()];
					tempoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(j - 1).getNumero()][ordemDeVisitacao.get(j).getNumero()];
					;

					// se o cliente chega dentro da janela
					if (tempoVeiculo >= ordemDeVisitacao.get(j).getInicioJanela()
							&& tempoVeiculo >= ordemDeVisitacao.get(j).getFimJanela())
						tempoVeiculo += ordemDeVisitacao.get(j).getDuracaoServico();

					// se o cliente chega antes da janela (espera a janela abrir e paga multa)
					else if (tempoVeiculo < ordemDeVisitacao.get(j).getInicioJanela()) {
						tempoVeiculo += ordemDeVisitacao.get(j).getInicioJanela() - tempoVeiculo;
						tempoVeiculo += ordemDeVisitacao.get(j).getDuracaoServico();
						custoVeiculo += multa;
					}
					// se o cliente chega depois da janela o cliente é atendido mas é paga a multa
					else if (tempoVeiculo > ordemDeVisitacao.get(j).getInicioJanela()) {
						tempoVeiculo += ordemDeVisitacao.get(j).getDuracaoServico();
						custoVeiculo += multa;
					}


					custoTotalRota[j] += custoVeiculo;
					System.out.println("Custo Total da Rota " + (j) + ": " + custoTotalRota[j]);

				} 

			} 			

		}

	}