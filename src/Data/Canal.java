package Data;

import java.util.Date;

public class Canal {

	private String twitch = "";
	private String twitter = "";
	private Date ultimoTweet = new Date();

	public Canal() {
		this.twitch = "";
		this.twitter = "";
		this.ultimoTweet = new Date();
	}

	public Canal(String twitch, String twitter) {
		super();
		this.twitch = twitch;
		this.twitter = twitter;
		this.ultimoTweet = new Date();
	}

	public Canal(String twitch, String twitter, Date ultimoTweet) {
		super();
		this.twitch = twitch;
		this.twitter = twitter;
		this.ultimoTweet = ultimoTweet;
	}

	public String getTwitch() {
		return twitch;
	}

	public String getTwitter() {
		return twitter;
	}

	public Date getUltimoTweet() {
		return ultimoTweet;
	}

}
