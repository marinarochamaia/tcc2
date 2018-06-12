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

	//fun��o para calcular o custo de um ve�culo e retornar um booleano que indica se a rota atende ou n�o � restri��o da janela de tempo
	public boolean calculaCustos(double[][] matrizDeDistancias, int multa) {

		//vari�veis para armazenar os valores de tempo e custo dos ve�culos
		double auxTempo = 0.0;
		double auxCustoVeiculo = 0.0;

		//vari�vel para indicar se a rota atende � restri��o da janela de tempo
		boolean semMulta = true;

		//percorre a rota de um ve�culo em espec�fico
		for (int row = 1; row < ordemDeVisitacao.size(); row++) {

			//o tempo e o custo inicialmente s�o dados pela dist�ncia de um ponto ao outro
			auxTempo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];
			auxCustoVeiculo += matrizDeDistancias[ordemDeVisitacao.get(row - 1).getNumero()][ordemDeVisitacao.get(row).getNumero()];

			//se o ve�culo chega dentro da janela, soma-se ao tempo a dura��o do servi�o
			if (auxTempo >= ordemDeVisitacao.get(row).getInicioJanela()
					&& auxTempo <= ordemDeVisitacao.get(row).getFimJanela()) {

				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();

			} //se o ve�culo chega antes da janela, soma-se ao tempo o tempo de espera e a dura��o do servi�o e soma-se ao custo o tempo de espera e a multa
			else if (auxTempo< ordemDeVisitacao.get(row).getInicioJanela()) {

				double tempoDeEspera = ordemDeVisitacao.get(row).getInicioJanela() - auxTempo;

				auxTempo += tempoDeEspera;
				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();
				auxCustoVeiculo += tempoDeEspera;
				auxCustoVeiculo += multa;
				semMulta = false;

			} //se o ve�culo chega depois da janela, soma-se ao tempo a dura��o do servi�o e soma-se ao custo a multa
			else if (auxTempo > ordemDeVisitacao.get(row).getFimJanela()) {

				auxTempo += ordemDeVisitacao.get(row).getDuracaoServico();
				auxCustoVeiculo += multa;
				semMulta = false;
			}		
		}

		//o custo e o tempo do ve�culo s�o resetados
		setCustoVeiculo(0);
		setTempoVeiculo(0);

		//as vari�veis do ve�culo s�o setadas
		setTempoVeiculo(auxTempo);
		setCustoVeiculo(auxCustoVeiculo);

		return semMulta;	
	}

	//fun��o para criar e copiar o objeto ve�culo
	@Override
	public Object clone() throws CloneNotSupportedException {

		Veiculo v = new Veiculo(cargaMaxima);

		v.cargaOcupada = this.cargaOcupada;
		v.custoVeiculo = this.custoVeiculo;
		v.tempoVeiculo = this.tempoVeiculo;
		v.ordemDeVisitacao = new ArrayList<>(ordemDeVisitacao);

		return v;
	}

	@Override
	public String toString() {

		return "Ve�culo: [carga m�xima = " + cargaMaxima + " ,carga ocupada = " + cargaOcupada
				+ " ,custo do ve�culo = " + custoVeiculo + "]\n";
	}
}