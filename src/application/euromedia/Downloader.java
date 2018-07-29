package application.euromedia;

import java.util.HashMap;

import org.jsoup.nodes.Element;

public abstract class Downloader {
	protected Element element;
	protected HashMap<String, String> cookies;
	protected String USER_AGENT, loginFormUrl, tempPath;

	public Downloader(Element element, String USER_AGENT, String loginFormUrl, String tempPath,
			HashMap<String, String> cookies) {
		this.element = element;
		this.USER_AGENT = USER_AGENT;
		this.loginFormUrl = loginFormUrl;
		this.tempPath = tempPath;
		this.cookies = cookies;
	}
}
