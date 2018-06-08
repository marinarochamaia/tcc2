package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota implements Cloneable, Comparable<Rota> {

	private int multa, numeroDeClientes, numeroDeVeiculos, veiculosUtilizados;

	private Cliente deposito; 
	private double distanciaTotalRota, tempoTotalRota;

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

	public double getDistanciaTotalRota() {
		return distanciaTotalRota;
	}

	public void setDistanciaTotalRota(double distanciaTotalRota) {
		this.distanciaTotalRota += distanciaTotalRota;
	}

	public void resetDistanciaTotalRota() {
		this.distanciaTotalRota = 0;
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
	
	public void criaRotas() {

		//arraylist para salvar a rota inicial partindo de zero (dep�sito) at� o m�ximo de clientes, ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo
		//cliente em ordem crescente
		ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();

		//cria uma rota partindo de zero(dep�sito) at� o maximo de clientes
		for (Cliente auxiliar : listaClientes)
			sequenciaDeVisitas.add(auxiliar);
		
		//o deposito que sempre � o primeiro cliente � instanciado
		setDeposito(listaClientes.get(0));

		sequenciaDeVisitas.remove(deposito);

		// a lista de clientes � limpa para receber a lista gerada aleatoriamente
		listaClientes.clear();
		Collections.shuffle(sequenciaDeVisitas);
		listaClientes.add(deposito);
		listaClientes.addAll(sequenciaDeVisitas);

		criaOrdemDeVisitacao(numeroDeVeiculos, listaVeiculos, listaClientes, deposito, matrizDeDistancias, multa);

	}
	
	public void criaOrdemDeVisitacao(int numeroDeVeiculos, ArrayList<Veiculo> listaVeiculos, ArrayList<Cliente> listaClientes,
			Cliente deposito, double [][] matrizDeDistancias, int multa) {
		
		int contadorDeCliente = 0;
		resetDistanciaTotalRota();
		resetTempoTotalRota();
		
		//percorre os ve�culos dispon�veis
		for (int j = 0; j < numeroDeVeiculos; j++) {

			Veiculo veiculo = listaVeiculos.get(j);
			
			//limpa-se a ordem de visita��o salva para receber a nova
			veiculo.ordemDeVisitacao.clear();
			
			//os valores salvos s�o resetados
			veiculo.resetCargaOcupada();
			veiculo.setDistanciaPercorridaVeiculo(0);
			veiculo.setTempoVeiculo(0);

			//arraylist para salvar a rota ale�toria que ser� criada
			//(esta � ser� inclu�da na popula��o de rotas se atender as restri��o da capacidade do ve�culo)
			ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

			//limpa-se a rota do cliente para poder receber a nova rota
			possivelRotaVeiculo.clear();

			//para que a ordem de visita��o dos clientes n�o seja alterada, � salvo no
			//auxiliar qual foi o cliente que n�o pode ser inserido na ultima sequ�ncia para ser o primeiro cliente da atual sequ�ncia
			int aux = contadorDeCliente;
			
			//percorre as colunas de cada cliente
			for (int column = aux; column < listaClientes.size(); column++) {

				Cliente clienteAtual = listaClientes.get(column);

				//se a demanda do cliente que est� sendo analisado somado a carga do ve�culo que j� est� ocupada for menor
				//que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
				if (veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()) {
					if (clienteAtual.getNumero() != 0) {
						
						possivelRotaVeiculo.add(clienteAtual);
						veiculo.setCargaOcupada(clienteAtual.getDemanda());
						
					}
					contadorDeCliente++;					
				}
				//se n�o, � feito um break e inicia a rota do pr�ximo ve�culo
				else
					break;
				
			}

			//a rota de cada ve�culo � inserido no array de ordem de visita��o iniciando e terminando no dep�sito
			if (veiculo.getCargaOcupada() > 0) {
				veiculo.ordemDeVisitacao.add(deposito);
				veiculo.ordemDeVisitacao.addAll(possivelRotaVeiculo);
				veiculo.ordemDeVisitacao.add(deposito);

			} else 
				break;
			
			//os ve�culos utilizados s�o setados
			setVeiculosUtilizados(j+1);
	
			// calcula o custo de cada ve�culo e adiciona ao custo total da rota
			veiculo.calculaCustos(matrizDeDistancias, multa);
			setDistanciaTotalRota(veiculo.getDistanciaPercorridaVeiculo());
			setTempoTotalRota(veiculo.getTempoVeiculo());
	
		}

	}

    public Rota getClone(Rota r) {
        try {
                    
                    r.multa = this.multa;
                    r.numeroDeClientes = this.numeroDeClientes;
                    r.numeroDeVeiculos = this.numeroDeVeiculos;
                    r.veiculosUtilizados = this.veiculosUtilizados;
                    r.deposito = (Cliente) this.deposito.clone();
                    r.distanciaTotalRota = this.distanciaTotalRota;
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

	public int compareTo(Rota rota) {
		
		if (this.distanciaTotalRota < rota.distanciaTotalRota) {
			return -1;
		}
		
		if (this.distanciaTotalRota > rota.distanciaTotalRota) {
			return 1;
		}
		
		return 0;
		
	}
    
	@Override
	public String toString() {
		
		return listaClientes.toString() + "\n";
		
	}
}
