package ru.natty.web.server;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import ru.natty.web.shared.CompositePanelDP;
import ru.natty.web.shared.DiffPatcher;

abstract public class WCompositePanelContent extends WContent {

    private static com.google.gwt.logging.client.SystemLogHandler slh =
               new com.google.gwt.logging.client.SystemLogHandler();

    private static void log (Level l, String message)
    {
        slh.publish(new LogRecord(l, message));
    }

	static abstract public class UnitContent
	{
		protected WContent content;
		
		public UnitContent (WContent content)
		{
			this.content = content;
		}
		
		public void setContent(WContent content) {
			this.content = content;
		}
		
		/**
		 * @param old - last state of tab
		 * @return null if there is no difference, and patch otherwise
		 */
		public DiffPatcher getDifference (UnitContent old, boolean amputation)
		{
			if (old.content.isNotVoid())
				return content.getDifference (old.content, amputation);
			else
				if (content.isNotVoid())
					return content.getAllContent();
				else return null;
		}
		
		public abstract void bringContentTo (CompositePanelDP cpdp, Integer id);
		public abstract UnitContent copy();
	}
	public Map<Integer, UnitContent> contents; 
	
	public WCompositePanelContent() {
		contents = new TreeMap<Integer, UnitContent>();
	}

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
        log (Level.SEVERE, "computing diff for" + toString());

		CompositePanelDP cpdp = create();
		if (amputation)
			for (Map.Entry<Integer, UnitContent> e: ((WCompositePanelContent)prev).contents.entrySet())
				if (!contents.containsKey(e.getKey()))
					cpdp.addDeletion(e.getKey());
		
		for (Map.Entry<Integer, UnitContent> e: contents.entrySet())
		{
			log (Level.SEVERE, "differing: " + e.getValue().toString());
			UnitContent old = ((WCompositePanelContent)prev).contents.get (e.getKey());
			if (null == old)
				e.getValue().bringContentTo(cpdp, e.getKey());
			else
			{
				DiffPatcher dp = e.getValue().getDifference(old, amputation);
				if (null != dp)
					cpdp.addChange(e.getKey(), dp);
			}
		}
        log (Level.SEVERE, "diff computed: " + cpdp.toString());
		if (cpdp.vital())
			return cpdp;
		return null;
	}
	
	abstract protected CompositePanelDP create();

	@Override
	public DiffPatcher getAllContent()
	{
		CompositePanelDP cpdp = create();
		for (Map.Entry<Integer, UnitContent> e: contents.entrySet())
			e.getValue().bringContentTo(cpdp, e.getKey());
		return cpdp;
	}
	
	abstract protected WCompositePanelContent makeMe();

	@Override
	public WContent copy()
	{
		WCompositePanelContent ret = makeMe();
		for (Map.Entry<Integer, UnitContent> e: contents.entrySet())
			ret.contents.put(e.getKey(), e.getValue().copy());
		return ret;
	}

	@Override
	public String toString() {
		return "WCompositePanelContent [contents=" + contents + "]";
	}
}
