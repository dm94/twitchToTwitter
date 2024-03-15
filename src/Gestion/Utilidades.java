package Gestion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Data.Canal;

public class Utilidades {

	private String clientId = "9tp7zk1yrrckqm86grbi6q18j8cizan";

	public Date pasarStringADate(String fecha) {
		Date date = null;

		if (fecha == null || fecha.length() < 2) {
			fecha = "2016-04-20 12:00:00";
		}

		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = formatoDelTexto.parse(fecha);
		} catch (ParseException ex) {
			System.out.println(ex.getMessage());
		}

		return date;
	}

	public String pasarDateAString(Date date) {
		String fecha = "";

		DateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fecha = formatoDelTexto.format(date);

		return fecha;
	}

	public String sacarJuego(String canalTwitch) {
		String juego = "";
		String usuarioTwitch = "";
		usuarioTwitch = sacarUsuarioTwitch(canalTwitch);
		URL url;
		BufferedReader reader;

		try {
			url = new URL(
					"https://api.rtainc.co/twitch/channels/" + usuarioTwitch.toLowerCase() + "/status?format=%5B1%5D");
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			juego = reader.readLine();

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return juego;
	}

	public String sacarImagen(String canalTwitch) {
		String urlImagen = "";
		String usuarioTwitch = "";
		usuarioTwitch = sacarUsuarioTwitch(canalTwitch);
		URL url;
		BufferedReader reader;

		try {
			url = new URL(
					"https://api.twitch.tv/kraken/streams/" + usuarioTwitch.toLowerCase() + "?client_id=" + clientId);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String cad = reader.readLine();
			int inicio = cad.indexOf("large") + 8;
			int fin = cad.indexOf("template") - 3;

			urlImagen = cad.substring(inicio, fin);

		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return urlImagen;
	}

	public String sacarUsuarioTwitch(String canalTwitch) {

		if (canalTwitch.contains("https://www.twitch.tv/")) {
			canalTwitch = canalTwitch.substring(canalTwitch.indexOf("https://www.twitch.tv/") + 22,
					canalTwitch.length());
		}

		return canalTwitch.trim();
	}

	public boolean estaOnlineFalse(String canalTwitch) {
		String usuarioTwitch = "";
		usuarioTwitch = sacarUsuarioTwitch(canalTwitch);
		URL url;
		BufferedReader reader;
		boolean estaOnline = false;

		try {
			url = new URL(
					"https://api.twitch.tv/kraken/streams/" + usuarioTwitch.toLowerCase() + "?client_id=" + clientId);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String cad = reader.readLine();
			if (cad.toLowerCase().contains("viewers")) {
				estaOnline = true;
			} else {
				estaOnline = false;
			}
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return estaOnline;
	}

	public Date fechaActualMas() {
		Date fecha = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.HOUR_OF_DAY, -12); // numero de d�as a a�adir, o restar en caso de d�as<0

		return calendar.getTime();
	}

	public ArrayList<String> sacarTodosLosCanales() {
		ArrayList<String> canalesTwitch = new ArrayList<String>();

		String url = "";

		for (int i = 1; i < 10; i++) {
			url = "https://www.twitch.tv/team/gzone/live_member_list?page=" + i;
			if (getStatusConnectionCode(url) == 200) {

				Document document = getHtmlDocument(url);
				Elements entradas = document.select("span[class=member_name]");

				for (Element elem : entradas) {
					String nick = elem.text();

					canalesTwitch.add(nick);
				}

			}
		}

		return canalesTwitch;
	}

	public int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepci�n al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	public Document getHtmlDocument(String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepci�n al obtener el HTML de la p�gina" + ex.getMessage());
		}
		return doc;
	}

	public boolean estaCanal(String canal, ArrayList<String> canalesDb) {
		boolean esta = false;

		for (int i = 0; i < canalesDb.size(); i++) {
			if (canalesDb.get(i).equalsIgnoreCase(canal)) {
				esta = true;
			}
			if (canal.contains(canalesDb.get(i))) {
				esta = true;
			}
		}

		return esta;
	}

}
