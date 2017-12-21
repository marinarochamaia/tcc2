package modelos;

public class Cliente implements Cloneable{
	
	private float coordenadaX, coordenadaY, demanda, duracaoServico;
	private int frequenciaVisita, possiveisCombinacoesDeVisitas, listaDeTodasPossiveisVisitas, inicioJanela, fimJanela, numero;
	
	public Cliente(int numero, float coordenadaX, float coordenadaY, float demanda, float duracaoServico, int frequenciaVisita,
			int possiveisCombinacoesDeVisitas, int listaDeTodasPossiveisVisitas, int inicioJanela, int fimJanela)
	{
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
	
	public float getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(float coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public float getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(float coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public float getDemanda() {
		return demanda;
	}

	public void setDemanda(float demanda) {
		this.demanda = demanda;
	}

	public float getDuracaoServico() {
		return duracaoServico;
	}

	public void setDuracaoServico(float duracaoServico) {
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

	public int getInicioJanela() {
		return inicioJanela;
	}

	public void setInicioJanela(int inicioJanela) {
		this.inicioJanela = inicioJanela;
	}

	public int getFimJanela() {
		return fimJanela;
	}

	public void setFimJanela(int fimJanela) {
		this.fimJanela = fimJanela;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}


	/**
	 * Calcula a distância euclidiana entre o Cliente que chama o método e o cliente passado como parâmetro
	 * @param outroCliente A outra instância de cliente a qual se deseja calcular a distância
	 * @return A distância euclidiana entre os dois clientes
	 */
	public double distanciaEuclidianaAte(Cliente outroCliente)
	{
		double soma = Math.pow(this.coordenadaX + outroCliente.getCoordenadaX(), 2)
				      + Math.pow(this.coordenadaY + outroCliente.getCoordenadaY(), 2);
		return Math.sqrt(soma);
	}
	
	/**
	 * Calcula a distância euclidiana entre duas instâncias
	 * @param clienteA Primeira instância
	 * @param clienteB Segunda instância
	 * @return A distância euclidiana entre os dois clientes
	 */
	public static double distanciaEuclidianaEntre(Cliente clienteA, Cliente clienteB)
	{
		return clienteA.distanciaEuclidianaAte(clienteB);
	}

	@Override
	public String toString() {
		return "Cliente [numero=" + numero + " coordenadaX=" + coordenadaX + ", coordenadaY=" + coordenadaY + ", demanda=" + demanda
				+ ", duracaoServico=" + duracaoServico + ", frequenciaVisita=" + frequenciaVisita
				+ ", possiveisCombinacoesDeVisitas=" + possiveisCombinacoesDeVisitas + ", listaDeTodasPossiveisVisitas="
				+ listaDeTodasPossiveisVisitas + ", inicioJanela=" + inicioJanela + ", fimJanela=" + fimJanela
			    + "]\n";
	}
	
	
    // This method calls Object's clone().
    public Cliente getClone() {
        try {
            // call clone in Object.
            return (Cliente) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Clone não pode ser feito. " );
            return this;
        }
    }
}


