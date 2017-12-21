package principal;

import java.util.*;

import io.Conversor;
import modelos.Cliente;
import modelos.Rota;
import modelos.Veiculo;

public class Main {


    public static void main(String[] args) {

        double menorCusto = 0;

        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Veiculo> veiculos = new ArrayList<>();
        ArrayList<Rota> populacao = new ArrayList<>();
        double[][] matrizDeDistancias = new double[clientes.size()][clientes.size()];

        int numeroDeRotas = 1;
        int multa = 1000;

        // args[0] é o primeiro parâmetro do programa, que é o nome do arquivo que será
        // lido
        Conversor conversor = new Conversor(args[0]);
        conversor.converterArquivo(clientes, veiculos);

        matrizDeDistancias = conversor.calculaDistancias(clientes.size(), clientes);

        //criação da população
        for (int i = 0; i < numeroDeRotas; i++) {
            Rota r = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
            r.criaRotas();
            populacao.add(r);
        }

       
        //busca pelo menor custo da população inicial
        menorCusto = Double.MAX_VALUE;
        for (Rota r : populacao) {
            if (menorCusto > r.getCustoTotalRota()) {
                menorCusto = r.getCustoTotalRota();
            }
        }

        ///número de gerações que serão criadas
        int geracoes = 0;
        
        //laço para fazer a mutação em todas as gerações criadas
        while (geracoes < 1) {

        	//para cada indivíduo da população (rota)
            for (Rota r : populacao) {
            	
            	//a rota é clonada
            	Rota rotaClonada = new Rota(clientes, veiculos, clientes.size(), multa, veiculos.size(), matrizDeDistancias);
            	rotaClonada = (Rota) r.getClone(rotaClonada);
              
            	//são selecionados números aleatórios que serão utilizados para pegar os veículos 
                Random rnd = new Random();
                int j = rnd.nextInt(veiculos.size());

                //os veículos são selecionados
                Veiculo v1 = rotaClonada.listaVeiculos.get(j);
                
                
                //uma posição de cada veículo é selecionada
                //esta deve ser diferente do depósito, enquanto não for, outra posição é selecionada
                int pv1;
                
                do {
                        pv1 = rnd.nextInt(v1.ordemDeVisitacao.size());
                } while (v1.ordemDeVisitacao.get(pv1).getNumero() == 0);
                            
                
                int pv2;                               
                do {
                    pv2 = rnd.nextInt(v1.ordemDeVisitacao.size());
                } while (v1.ordemDeVisitacao.get(pv2).getNumero() == 0 || pv2 == pv1);  
                
                //são clonados dois clientes
                Cliente c1 = (Cliente)v1.ordemDeVisitacao.get(pv1).getClone();
                Cliente c2 = (Cliente)v1.ordemDeVisitacao.get(pv2).getClone();
                
                System.out.println(c1);
                System.out.println(c2);


                
                                
                //Collections.swap(rotaClonada., i, j);
                

                
                geracoes++;

            }

        }


    }// fecha a main
}// fecha a classe
