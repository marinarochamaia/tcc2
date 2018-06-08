package modelos;

import java.util.ArrayList;

public class Veiculo implements Cloneable {
	
	private int cargaMaxima;
	private double cargaOcupada, distanciaPercorridaVeiculo, tempoVeiculo;

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

	public void setDistanciaPercorridaVeiculo(double distanciaPercorridaVeiculo) {
		this.distanciaPercorridaVeiculo = distanciaPercorridaVeiculo;
	}

	public double getDistanciaPercorridaVeiculo() {
		return distanciaPercorridaVeiculo;
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

	public boolean calculaCustos(double[][] matrizDeDistancias, int multa) {
		
		//variáveis para armazenar os valores de tempo e custo dos veículos
		double auxTempo = 0.0;
		double auxDistanciaPercorrida = 0.0;

		boolean semMulta = true;

		
		//percorre a rota de um veículo em específico
		for (int row = 1; row < ordemDeVisitacao.size(); row++) {

			//o tempo e o custo inicialmente são dados pela distância de um ponto ao outro
			auxTempo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];
			auxDistanciaPercorrida += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];

			//se o veículo chega dentro da janela, soma-se ao tempo a duração do serviço
			if (auxTempo >= ordemDeVisitacao.get(row).getInicioJanela()
					&& auxTempo <= ordemDeVisitacao.get(row).getFimJanela()) {

				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();
				semMulta = true;
				
			}
			//se o veículo chega antes da janela, soma-se ao tempo o tempo de espera e a duração do serviço e soma-se ao custo o tempo de espera e a multa
			else if (auxTempo< ordemDeVisitacao.get(row).getInicioJanela()) {
				
				double tempoDeEspera = ordemDeVisitacao.get(row).getInicioJanela() - auxTempo;

				auxTempo += tempoDeEspera;
				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();
				auxDistanciaPercorrida += multa;
				semMulta = false;

			}
			//se o veículo chega depois da janela, soma-se ao tempo a duração do serviço e soma-se ao custo a multa
			else if (auxTempo > ordemDeVisitacao.get(row).getFimJanela()) {
				
				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();
				auxDistanciaPercorrida += multa;
				semMulta = false;

			}		
		}
		
		//o custo e o tempo do veículo são resetados
		setDistanciaPercorridaVeiculo(0);
		setTempoVeiculo(0);
		
		//as variáveis do veículo são setadas
		setTempoVeiculo(auxTempo);
		setDistanciaPercorridaVeiculo(auxDistanciaPercorrida);
		
		return semMulta;
			
	}

    @Override
    public Object clone() throws CloneNotSupportedException {
    	
        Veiculo v = new Veiculo(cargaMaxima);
        
        v.cargaOcupada = this.cargaOcupada;
        v.distanciaPercorridaVeiculo = this.distanciaPercorridaVeiculo;
        v.tempoVeiculo = this.tempoVeiculo;
        v.ordemDeVisitacao = new ArrayList<>(ordemDeVisitacao);
        
        return v;
        
    }
    
	@Override
	public String toString() {
	
		return "Veículo: [carga máxima = " + cargaMaxima + " ,carga ocupada = " + cargaOcupada
				+ " ,custo do veículo = " + distanciaPercorridaVeiculo + "]\n";
	
	}

}// fecha classe
