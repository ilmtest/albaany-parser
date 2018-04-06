package com.ilmtest.albaany.processors;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ilmtest.lib.io.DBUtils;
import com.ilmtest.lib.io.NetworkBoundary;
import com.ilmtest.util.text.TextUtils;

public class SilsilahHudaNoorProcessor
{
	private ArrayList<Article> m_articles = new ArrayList<>();

	public SilsilahHudaNoorProcessor()
	{
	}

	public void process(int articleId) throws MalformedURLException, IOException
	{
		String body = NetworkBoundary.getHTML("http://alalbany.net/print.php?id="+articleId, "Windows-1256");
		int lastDiv = body.lastIndexOf("<div align='center'>");
		body = body.substring( body.indexOf("<div align"), body.lastIndexOf("<div align='center'>", lastDiv-1) );
		body = TextUtils.htmlToPlainText( Jsoup.parse(body) );

		Document doc = Jsoup.parse( new URL("http://alalbany.net/play.php?catsmktba="+articleId), 10000 );
		String mp3 = doc.select("a[href$=mp3]").first().attr("href");

		Article e = new Article();
		e.body = body;

		String[] all = mp3.split("[/.-]");
		String tapeField = all[all.length-3];
		int tape = Integer.parseInt( tapeField.replaceAll("[^\\d.]", "") );
		int segment = Integer.parseInt( all[all.length-2] );
		e.tape = tape;
		e.inTapeSegment = segment;
		e.pageNumber = articleId;

		m_articles.add(e);
	}


	public void write(Connection c) throws SQLException
	{
		c.setAutoCommit(false);

		List<String> columns = DBUtils.createNullColumns( DBUtils.createNotNullColumns("page_number INTEGER", "arabic_plain TEXT", "arabic_vowelled TEXT"), "part_number INTEGER", "part_page INTEGER" );
		DBUtils.createTable(c, "entries", columns);

		List<String> popFields = new ArrayList<>();

		for (String column: columns) {
			popFields.add( column.split(" ")[0] );
		}

		PreparedStatement ps = DBUtils.createInsert(c, "entries", popFields);

		for (final Article n: m_articles)
		{
			int i = 0;

			ps.setInt(++i, n.pageNumber);
			ps.setString(++i, TextUtils.normalize(n.body.trim()));
			ps.setString(++i, n.body.trim());
			DBUtils.setNullInt(++i, n.tape, ps);
			DBUtils.setNullInt(++i, n.inTapeSegment, ps);
			ps.execute();
		}

		DBUtils.execStatement(c, "CREATE INDEX IF NOT EXISTS lookups ON entries (part_number,part_page)");

		c.commit();
		ps.close();
	}


	/**
	 * @return the entries
	 */
	ArrayList<Article> getArticles() {
		return m_articles;
	}
}