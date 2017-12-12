package modelos;

import java.util.*;

import modelos.Cliente;
import modelos.Veiculo;

public class Rota {

	private int numeroDeRotas, multa, numeroDeClientes,numeroDeVeiculos; 
    public ArrayList<Cliente> listaClientes = new ArrayList<>();
    public ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
    
	public Rota(int numeroDeRotas, ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos,
            int numeroDeClientes, int multa, int numeroDeVeiculos) {

		this.numeroDeRotas = numeroDeRotas;
		this.numeroDeClientes = numeroDeClientes;
		this.multa = multa;
		this.numeroDeVeiculos = numeroDeVeiculos;
		this.listaClientes = clientes;
		this.listaVeiculos = veiculos;

	}

	public int getNumeroDeRotas() {
		return numeroDeRotas;
	}

	public void setNumeroDeRotas(int numeroDeRotas) {
		this.numeroDeRotas = numeroDeRotas;
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

	public void criaRotas() {

		// arraylist para salvar a rota aleátoria que será criada
		// (esta é será incluída na população de rotas se atender as restrição da
		// capacidade do veículo)
		ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

		// arraylist para salvar a rota inicial partindo de zero(depósito) até o máximo
		// de clientes, ou seja, cria uma rota partindo do 0 (depósito) até o último cliente em ordem
		// crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		// cria uma rota partindo de zero(depósito) até o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);

		Cliente deposito = sequenciaDeVisitas.get(0);
		sequenciaDeVisitas.remove(listaClientes.get(0));

		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.addAll(sequenciaDeVisitas);
		
		int contadorDeCliente = 0;
		@SuppressWarnings("unused")
		int demandaTotal = 0;
		@SuppressWarnings("unused")
		int demandaAtendida = 0;
		@SuppressWarnings("unused")
		int veiculosUtilizados = 0;

		// soma a demanda total de cada rota
		for (int i = 0; i < listaClientes.size(); i++)
			demandaTotal += listaClientes.get(i).getDemanda();

		// percorre os veículos disponíveis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = listaVeiculos.get(j);
			veiculo.resetCargaOcupada();
			int aux = contadorDeCliente;

			// percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				// se a demanda do cliente que está sendo analisado somado a carga do veículo
				// que já está ocupada for menor
				// que a capacidade máxima do veículo este é incluído a rota deste veículo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());

					demandaAtendida += clienteAtual.getDemanda();
					contadorDeCliente++;
				}
				// se não, é feito um break e inicia a rota do próximo veículo
				else
					break;
			}
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);
				veiculosUtilizados++;

			} else
				break;
		} 
	}// fecha o cria Rotas

}// fecha a classe
