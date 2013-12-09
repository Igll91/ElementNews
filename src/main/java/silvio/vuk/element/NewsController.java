package silvio.vuk.element;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import silvio.vuk.element.dao.Index;
import silvio.vuk.element.news.News;

@Controller
public class NewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	private static final String NEWS = "Dohvati podatke Vijesti";
	private static final String SPORT = "Dohvati podatke Sport";
	private static final int NUMBER_OF_NEWS_PER_PAGE = 10;
	
	@Autowired
	@Qualifier("newsIndex")
	private Index index;
	
	/**
	 * Dohvaæa podatke ovisno o primljenom request parametru.
	 * 
	 * @param dataType parametar èija vrijednost odreðuje koji podaci æe se dohvaæati.
	 * @return jsp name
	 */
	@RequestMapping(value="/dohvati", method=RequestMethod.POST)
	public String receiveData(Locale locale, Model model, @RequestParam("data") String dataType)
	{
		try 
		{
			if(dataType.equals(NEWS))
				index.execute();
			else if(dataType.equals(SPORT))
				throw new IllegalArgumentException("U izradi...");
			else
				throw new IllegalArgumentException("No such dataType!");
		}
		catch (IllegalStateException e) 
		{
			model.addAttribute("error", "contact administrator - " + e.getMessage());
			return "home";
		}
		catch (IllegalArgumentException e) 
		{
			model.addAttribute("error", e.getMessage());
			return "home";
		}
		catch (IOException e) 
		{
			model.addAttribute("error", e.getMessage());
			logger.error(e.getMessage());
			return "home";
		}
		
		model.addAttribute("error", "podaci dohvaæeni!");
		return "home";
	}
	
	/**
	 * Dohvaæa odreðene vijesti iz baze, ovisno o broju trenutne stranice.
	 * 
	 * @param currPageNum broj trenutno odabrane stranice.
	 * @return ime jsp-a koji æe prikazati podatke
	 */
	@RequestMapping(value="/podaci", method=RequestMethod.POST, params="page")
	public String page(Locale locale, Model model, @RequestParam("page") int currPageNum)
	{
		int numberOfNews = index.numberOfNews();
		int page = numberOfNews / NUMBER_OF_NEWS_PER_PAGE;
		page++;
		
		List<News> listOfNews = index.getNews((currPageNum -1) * NUMBER_OF_NEWS_PER_PAGE);
		
		model.addAttribute("news", listOfNews);
		model.addAttribute("numberOfNews", page);
		
		return "data";
	}
	
	/**
	 * Poèetni prikaz podataka, uèitava odreðeni broj zadnjih vijesti {@link NewsController#NUMBER_OF_NEWS_PER_PAGE}.
	 * 
	 * @return ime jsp-a koji æe prikazati podatke
	 */
	@RequestMapping(value="/podaci", method=RequestMethod.POST)
	public String showData(Locale locale, Model model)
	{
		int numberOfNews = index.numberOfNews();
		int page = numberOfNews / NUMBER_OF_NEWS_PER_PAGE + 1;
		
		List<News> listOfNews = index.getNews(0);
		
		model.addAttribute("news", listOfNews);
		model.addAttribute("numberOfNews", page);
		
		return "data";
	}
	
}
