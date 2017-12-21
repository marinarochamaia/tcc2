package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable {

	private int multa, numeroDeClientes,numeroDeVeiculos;
	double custoTotalRota=0;

	Conversor conversor;
    public ArrayList<Cliente> listaClientes = new ArrayList<>();
    public ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
	double [][] matrizDeDistancias = new double [listaClientes.size()][listaClientes.size()];
    
	public Rota(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos,
            int numeroDeClientes, int multa, int numeroDeVeiculos, double [][] matrizDeDistancias) {

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

	public double getCustoTotalRota() {
		return custoTotalRota;
	}

	public void setCustoTotalRota(double custoTotalRota) {
		this.custoTotalRota = custoTotalRota;
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
		listaClientes.add(deposito);
		listaClientes.addAll(sequenciaDeVisitas);

		
		int contadorDeCliente = 0;

		// percorre os veículos disponíveis
		for (int j = 0; j < numeroDeVeiculos; j++) {
			

			possivelRotaVeiculo.clear();
			Veiculo veiculo = listaVeiculos.get(j);
			veiculo.ordemDeVisitacao.clear();
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
				
				//System.out.println("Ordem de visitação do véiculo " + (j+1) + ": " +veiculo.ordemDeVisitacao);


			} else
				break;
			
			veiculo.calculaCustos(matrizDeDistancias, multa, listaClientes.size(), listaVeiculos.size());
			custoTotalRota += veiculo.getCustoVeiculo();
		} 


		
		
	}// fecha o cria Rotas

	
    // This method calls Object's clone().
    public Rota getClone(Rota r) {
        try {
            // call clone in Object.
            return (Rota) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Rota não pode ser clonada. " );
            return this;
        }
    }
	
	
	
}// fecha a classe
