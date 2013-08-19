package com.hexj.tools.htmlparse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parsing {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String baseurl = "http://dsnexus.uk.hibm.hexj:8082/nexus";
		String starturl = "http://dsnexus.uk.hibm.hexj:8082/nexus/content/repositories/R2DS-QA-Test/";

		if (starturl.contains(baseurl)) {
			NexusDown parsing = new NexusDown(baseurl, starturl);
			System.out.println("Start download file..." + new Date());
			parsing.setCounter(new AtomicInteger(0));

			parsing.firstlevel();

			int num = parsing.getCounter().get();
			System.out.println("Downloaded files :" + num);
		}
	}

}

class NexusDown implements Runnable {
	private String baseurl, starturl;
	private String localpath = "c:/hexj/download";
	private String downfilesType[] = { "jar", "war", "zip", "ear", "xml",
			"pom", "asc", "md5", "sha1" };
	private String excludes[] = { "r2ds-group" };
	private String includes[] = { "hexj", "esf" };

	private static AtomicInteger counter;

	public NexusDown() {
		super();
	}

	public NexusDown(String pbaseURL, String pStarturl) {
		baseurl = pbaseURL;
		this.starturl = pStarturl;
	}

	public synchronized void run() {
		System.out.println(starturl + " run ...... start");
		handleIndexPage(this.starturl);
		System.out
				.println(starturl + " run ...... end, files:" + counter.get());
	}

	public void firstlevel() {
		String startURL = starturl;
		String fileName = getURLFileName(startURL);
		String ext = "";
		if (fileName.lastIndexOf(".") > 0) {
			ext = fileName.substring(fileName.lastIndexOf('.') + 1)
					.toLowerCase();
		}
		if (StringUtils.isNotBlank(ext)) {
			if (ArrayUtils.contains(this.downfilesType, ext)) {
				String filePath = localpath + startURL.replace(baseurl, "");
				downloadFile(startURL, filePath);
			}
		} else {
			Document doc = getDocByURL(startURL);
			if (doc != null) {
				Elements links = doc.select("a[href]");
				ExecutorService es = Executors.newCachedThreadPool();
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					if ("../".equals(linkHref)
							|| "Parent Directory".equals(linkText)) {
						continue;
					}

					if (!linkHref.startsWith("http")) {
						linkHref = baseurl + linkHref;
					}

					NexusDown firsLevelThread = new NexusDown(baseurl, linkHref);
					// String threadName = linkText.replace("/", "");
					// System.out.println(threadName + " start ......");

					es.execute(firsLevelThread);

				}// for
				es.shutdown();
			}// if
		}// else

	}

	private void handleIndexPage(String startURL) {
		String fileName = getURLFileName(startURL);
		String ext = "";
		if (fileName.lastIndexOf(".") > 0) {
			ext = fileName.substring(fileName.lastIndexOf('.') + 1)
					.toLowerCase();
		}
		if (StringUtils.isNotBlank(ext)) {
			if (ArrayUtils.contains(this.downfilesType, ext)) {
				String filePath = localpath + startURL.replace(baseurl, "");

				if (haveKeys(startURL, includes)) {
					System.out.println(startURL);
					downloadFile(startURL, filePath);
				}
			}
		} else {
			// System.out.println("continue link ----> " + startURL);
			Document doc = getDocByURL(startURL);
			if (doc != null) {
				Elements links = doc.select("a[href]");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkText = link.text();
					if ("../".equals(linkHref)
							|| "Parent Directory".equals(linkText)) {
						continue;
					}

					if (!linkHref.startsWith("http")) {
						linkHref = baseurl + linkHref;
					}
					// System.out.println(linkText + " = " + linkHref); //
					if (!haveKeys(linkHref, excludes)) {
						handleIndexPage(linkHref);
					}
				} // for
			}// if
		}// else

	}

	private boolean haveKeys(String linkHref, String[] arr) {
		String lowlink = linkHref.replace(baseurl, "").toLowerCase();
		boolean haveKey = false;
		for (int i = 0; i < arr.length; i++) {
			if (lowlink.contains(arr[i].toLowerCase())) {
				haveKey = true;
				break;
			}
		}
		return haveKey;
	}

	public void downloadFile(String url, String dir) {
		counter.incrementAndGet();
		// System.out.println(url + " to path :" + dir);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams
				.setUserAgent(
						httpClient.getParams(),
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9");
		InputStream input = null;
		FileOutputStream output = null;
		try {
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(new java.net.URI(url));
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			File file = new File(dir);
			output = FileUtils.openOutputStream(file);
			IOUtils.copy(input, output);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}
	}

	private String getURLFileName(String url) {
		String filename = "";
		filename = url.substring(url.lastIndexOf('/') + 1);
		return filename;
	}

	public Document getDocByURL(String url) {
		Document doc = null;
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9")
					.referrer("http://home.global.hexj/").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	// private void enterURL(String enter) {
	// // enter =
	// // "http://dsnexus.uk.hibm.hexj:8082/nexus/service/local/repositories";
	// try {
	// Document doc = Jsoup.connect(enter).get();
	// Elements links = doc.select("repositories-item");
	// for (Element e : links) {
	// final String contentresourceuri = e
	// .select("contentresourceuri").first().text();
	// final String id = e.select("id").first().text();
	// System.out.println(id + "-------" + contentresourceuri);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public AtomicInteger getCounter() {
		return counter;
	}

	public void setCounter(AtomicInteger counter) {
		NexusDown.counter = counter;
	}
}
