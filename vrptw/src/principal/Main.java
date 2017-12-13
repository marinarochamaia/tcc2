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
		ArrayList<Rota> populacao = new ArrayList<>();
		
		int numeroDeRotas = 1;
		int multa = 1000;

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);


		
		for(int i = 0; i < numeroDeRotas; i++) {
		Rota r = new Rota(numeroDeRotas, clientes, veiculos, clientes.size(), multa, veiculos.size(), conversor);
		r.criaRotas();
		populacao.add(r);
		}

		
	}//fecha a main
}//fecha a classe