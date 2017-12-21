package principal;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		
		double menorCusto = 0, custoAtual = 0, menorCustoMutacao = 0, menorCustoFinal=0;

		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Veiculo> veiculos = new ArrayList<>();
		ArrayList<Rota> populacao = new ArrayList<>();
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		int numeroDeRotas = 1000;
		int multa = 1000;

		// args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será
		// lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		for (int i = 0; i < numeroDeRotas; i++) {
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			r.criaRotas();
			populacao.add(r);
			
			if (i == 0)
				menorCusto = r.getCustoTotalRota();
			else if (menorCusto > r.getCustoTotalRota())
				menorCusto = r.getCustoTotalRota();

			int geracoes = 0;

			while (geracoes < 2) {

				for (int j = 0; j < veiculos.size(); j++) {
					ArrayList<Cliente> descendenteAtual = new ArrayList<>();
					ArrayList<Veiculo> veiculosDescentes = new ArrayList<>();
					veiculosDescentes.clear();
					descendenteAtual = (ArrayList<Cliente>) r.listaVeiculos.get(j).ordemDeVisitacao.clone();
					veiculosDescentes = (ArrayList<Veiculo>) r.listaVeiculos.clone();
					
					int troca = veiculos.size()/2;

					Collections.swap(descendenteAtual, (troca/2), (troca/3));
					Collections.swap(descendenteAtual, (troca/2), (troca/4));
					veiculosDescentes.get(j).calculaCustos(matrizDeDistancias, multa, clientes.size(), veiculos.size());
					custoAtual = veiculosDescentes.get(j).getCustoVeiculo();

					if(j == 0) 
						menorCustoMutacao = custoAtual;
					else if(custoAtual < menorCustoMutacao) 
						menorCustoMutacao = custoAtual;
					
				}
				
				if(i == 0) 
					menorCustoFinal = menorCustoMutacao;
				else if(menorCusto < menorCustoFinal) 
					menorCustoFinal = menorCusto;
				
				geracoes++;

			}

		}

		System.out.println("Menor custo encontrado nas rotas: \n" + menorCusto);
		System.out.println("Menor custo encontrado nas rotas nas mutação: \n" + menorCustoMutacao);
		System.out.println("Menor custo encontrado nas rotas depois mutação: \n" + menorCustoFinal);

	}// fecha a main
}// fecha a classe
