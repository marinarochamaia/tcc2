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
	
	
	//matriz de dist�ncias entre clientes
	public double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
	//matriz de custos de cada rota
	public double [][] custoRota = new double [numeroDeRotas][numeroDeVeiculos];
	//matriz onde as rotas aleat�rias ser�o salvas
	public ArrayList<ArrayList<Cliente>> rotas = new ArrayList<ArrayList<Cliente>>();
	//arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o m�ximo de clientes
	//ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo cliente em ordem crescente
	public ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();
	//arraylist para salvar a rota ale�toria que ser� criada
	//(esta s� ser� inclu�da na matriz de rotas de atender as restri��o da capacidade do ve�culo)
	public ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();
	//arraylist para calcular o custo total de cada rota
	public double [] custoTotalRota = new double [numeroDeRotas];

	
	public void criaRotas(List<Cliente> clientes, List<Veiculo> veiculos){

		//la�o para preencher a matriz com as dist�ncias entre clientes calculadas atrav�s da dist�ncia euclidiana
		//percorre as linhas da matriz de dist�ncias
		for(int row = 0; row < numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de dist�ncias
			for(int column = 0; column < numeroDeClientes; column++){
				if(row == column)
					matrizDeDistancias[row][column] = 0;
				else
					matrizDeDistancias[row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
			}//fecha segundo for
		}//fecha o primeiro for


		//cria as rotas aleat�rias sempre partindo do zero(dep�sito)
		for(int i = 0; i < numeroDeRotas; i++){
			//cria  uma rota partindo de zero(dep�sito) at� o maximo de clientes
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

			//percorre os ve�culos dispon�veis
			for(int i = 0; i < numeroDeVeiculos ; i++) {

				Veiculo veiculo = veiculos.get(i);
				veiculo.resetCargaOcupada();
				int aux = contadorDeCliente;

				//percorre as colunas de cada linha
				for(int column = aux; column < rotaAtual.size(); column++) {

					Cliente clienteAtual = rotaAtual.get(column);

					//se a demanda do cliente que est� sendo analisado mais a carga do ve�culo que j� est� ocupada for menor
					//que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
					if(veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()){
						possivelRotaVeiculo.add(clienteAtual);
						veiculo.setCargaOcupada(clienteAtual.getDemanda());
						demandaAtendida += clienteAtual.getDemanda();
						contadorDeCliente++;
					}
					//se n�o, � feito um break e inicia a rota do pr�ximo ve�culo
					else break;
				}//fecha o terceiro for
				if(veiculo.getCargaOcupada() > 0){
					//a rota de cada ve�culo � inclu�da na matriz de cada ve�culo
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
