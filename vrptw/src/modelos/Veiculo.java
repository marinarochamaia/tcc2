package modelos;

import java.util.ArrayList;

public class Veiculo {
	private int cargaMaxima;
	public double cargaOcupada = 0, custoVeiculo, tempoVeiculo;

	// matriz para salvar a rota feita por cada veículo
	public static ArrayList<ArrayList<Cliente>> rotasVeiculo = new ArrayList<ArrayList<Cliente>>();

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
		
		System.out.println("Rotas veiculo " + rotasVeiculo.size() );
		
		double [] custoTotalRota = new double [numeroDeRotas];

		// percorre cada uma das rotas
		for (int i = 0; i < numeroDeRotas; i++) {
			ArrayList<Cliente> rotaAtual = rotasVeiculo.get(i);

			// percorre um veículo em específico
			for (int j = 1; j <= numeroDeVeiculos; j++) {
				
				custoVeiculo = 0;
				tempoVeiculo = 0;
				
				// percorre uma rota em específico
				for (int row = 0; row < rotaAtual.size(); row++) {

					custoVeiculo += matrizDeDistancias[rotaAtual.get(j - 1).getNumero()][rotaAtual.get(j).getNumero()];
					tempoVeiculo += matrizDeDistancias[rotaAtual.get(j - 1).getNumero()][rotaAtual.get(j).getNumero()];
					;

					// se o cliente chega dentro da janela
					if (tempoVeiculo >= rotaAtual.get(j).getInicioJanela()
							&& tempoVeiculo >= rotaAtual.get(j).getFimJanela())
						tempoVeiculo += rotaAtual.get(j).getDuracaoServico();

					// se o cliente chega antes da janela (espera a janela abrir e paga multa)
					else if (tempoVeiculo < rotaAtual.get(j).getInicioJanela()) {
						tempoVeiculo += rotaAtual.get(j).getInicioJanela() - tempoVeiculo;
						tempoVeiculo += rotaAtual.get(j).getDuracaoServico();
						custoVeiculo += multa;
					}
					// se o cliente chega depois da janela o cliente é atendido mas é paga a multa
					else if (tempoVeiculo > rotaAtual.get(j).getInicioJanela()) {
						tempoVeiculo += rotaAtual.get(j).getDuracaoServico();
						custoVeiculo += multa;
					} // fecha if


				} // fecha o terceiro for
				custoTotalRota[i] += custoVeiculo;
				System.out.println("Custo do Veículo " + j + ": " + custoVeiculo);
			} // fecha segundo for
			
			
			System.out.println("Custo Total da Rota " + (i+1) + ": " + custoTotalRota[i]);

		} // fecha primeiro for

	}// fecha função


	
}// fecha classe