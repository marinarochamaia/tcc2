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

	//matriz de distâncias entre clientes
	public static double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
	//matriz de custos de cada rota
	public double [][] custoRota = new double [numeroDeRotas][numeroDeVeiculos];
	//cria uma matriz onde as rotas aleatórias serão salvas
	public static ArrayList<ArrayList<Cliente>> rotas = new ArrayList<ArrayList<Cliente>>();
	//cria  um arraylist para salvar a rota inicial partindo de zero(depósito) até o maximo de clientes
	public static ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<Cliente>();
	//cria  um arraylist para salvar a rota que será criada (esta só será incluída na matriz de rotas de atender as restrições)
	public static ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<Cliente>();

	public static void criaRotas(String[] args){

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//laço para preencher a matriz com as distâncias entre clientes calculadas através da distância euclidiana
		//percorre as linhas da matriz de distâncias
		for(int row = 0; row < numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de distâncias
			for(int column = 0; column < numeroDeClientes; column++){
				if(row == column)
					matrizDeDistancias [row][column] = 0;
				else
					matrizDeDistancias [row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
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
			int demandaTotal = 0;
			int demandaAtendida = 0;
			int veiculosUtilizados = 0;

			//soma a demanda total de cada rota
			for(int j = 0; j < numeroDeClientes; j++)
				demandaTotal += rotaAtual.get(j).getDemanda();

			//percorre os veículos disponíveis
			for(int i = 0; i < numeroDeVeiculos; i++) {

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
				if(veiculo.getCargaOcupada()!=0){
					//a rota de cada veículo é incluída na matriz de cada veículo
					veiculo.rotasVeiculo.add(possivelRotaVeiculo);
					veiculosUtilizados++;
				}else break;
			}//fecha o segundo for

		}//fecha o primeiro for	

	}
}//fecha a classe
