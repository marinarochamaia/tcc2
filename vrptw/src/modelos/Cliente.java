package modelos;

public class Cliente implements Cloneable {

	private double coordenadaX, coordenadaY, demanda, duracaoServico, inicioJanela, fimJanela;
	private int frequenciaVisita, possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas,
	numero;

	public Cliente(int numero, double coordenadaX, double coordenadaY, double demanda, double duracaoServico,
			int frequenciaVisita, int possiveisCombinacoesDeVisitas, int listaDeTodasPossiveisVisitas, double inicioJanela,
			double fimJanela) {
		this.numero = numero;
		this.coordenadaX = coordenadaX;
		this.coordenadaY = coordenadaY;
		this.demanda = demanda;
		this.duracaoServico = duracaoServico;
		this.frequenciaVisita = frequenciaVisita;
		this.possiveisCombinacoesDeVisitas = possiveisCombinacoesDeVisitas;
		this.listaDeTodasPossiveisVisitas = listaDeTodasPossiveisVisitas;
		this.inicioJanela = inicioJanela;
		this.fimJanela = fimJanela;
	}

	public double getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(double coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public double getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(double coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public double getDemanda() {
		return demanda;
	}

	public void setDemanda(double demanda) {
		this.demanda = demanda;
	}

	public double getDuracaoServico() {
		return duracaoServico;
	}

	public void setDuracaoServico(double duracaoServico) {
		this.duracaoServico = duracaoServico;
	}

	public int getFrequenciaVisita() {
		return frequenciaVisita;
	}

	public void setFrequenciaVisita(int frequenciaVisita) {
		this.frequenciaVisita = frequenciaVisita;
	}

	public int getPossiveisCombinacoesDeVisitas() {
		return possiveisCombinacoesDeVisitas;
	}

	public void setPossiveisCombinacoesDeVisitas(int possiveisCombinacoesDeVisitas) {
		this.possiveisCombinacoesDeVisitas = possiveisCombinacoesDeVisitas;
	}

	public int getListaDeTodasPossiveisVisitas() {
		return listaDeTodasPossiveisVisitas;
	}

	public void setListaDeTodasPossiveisVisitas(int listaDeTodasPossiveisVisitas) {
		this.listaDeTodasPossiveisVisitas = listaDeTodasPossiveisVisitas;
	}

	public double getInicioJanela() {
		return inicioJanela;
	}

	public void setInicioJanela(double inicioJanela) {
		this.inicioJanela = inicioJanela;
	}

	public double getFimJanela() {
		return fimJanela;
	}

	public void setFimJanela(double fimJanela) {
		this.fimJanela = fimJanela;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	//calcula a distância euclidiana entre o Cliente que chama o método e o cliente passado como parâmetro
	public double distanciaEuclidianaAte(Cliente outroCliente) {

		double soma = Math.pow(this.coordenadaX - outroCliente.getCoordenadaX(), 2)
				+ Math.pow(this.coordenadaY - outroCliente.getCoordenadaY(), 2);

		return Math.sqrt(soma);
	}

	//calcula a distância euclidiana entre duas instâncias
	public static double distanciaEuclidianaEntre(Cliente clienteA, Cliente clienteB) {

		return clienteA.distanciaEuclidianaAte(clienteB);
	}

	//função para criar e copiar o objeto cliente
	@Override
	public Object clone() throws CloneNotSupportedException {

		Cliente c = new Cliente(numero, coordenadaX, coordenadaY, demanda, 
				duracaoServico, frequenciaVisita, possiveisCombinacoesDeVisitas, 
				listaDeTodasPossiveisVisitas, inicioJanela, fimJanela);

		return c;
	}

	@Override
	public String toString() {

		return "(" + numero + ")" + " ";
	}
}