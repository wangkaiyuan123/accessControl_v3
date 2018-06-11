package com.weixin.pool;

import java.util.List;
/**
 * 回复图文消息类
 * @author Administrator
 *
 */
public class NewsMessage extends BaseMessage{
	private int ArticleCount;
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
}
