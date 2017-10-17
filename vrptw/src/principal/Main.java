package principal;


import java.util.*;


import io.Conversor;
import modelos.Cliente;
import modelos.Veiculo;

public class Main {

	public static void main(String[] args) {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		ArrayList<Veiculo> veiculos = new ArrayList<Veiculo>();
		int numeroDeRotas = 2;
//		int multa = 1000;
//		double [] custoTotalRota = new double [numeroDeRotas];


		// args[0] � o primeiro par�metro do programa, que � o nome do arquivo que ser� lido
		Conversor conversor = new Conversor(args[0]);
		conversor.converterArquivo(clientes, veiculos);

		//matriz de dist�ncias
		int numeroClientes = clientes.size();
		double [][] matrizDistancias = new double [numeroClientes][numeroClientes];
		
		//matriz de custos das rotas
		int numeroVeiculos = veiculos.size();
//		double [][] custoRota = new double [numeroDeRotas][numeroVeiculos];

		
		//percorre as linhas da matrizDistancias preenche com os valores das dist�ncias calculadas atrav�s da dist�ncia euclidiana
		for(int row = 0; row < numeroClientes; row++){		
			//percorre as colunas da matrizResultado
			for(int column = 0; column < numeroClientes; column++){
				if(row == column)
					matrizDistancias[row][column] = 0;
				else
					matrizDistancias[row][column] = Cliente.distanciaEuclidianaEntre(clientes.get(row), clientes.get(column));	
			}
		}		


		
		//cria uma matriz onde as rotas aleat�rias ser�o salvas
		ArrayList<ArrayList<Cliente>> rotas = new ArrayList<ArrayList<Cliente>>();

		//cria as rotas aleat�rias sempre partindo do zero(dep�sito)
		for(int i = 0; i < numeroDeRotas; i++){
			
			//cria  um arraylist para salvar a rota inicial partindo de zero(dep�sito) at� o maximo de clientes
			ArrayList<Cliente> sequenciaDeVisitas = new ArrayList<Cliente>();
			
			//cria  uma rota partindo de zero(dep�sito) at� o maximo de clientes
			for(Cliente auxiliar: clientes)
				sequenciaDeVisitas.add(auxiliar);
			
			Cliente deposito = sequenciaDeVisitas.remove(0);
			
			Collections.shuffle(sequenciaDeVisitas);
			
			sequenciaDeVisitas.add(0, deposito);
			sequenciaDeVisitas.add(deposito);
			rotas.add(sequenciaDeVisitas);
		}

		//percorre as linhas da matriz
		for(int row = 0; row<numeroDeRotas;row++){
			ArrayList<Cliente> rotaAtual = rotas.get(row);
			int contadorDeCliente = 0;
			int demandaTotal = 0;
			int demandaAtendida = 0;
			int veiculosUtilizados = 0;

			
			//soma a demanda total de cada rota
			for(int j = 0; j < numeroClientes; j++)
				demandaTotal += rotaAtual.get(j).getDemanda();
			
			
			//percorre os ve�culos dispon�veis
			for(int i = 0; i < numeroVeiculos; i++) {
				
				Veiculo veiculo = veiculos.get(i);
				ArrayList<Cliente> possivelRotaVeiculo = new ArrayList<Cliente>();
				veiculo.resetCargaOcupada();
				int aux = contadorDeCliente;
				
				//percorre as colunas de cada linha
				for(int column = aux; column < rotaAtual.size(); column++) {
					
					Cliente clienteAtual = rotaAtual.get(column);
//					int tamanho;
//					if(column == 0)
//						tamanho = 0;
//					else
//						tamanho = column-1;
//					Cliente clienteAnterior = rotaAtual.get(tamanho);
//					
//					
					
					//se a demanda do cliente que est� sendo analisado mais a carga do ve�culo que j� est� ocupada for menor
					//que a capacidade m�xima do ve�culo este � inclu�do a rota deste ve�culo
					if(veiculo.getCargaOcupada() + clienteAtual.getDemanda() <= veiculo.getCargaMaxima()){
						possivelRotaVeiculo.add(clienteAtual);
						veiculo.setCargaOcupada(clienteAtual.getDemanda());
						demandaAtendida += clienteAtual.getDemanda();
						contadorDeCliente++;
						
//						//se o ve�culo est� no d�posito n�o soma nada
//						if(clienteAtual.getNumero() == 0)
//							veiculo.custosVeiculo[i][column] += clienteAtual.getDuracaoServico();
//						//se o ve�culo chega antes de abrir a janela, soma-se a diferen�a do tempo que chegou com a abertura da janela mais a multa
//						else if((matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()] + clienteAnterior.getDuracaoServico())
//								< clienteAtual.getInicioJanela()){
//							veiculo.custosVeiculo[i][column] += (matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()]
//									+ clienteAnterior.getDuracaoServico() + multa + (clienteAtual.getInicioJanela() 
//									- matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()]));
//						//se o ve�culo chega depois da janela atende-se o cliente, mas adiciona a multa
//						}else if ((matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()] + clienteAnterior.getDuracaoServico())
//										>= clienteAtual.getFimJanela()){
//							veiculo.custosVeiculo[i][column] += ((matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()]
//										+ clienteAnterior.getDuracaoServico() + multa));
//						//se chega dentro da janela o cliente � atendido
//						}else if ((matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()] + clienteAnterior.getDuracaoServico())
//										>= clienteAtual.getInicioJanela() && (matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()] 
//										+ clienteAnterior.getDuracaoServico()) >= clienteAtual.getFimJanela()){
//							veiculo.custosVeiculo[i][column] += ((matrizDistancias[clienteAtual.getNumero()][clienteAnterior.getNumero()]
//								+ clienteAnterior.getDuracaoServico()));
//						}
					}
					//se n�o, � feito um break e inicia a rota do pr�ximo ve�culo
					else break;
				}//fecha o terceiro for
				if(veiculo.getCargaOcupada()!=0){
					//a rota de cada ve�culo � inclu�da na matriz de cada ve�culo
					veiculo.rotasVeiculo.add(possivelRotaVeiculo);
//					System.out.println( " Carga ocupada: " + veiculo.getCargaOcupada());
					veiculosUtilizados++;
				}else break;
//				System.out.println("Custo Rota: " + custoTotalRota[row]);
			}//fecha o segundo for
			
//			if(contadorDeCliente < rotaAtual.size()){
//				System.out.println("Sobrou clientes nessa rota");
//			}

			
			if(demandaAtendida < demandaTotal){
				System.out.println("Sobrou clientes nessa rota");
			}
			else 
				System.out.println("Rota vi�vel");
			System.out.println("Demanda Atendida: " + demandaAtendida + "\nDemanda Total: " + demandaTotal + "\nVe�culos utilizados: " + veiculosUtilizados);
			
		}//fecha o primeiro for
		
		
		
	}//fecha a main
}//fecha a classe