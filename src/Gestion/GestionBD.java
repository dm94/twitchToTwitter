package Gestion;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import Data.Canal;
import Data.Frase;

public class GestionBD {

	public void crearBd() {
		File f = new File("streamers.db");

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			Connection conexion = null;
			try {
				Class.forName("org.sqlite.JDBC");
				conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

				Statement sentencia = conexion.createStatement();
				sentencia.executeUpdate(
						"create table usuarios(twitch varchar(60),twitter varchar(60) ,ultimo_tweet varchar(30))");
				sentencia.executeUpdate(
						"CREATE TABLE 'frases' ('frase'	varchar(60),'tipo'	INTEGER,'juego'	varchar(60))");

				sentencia.close();

				System.out.println("BD Creada");
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				if (conexion != null) {
					try {
						conexion.close();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}

	public ArrayList<Canal> getCanales() {
		ArrayList<Canal> todosLosCanales = new ArrayList<Canal>(); // Lista con todos los canales
		Utilidades util = new Utilidades();

		Connection conexion = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

			Statement sentencia = conexion.createStatement();
			ResultSet resul = sentencia.executeQuery("select * from usuarios");

			while (resul.next()) {
				Canal canal = new Canal(resul.getString(1), resul.getString(2),
						util.pasarStringADate(resul.getString(3)));

				todosLosCanales.add(canal);
			}

			sentencia.close();

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return todosLosCanales;
	}

	public ArrayList<Frase> getFrases() {
		ArrayList<Frase> frases = new ArrayList<Frase>(); // Lista con todos los canales

		Connection conexion = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

			Statement sentencia = conexion.createStatement();
			ResultSet resul = sentencia.executeQuery("select * from frases");

			while (resul.next()) {
				String juego = resul.getString(3);

				if (juego != null && juego.length() > 0) {
					frases.add(new Frase(resul.getString(1), resul.getInt(2), juego));
				} else {
					frases.add(new Frase(resul.getString(1), resul.getInt(2)));
				}
			}

			sentencia.close();

		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return frases;
	}

	public boolean actualizarFecha(String twitch) {
		boolean seHaActualizado = false;
		Utilidades util = new Utilidades();

		Date fecha = new Date();

		Connection conexion = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

			Statement sentencia = conexion.createStatement();
			sentencia.executeUpdate("update usuarios set ultimo_tweet='" + util.pasarDateAString(fecha)
					+ "' where twitch='" + twitch + "'");

			seHaActualizado = true;

			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return seHaActualizado;
	}

	public boolean insertarUsuario(Canal canalTwitch) {
		boolean seHaActualizado = false;
		Utilidades util = new Utilidades();

		Connection conexion = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

			Statement sentencia = conexion.createStatement();
			int resultado = sentencia.executeUpdate("insert into usuarios values('" + canalTwitch.getTwitch() + "','"
					+ canalTwitch.getTwitter() + "','" + util.pasarDateAString(canalTwitch.getUltimoTweet()) + "')");

			if (resultado > 0) {
				seHaActualizado = true;
			}

			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return seHaActualizado;
	}

	public boolean borrarUsuario(String usuarioTwitch) {
		boolean seHaActualizado = false;
		Utilidades util = new Utilidades();

		Connection conexion = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conexion = DriverManager.getConnection("jdbc:sqlite:streamers.db");

			Statement sentencia = conexion.createStatement();
			int resultado = sentencia.executeUpdate("delete from usuarios where twitch='" + usuarioTwitch + "'");

			if (resultado > 0) {
				seHaActualizado = true;
			}

			sentencia.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return seHaActualizado;

	}
}
