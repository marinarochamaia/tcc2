package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import modelos.Cliente;
import modelos.Veiculo;

public class Conversor {

	private String nomeDoArquivo;

	public Conversor(String nomeDoArquivo) {
		
		this.nomeDoArquivo = nomeDoArquivo;
		
	}

	//transforma uma lista de parâmetros em um objeto do tipo Cliente
	private Cliente decodificaCliente(String[] parametros) {
		// Decodifica os parâmetros
		int numero = Integer.valueOf(parametros[0]);
		double coordenadaX = Double.valueOf(parametros[1]);
		double coordenadaY = Double.valueOf(parametros[2]);
		double duracaoServico = Double.valueOf(parametros[3]);
		double demanda = Double.valueOf(parametros[4]);
		int frequenciaVisita = Integer.valueOf(parametros[5]);
		int possiveisCombinacoesDeVisitas = Integer.valueOf(parametros[6]);
		int listaDeTodasPossiveisVisitas = Integer.valueOf(parametros[7]);
		double inicioJanela = Double.valueOf(parametros[8]);

		//verifica-se existe o final da janela na linha
		double fimJanela;
		//se existe ele é decodificado
		if (parametros.length == 10)
			fimJanela = Double.valueOf(parametros[9]);
		 //senão é dado o valor padrão igual a -1 para o mesmo
		else
			fimJanela = -1;

		//o cliente é instanciado
		Cliente cliente = new Cliente(numero, coordenadaX, coordenadaY, demanda, duracaoServico, frequenciaVisita,
				possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas, inicioJanela, fimJanela);

		return cliente;

	}

	//converte as informações contidas em um arquivo em objetos do tipo Veículo e Cliente, devidamente parametrizados
	public void converterArquivo(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos) {
		
		try {
			
			//contador da posição da linha atual
			int posicaoDaLinha = 1;
			//contador da quantidade de veículos
			int quantidadeDeVeiculos = 0;
			//contador da quantidade de clientes
			int quantidadeDeClientes = 0;
			//leitor que utiliza buffer para a leitura do arquivo
			BufferedReader leitor = new BufferedReader(new FileReader(this.nomeDoArquivo));

			//lê-se a primeira linha
			String linhaAtual = leitor.readLine();

			//enquanto ela não for igual a NULL, ou seja, enquanto não acabar de ler o arquivo (EOF)
			while (linhaAtual != null) {
				
				//remove os espaços em branco no início e no final da String
				linhaAtual = linhaAtual.trim();
				//recupera os parâmetros que estão separados por um espaço em branco
				String[] parametros = linhaAtual.split(" ");

				switch (posicaoDaLinha) {
				
				//se for a primeira linha do arquivo, então contém o tipo do problema, a quantidade de clientes e veículos e a quantidade máxima de dias
				case 1: {

					//obtêm-se a quantidade de veículos e a quantidade de clientes
					quantidadeDeVeiculos = Integer.valueOf(parametros[1]);
					quantidadeDeClientes = Integer.valueOf(parametros[2]);

					//determina-se o tamanho da lista de ambos modelos
					//veiculos = new ArrayList<Veiculo>(quantidadeDeVeiculos);
					veiculos.ensureCapacity(quantidadeDeVeiculos);
					//clientes = new ArrayList<Cliente>(quantidadeDeClientes);
					clientes.ensureCapacity(quantidadeDeClientes);
					break;
				}
				
				//se for a segunda linha do arquivo, que contém a limitação de dias e a carga máxima dos veículos	
				case 2: {
					
					//obtêm-se a carga máxima
					int cargaMaxima = Integer.valueOf(parametros[1]);

					//cria-se os objetos de veículos
					for (int i = 0; i < quantidadeDeVeiculos; ++i)
						veiculos.add(new Veiculo(cargaMaxima));
					break;
				
				}
				
				//para as demais linhas, que só contém os dados dos clientes
				default: { 
				
					//adiciona o objeto decodificado na lista de clientes
					clientes.add(decodificaCliente(parametros));
					break;
					
				}
				}

				//incrementa-se a posição da linha
				++posicaoDaLinha; 
				//lê-se a próxima linha
				linhaAtual = leitor.readLine(); 
				
			}

			leitor.close();
			
		} catch (Exception e) {
			
			System.out.println("Erro durante leitura de arquivo: " + e.getMessage());
			
		}
	}

	//calcula a distância euclidiana de um cliente ao outro
	public double[][] calculaDistancias(int numeroDeClientes, ArrayList<Cliente> clientes) {

		//arraylist para calcular o custo total de cada rota
		//matriz de distâncias entre clientes
		double[][] matrizDeDistancias = new double[numeroDeClientes][numeroDeClientes];

		//laço para preencher a matriz com as distâncias entre clientes calculadas através da distância euclidiana
		//percorre as linhas da matriz de distâncias
		for (int row = 0; row < numeroDeClientes; row++) {
			//percorre as colunas da matriz de distâncias
			for (int column = 0; column < numeroDeClientes; column++) {
				
				if (row == column)
					matrizDeDistancias[row][column] = 0.0;
				else
					matrizDeDistancias[row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
			
			}
		}

		return matrizDeDistancias;
	}
}