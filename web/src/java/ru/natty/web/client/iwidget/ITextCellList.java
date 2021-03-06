/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;
import ru.natty.web.client.ElementReceiver;
import ru.natty.web.client.ParamsBuilder;
import ru.natty.web.shared.IText;

/**
 *
 * @author necto
 */
public class ITextCellList extends IWidget
{
	static private class ICell extends AbstractCell<IText>
	{
		@Override
		public void render (Context context, IText value, SafeHtmlBuilder sb)
		{
			sb.appendHtmlConstant (value.getText());
		}
	}

	private List<IText> items;
	private VerticalPanel panel;
	private Button backB;
	private Button nextB;
	private CellList list;
	private String name;
	private SingleSelectionModel<IText> ssm;
	private boolean reactToSelChange = true;

	public ITextCellList (final Integer id, final String name)
	{
		super(id, new VerticalPanel());
		panel = (VerticalPanel)getWidget();
		backB = new Button("prev");
		list = new CellList (new ICell());
		nextB = new Button("next");
		panel.add(backB);
		panel.add (list);
		panel.add (nextB);

		this.name = name;

		ListDataProvider<IText> ldp = new ListDataProvider<IText>();
		items = ldp.getList();

		ldp.addDataDisplay (list);

		ssm = new SingleSelectionModel<IText>();
		list.setSelectionModel (ssm);
		ssm.addSelectionChangeHandler (new SelectionChangeEvent.Handler()
			{
				@Override
				public void onSelectionChange(SelectionChangeEvent event)
				{
					if (!reactToSelChange) return;
					ParamsBuilder.getCurrent().setVal (name, ssm.getSelectedObject().getId().toString());
					ElementReceiver.get().queryElement();
				}
			});
		nextB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				setStart (getStart() + list.getRowCount());
				ElementReceiver.get().queryElement();
			}
		});
		backB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				Integer start = getStart() - list.getRowCount();
				if (start < 0) start = 0;
				setStart (start);
				ElementReceiver.get().queryElement();
			}
		});
	}

	public List<IText> getItems()
	{
		return items;
	}

	private Integer getStart()
	{
		if (!ParamsBuilder.getCurrent().hasParam (getStartName())) return 0;
		return ParamsBuilder.getCurrent().getIntVal (getStartName());
	}

	private String getStartName()
	{
		return name + ".start";
	}

	public void setStart (Integer start)
	{
		if (start > 0)
			ParamsBuilder.getCurrent().setVal(getStartName(), start.toString());
		else if (ParamsBuilder.getCurrent().hasParam(getStartName()))
				ParamsBuilder.getCurrent().removeParam (getStartName());
	}

	public void selectElement (Integer sel)
	{
		reactToSelChange = false;
		ssm.setSelected(ssm.getSelectedObject(), false);
		if (null != sel)
			for (IText item : items)
				if (item.getId().equals(sel))
					ssm.setSelected (item, true);
		reactToSelChange = true;
	}
}
