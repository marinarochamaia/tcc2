package modelos;

import java.util.ArrayList;

public class Veiculo {
	private int cargaMaxima;
	public double cargaOcupada=0, custoVeiculo=0, tempoVeiculo=0;

	//matriz para salvar a rota feita por cada veículo
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
	
	
	public void calculaCustoVeiculo() {
		
		Rota rotaVeiculo = new Rota (100,rotasVeiculo.size(),100,rotasVeiculo.size());

		
		//percorre cada uma das rotas
		for(int i = 0; i < rotasVeiculo.size(); i++) {
			
			ArrayList<Cliente> rotaAtual = rotasVeiculo.get(i);
			
			//percorre uma rota em específico
			for(int j = 1; j < rotasVeiculo.size(); j++) {
				
				custoVeiculo += rotaVeiculo.matrizDeDistancias[rotaAtual.get(j-1).getNumero()][rotaAtual.get(j).getNumero()];
				tempoVeiculo += rotaVeiculo.matrizDeDistancias[j-1][j];
				
				//se o cliente chega dentro da janela
				if(tempoVeiculo >= rotaAtual.get(j).getInicioJanela() && tempoVeiculo >= rotaAtual.get(j).getFimJanela())
					tempoVeiculo += rotaAtual.get(j).getDuracaoServico();
				//se o cliente chega antes da janela (espera a janela abrir e paga multa)
				else if(tempoVeiculo < rotaAtual.get(j).getInicioJanela()) {
					tempoVeiculo += rotaAtual.get(j).getInicioJanela() - tempoVeiculo;
					tempoVeiculo += rotaAtual.get(j).getDuracaoServico();
					custoVeiculo += rotaVeiculo.getMulta();
				}
				//se o cliente chega depois da janela o cliente é atendido mas é paga a multa
				else if(tempoVeiculo > rotaAtual.get(j).getInicioJanela()) {
					tempoVeiculo += rotaAtual.get(j).getDuracaoServico();
					custoVeiculo += rotaVeiculo.getMulta();
				}
		
			}
		}
	}
	
	
}//fecha classe