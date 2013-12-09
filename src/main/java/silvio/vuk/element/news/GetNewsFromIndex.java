package silvio.vuk.element.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import silvio.vuk.element.dao.Index;
import silvio.vuk.element.dao.NewsDAO;

@Component("newsIndex")
public class GetNewsFromIndex implements Index{

	private final String INDEX_URL = "http://www.index.hr";
	private final String INDEX_NEWS_BOX_QUERY = "div.fpnewsbox";
	private static final Logger logger = LoggerFactory.getLogger(GetNewsFromIndex.class);
	
	@Autowired
	@Qualifier("newsDAO")
	private NewsDAO newsDAO;

	public void setNewsDAO(NewsDAO newsDAO) {
		this.newsDAO = newsDAO;
	}

	public GetNewsFromIndex()
	{
	}
	
//	@Async
	/**
	 * Dohvaæa listu linkova najnovijih vijesti, ta zatim svaki link otvara i dodatno parsira. Podaci se na kraju zapisuju u bazu i logiraju.
	 */
	public void execute() throws IOException, IllegalStateException
	{
		Set<String> setOfLinks = getCurrentNewsLinks();

		List<News> listOfNews = new ArrayList<News>();
		
		for(String currentLink: setOfLinks)
		{
			listOfNews.add(News.getNewsData(INDEX_URL + currentLink));
		}
		
		for(News news: listOfNews)
		{
			boolean exists = newsDAO.checkIfNewsExists(news);
			
			if(exists == false)
			{
				logger.info("Naslov: {} ", news.getTitle());
				logger.info("Autor:  {} ", news.getAuthor());
				logger.info(news.getText());
				
				logger.info("*"); 
				logger.info("*");
				
				newsDAO.insertSingleNews(news);
			}
		}
	}

	/**
	 * Dohvaæa listu linkova najnovijih vijesti.
	 * 
	 * S glavne stranice www.index.hr-a parsira linkove od najnovijih vijesti.
	 * 
	 * <p>
	 * Prvo se selektira div s podacima, zatim se iz njega izvuku svi linkovi sa sadržajem /vijesti/...
	 * Zbog duplih vrijednosti, Stringovi se ubacuju u HashSet.
	 * </p>
	 * 
	 * @return popis linkova najnovijih vijesti.
 	 * @throws IOException
	 */
	private Set<String> getCurrentNewsLinks() throws IOException
	{
		Set<String> setOfLinks = new HashSet<String>();

		Document doc = Jsoup.connect(INDEX_URL).get();

		Elements news = doc.select(INDEX_NEWS_BOX_QUERY);

		Elements linkNewsElements = news.select("a[href~=/vijesti/[0-9a-zA-Z]{1,}]");

		for(Element el: linkNewsElements)
		{
			setOfLinks.add(el.attr("href"));
		}

		return setOfLinks;
	}

	@Override
	public List<News> getNews(int limit) {
		return newsDAO.getNews(limit);
	}

	@Override
	public int numberOfNews() {
		return newsDAO.numberOfNews();
	}

}
