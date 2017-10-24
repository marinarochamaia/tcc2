package modelos;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Rota {

	private static int numeroDeRotas;

	private static int numeroDeClientes;

	private static int multa;

	private static int numeroDeVeiculos;

	static ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	static ArrayList<Veiculo> veiculos = new ArrayList<Veiculo>();
	
	


	public Rota(int numeroDeRotas, int numeroDeClientes, int multa, int numeroDeVeiculos){

		Rota.numeroDeRotas = 2;
		Rota.numeroDeClientes = clientes.size();
		Rota.multa = 1000;
		Rota.numeroDeVeiculos = veiculos.size();
	}

	public int getNumeroDeRotas() {
		return numeroDeRotas;
	}
	public void setNumeroDeRotas(int numeroDeRotas){
		Rota.numeroDeRotas = numeroDeRotas;
	}
	public int getNumeroDeClientes() {
		return numeroDeClientes;
	}
	public void setNumeroDeClientes(int numeroDeClientes){
		Rota.numeroDeRotas = numeroDeClientes;
	}
	public int getMulta() {
		return multa;
	}
	public void setMulta(int multa){
		Rota.multa = multa;
	}
	public int getNumeroDeVeiculos() {
		return numeroDeVeiculos;
	}
	public void setNumeroDeVeiculos(int numeroDeVeiculos){
		Rota.numeroDeVeiculos = numeroDeVeiculos;
	}

	//matriz de dist�ncias entre clientes
	public static double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
	//matriz de custos de cada rota
	public static double [][] custoRota = new double [numeroDeRotas][numeroDeVeiculos];
	//cria uma matriz onde as rotas aleat�rias ser�o salvas
	public static ArrayList<ArrayList<Cliente>> rotas = new ArrayList<ArrayList<Cliente>>();
	//cria  um arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o m�ximo de clientes
	//ou seja, cria uma rota partindo do 0 (dep�sito) at� o �ltimo cliente em ordem crescente
	public static ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<>();
	//cria  um arraylist para salvar a rota ale�toria que ser� criada
	//(esta s� ser� inclu�da na matriz de rotas de atender as restri��o da capacidade do ve�culo)
	public static ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<>();

	public static void criaRotas(String[] args){

		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//la�o para preencher a matriz com as dist�ncias entre clientes calculadas atrav�s da dist�ncia euclidiana
		//percorre as linhas da matriz de dist�ncias
		for(int row = 0; row < numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de dist�ncias
			for(int column = 0; column < numeroDeClientes; column++){
				if(row == column)
					matrizDeDistancias [row][column] = 0;
				else
					matrizDeDistancias [row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
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
			int demandaTotal = 0;
			int demandaAtendida = 0;
			int veiculosUtilizados = 0;

			//soma a demanda total de cada rota
			for(int j = 0; j < numeroDeClientes; j++)
				demandaTotal += rotaAtual.get(j).getDemanda();

			//percorre os ve�culos dispon�veis
			for(int i = 0; i < numeroDeVeiculos; i++) {

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
				if(veiculo.getCargaOcupada()!=0){
					//a rota de cada ve�culo � inclu�da na matriz de cada ve�culo
					veiculo.rotasVeiculo.add(possivelRotaVeiculo);
					veiculosUtilizados++;
				}else break;
			}//fecha o segundo for

		}//fecha o primeiro for		

	}//fecha o cria Rotas
	
	public static void calculaCustos(){
		
		//percorre as linhas da matriz que s�o as rotas
		for(int row = 0; row < numeroDeRotas; row++) {
			//percorre as colunas da matriz que s�o os ve�culos
			for(int column = 0; column < numeroDeVeiculos; column++) {
				
			}
			
		}
		
	}
	
}//fecha a classe
