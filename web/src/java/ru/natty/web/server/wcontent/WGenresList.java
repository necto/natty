/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.List;
import ru.natty.persist.Genre;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WGenresList extends WContent
{
	private WVerticalPanel data;

	public WGenresList (List<Genre> genres)
	{
		data = new WVerticalPanel();
		
		int i = 0;
		for (Genre g : genres)
			data.addItem(100500 + i/*TODO: !!! completely wrong*/, i++, new WLabel(g.getName()));
	}

	@Override
	public DiffPatcher getDifferenceInt(WContent prev, boolean amputation)
	{
		//TODO: assert prev to be instance of WGenresList
		return data.getDifference (((WGenresList)prev).data, amputation);
	}

	@Override
	public DiffPatcher getAllContentInt() {
		return data.getAllContent();
	}

	@Override
	public WContent copyInt() {
		return data.copy();
	}

	@Override
	public boolean isAggregating() {
		return false;//!!! TODO: or true?
	}

	public static WGenresList make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
        return new WGenresList (db.queryGenreByPattern(ps.getQuery()));
	}
}
