package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;

public class Saida {

	//nome do arquivo
	private String resultado;

	public Saida(String resultado) {
		this.resultado = resultado;			
	}
		
	//m�todo para salvar em um arquivo externo as solu��es encontradas
	public void solucoes(BigDecimal menorCustoTotal, BigDecimal tempoDeExecucao, BigDecimal tempoTotalRota, int quantidadeDeVeiculos, int geracoes) {

		try {
			
			BufferedReader leitor;
			leitor = new BufferedReader(new FileReader(this.resultado));
			
			String linhaAtual = leitor.readLine();
			
			BufferedWriter gravarArq = new BufferedWriter(new FileWriter(resultado, true));
			
			if(linhaAtual != null) {
			
				String linha = "\t\t";
		        
		        gravarArq.newLine();
		        gravarArq.append("" + tempoDeExecucao);
		        gravarArq.append(linha + linha + menorCustoTotal);
		        gravarArq.append(linha + "\t" + tempoTotalRota);
		        gravarArq.append(linha + linha + quantidadeDeVeiculos);
		        gravarArq.append(linha + linha + linha+ geracoes);	
			}
	        
			leitor.close();
			gravarArq.close();
		} catch (Exception e) {
			System.out.println("Erro durante leitura de arquivo: " + e.getMessage());
		}
	}
}