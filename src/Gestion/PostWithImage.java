package Gestion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public final class PostWithImage {

	private String consumerKey = "";
	private String consumerSecret = "";
	private String UserAccessToken = "";
	private String UserAccessSecret = "";
	private sacarAccesoTwitter act;

	public PostWithImage(sacarAccesoTwitter act) {
		this.act = act;
		consumerKey = act.getConsumerKey();
		consumerSecret = act.getConsumerKeySecret();
		UserAccessToken = act.getDatoAccessToken();
		UserAccessSecret = act.getDatoAccessTokenSecret();
	}

	public void mandarTweet(String mensaje, String urlImg) throws TwitterException {

		try {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthAccessToken(UserAccessToken);
			builder.setOAuthAccessTokenSecret(UserAccessSecret);
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			TwitterFactory tf = new TwitterFactory(builder.build());
			Twitter twitter = tf.getInstance();

			StatusUpdate status = new StatusUpdate(mensaje);
			URL url = new URL(urlImg);
			BufferedImage image = ImageIO.read(url);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "png", os);
			InputStream fis = new ByteArrayInputStream(os.toByteArray());

			status.setMedia("image.jpg", fis); // Para que lo coja desde la url
			twitter.updateStatus(status);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}