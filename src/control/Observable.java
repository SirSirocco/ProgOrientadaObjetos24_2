package control;

public interface Observable {
	public void addObserver(Observer o, char tipoEvento);
	public void removeObserver(Observer o, char tipoEvento);
	public Object get();
}
