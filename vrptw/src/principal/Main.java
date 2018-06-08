package principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import estrategiaEvolutiva.BuscaLocal;
import estrategiaEvolutiva.FuncoesBuscaLocal;
import estrategiaEvolutiva.Mutacao;
import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {

		double menorDistancia = 0; //menor distância encontrada na população inicial
		double menorDistanciaDescendente = 0; //menor distância encontrada nas novas gerações
		double menorDistanciaFinal = 0; //menor distância Final
		int gmax = 5000; //número de gerações
		int numeroDeRotas = 50; //mu, tamanho da população inicial
		int descendentes = 250; //lamba, número de descendentes
		int criterioParadaBL = 5; //critério de parada da busca local
		int multa = 0; //multa aplicada às rotas que não chegarem dentro da janela
		double cMutacao = 0.8; //coeficiente de mutação
		double cBuscaLocal = 0.3; //coeficiente de busca local
		int melhora = 0;
		double distanciaAnterior = 0;

		//array auxiliar para guardar todas os indíviduos criados através da busca local com o merge com a população inicial
		ArrayList<Rota> aux = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); //lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); //lista de veículos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); //array das rotas iniciais (pais)

		//args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//matriz que salva as distâncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		//array para salvar a melhor rota encontrada
		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		melhorRota.setDistanciaTotalRota(Double.MAX_VALUE);

		//as distâncias entre os clientes são calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		//criação da população inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			//a rota é instanciada
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			//a função para mudar as rotas é chamada
			r.criaRotas();

			//as rotas criadas são clonadas
			Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			rotaInicial = (Rota) r.getClone(rotaInicial);

			//verificação se os veículos utilizados não são maiores do que os disponíveis
			if (rotaInicial.getVeiculosUtilizados() <= veiculos.size()) {
				//se os veículos utilizados são menores ou iguais aos disponíveis, a rota é incluída na população
				populacao.add(rotaInicial);
			} else {
				//se não a rota é infactível
				System.out.println("Rota infactível");
			}
		}

		//busca pela menor distância da população inicial
		menorDistancia = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorDistancia > r.getDistanciaTotalRota()) {
				menorDistancia = r.getDistanciaTotalRota();
			}
		}

		//contador para o número de gerações que serão criadas
		int geracoes = 0;

		//laço para fazer a mutação em todas as gerações criadas
		while (geracoes < gmax) {
			//para cada indivíduo da população (pai) 
			for (Rota r : populacao) {

				//gerar (lambda/mu) filhos
				for (int i = 0; i < (descendentes / numeroDeRotas); i++) {

					//a rota é clonada
					Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
					rotaClonada = (Rota) r.getClone(rotaClonada);

					//o depósito é instanciado
					Cliente deposito = rotaClonada.getDeposito();

					//a mutação é feita
					Mutacao mut = new Mutacao();
					mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, deposito);

					//a busca local é feita
					BuscaLocal bl = new BuscaLocal();
					bl.fazBuscaLocal(rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito, criterioParadaBL);

					//as novas rotas são adicionadas em um array auxiliar
					aux.add(rotaClonada);

				}
			}

			//populacao = populacao + aux
			populacao.addAll(aux);

			// as rotas são ordenadas por valor de distâncias
			Collections.sort(populacao);

			// é feito um corte para mu indivíduos
			populacao.subList(numeroDeRotas, populacao.size()).clear();

			// busca pela menor distância da nova populacao
			menorDistanciaDescendente = Double.MAX_VALUE;
			for (int i = 0; i < populacao.size(); i++) {

				if (menorDistanciaDescendente >= populacao.get(i).getDistanciaTotalRota()) {
					menorDistanciaDescendente = populacao.get(i).getDistanciaTotalRota();
				}
			}

			//a lista auxiliar é limpa
			aux.clear();

			//é impressa a distância encontrada nessa geração
			BigDecimal bd3 = new BigDecimal(menorDistanciaDescendente).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println(geracoes + " " + bd3);

			//auto-adaptação
			if(distanciaAnterior == menorDistanciaDescendente)
				melhora++;
			else {

				distanciaAnterior = menorDistanciaDescendente;
				melhora = 0;

			}

			if(melhora > 10)
				cBuscaLocal = 0.6;
			if(melhora > 20)
				cMutacao = 0.6;
			if(melhora > 30)
				cBuscaLocal = 0.8;
			if(melhora > 40)
				cMutacao = 0.3;
			if(melhora > 50)
				cBuscaLocal = 0.6;
			if(melhora > 60)
				cMutacao = 0.6;
			if(melhora > 70)
				cBuscaLocal = 0.3;
			if(melhora > 80)
				cMutacao = 0.8;

			geracoes++;	

		}

		// menor distância final é encontrada
		if (menorDistancia < menorDistanciaDescendente) {

			menorDistanciaFinal = menorDistancia;

		} else {

			menorDistanciaFinal = menorDistanciaDescendente;

		}

		//a melhor rota é selecionada
		melhorRota = (Rota) populacao.get(0).getClone(melhorRota);

		//é impressa a melhor rota
		for (int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {

			System.out.println((i + 1) + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);

		}

		boolean semMulta;

		FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
		semMulta = fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, melhorRota);

		if(semMulta)
			System.out.println("Rota atende a janela de tempo!");
		else
			System.out.println("Rota não atende a janela de tempo!");

		//é impressa a menor distância antes da estratégia evolutiva
		BigDecimal bd1 = new BigDecimal(menorDistancia).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Distância antes da estratégia evolutiva: " + bd1);

		//é impressa a menor distância depois da estratégia evolutiva
		BigDecimal bd2 = new BigDecimal(menorDistanciaFinal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor distância encontrada: " + bd2.doubleValue() + "\nTempo da rota: " + melhorRota.getTempoTotalRota());

		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		System.out.println("Hora: " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println("Minuto: " + calendar.get(Calendar.MINUTE));
		System.out.println("Segundo: " + calendar.get(Calendar.SECOND));

	}
}