package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;

public class Saida {


	private String resultado;

	public Saida(String resultado) {
		this.resultado = resultado;			
	}
		
	public void solucoes(BigDecimal menorCustoTotal, BigDecimal tempoDeExecucao, BigDecimal tempoTotalRota, int quantidadeDeVeiculos, int geracoes, boolean factivel) {
	

		try {
			
			
			
			BufferedReader leitor;
			//contador da posi��o da linha atual
			leitor = new BufferedReader(new FileReader(this.resultado));
			
			//l�-se a primeira linha
			String linhaAtual = leitor.readLine();
			
			BufferedWriter gravarArq = new BufferedWriter(new FileWriter(resultado, true));
			
			if(linhaAtual != null) {
			
				String linha = "\t\t";
		        
		        gravarArq.newLine();
		        gravarArq.append("" + tempoDeExecucao);
		        gravarArq.append(linha + menorCustoTotal);
		        gravarArq.append(linha + tempoTotalRota);
		        gravarArq.append(linha + quantidadeDeVeiculos);
		        gravarArq.append(linha + "\t" + geracoes);
		        gravarArq.append(linha + "\t" + factivel);
		        
			}
			
	        
	        
			leitor.close();
			gravarArq.close();
			
			
		} catch (Exception e) {
			System.out.println("Erro durante leitura de arquivo: " + e.getMessage());
		}
		
	
	}
	
}
