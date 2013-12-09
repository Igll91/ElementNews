package silvio.vuk.element.dao;

import java.util.List;

import silvio.vuk.element.news.News;

public interface NewsDAO {

	public abstract List<News> getNews(int limit);
	
	public abstract void insertSingleNews(News news);
	
	public abstract boolean checkIfNewsExists(News news);
	
	public abstract int numberOfNews();
}
