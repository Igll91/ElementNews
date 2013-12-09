package silvio.vuk.element.dao;

import java.io.IOException;
import java.util.List;

import silvio.vuk.element.news.News;

public interface Index {

	public abstract void execute() throws IOException, IllegalStateException;
	
	public abstract List<News> getNews(int limit);
	
	public abstract int numberOfNews();
}
