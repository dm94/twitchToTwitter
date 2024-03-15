package Gestion;

import Data.Canal;
import Data.Frase;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import twitter4j.TwitterException;

public class Principal {
	private static PostWithImage pwi;
	private static GestionBD gbd;
	private ArrayList<Canal> todosLosCanales;
	private ArrayList<Canal> canalesValidos;
	private long unMinuto = 60000L;
	private Random rnd;
	private Timer timer = new Timer();
	private Utilidades util;
	private ArrayList<Frase> frases = new ArrayList();

	public static void main(String[] args) {
		actualizacion actu = new actualizacion();
		actu.comprobarActualizacion();

		sacarAccesoTwitter act = new sacarAccesoTwitter();

		pwi = new PostWithImage(act);
		gbd = new GestionBD();

		Principal p = new Principal();
		p.iniciarBot();
	}

	private void iniciarBot() {
		this.util = new Utilidades();
		this.rnd = new Random();
		this.todosLosCanales = new ArrayList();
		this.canalesValidos = new ArrayList();

		gbd.crearBd();

		System.out.println("Bot funcionando");
		this.timer.scheduleAtFixedRate(this.timerTask, 0L, this.unMinuto * 180L);
	}

	private void publicarTweet() {
		if (this.canalesValidos.size() > 0) {
			int numCanales = this.canalesValidos.size();
			boolean mandado = false;
			Canal canal = (Canal) this.canalesValidos.get(this.rnd.nextInt(numCanales));

			int numFrases = this.frases.size();
			do {
				Frase frase = (Frase) this.frases.get(this.rnd.nextInt(numFrases));
				try {
					System.out.println();
					System.out.println(new Date().toString());
					if (frase.getTipo() == 1) {
						pwi.mandarTweet(String.format(frase.getFrase(), new Object[] { canal.getTwitch() }),
								this.util.sacarImagen(canal.getTwitch()));
						System.out.printf(frase.getFrase(),
								new Object[] { canal.getTwitch(), this.util.sacarImagen(canal.getTwitch()) });
						mandado = true;
					} else if (frase.getTipo() == 2) {
						pwi.mandarTweet(
								String.format(frase.getFrase(), new Object[] { canal.getTwitter(), canal.getTwitch() }),
								this.util.sacarImagen(canal.getTwitch()));
						System.out.printf(frase.getFrase(),
								new Object[] { canal.getTwitter(), canal.getTwitch(),
										this.util.sacarImagen(canal.getTwitch()) });
						mandado = true;
					} else if (frase.getTipo() == 3) {
						pwi.mandarTweet(
								String.format(frase.getFrase(),
										new Object[] { canal.getTwitter(), this.util.sacarJuego(canal.getTwitch()),
												canal.getTwitch() }),
								this.util.sacarImagen(canal.getTwitch()));
						System.out.printf(frase.getFrase(), new Object[] { canal.getTwitter(),
								this.util.sacarJuego(canal.getTwitch()), canal.getTwitch(),
								this.util.sacarImagen(canal.getTwitch()) });
						mandado = true;
					} else if (frase.getTipo() == 4) {
						pwi.mandarTweet(
								String.format(frase.getFrase(),
										new Object[] { this.util.sacarJuego(canal.getTwitch()), canal.getTwitter(),
												canal.getTwitch() }),
								this.util.sacarImagen(canal.getTwitch()));
						System.out.printf(frase.getFrase(), new Object[] { this.util.sacarJuego(canal.getTwitch()),
								canal.getTwitter(), canal.getTwitch(), this.util.sacarImagen(canal.getTwitch()) });
						mandado = true;
					} else if (frase.getTipo() == 5) {
						if (frase.getJuego().toLowerCase()
								.contains(this.util.sacarJuego(canal.getTwitch().toLowerCase()).toLowerCase())) {
							pwi.mandarTweet(
									String.format(frase.getFrase(),
											new Object[] { canal.getTwitter(), canal.getTwitch() }),
									this.util.sacarImagen(canal.getTwitch()));
							System.out.printf(frase.getFrase(),
									new Object[] { canal.getTwitter(), canal.getTwitch(),
											this.util.sacarImagen(canal.getTwitch()) });
							mandado = true;
						}
					} else {
						System.out.println("Esa frase tiene un tipo incorrecto");
					}
					gbd.actualizarFecha(canal.getTwitch());
				} catch (TwitterException e) {
					System.out.println(e.getMessage());
				}
			} while (!mandado);
		}
	}

	private void filtrarCanales() {
		this.canalesValidos.clear();
		for (int i = 0; i < this.todosLosCanales.size(); i++) {
			if ((this.util.estaOnlineFalse(((Canal) this.todosLosCanales.get(i)).getTwitch())) &&
					(((Canal) this.todosLosCanales.get(i)).getUltimoTweet().before(this.util.fechaActualMas()))) {
				this.canalesValidos.add((Canal) this.todosLosCanales.get(i));
			}
		}
	}

	private void actualizarCanales() {
		this.todosLosCanales.clear();
		this.canalesValidos.clear();

		this.todosLosCanales = gbd.getCanales();
		this.frases = gbd.getFrases();

		filtrarCanales();
	}

	private void filtrarMiembrosTwitch() {
		ArrayList<String> canalesTwitch = this.util.sacarTodosLosCanales();
		ArrayList<Canal> canalesDb = gbd.getCanales();
		for (int i = 0; i < canalesDb.size(); i++) {
			if (!this.util.estaCanal(this.util.sacarUsuarioTwitch(((Canal) canalesDb.get(i)).getTwitch()),
					canalesTwitch)) {
				try {
					Thread.sleep(60000L);
				} catch (InterruptedException localInterruptedException) {
				}
				if (!this.util.estaCanal(this.util.sacarUsuarioTwitch(((Canal) canalesDb.get(i)).getTwitch()),
						this.util.sacarTodosLosCanales())) {
					gbd.borrarUsuario(((Canal) canalesDb.get(i)).getTwitch());
				}
			}
		}
	}

	public TimerTask timerTask = new TimerTask() {
		public void run() {
			Principal.this.filtrarMiembrosTwitch();
			Principal.this.actualizarCanales();
			Principal.this.publicarTweet();
		}
	};
}
