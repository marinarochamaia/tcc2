package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable, Comparable<Rota> {

	private int numeroDeClientes, numeroDeVeiculos, veiculosUtilizados, possivel;

	private Cliente deposito; 
	private double custoTotalRota;

	Conversor conversor;
	public ArrayList<Cliente> listaClientes = new ArrayList<>();
	public ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
	double[][] matrizDeDistancias = new double[listaClientes.size()][listaClientes.size()];

	public Rota(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos, int numeroDeClientes,
			int numeroDeVeiculos, double[][] matrizDeDistancias) {

		this.numeroDeClientes = numeroDeClientes;
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

	public int criaRotas() {

		int controle;
		// arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o m�ximo
		// de clientes, ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo
		// cliente em ordem
		// crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		// cria uma rota partindo de zero(dep�sito) at� o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);
		
		// o deposito que sempre � o primeiro cliente � instanciado
		setDeposito(sequenciaDeVisitas.get(0));

		// confere se a primeira posi��o � rrealmente o dep�sito, se sim este � removido
		if (sequenciaDeVisitas.get(0).getNumero() == 0)
			sequenciaDeVisitas.remove(getDeposito());

		// a lista de clientes � limpa para receber a lista gerada aleatoriamente
		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.add(getDeposito());// o dep�sito deve vir primeiro
		listaClientes.addAll(sequenciaDeVisitas);
	
		controle = criaOrdemDeVisitacao(numeroDeVeiculos, listaVeiculos, listaClientes, deposito, matrizDeDistancias);
		if(controle == -1) {
			return -1;
		}
		
		return 1;
			

	}// fecha o cria Rotas


	
	public int criaOrdemDeVisitacao(int numeroDeVeiculos, ArrayList<Veiculo> listaVeiculos, ArrayList<Cliente> listaClientes,
			Cliente deposito, double [][] matrizDeDistancias) {
		
		int contadorDeCliente = 0;
		resetCustoTotalRota();	

		// percorre os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = listaVeiculos.get(j);
			// limpa-se a ordem de visita��o salva para receber a nova

			veiculo.ordemDeVisitacao.clear();
			// os valores salvos s�o resetados
			veiculo.resetCargaOcupada();
			veiculo.resetCustoVeiculo();
			veiculo.resetTempoVeiculo();

			// arraylist para salvar a rota ale�toria que ser� criada
			// (esta � ser� inclu�da na popula��o de rotas se atender as restri��o da
			// capacidade do ve�culo)
			ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

			// limpa-se a rota do cliente para poder receber a nova rota
			possivelRotaVeiculo.clear();

			// para que a ordem de visita��o dos clientes n�o seja alterada, � salvo no
			// auxiliar qual foi o cliente que n�o pode ser
			// inserido na ultima sequ�ncia para ser o primeiro cliente da atual sequ�ncia
			int aux = contadorDeCliente;
			// percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				// se a demanda do cliente que est� sendo analisado somado a carga do ve�culo
				// que j� est� ocupada for menor
				// que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					if (clienteAtual.getNumero() == 0)
						continue;
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());
					contadorDeCliente++;

				}
				// se n�o, � feito um break e inicia a rota do pr�ximo ve�culo
				else
					break;
			}

			// a rota de cada ve�culo � inserido no array de ordem de visita��o
			// iniciando e terminando no dep�sito
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);
			} else
				break;

			setVeiculosUtilizados(j);

			// calcula o custo de cada ve�culo e adiciona ao custo total da rota
			possivel = veiculo.calculaCustos(matrizDeDistancias);
			if(possivel == 1)
				setCustoTotalRota(veiculo.getCustoVeiculo());
			else if(possivel == -1)
				return -1;
				
		}
		
		return 1;

	}

	// Esse m�todo chama o Object's clone().
	public Rota getClone(Rota r) {
		try {
			// call clone in Object.
			return (Rota) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(" Rota n�o pode ser clonada. ");
			return this;
		}
	}

	
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
		return "Custo total da rota: " + custoTotalRota + "\n";
	}

}
