package silvio.vuk.element.news;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;


/**
 * Entitet koji sadrži podatke vezane uz vijest.
 * 
 * @author Silvio Vuk
 *
 */
public class News {

	private int id;
	private String title;
	private String author;
	private String text;

	public News() {
	}

	public News(String title, String author, String text) {
		super();
		this.title = title;
		this.author = author;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Metoda jedinstvena za preuzimanje vijesti sa www.index.hr web stranice. Vrijedi za sve kategorije.
	 * 
	 * Sa adrese na kojoj se nalazi vijest izvlaèe se sljedeæi podaci:
	 * 
	 * naslov -> Elements el = doc.select("h1");
	 * 
	 * autor -> Elements author = el.select("div.writer > strong");
	 * 
	 * tekst vijesti -> Elements articleText = el.select("div#article_text > p");
	 * 
	 * za dodatno filtriranje nepotrebnog sadržaja vijesti -> s.select("img, span, iframe").remove();
	 * 
	 * @param URL adresa pojedine vijesti nad kojom æe se vršiti parsiranje podataka.
	 * @return objekt Vijest kreiran iz preuzetih podataka.
	 * @throws IOException 
	 */
	public static News getNewsData(String URL) throws IOException {
		News news = new News();

		Document doc = Jsoup.connect(URL).get();
		Elements el = doc.select("h1");
		String heading = "";

		for (Element e : el) {
			heading += e.html();
		}

		news.setTitle(heading);

		el = doc.select("div.columnleft");
		Elements author = el.select("div.writer > strong");
		news.setAuthor(author.first().html());

		Elements articleText = el.select("div#article_text > p");

		String text = "";
		for (Element s : articleText) {
			s.select("img, span, iframe").remove();
			text += s.html();
		}
		news.setText(text);
		
		return news;
	}
}
