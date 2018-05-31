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

	//transforma uma lista de par�metros em um objeto do tipo Cliente
	private Cliente decodificaCliente(String[] parametros) {
		// Decodifica os par�metros
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
		//se existe ele � decodificado
		if (parametros.length == 10)
			fimJanela = Double.valueOf(parametros[9]);
		 //sen�o � dado o valor padr�o igual a -1 para o mesmo
		else
			fimJanela = -1;

		//o cliente � instanciado
		Cliente cliente = new Cliente(numero, coordenadaX, coordenadaY, demanda, duracaoServico, frequenciaVisita,
				possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas, inicioJanela, fimJanela);

		return cliente;

	}

	//converte as informa��es contidas em um arquivo em objetos do tipo Ve�culo e Cliente, devidamente parametrizados
	public void converterArquivo(ArrayList<Cliente> clientes, ArrayList<Veiculo> veiculos) {
		
		try {
			
			//contador da posi��o da linha atual
			int posicaoDaLinha = 1;
			//contador da quantidade de ve�culos
			int quantidadeDeVeiculos = 0;
			//contador da quantidade de clientes
			int quantidadeDeClientes = 0;
			//leitor que utiliza buffer para a leitura do arquivo
			BufferedReader leitor = new BufferedReader(new FileReader(this.nomeDoArquivo));

			//l�-se a primeira linha
			String linhaAtual = leitor.readLine();

			//enquanto ela n�o for igual a NULL, ou seja, enquanto n�o acabar de ler o arquivo (EOF)
			while (linhaAtual != null) {
				
				//remove os espa�os em branco no in�cio e no final da String
				linhaAtual = linhaAtual.trim();
				//recupera os par�metros que est�o separados por um espa�o em branco
				String[] parametros = linhaAtual.split(" ");

				switch (posicaoDaLinha) {
				
				//se for a primeira linha do arquivo, ent�o cont�m o tipo do problema, a quantidade de clientes e ve�culos e a quantidade m�xima de dias
				case 1: {

					//obt�m-se a quantidade de ve�culos e a quantidade de clientes
					quantidadeDeVeiculos = Integer.valueOf(parametros[1]);
					quantidadeDeClientes = Integer.valueOf(parametros[2]);

					//determina-se o tamanho da lista de ambos modelos
					//veiculos = new ArrayList<Veiculo>(quantidadeDeVeiculos);
					veiculos.ensureCapacity(quantidadeDeVeiculos);
					//clientes = new ArrayList<Cliente>(quantidadeDeClientes);
					clientes.ensureCapacity(quantidadeDeClientes);
					break;
				}
				
				//se for a segunda linha do arquivo, que cont�m a limita��o de dias e a carga m�xima dos ve�culos	
				case 2: {
					
					//obt�m-se a carga m�xima
					int cargaMaxima = Integer.valueOf(parametros[1]);

					//cria-se os objetos de ve�culos
					for (int i = 0; i < quantidadeDeVeiculos; ++i)
						veiculos.add(new Veiculo(cargaMaxima));
					break;
				
				}
				
				//para as demais linhas, que s� cont�m os dados dos clientes
				default: { 
				
					//adiciona o objeto decodificado na lista de clientes
					clientes.add(decodificaCliente(parametros));
					break;
					
				}
				}

				//incrementa-se a posi��o da linha
				++posicaoDaLinha; 
				//l�-se a pr�xima linha
				linhaAtual = leitor.readLine(); 
				
			}

			leitor.close();
			
		} catch (Exception e) {
			
			System.out.println("Erro durante leitura de arquivo: " + e.getMessage());
			
		}
	}

	//calcula a dist�ncia euclidiana de um cliente ao outro
	public double[][] calculaDistancias(int numeroDeClientes, ArrayList<Cliente> clientes) {

		//arraylist para calcular o custo total de cada rota
		//matriz de dist�ncias entre clientes
		double[][] matrizDeDistancias = new double[numeroDeClientes][numeroDeClientes];

		//la�o para preencher a matriz com as dist�ncias entre clientes calculadas atrav�s da dist�ncia euclidiana
		//percorre as linhas da matriz de dist�ncias
		for (int row = 0; row < numeroDeClientes; row++) {
			//percorre as colunas da matriz de dist�ncias
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