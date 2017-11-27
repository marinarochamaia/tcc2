package principal;


import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Veiculo> veiculos = new ArrayList<>();
		
		int numeroDeRotas = 2;
		int multa = 1000;
		double [][] matrizDeDistancias = new double [clientes.size()][clientes.size()];

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//cria as rotas aleatórias
		Rota r = new Rota(numeroDeRotas, clientes.size(), multa, veiculos.size());
		matrizDeDistancias = r.criaRotas(clientes, veiculos);
		
		//calcula os custos de cada veículo
		Veiculo v = new Veiculo(200);
		v.calculaCustos(matrizDeDistancias, numeroDeRotas, multa, clientes.size(), veiculos.size());
		
	}//fecha a main
}//fecha a classe