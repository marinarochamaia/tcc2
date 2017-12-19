package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota {

	private int multa, numeroDeClientes,numeroDeVeiculos;
	double custoTotalRota=0;
	public double getCustoTotalRota() {
		return custoTotalRota;
	}

	public void setCustoTotalRota(double custoTotalRota) {
		this.custoTotalRota = custoTotalRota;
	}

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

	public void criaRotas() {
		
		// arraylist para salvar a rota ale�toria que ser� criada
		// (esta � ser� inclu�da na popula��o de rotas se atender as restri��o da
		// capacidade do ve�culo)
		ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

		// arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o m�ximo
		// de clientes, ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo cliente em ordem
		// crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		
		// cria uma rota partindo de zero(dep�sito) at� o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);
		
		Cliente deposito = sequenciaDeVisitas.get(0);
		sequenciaDeVisitas.remove(listaClientes.get(0));
		
		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.addAll(sequenciaDeVisitas);
		
		
		int contadorDeCliente = 0;

		// percorre os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {
			

			possivelRotaVeiculo.clear();
			Veiculo veiculo = listaVeiculos.get(j);
			veiculo.ordemDeVisitacao.clear();
			veiculo.resetCargaOcupada();
			int aux = contadorDeCliente;


			// percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);
				listaClientes.add(deposito);
				
				// se a demanda do cliente que est� sendo analisado somado a carga do ve�culo
				// que j� est� ocupada for menor
				// que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
				if ((veiculo.getCargaOcupada() + clienteAtual.getDemanda()) <= veiculo.getCargaMaxima()) {
					
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());
					contadorDeCliente++;
					
				}
				else if((veiculo.getCargaOcupada() + clienteAtual.getDemanda()) > veiculo.getCargaMaxima() && column == listaClientes.size())
					possivelRotaVeiculo.clear();
				// se n�o, � feito um break e inicia a rota do pr�ximo ve�culo				
				else 
					break;
				
									
			}
			
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);
				
				System.out.println(veiculo.ordemDeVisitacao.size());


			} else
				break;
			
			veiculo.calculaCustos(matrizDeDistancias, multa, listaClientes.size(), listaVeiculos.size());
			custoTotalRota += veiculo.getCustoVeiculo();
		} 

		System.out.println("\n\n\n\n");
		//System.out.println("Custo total da rota:" +  custoTotalRota);

	}// fecha o cria Rotas

}// fecha a classe
