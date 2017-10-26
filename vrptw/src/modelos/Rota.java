package modelos;

import java.util.*;

import modelos.Cliente;
import modelos.Veiculo;

public class Rota {

	private int numeroDeRotas;

	private int multa;

	private int numeroDeClientes;
	
	private int numeroDeVeiculos;

	public Rota(int numeroDeRotas, int numeroDeClientes, int multa, int numeroDeVeiculos){

		this.numeroDeRotas = numeroDeRotas;
		this.numeroDeClientes = numeroDeClientes;
		this.multa = multa;
		this.numeroDeVeiculos = numeroDeVeiculos;
	
	}

	public int getNumeroDeRotas() {
		return numeroDeRotas;
	}
	public void setNumeroDeRotas(int numeroDeRotas){
		this.numeroDeRotas = numeroDeRotas;
	}
	public int getNumeroDeClientes() {
		return numeroDeClientes;
	}
	public void setNumeroDeClientes(int numeroDeClientes){
		this.numeroDeClientes = numeroDeClientes;
	}
	public int getMulta() {
		return multa;
	}
	public void setMulta(int multa){
		this.multa = multa;
	}
	public int getNumeroDeVeiculos() {
		return numeroDeVeiculos;
	}
	public void setNumeroDeVeiculos(int numeroDeVeiculos){
		this.numeroDeVeiculos = numeroDeVeiculos;
	}
	
	
	//matriz de distâncias entre clientes
	public double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
	//matriz de custos de cada rota
	public double [][] custoRota = new double [numeroDeRotas][numeroDeVeiculos];
	//matriz onde as rotas aleatórias serão salvas
	public ArrayList<ArrayList<Cliente>> rotas = new ArrayList<ArrayList<Cliente>>();
	//arraylist para salvar a rota inicial partindo de zero(depósito) até o máximo de clientes
	//ou seja, cria uma rota partindo do 0 (depósito) até o último cliente em ordem crescente
	public ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();
	//arraylist para salvar a rota aleátoria que será criada
	//(esta só será incluída na matriz de rotas de atender as restrição da capacidade do veículo)
	public ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();
	//arraylist para calcular o custo total de cada rota
	public double [] custoTotalRota = new double [numeroDeRotas];

	
	public void criaRotas(List<Cliente> clientes, List<Veiculo> veiculos){

		//laço para preencher a matriz com as distâncias entre clientes calculadas através da distância euclidiana
		//percorre as linhas da matriz de distâncias
		for(int row = 0; row < numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de distâncias
			for(int column = 0; column < numeroDeClientes; column++){
				if(row == column)
					matrizDeDistancias[row][column] = 0;
				else
					matrizDeDistancias[row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
			}//fecha segundo for
		}//fecha o primeiro for


		//cria as rotas aleatórias sempre partindo do zero(depósito)
		for(int i = 0; i < numeroDeRotas; i++){
			//cria  uma rota partindo de zero(depósito) até o maximo de clientes
			for(Cliente auxiliar: clientes)
				sequenciaDeVisitas.add(auxiliar);

			Cliente deposito = sequenciaDeVisitas.remove(0);

			Collections.shuffle(sequenciaDeVisitas);

			sequenciaDeVisitas.add(0, deposito);
			sequenciaDeVisitas.add(deposito);
			rotas.add(sequenciaDeVisitas);
		}


		//percorre as linhas da matriz
		for(int row = 0; row<numeroDeRotas;row++){
			ArrayList<Cliente> rotaAtual = rotas.get(row);
			int contadorDeCliente = 0;
			@SuppressWarnings("unused")
			int demandaTotal = 0;
			@SuppressWarnings("unused")
			int demandaAtendida = 0;
			@SuppressWarnings("unused")
			int veiculosUtilizados = 0;

			//soma a demanda total de cada rota
			for(int j = 0; j < numeroDeClientes; j++)
				demandaTotal += rotaAtual.get(j).getDemanda();

			//percorre os veículos disponíveis
			for(int i = 0; i < numeroDeVeiculos ; i++) {

				Veiculo veiculo = veiculos.get(i);
				veiculo.resetCargaOcupada();
				int aux = contadorDeCliente;

				//percorre as colunas de cada linha
				for(int column = aux; column < rotaAtual.size(); column++) {

					Cliente clienteAtual = rotaAtual.get(column);

					//se a demanda do cliente que está sendo analisado mais a carga do veículo que já está ocupada for menor
					//que a capacidade máxima do veículo este é incluído a rota deste veículo
					if(veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()){
						possivelRotaVeiculo.add(clienteAtual);
						veiculo.setCargaOcupada(clienteAtual.getDemanda());
						demandaAtendida += clienteAtual.getDemanda();
						contadorDeCliente++;
					}
					//se não, é feito um break e inicia a rota do próximo veículo
					else break;
				}//fecha o terceiro for
				if(veiculo.getCargaOcupada() > 0){
					//a rota de cada veículo é incluída na matriz de cada veículo
					Veiculo.rotasVeiculo.add(possivelRotaVeiculo);
					veiculosUtilizados++;
				}else break;
			}//fecha o segundo for

		}//fecha o primeiro for		

	}//fecha o cria Rotas
	
	public void calculaCustoRota(){
		
		Veiculo veiculo = new Veiculo(10000);
		
		for(int i = 0; i < numeroDeRotas; i++) {
			for(int j = 0; j < numeroDeVeiculos; j++)
			custoTotalRota[j] += veiculo.getCustoVeiculo();
		}
		System.out.println();
		
	}
	
	
}//fecha a classe
