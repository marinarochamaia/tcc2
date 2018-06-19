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
		this.custoTotalRota = custoTotalRota;
	}

	public void resetCustoTotalRota() {
		this.custoTotalRota = 0;
	}

	public double getTempoTotalRota() {
		return tempoTotalRota;
	}

	public void setTempoTotalRota(double tempoTotalRota) {
		this.tempoTotalRota = tempoTotalRota;
	}

	public void resetTempoTotalRota() {
		this.tempoTotalRota = 0;
	}

	//função para criação das rotas iniciais (pais)
	public void criaRotas(Rota r) {

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

		criaOrdemDeVisitacao(numeroDeVeiculos, listaVeiculos, listaClientes, deposito, matrizDeDistancias, multa, r);
	}

	//função para criar a ordem de visitação de cada veículo
	public void criaOrdemDeVisitacao(int numeroDeVeiculos, ArrayList<Veiculo> listaVeiculos, ArrayList<Cliente> listaClientes,
			Cliente deposito, double [][] matrizDeDistancias, int multa, Rota r) {

		//é criado um contador para controlar os clientes que já foram adicionados em algum veículo
		//ele inicia em um por causa do depósito
		int contadorDeClientes = 1;

		//o custo e o tempo da rota são resetados
		resetCustoTotalRota();
		resetTempoTotalRota();
		
		double auxCusto = 0;
		double auxTempo = 0;
		
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
			//variável para armazenar o tempo gasto de cada veículo, para poder moldar as rotas
			double tempoVeiculo = 0;

			//percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {


				Cliente clienteAtual = listaClientes.get(column);

				//se o cliente for o depósito a busca continua a partir do próximo cliente
				if(clienteAtual.getNumero() == 0)
					continue;

				//o tempo gasto entre os clientes é incluso
				tempoVeiculo += matrizDeDistancias[listaClientes.get(column - 1).getNumero()][clienteAtual.getNumero()];

				//é verificado se a carga máxima do veículo é respeitada
				if(veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {

					//é verificado se o tempo do veículo não é maior que o fim da janela do depósito que sempre será o último cliente
					if (tempoVeiculo <= deposito.getFimJanela()) {

						possivelRotaVeiculo.add(clienteAtual);

						//o contador de clientes é incrementado para indicar quantos já foram incluídos em alguma rota
						contadorDeClientes++;

						//a demanda deste cliente é adicionada à carga ocupada do veículo 
						veiculo.setCargaOcupada(clienteAtual.getDemanda());			

						//se o veículo chega antes do início da janela é incluso no tempo do veículo o tempo de espera e o tempo de serviço
						if(tempoVeiculo < clienteAtual.getInicioJanela()) {
							tempoVeiculo += clienteAtual.getInicioJanela() - tempoVeiculo;
							tempoVeiculo += clienteAtual.getDuracaoServico();
						} //se chega depois da abertura da janela é somado somente a duração do serviço
						else
							tempoVeiculo += clienteAtual.getDuracaoServico();
					}//se o fim da janela do depósito é maior, é feito um break para começar a rota do próximo veículo
					else 
						break;

				}//se a carga não é respeitada é feito um break para começar a rota do próximo veículo
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
			auxCusto += veiculo.getCustoVeiculo();
			auxTempo += veiculo.getTempoVeiculo();
		}
		
		r.setCustoTotalRota(auxCusto); 
		r.setTempoTotalRota(auxTempo);
	}

	public void atualizaVeiculosUtilizados(Rota r) {

		int veiculosUtilizados = 0;


		for(int i = 0; i < r.numeroDeVeiculos; i++) {

			if(r.listaVeiculos.get(i).getCustoVeiculo() != 0)
				veiculosUtilizados++;
		}

		r.setVeiculosUtilizados(veiculosUtilizados);

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