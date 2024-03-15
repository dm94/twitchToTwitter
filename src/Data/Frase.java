package Data;

public class Frase {

	private int tipo = 0;
	private String frase = "";
	private String juego = "";

	/*
	 * Tipos:
	 * 1� : Canal
	 * 2� : Mencion y Canal
	 * 3� : Mencion, juego y canal
	 * 4� : Juego, mencion y canal
	 * 5� : Mencion y Canal - Juego
	 */

	public Frase(String frase, int tipo) {
		this.tipo = tipo;
		this.frase = frase;
	}

	public Frase(String frase, int tipo, String juego) {
		this.tipo = tipo;
		this.frase = frase;
		this.juego = juego;
	}

	public int getTipo() {
		return tipo;
	}

	public String getFrase() {
		return frase;
	}

	public String getJuego() {
		return juego;
	}
}
