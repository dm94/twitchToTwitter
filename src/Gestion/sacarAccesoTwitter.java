package Gestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class sacarAccesoTwitter {

	private String CONSUMER_KEY = "";
	private String CONSUMER_KEY_SECRET = "";
	private String datoAccessToken = "";
	private String datoAccessTokenSecret = "";

	public sacarAccesoTwitter() {
		leerDatos();

		if (datoAccessToken.length() < 1 || datoAccessTokenSecret.length() < 1) {
			conectarAplicacionConUsuario();
		}

		guardarDatos();
	}

	private void leerDatos() {
		String cad = null;
		int inicio = 0;
		File f = new File("config.txt");
		try {
			if (!(f.canRead())) {

				f.createNewFile();
				System.out.println("El archivo de configuración no existia se ha creado uno");
			}

			FileReader pr = new FileReader(f);
			BufferedReader br = new BufferedReader(pr);

			while ((cad = br.readLine()) != null) {
				if (cad.contains("accesstoken")) {
					inicio = cad.indexOf(":");
					datoAccessToken = cad.substring(inicio + 1).trim();
				} else if (cad.contains("tokensecret")) {
					inicio = cad.indexOf(":");
					datoAccessTokenSecret = cad.substring(inicio + 1).trim();
				}
			}

			br.close();
		} catch (IOException e) {
			System.out.println("Error en la lectura de los datos");
		}

	}

	private void guardarDatos() {
		File f = new File("config.txt");
		try {
			if (!(f.canRead())) {
				f.createNewFile();
				System.out.println("El archivo de configuración no existia se ha creado uno");
			}

			FileWriter fr = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fr);

			pw.println("accesstoken:" + datoAccessToken);
			pw.println("tokensecret:" + datoAccessTokenSecret);

			pw.close();
			fr.close();

		} catch (IOException e) {
			System.out.println("Error en la grabacion de los datos");
		}
	}

	public void conectarAplicacionConUsuario() {
		String testStatus = "Hola";

		ConfigurationBuilder cb = new ConfigurationBuilder();

		// the following is set without accesstoken- desktop client
		cb.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY)
				.setOAuthConsumerSecret(CONSUMER_KEY_SECRET);

		try {
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			try {
				System.out.println("-----");

				// get request token.
				// this will throw IllegalStateException if access token is
				// already available
				// this is oob, desktop client version
				RequestToken requestToken = twitter.getOAuthRequestToken();

				System.out.println("Got request token.");
				System.out.println("Request token: " + requestToken.getToken());
				System.out.println("Request token secret: " + requestToken.getTokenSecret());

				System.out.println("|-----");

				AccessToken accessToken = null;

				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				while (null == accessToken) {
					System.out.println("Open the following URL and grant access to your account:");
					System.out.println(requestToken.getAuthorizationURL());
					System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
					String pin = br.readLine();

					try {
						if (pin.length() > 0) {
							accessToken = twitter.getOAuthAccessToken(requestToken, pin);
						} else {
							accessToken = twitter.getOAuthAccessToken(requestToken);
						}
					} catch (TwitterException te) {
						if (401 == te.getStatusCode()) {
							System.out.println("Unable to get the access token.");
						} else {
							te.printStackTrace();
						}
					}
				}
				System.out.println("Got access token.");
				System.out.println("Access token: " + accessToken.getToken());
				System.out.println("Access token secret: " + accessToken.getTokenSecret());

			} catch (IllegalStateException ie) {
				// access token is already available, or consumer key/secret is
				// not set.
				if (!twitter.getAuthorization().isEnabled()) {
					System.out.println("OAuth consumer key/secret is not set.");
					System.exit(-1);
				}
			}

			Status status = twitter.updateStatus(testStatus);

			System.out.println("Successfully updated the status to [" + status.getText() + "].");

			System.out.println("ready exit");

			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Failed to read the system input.");
			System.exit(-1);
		}
	}

	public String getDatoAccessToken() {
		return datoAccessToken;
	}

	public String getDatoAccessTokenSecret() {
		return datoAccessTokenSecret;
	}

	public String getConsumerKey() {
		return CONSUMER_KEY;
	}

	public String getConsumerKeySecret() {
		return CONSUMER_KEY_SECRET;
	}

}
