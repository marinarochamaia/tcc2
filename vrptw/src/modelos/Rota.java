package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable, Comparable<Rota> {

	private int multa, numeroDeClientes, numeroDeVeiculos, veiculosUtilizados;

	private Cliente deposito; 
	private double custoTotalRota, tempoTotalRota;
	private boolean factivel = true;
	
	Conversor conversor;
	public ArrayList<Cliente> listaClientes = new ArrayList<>();
	public ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
	double[][] matrizDeDistancias = new double[listaClientes.size()][listaClientes.size()];

	public Rota(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos, int numeroDeClientes, int multa,
			int numeroDeVeiculos, double[][] matrizDeDistancias) {

		this.numeroDeClientes = numeroDeClientes;
		this.multa = multa;
		this.numeroDeVeiculos = numeroDeVeiculos;
		this.listaClientes = clientes;
		this.listaVeiculos = veiculos;
		this.matrizDeDistancias = matrizDeDistancias;
	}

	public int getNumeroDeClientes() {
		return numeroDeClientes;
	}

	public void setNumeroDeClientes(int numeroDeClientes) {
		this.numeroDeClientes = numeroDeClientes;
	}

	public int getMulta() {
		return multa;
	}

	public void setMulta(int multa) {
		this.multa = multa;
	}

	public int getNumeroDeVeiculos() {
		return numeroDeVeiculos;
	}

	public void setNumeroDeVeiculos(int numeroDeVeiculos) {
		this.numeroDeVeiculos = numeroDeVeiculos;
	}

	public int getVeiculosUtilizados() {
		return veiculosUtilizados;
	}

	public void setVeiculosUtilizados(int veiculosUtilizados) {
		this.veiculosUtilizados = veiculosUtilizados;
	}

	public Cliente getDeposito() {
		return deposito;
	}

	public void setDeposito(Cliente deposito) {
		this.deposito = deposito;
	}

	public double getCustoTotalRota() {
		return custoTotalRota;
	}

	public void setCustoTotalRota(double custoTotalRota) {
		this.custoTotalRota += custoTotalRota;
	}

	public void resetCustoTotalRota() {
		this.custoTotalRota = 0;
	}

	public double getTempoTotalRota() {
		return tempoTotalRota;
	}

	public void setTempoTotalRota(double tempoTotalRota) {
		this.tempoTotalRota += tempoTotalRota;
	}

	public void resetTempoTotalRota() {
		this.tempoTotalRota = 0;
	}
	
	public boolean isFactivel() {
		return factivel;
	}

	public void setFactivel(boolean factivel) {
		this.factivel = factivel;
	}

	//fun��o para cria��o das rotas iniciais (pais)
	public void criaRotas(Rota r) {

		//arraylist para salvar a rota inicial partindo de zero (dep�sito) at� o m�ximo de clientes, ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo
		//cliente em ordem crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		//cria uma rota partindo de zero(dep�sito) at� o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);

		//o deposito que sempre � o primeiro cliente � instanciado
		setDeposito(listaClientes.get(0));

		//o deposito � removido para n�o mudar de posi��o
		sequenciaDeVisitas.remove(deposito);

		// a lista de clientes � limpa para receber a lista gerada aleatoriamente
		listaClientes.clear();

		//� dado um shuffle para criar ordens aleat�rias
		Collections.shuffle(sequenciaDeVisitas);

		//o dep�sito � adicionado na primeira posi��o
		listaClientes.add(deposito);

		//a lista de clientes recebe a sequencia de visitas gerada aleatoriamente
		listaClientes.addAll(sequenciaDeVisitas);

		criaOrdemDeVisitacao(numeroDeVeiculos, listaVeiculos, listaClientes, deposito, matrizDeDistancias, multa, r);
	}

	//fun��o para criar a ordem de visita��o de cada ve�culo
	public void criaOrdemDeVisitacao(int numeroDeVeiculos, ArrayList<Veiculo> listaVeiculos, ArrayList<Cliente> listaClientes,
			Cliente deposito, double [][] matrizDeDistancias, int multa, Rota r) {

		//� criado um contador para controlar os clientes que j� foram adicionados em algum ve�culo
		//ele inicia em um por causa do dep�sito
		int contadorDeClientes = 1;

		//o custo e o tempo da rota s�o resetados
		resetCustoTotalRota();
		resetTempoTotalRota();
				
		//percorre-se os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = listaVeiculos.get(j);

			//limpa-se a ordem de visita��o salva para receber a nova
			veiculo.ordemDeVisitacao.clear();

			//os valores salvos s�o resetados
			veiculo.resetCargaOcupada();
			veiculo.setCustoVeiculo(0);
			veiculo.setTempoVeiculo(0);

			//arraylist para salvar a rota ale�toria que ser� criada
			//(esta � ser� inclu�da na popula��o de rotas se atender as restri��o da capacidade do ve�culo)
			ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

			//limpa-se a rota do cliente para poder receber a nova rota
			possivelRotaVeiculo.clear();

			//para que a ordem de visita��o dos clientes n�o seja alterada, � salvo no
			//auxiliar qual foi o cliente que n�o p�de ser inserido na ultima sequ�ncia para ser o primeiro cliente da atual sequ�ncia
			int aux = contadorDeClientes;
			//vari�vel para armazenar o tempo gasto de cada ve�culo, para poder moldar as rotas
			double tempoVeiculo = 0;

			//percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {


				Cliente clienteAtual = listaClientes.get(column);

				//se o cliente for o dep�sito a busca continua a partir do pr�ximo cliente
				if(clienteAtual.getNumero() == 0)
					continue;

				//o tempo gasto entre os clientes � incluso
				tempoVeiculo += matrizDeDistancias[listaClientes.get(column - 1).getNumero()][clienteAtual.getNumero()];

				//� verificado se a carga m�xima do ve�culo � respeitada
				if(veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {

					//� verificado se o tempo do ve�culo n�o � maior que o fim da janela do dep�sito que sempre ser� o �ltimo cliente
					if (tempoVeiculo <= deposito.getFimJanela()) {

						possivelRotaVeiculo.add(clienteAtual);

						//o contador de clientes � incrementado para indicar quantos j� foram inclu�dos em alguma rota
						contadorDeClientes++;

						//a demanda deste cliente � adicionada � carga ocupada do ve�culo 
						veiculo.setCargaOcupada(clienteAtual.getDemanda());			

						//se o ve�culo chega antes do in�cio da janela � incluso no tempo do ve�culo o tempo de espera e o tempo de servi�o
						if(tempoVeiculo < clienteAtual.getInicioJanela()) {
							tempoVeiculo += clienteAtual.getInicioJanela() - tempoVeiculo;
							tempoVeiculo += clienteAtual.getDuracaoServico();
						} //se chega depois da abertura da janela � somado somente a dura��o do servi�o
						else
							tempoVeiculo += clienteAtual.getDuracaoServico();
					}//se o fim da janela do dep�sito � maior, � feito um break para come�ar a rota do pr�ximo ve�culo
					else 
						break;

				}//se a carga n�o � respeitada � feito um break para come�ar a rota do pr�ximo ve�culo
				else 
					break;		
			}

			//a rota de cada ve�culo (que possui alguma carga) � inserido no array de ordem de visita��o iniciando e terminando no dep�sito
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);

			} //caso contr�rio, � dado um return pois todos os clientes j� foram alocados
			else 
				return;

			//os ve�culos utilizados s�o setados
			setVeiculosUtilizados(j+1);

			// calcula o custo de cada ve�culo e adiciona ao custo e ao tempo totais da rota
			veiculo.calculaCustos(matrizDeDistancias, multa, r);
			r.setCustoTotalRota(veiculo.getCustoVeiculo()); 
			r.setTempoTotalRota(veiculo.getTempoVeiculo());		}
		

	}

	public void atualizaVeiculosUtilizados(Rota r) {

		int veiculosUtilizados = 0;


		for(int i = 0; i < r.numeroDeVeiculos; i++) {

			if(r.listaVeiculos.get(i).getCustoVeiculo() != 0)
				veiculosUtilizados++;
		}

		r.setVeiculosUtilizados(veiculosUtilizados);

	}

	//fun��o para criar e copiar o objeto rota
	public Rota getClone(Rota r) {
		try {

			r.multa = this.multa;
			r.numeroDeClientes = this.numeroDeClientes;
			r.numeroDeVeiculos = this.numeroDeVeiculos;
			r.veiculosUtilizados = this.veiculosUtilizados;
			r.deposito = (Cliente) this.deposito.clone();
			r.custoTotalRota = this.custoTotalRota;
			r.tempoTotalRota = this.tempoTotalRota;
			r.conversor = this.conversor;
			r.listaClientes = new ArrayList<>(this.listaClientes);
			r.listaVeiculos = new ArrayList<>(this.listaVeiculos);
			r.matrizDeDistancias = this.matrizDeDistancias;

			return r;
		} catch (CloneNotSupportedException e) {
			System.out.println(" Rota n�o pode ser clonada. ");
			return this;
		}
	}

	//fun��o para comparar os custos das rotas
	public int compareTo(Rota rota) {

		if (this.custoTotalRota < rota.custoTotalRota) {
			return -1;
		}

		if (this.custoTotalRota > rota.custoTotalRota) {
			return 1;
		}

		return 0;
	}

	@Override
	public String toString() {

		return listaClientes.toString() + "\n";
	}
}