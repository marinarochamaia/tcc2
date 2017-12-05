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

		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		conversor.calculaDistancias(clientes.size(), clientes);
		
		//cria as rotas aleat�rias
		Rota r = new Rota(numeroDeRotas, clientes.size(), multa, veiculos.size());
		r.criaRotas();
		
		//calcula os custos de cada ve�culo
		Veiculo v = new Veiculo(200);
		v.calculaCustos(matrizDeDistancias, numeroDeRotas, multa, clientes.size(), veiculos.size());
		
	}//fecha a main
}//fecha a classe