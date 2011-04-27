/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.diffpatchers.TextCellListDP;

/**
 *
 * @author necto
 */
public class WTextCellList extends WContent
{
	static private final Integer MAXIMUM_ITEMS = 20;

	public static interface Translator<T>
	{
		List<IText> translate (List <? extends T> list);
	}

	String name;
	Map<Integer, IText> items;
	Integer selected = null;

	public WTextCellList (String name)
	{
		this.name = name;
		this.items = new HashMap<Integer, IText>();
		this.selected = null;
	}

	public WTextCellList (String name, Map<Integer, IText> items)
	{
		this.name = name;
		this.items = new HashMap<Integer, IText>();
		this.items.putAll(items);
		this.selected = null;
	}

	public void addText (IText t)
	{
		items.put(t.getId(), t);
	}

	public void setSelection (Integer sel)
	{
		this.selected = sel;
	}

	public void addTexts (Collection<? extends IText> col)
	{
		for (IText t : col)
			addText (t);
	}

	@Override
	protected DiffPatcher getDifferenceInt (WContent prev, boolean amputation)
	{
		TextCellListDP dp = new TextCellListDP (name);
		WTextCellList tcl = ((WTextCellList)prev);
		Map<Integer, IText> that = ((WTextCellList)prev).items;

		for (Integer id : that.keySet())
			if (!items.containsKey(id))
				dp.addDeletion(id);

		for (Map.Entry<Integer, IText> entry: items.entrySet())
			if (that.containsKey (entry.getKey()))
			{
				if (!that.get(entry.getKey()).equals(entry.getValue()))
					dp.addChange(entry.getValue());
			}
			else
				dp.addCreation(entry.getValue());
		if (null != tcl.selected)
		{
			if (!tcl.selected.equals(selected))
				dp.setSelected (selected);
		}
		else
			if (selected != null)
				dp.setSelected (selected);
			
		if (dp.vital())
			return dp;
		return null;
	}

	@Override
	protected DiffPatcher getAllContentInt()
	{
		TextCellListDP dp = new TextCellListDP (name);
		for (Map.Entry<Integer, IText> entry: items.entrySet())
			dp.addCreation(entry.getValue());
		dp.setSelected (selected);
		return dp;
	}

	@Override
	protected WContent copyInt()
	{
		return new WTextCellList(name, items);
	}

	@Override
	public boolean isAggregating()
	{
		return false;
	}

	public static WContent make (Integer id, Parameters ps,
								 DataBase db, WContentCreator creator)
	{
		WTextCellList tcl = new WTextCellList("bugaga");
		tcl.addText(new IText(10, "First label"));
		tcl.addText(new IText(14, "Sec on label"));
		tcl.addText(new IText(45, "And 45 more label"));
        return tcl.setStyle(id, db);
	}

	public static <T> WContent make (Integer id, Parameters ps,
								 DataBase db, WContentCreator creator,
								 String name, DataBase.Query<T> query,
								 Translator<? super T> trans)
	{
		WTextCellList tcl = new WTextCellList (name);
		query.setWindow (0, MAXIMUM_ITEMS);
		List<IText> items = trans.translate (query.getResults());
		tcl.addTexts (items);

		if (ps.hasParam(name))
			tcl.setSelection(ps.getIntVal(name));

        return tcl.setStyle (id, db);
	}
}
