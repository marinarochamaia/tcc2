package modelos;

import java.util.*;

import modelos.Cliente;
import modelos.Veiculo;

public class Rota {

	private int numeroDeRotas;

	private int multa;

	private int numeroDeClientes;

	private int numeroDeVeiculos;

	public Rota(int numeroDeRotas, int numeroDeClientes, int multa, int numeroDeVeiculos) {

		this.numeroDeRotas = numeroDeRotas;
		this.numeroDeClientes = numeroDeClientes;
		this.multa = multa;
		this.numeroDeVeiculos = numeroDeVeiculos;

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

		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Veiculo> veiculos = new ArrayList<>();

		// arraylist para salvar a rota ale�toria que ser� criada
		// (esta � ser� inclu�da na matriz de rotas de atender as restri��o da
		// capacidade do ve�culo)
		ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

		// arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o m�ximo
		// de clientes
		// ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo cliente em ordem
		// crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		// cria uma rota partindo de zero(dep�sito) at� o maximo de clientes
		for (Cliente auxiliar : clientes)
			sequenciaDeVisitas.add(auxiliar);

		sequenciaDeVisitas.remove(0);

		Collections.shuffle(sequenciaDeVisitas);
		clientes.addAll(sequenciaDeVisitas);

		int contadorDeCliente = 0;
		@SuppressWarnings("unused")
		int demandaTotal = 0;
		@SuppressWarnings("unused")
		int demandaAtendida = 0;
		@SuppressWarnings("unused")
		int veiculosUtilizados = 0;

		// soma a demanda total de cada rota
		for (int i = 0; i < numeroDeClientes; i++)
			demandaTotal += clientes.get(i).getDemanda();

		// percorre os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = veiculos.get(j);
			veiculo.resetCargaOcupada();
			int aux = contadorDeCliente;

			// percorre as colunas de cada cliente
			for (int column = aux; column < clientes.size(); column++) {

				Cliente clienteAtual = clientes.get(column);

				// se a demanda do cliente que est� sendo analisado somado a carga do ve�culo
				// que j� est� ocupada for menor
				// que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());
					demandaAtendida += clienteAtual.getDemanda();
					contadorDeCliente++;
				}
				// se n�o, � feito um break e inicia a rota do pr�ximo ve�culo
				else
					break;
			} // fecha o terceiro for

			if (veiculo.getCargaOcupada() > 0) {
				// a rota de cada ve�culo � inclu�da na matriz de cada ve�culo
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculosUtilizados++;
			} else
				break;
		} // fecha o segundo for

	}// fecha o cria Rotas

}// fecha a classe
