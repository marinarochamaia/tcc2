package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class Mutacao {

    public void fazMutacao(Rota rotaClonada, double cMutacao, double[][] matrizDeDistancias, int multa, Cliente deposito) {

        Random rnd = new Random();
        int k;

        //o giant tour é percorrido
        for (int j = 0; j < rotaClonada.listaClientes.size(); j++) {

            double m = rnd.nextDouble();

            //a mutação só é feita se o fator de mutação for atendido
            if (m <= cMutacao) {

                do {
                    k = rnd.nextInt(rotaClonada.listaClientes.size());
                } while (j == k);

                //se os clientes visitados forem o depósito ou iguais a mutação não é feita
                if (rotaClonada.listaClientes.get(j).getNumero() == 0 || rotaClonada.listaClientes.get(k).getNumero() == 0) {
                    continue;
                }

                Collections.swap(rotaClonada.listaClientes, j, k);

            }
        }

        //o giant tour é subdividido em ordens de visitação de cada veículo
        rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
                matrizDeDistancias, multa);

    }
}
