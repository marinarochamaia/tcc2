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
	 * Transforma uma lista de par�metros em um objeto do tipo Cliente
	 * @param par�metros Vetor de String com os par�metros do novo objeto Cliente
	 * @return Objeto Cliente com os par�metros informados
	 */
	private Cliente decodificaCliente(String[] parametros)
	{
		// Decodifica os par�metros
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
		if(parametros.length == 10) fimJanela = Integer.valueOf(parametros[9]); // Se existe ele � decodificado
		else fimJanela = -1; // Sen�o � dado o valor padr�o igual a -1 para o mesmo
		
		Cliente cliente = new Cliente(numero, coordenadaX, coordenadaY, demanda, duracaoServico, frequenciaVisita,
				possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas, inicioJanela, fimJanela);
		
		return cliente;
		
	}
	
	/**
	 * Converte as informa��es contidas em um arquivo em objetos do tipo Ve�culo e Cliente, devidamente parametrizados
	 * @param clientes Lista onde ser�o armazenados os objetos do tipo Cliente
	 * @param veiculos Lista onde ser�o armazenados os objetos do tipo Veiculo
	 */
	public void converterArquivo(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos)
	{
		try
		{
			int posicaoDaLinha = 1; // Contador da posi��o da linha atual
			int quantidadeDeVeiculos = 0; // Contador da quantidade de ve�culos 
			int quantidadeDeClientes = 0; // Contador da quantidade de clientes
			BufferedReader leitor = new BufferedReader(new FileReader(this.nomeDoArquivo)); // Leitor que utiliza buffer para a leitura do arquivo
			
			// L�-se a primeira linha
			String linhaAtual = leitor.readLine();
			
			// Enquanto ela n�o for igual a NULL, ou seja, enquanto n�o acabar de ler o arquivo (EOF)
			while(linhaAtual != null)
			{
				linhaAtual = linhaAtual.trim(); // Remove os espa�os em branco no in�cio e no final da String
				String[] parametros = linhaAtual.split(" "); // Recupera os par�metros que est�o separados por um espa�o em branco
				
				switch(posicaoDaLinha)
				{
					case 1: // Se for a primeira linha do arquivo, ent�o cont�m o tipo do problema,
						//a quantidade de clientes e ve�culos e a quantidade m�xima de dias
						
						// Obt�m-se a quantidade de ve�culos e a quantidade de clientes
						quantidadeDeVeiculos = Integer.valueOf(parametros[1]); 
						quantidadeDeClientes = Integer.valueOf(parametros[2]);
						
						// Determina-se o tamanho da lista de ambos modelos
						//veiculos = new ArrayList<Veiculo>(quantidadeDeVeiculos);
						veiculos.ensureCapacity(quantidadeDeVeiculos);
						//clientes = new ArrayList<Cliente>(quantidadeDeClientes);
						clientes.ensureCapacity(quantidadeDeClientes);
						break;
						
					case 2: // Se for a segunda linha do arquivo, que cont�m a limita��o de dias e a carga m�xima dos ve�culos
						int cargaMaxima = Integer.valueOf(parametros[1]); // Obt�m-se a carga m�xima
						
						// Cria-se os objetos de ve�culos
						for(int i = 0; i < quantidadeDeVeiculos; ++i) veiculos.add(new Veiculo(cargaMaxima));
						break;
						
					default: // Para as demais linhas, que s� cont�m os dados dos clientes
						clientes.add(decodificaCliente(parametros)); // Adiciona o objeto decodificado na lista de clientes
						break;
				}
				
				++posicaoDaLinha; // Incrementa a posi��o da linha
				linhaAtual = leitor.readLine(); // L� a pr�xima linha
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
		//matriz de dist�ncias entre clientes
		double [][] matrizDeDistancias = new double [numeroDeClientes][numeroDeClientes];
		
		//la�o para preencher a matriz com as dist�ncias entre clientes calculadas atrav�s da dist�ncia euclidiana
		
		//percorre as linhas da matriz de dist�ncias
		for(int row = 0; row <numeroDeClientes; row++)
		{
			//percorre as colunas da matriz de dist�ncias
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
