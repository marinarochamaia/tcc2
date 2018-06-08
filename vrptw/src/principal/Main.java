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

		double menorDistancia = 0; //menor dist�ncia encontrada na popula��o inicial
		double menorDistanciaDescendente = 0; //menor dist�ncia encontrada nas novas gera��es
		double menorDistanciaFinal = 0; //menor dist�ncia Final
		int gmax = 5000; //n�mero de gera��es
		int numeroDeRotas = 50; //mu, tamanho da popula��o inicial
		int descendentes = 250; //lamba, n�mero de descendentes
		int criterioParadaBL = 5; //crit�rio de parada da busca local
		int multa = 0; //multa aplicada �s rotas que n�o chegarem dentro da janela
		double cMutacao = 0.8; //coeficiente de muta��o
		double cBuscaLocal = 0.3; //coeficiente de busca local
		int melhora = 0;
		double distanciaAnterior = 0;

		//array auxiliar para guardar todas os ind�viduos criados atrav�s da busca local com o merge com a popula��o inicial
		ArrayList<Rota> aux = new ArrayList<>();
		ArrayList<Cliente> clientes = new ArrayList<>(); //lista de clientes passados pelo arquivo
		ArrayList<Veiculo> veiculos = new ArrayList<>(); //lista de ve�culos passados pelo arquivo
		ArrayList<Rota> populacao = new ArrayList<>(); //array das rotas iniciais (pais)

		//args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//matriz que salva as dist�ncias de todos os clientes para os outros
		double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

		//array para salvar a melhor rota encontrada
		Rota melhorRota = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
		melhorRota.setDistanciaTotalRota(Double.MAX_VALUE);

		//as dist�ncias entre os clientes s�o calculadas
		matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

		//cria��o da popula��o inicial (pais)
		for (int i = 0; i < numeroDeRotas; i++) {
			//a rota � instanciada
			Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			//a fun��o para mudar as rotas � chamada
			r.criaRotas();

			//as rotas criadas s�o clonadas
			Rota rotaInicial = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
			rotaInicial = (Rota) r.getClone(rotaInicial);

			//verifica��o se os ve�culos utilizados n�o s�o maiores do que os dispon�veis
			if (rotaInicial.getVeiculosUtilizados() <= veiculos.size()) {
				//se os ve�culos utilizados s�o menores ou iguais aos dispon�veis, a rota � inclu�da na popula��o
				populacao.add(rotaInicial);
			} else {
				//se n�o a rota � infact�vel
				System.out.println("Rota infact�vel");
			}
		}

		//busca pela menor dist�ncia da popula��o inicial
		menorDistancia = Double.MAX_VALUE;
		for (Rota r : populacao) {
			if (menorDistancia > r.getDistanciaTotalRota()) {
				menorDistancia = r.getDistanciaTotalRota();
			}
		}

		//contador para o n�mero de gera��es que ser�o criadas
		int geracoes = 0;

		//la�o para fazer a muta��o em todas as gera��es criadas
		while (geracoes < gmax) {
			//para cada indiv�duo da popula��o (pai) 
			for (Rota r : populacao) {

				//gerar (lambda/mu) filhos
				for (int i = 0; i < (descendentes / numeroDeRotas); i++) {

					//a rota � clonada
					Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
					rotaClonada = (Rota) r.getClone(rotaClonada);

					//o dep�sito � instanciado
					Cliente deposito = rotaClonada.getDeposito();

					//a muta��o � feita
					Mutacao mut = new Mutacao();
					mut.fazMutacao(rotaClonada, cMutacao, matrizDeDistancias, multa, deposito);

					//a busca local � feita
					BuscaLocal bl = new BuscaLocal();
					bl.fazBuscaLocal(rotaClonada, matrizDeDistancias, multa, cBuscaLocal, deposito, criterioParadaBL);

					//as novas rotas s�o adicionadas em um array auxiliar
					aux.add(rotaClonada);

				}
			}

			//populacao = populacao + aux
			populacao.addAll(aux);

			// as rotas s�o ordenadas por valor de dist�ncias
			Collections.sort(populacao);

			// � feito um corte para mu indiv�duos
			populacao.subList(numeroDeRotas, populacao.size()).clear();

			// busca pela menor dist�ncia da nova populacao
			menorDistanciaDescendente = Double.MAX_VALUE;
			for (int i = 0; i < populacao.size(); i++) {

				if (menorDistanciaDescendente >= populacao.get(i).getDistanciaTotalRota()) {
					menorDistanciaDescendente = populacao.get(i).getDistanciaTotalRota();
				}
			}

			//a lista auxiliar � limpa
			aux.clear();

			//� impressa a dist�ncia encontrada nessa gera��o
			BigDecimal bd3 = new BigDecimal(menorDistanciaDescendente).setScale(2, RoundingMode.HALF_EVEN);
			System.out.println(geracoes + " " + bd3);

			//auto-adapta��o
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

		// menor dist�ncia final � encontrada
		if (menorDistancia < menorDistanciaDescendente) {

			menorDistanciaFinal = menorDistancia;

		} else {

			menorDistanciaFinal = menorDistanciaDescendente;

		}

		//a melhor rota � selecionada
		melhorRota = (Rota) populacao.get(0).getClone(melhorRota);

		//� impressa a melhor rota
		for (int i = 0; i < melhorRota.getNumeroDeVeiculos(); i++) {

			System.out.println((i + 1) + "   " + melhorRota.listaVeiculos.get(i).ordemDeVisitacao);

		}

		boolean semMulta;

		FuncoesBuscaLocal fbl = new FuncoesBuscaLocal();
		semMulta = fbl.calculaFuncaoObjetivo(matrizDeDistancias, multa, melhorRota);

		if(semMulta)
			System.out.println("Rota atende a janela de tempo!");
		else
			System.out.println("Rota n�o atende a janela de tempo!");

		//� impressa a menor dist�ncia antes da estrat�gia evolutiva
		BigDecimal bd1 = new BigDecimal(menorDistancia).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Dist�ncia antes da estrat�gia evolutiva: " + bd1);

		//� impressa a menor dist�ncia depois da estrat�gia evolutiva
		BigDecimal bd2 = new BigDecimal(menorDistanciaFinal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println("Menor dist�ncia encontrada: " + bd2.doubleValue() + "\nTempo da rota: " + melhorRota.getTempoTotalRota());

		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);
		System.out.println("Hora: " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println("Minuto: " + calendar.get(Calendar.MINUTE));
		System.out.println("Segundo: " + calendar.get(Calendar.SECOND));

	}
}