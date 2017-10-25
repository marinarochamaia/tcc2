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
		

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		ArrayList<Rota> rotas = new ArrayList<>();
		for (int i = 0; i < 10; ++i)
		{
			Rota r = new Rota(1, clientes.size(), 1000, veiculos.size());
			rotas.add(r);
			r.criaRotas(clientes, veiculos);
		}

		Veiculo.calculaCustoVeiculo();


	}//fecha a main
}//fecha a classe