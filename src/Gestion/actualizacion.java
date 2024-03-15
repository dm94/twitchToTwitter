package Gestion;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class actualizacion {

	private final static double version = 0.1;
	private final static String programa = "twitchtotwitter";

	public void comprobarActualizacion() {
		URL url;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			url = new URL("localhost/programs.xml");
			URLConnection connection = url.openConnection();

			Document documento = parseXML(connection.getInputStream());

			// Obtener el elemento raíz del documento
			Element raiz = documento.getDocumentElement();

			NodeList listabots = raiz.getElementsByTagName("bot");

			for (int i = 0; i < listabots.getLength(); i++) {
				boolean esEste = false;
				boolean actualizar = false;
				Node bot = listabots.item(i);
				NodeList datosbot = bot.getChildNodes();

				for (int j = 0; j < datosbot.getLength(); j++) {
					// Obtener de la lista de datos un dato tras otro
					Node dato = datosbot.item(j);

					// Comprobar que el dato se trata de un nodo de tipo Element
					if (dato.getNodeType() == Node.ELEMENT_NODE) {
						if (dato.getNodeName().equalsIgnoreCase("nombre")) {
							Node datoContenido = dato.getFirstChild();
							if (datoContenido != null && datoContenido.getNodeType() == Node.TEXT_NODE) {
								if (datoContenido.getNodeValue().equalsIgnoreCase(programa)) {
									esEste = true;
								}
							}
						}
						if (esEste) {
							if (dato.getNodeName().equalsIgnoreCase("version")) {
								Node datoContenido = dato.getFirstChild();
								if (datoContenido != null && datoContenido.getNodeType() == Node.TEXT_NODE) {
									if (Double.parseDouble(datoContenido.getNodeValue()) > version) {
										System.out.println(
												"Hay una actualización, quieres descargar la nueva versión? (s/n)");
										if (in.readLine().contains("s")) {
											actualizar = true;
										}
									}
								}
							}
						}
						if (esEste && actualizar) {
							if (dato.getNodeName().equalsIgnoreCase("descarga")) {
								Node datoContenido = dato.getFirstChild();
								if (datoContenido != null && datoContenido.getNodeType() == Node.TEXT_NODE) {
									Desktop enlace = Desktop.getDesktop();
									enlace.browse(new URI(datoContenido.getNodeValue()));
									System.exit(1);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Hay un fallo");
		}

	}

	private static Document parseXML(InputStream stream)
			throws Exception {
		DocumentBuilderFactory objDocumentBuilderFactory = null;
		DocumentBuilder objDocumentBuilder = null;
		Document doc = null;
		try {
			objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

			doc = objDocumentBuilder.parse(stream);
		} catch (Exception ex) {
			throw ex;
		}

		return doc;
	}

}
