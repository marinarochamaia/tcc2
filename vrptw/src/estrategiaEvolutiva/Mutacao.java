package estrategiaEvolutiva;

import java.util.Collections;
import java.util.Random;

import modelos.Cliente;
import modelos.Rota;

public class Mutacao {

    public void fazMutacao(Rota rotaClonada, double cMutacao, double[][] matrizDeDistancias, int multa, Cliente deposito) {

        Random rnd = new Random();
        int k;

        //o giant tour � percorrido
        for (int j = 0; j < rotaClonada.listaClientes.size(); j++) {

            double m = rnd.nextDouble();

            //a muta��o s� � feita se o fator de muta��o for atendido
            if (m <= cMutacao) {

                do {
                    k = rnd.nextInt(rotaClonada.listaClientes.size());
                } while (j == k);

                //se os clientes visitados forem o dep�sito ou iguais a muta��o n�o � feita
                if (rotaClonada.listaClientes.get(j).getNumero() == 0 || rotaClonada.listaClientes.get(k).getNumero() == 0) {
                    continue;
                }

                Collections.swap(rotaClonada.listaClientes, j, k);

            }
        }

        //o giant tour � subdividido em ordens de visita��o de cada ve�culo
        rotaClonada.criaOrdemDeVisitacao(rotaClonada.listaVeiculos.size(), rotaClonada.listaVeiculos, rotaClonada.listaClientes, deposito,
                matrizDeDistancias, multa);

    }
}
