package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable, Comparable<Rota> {

	private int multa, numeroDeClientes, numeroDeVeiculos, veiculosUtilizados;

	private Cliente deposito; 
	private double custoTotalRota, tempoTotalRota;

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

	//função para criação das rotas iniciais (pais)
	public void criaRotas() {

		//arraylist para salvar a rota inicial partindo de zero (depósito) até o máximo de clientes, ou seja, cria uma rota partindo do 0 (depósito) até o último
		//cliente em ordem crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		//cria uma rota partindo de zero(depósito) até o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);

		//o deposito que sempre é o primeiro cliente é instanciado
		setDeposito(listaClientes.get(0));

		//o deposito é removido para não mudar de posição
		sequenciaDeVisitas.remove(deposito);

		// a lista de clientes é limpa para receber a lista gerada aleatoriamente
		listaClientes.clear();

		//é dado um shuffle para criar ordens aleatórias
		Collections.shuffle(sequenciaDeVisitas);

		//o depósito é adicionado na primeira posição
		listaClientes.add(deposito);

		//a lista de clientes recebe a sequencia de visitas gerada aleatoriamente
		listaClientes.addAll(sequenciaDeVisitas);

		criaOrdemDeVisitacao(numeroDeVeiculos, listaVeiculos, listaClientes, deposito, matrizDeDistancias, multa);

	}

	//função para criar a ordem de visitação de cada veículo
	public void criaOrdemDeVisitacao(int numeroDeVeiculos, ArrayList<Veiculo> listaVeiculos, ArrayList<Cliente> listaClientes,
			Cliente deposito, double [][] matrizDeDistancias, int multa) {

		//é criado um contador para controlar os clientes que já foram adicionados em algum veículo
		int contadorDeClientes = 0;

		//o custo e o tempo da rota são resetados
		resetCustoTotalRota();
		resetTempoTotalRota();

		//percorre-se os veículos disponíveis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = listaVeiculos.get(j);

			//limpa-se a ordem de visitação salva para receber a nova
			veiculo.ordemDeVisitacao.clear();

			//os valores salvos são resetados
			veiculo.resetCargaOcupada();
			veiculo.setCustoVeiculo(0);
			veiculo.setTempoVeiculo(0);

			//arraylist para salvar a rota aleátoria que será criada
			//(esta é será incluída na população de rotas se atender as restrição da capacidade do veículo)
			ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

			//limpa-se a rota do cliente para poder receber a nova rota
			possivelRotaVeiculo.clear();

			//para que a ordem de visitação dos clientes não seja alterada, é salvo no
			//auxiliar qual foi o cliente que não pôde ser inserido na ultima sequência para ser o primeiro cliente da atual sequência
			int aux = contadorDeClientes;

			//percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				//se a demanda do cliente que está sendo analisado somado a carga do veículo que já está ocupada for menor
				//que a capacidade máxima do veículo este é incluído a rota deste veículo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {

					//se o cliente atual não for o depósito este é incluído na rota deste veículo
					if(clienteAtual.getNumero() != 0) {

						possivelRotaVeiculo.add(clienteAtual);

						//a demanda deste cliente é adicionada à carga ocupada do veículo 
						veiculo.setCargaOcupada(clienteAtual.getDemanda());			
					}

					//o contador de clientes para saber quantos já foram incluídos em alguma rota
					contadorDeClientes++;	
				}
				//senão, é feito um break para iniciar a rota do próximo veículo
				else
					break;
			}

			//a rota de cada veículo (que possui alguma carga) é inserido no array de ordem de visitação iniciando e terminando no depósito
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);

			} //caso contrário, é dado um return pois todos os clientes já foram alocados
			else 
				return;

			//os veículos utilizados são setados
			setVeiculosUtilizados(j+1);

			// calcula o custo de cada veículo e adiciona ao custo e ao tempo totais da rota
			veiculo.calculaCustos(matrizDeDistancias, multa);
			setCustoTotalRota(veiculo.getCustoVeiculo());
			setTempoTotalRota(veiculo.getTempoVeiculo());
		}
	}

	//função para criar e copiar o objeto rota
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
			System.out.println(" Rota não pode ser clonada. ");
			return this;
		}
	}

	//função para comparar os custos das rotas
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