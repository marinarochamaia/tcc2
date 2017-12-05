package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import modelos.Cliente;
import modelos.Veiculo;

public class Conversor {
	
	private String nomeDoArquivo;
	

	public Conversor(String nomeDoArquivo)
	{
		this.nomeDoArquivo = nomeDoArquivo;
	}
	
	/**
	 * Transforma uma lista de parâmetros em um objeto do tipo Cliente
	 * @param parâmetros Vetor de String com os parâmetros do novo objeto Cliente
	 * @return Objeto Cliente com os parâmetros informados
	 */
	private Cliente decodificaCliente(String[] parametros)
	{
		// Decodifica os parâmetros
		int numero = Integer.valueOf(parametros[0]);
		float coordenadaX = Float.valueOf(parametros[1]);
		float coordenadaY = Float.valueOf(parametros[2]);
		float duracaoServico = Float.valueOf(parametros[3]);
		float demanda = Float.valueOf(parametros[4]);
		int frequenciaVisita = Integer.valueOf(parametros[5]);
		int possiveisCombinacoesDeVisitas = Integer.valueOf(parametros[6]);
		int listaDeTodasPossiveisVisitas = Integer.valueOf(parametros[7]);
		int inicioJanela = Integer.valueOf(parametros[8]);
		
		// Verifica-se existe o final da janela na linha
		int fimJanela;
		if(parametros.length == 10) fimJanela = Integer.valueOf(parametros[9]); // Se existe ele é decodificado
		else fimJanela = -1; // Senão é dado o valor padrão igual a -1 para o mesmo
		
		Cliente cliente = new Cliente(numero, coordenadaX, coordenadaY, demanda, duracaoServico, frequenciaVisita,
				possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas, inicioJanela, fimJanela);
		
		return cliente;
		
	}
	
	/**
	 * Converte as informações contidas em um arquivo em objetos do tipo Veículo e Cliente, devidamente parametrizados
	 * @param clientes Lista onde serão armazenados os objetos do tipo Cliente
	 * @param veiculos Lista onde serão armazenados os objetos do tipo Veiculo
	 */
	public void converterArquivo(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos)
	{
		try
		{
			int posicaoDaLinha = 1; // Contador da posição da linha atual
			int quantidadeDeVeiculos = 0; // Contador da quantidade de veículos 
			int quantidadeDeClientes = 0; // Contador da quantidade de clientes
			BufferedReader leitor = new BufferedReader(new FileReader(this.nomeDoArquivo)); // Leitor que utiliza buffer para a leitura do arquivo
			
			// Lê-se a primeira linha
			String linhaAtual = leitor.readLine();
			
			// Enquanto ela não for igual a NULL, ou seja, enquanto não acabar de ler o arquivo (EOF)
			while(linhaAtual != null)
			{
				linhaAtual = linhaAtual.trim(); // Remove os espaços em branco no início e no final da String
				String[] parametros = linhaAtual.split(" "); // Recupera os parâmetros que estão separados por um espaço em branco
				
				switch(posicaoDaLinha)
				{
					case 1: // Se for a primeira linha do arquivo, então contém o tipo do problema,
						//a quantidade de clientes e veículos e a quantidade máxima de dias
						
						// Obtêm-se a quantidade de veículos e a quantidade de clientes
						quantidadeDeVeiculos = Integer.valueOf(parametros[1]); 
						quantidadeDeClientes = Integer.valueOf(parametros[2]);
						
						// Determina-se o tamanho da lista de ambos modelos
						//veiculos = new ArrayList<Veiculo>(quantidadeDeVeiculos);
						veiculos.ensureCapacity(quantidadeDeVeiculos);
						//clientes = new ArrayList<Cliente>(quantidadeDeClientes);
						clientes.ensureCapacity(quantidadeDeClientes);
						break;
						
					case 2: // Se for a segunda linha do arquivo, que contém a limitação de dias e a carga máxima dos veículos
						int cargaMaxima = Integer.valueOf(parametros[1]); // Obtêm-se a carga máxima
						
						// Cria-se os objetos de veículos
						for(int i = 0; i < quantidadeDeVeiculos; ++i) veiculos.add(new Veiculo(cargaMaxima));
						break;
						
					default: // Para as demais linhas, que só contém os dados dos clientes
						clientes.add(decodificaCliente(parametros)); // Adiciona o objeto decodificado na lista de clientes
						break;
				}
				
				++posicaoDaLinha; // Incrementa a posição da linha
				linhaAtual = leitor.readLine(); // Lê a próxima linha
			}
			
			leitor.close();
		}
		catch (Exception e)
		{
			System.out.println("Erro durante leitura de arquivo: " + e.getMessage());
		}	
	}
	
	public double [][] calculaDistancias(int numeroDeClientes, ArrayList<Cliente> clientes) {
		
		//arraylist para calcular o custo total de cada rota
		//matriz de distâncias entre clientes
		double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
		
		//laço para preencher a matriz com as distâncias entre clientes calculadas atravás da distância euclidiana
		
		//percorre as linhas da matriz de distâncias
		for(int row = 0; row <numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de distâncias
			for(int column = 0; column < numeroDeClientes; column++){
				if(row == column)
					matrizDeDistancias[row][column] = 0.0;
				else
					matrizDeDistancias[row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));
			}//fecha segundo for
		}//fecha o primeiro for
		
		return matrizDeDistancias;
	}
	

}
