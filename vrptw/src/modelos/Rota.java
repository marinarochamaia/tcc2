package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable, Comparable<Rota> {

	private int multa, numeroDeClientes, numeroDeVeiculos, veiculosUtilizados = 0;

	private double custoTotalRota;

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

	public double getCustoTotalRota() {
		return custoTotalRota;
	}

	public void setCustoTotalRota(double custoTotalRota) {
		this.custoTotalRota += custoTotalRota;
	}
	
	public void resetCustoTotalRota() {
		this.custoTotalRota = 0;
	}

	public void criaRotas() {

		// arraylist para salvar a rota aleátoria que será criada
		// (esta é será incluída na população de rotas se atender as restrição da
		// capacidade do veículo)
		ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

		// arraylist para salvar a rota inicial partindo de zero(depósito) até o máximo
		// de clientes, ou seja, cria uma rota partindo do 0 (depósito) até o último
		// cliente em ordem
		// crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		// cria uma rota partindo de zero(depósito) até o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);

		// o deposito que sempre é o primeiro cliente é instanciado
		Cliente deposito = sequenciaDeVisitas.get(0);

		// confere se a primeira posição é rrealmente o depósito, se sim este é removido
		if (sequenciaDeVisitas.get(0).getNumero() == 0)
			sequenciaDeVisitas.remove(deposito);

		// a lista de clientes é limpa para receber a lista gerada aleatoriamente
		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.add(deposito);// o depósiro deve vir primeiro
		listaClientes.addAll(sequenciaDeVisitas);

		int contadorDeCliente = 0;

		// percorre os veículos disponíveis
		for (int j = 0; j < numeroDeVeiculos; j++) {
			// limpa-se a rota do cliente para poder receber a nova rota
			possivelRotaVeiculo.clear();
			Veiculo veiculo = listaVeiculos.get(j);
			// limpa-se a ordem de visitação salva para receber a nova
			veiculo.ordemDeVisitacao.clear();
			// os valores salvos são resetados
			veiculo.resetCargaOcupada();
			veiculo.resetCustoVeiculo();
			veiculo.resetTempoVeiculo();

			// para que a ordem de visitação dos clientes não seja alterada, é salvo no
			// auxiliar qual foi o cliente que não pode ser
			// inserido na ultima sequência para ser o primeiro cliente da atual sequência
			int aux = contadorDeCliente;

			// percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				// se a demanda do cliente que está sendo analisado somado a carga do veículo
				// que já está ocupada for menor
				// que a capacidade máxima do veículo este é incluído a rota deste veículo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					if (clienteAtual.getNumero() == 0)
						continue;
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());
					contadorDeCliente++;

				}
				// se não, é feito um break e inicia a rota do próximo veículo
				else
					break;
			}

			// a rota de cada veículo é inserido no array de ordem de visitação
			// iniciando e terminando no depósito
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);
				veiculosUtilizados++;
			} else
				break;

			// calcula o custo de cada veículo e adiciona ao custo total da rota
			veiculo.calculaCustos(matrizDeDistancias, multa, listaClientes.size(), listaVeiculos.size());
			custoTotalRota += veiculo.getCustoVeiculo();
		}

	}// fecha o cria Rotas

	// Esse método chama o Object's clone().
	public Rota getClone(Rota r) {
		try {
			// call clone in Object.
			return (Rota) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(" Rota não pode ser clonada. ");
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

}// fecha a classe
