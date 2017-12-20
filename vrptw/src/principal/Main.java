package principal;


import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {
		
		double menorCusto=0;
		
		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Veiculo> veiculos = new ArrayList<>();
		ArrayList<Rota> populacao = new ArrayList<>();
		double [][] matrizDeDistancias = new double [clientes.size()][clientes.size()];
		
		int numeroDeRotas = 1;
		int multa = 1000;

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		
		for(int i = 0; i < numeroDeRotas; i++) {
		Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		r.criaRotas();
		populacao.add(r);		
		
		if(i == 0)
			menorCusto = r.getCustoTotalRota();
		else if(menorCusto > r.getCustoTotalRota())
			menorCusto = r.getCustoTotalRota();
		}
		
		System.out.println("Menor custo encontrado nas rotas: " + menorCusto);
		
		

		
	}//fecha a main
}//fecha a classe
