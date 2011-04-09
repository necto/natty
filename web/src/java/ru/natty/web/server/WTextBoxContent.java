/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.TextBoxDP;

/**
 *
 * @author necto
 */
public class WTextBoxContent extends WContent
{
	private String text;

	public WTextBoxContent (String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
		assert prev instanceof WTextBoxContent;
		if (text.equals(((WTextBoxContent)prev).getText())) return null;
		return new TextBoxDP(text);
	}

	@Override
	public DiffPatcher getAllContent()
	{
		return new TextBoxDP (text);
	}

	@Override
	public WContent copy()
	{
		return new WTextBoxContent (text);
	}

	@Override
	public String toString() {
		return "WTextBoxContent\"" + text + "\"";
	}

}