package control;

public interface Observable extends TabelaCodigo {
	public void addObserver(Observer o, char tipoEvento);
	public void removeObserver(Observer o, char tipoEvento);
	public int 	get();
}
