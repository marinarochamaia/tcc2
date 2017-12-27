package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable {

	private int multa, numeroDeClientes,numeroDeVeiculos, veiculosUtilizados = 0;

	public double custoTotalRota=0;

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
		this.custoTotalRota = 0;
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
		
		if(sequenciaDeVisitas.get(0).getNumero()==0)
			sequenciaDeVisitas.remove(deposito);
		
		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.add(deposito);
		listaClientes.addAll(sequenciaDeVisitas);
		
		int contadorDeCliente = 0;

		// percorre os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {
			
			possivelRotaVeiculo.clear();
			Veiculo veiculo = listaVeiculos.get(j);
			veiculo.ordemDeVisitacao.clear();
			veiculo.resetCargaOcupada();
			veiculo.resetCustoVeiculo();
			veiculo.resetTempoVeiculo();
			int aux = contadorDeCliente;

			// percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				// se a demanda do cliente que est� sendo analisado somado a carga do ve�culo
				// que j� est� ocupada for menor
				// que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					if(clienteAtual.getNumero() == 0)
						continue;
					possivelRotaVeiculo.add(clienteAtual);
					veiculo.setCargaOcupada(clienteAtual.getDemanda());
					contadorDeCliente++;
					
				}
				// se n�o, � feito um break e inicia a rota do pr�ximo ve�culo				
				else 
					break;					
			}
			
			//a rota de cada ve�culo � inserido no array de ordem de visita��o
			if (veiculo.getCargaOcupada() > 0) {
				
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);
				veiculosUtilizados++;

			} else
				break;
			
			//calcula o custo de cada ve�culo e adiciona ao custo total da rota 
			veiculo.calculaCustos(matrizDeDistancias, multa, listaClientes.size(), listaVeiculos.size());
			custoTotalRota += veiculo.getCustoVeiculo();
		} 
		
	}// fecha o cria Rotas

    // Esse m�todo chama o Object's clone().
    public Rota getClone(Rota r) {
        try {
            // call clone in Object.
            return (Rota) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Rota n�o pode ser clonada. " );
            return this;
        }
    }
	
    @Override
    public String toString() {
    	return "Custo total da rota: " + custoTotalRota + "\n";
    }
    
    
}// fecha a classe
