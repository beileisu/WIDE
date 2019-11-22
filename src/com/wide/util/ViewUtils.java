package com.wide.util;

import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ViewUtils {

	public static void newTable(TableViewer tableViewer, Map<String, Integer> map) {
		Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        map.forEach((key,value)->{
        	TableColumn column = new TableColumn(table, 16384);
            column.setText(key);
            column.setWidth(value);
        });
	}


	
}
