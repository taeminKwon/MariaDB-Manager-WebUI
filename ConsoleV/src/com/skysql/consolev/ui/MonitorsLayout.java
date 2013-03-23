package com.skysql.consolev.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.skysql.consolev.MonitorRecord;
import com.skysql.consolev.api.Monitors;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MonitorsLayout extends VerticalLayout {
	private static final long serialVersionUID = 0x4C656F6E6172646FL;

	private int componentIndex;
	private ChartPreviewLayout chartPreviewLayout;
	private LinkedHashMap<String, MonitorRecord> availableMonitors;
	private ArrayList<String> selectedMonitorIDs;
	private ArrayList<ComboBox> selectMonitorList;

	private ValueChangeListener monitorSelectListener = new ValueChangeListener() {
		private static final long serialVersionUID = 0x4C656F6E6172646FL;

		public void valueChange(ValueChangeEvent event) {

			refreshMonitors();
		}
	};

	public MonitorsLayout(ArrayList<String> selectedMonitorIDs) {
		this.selectedMonitorIDs = selectedMonitorIDs;

		addStyleName("MonitorsLayout");
		setSpacing(true);
		setMargin(true);

		Monitors.reloadMonitors();
		availableMonitors = Monitors.getMonitorsList();
		initializeMonitors();

	}

	public void initializeMonitors() {

		final Label monitorsLabel = new Label("Select Monitors");
		monitorsLabel.setStyleName("dialogLabel");
		addComponent(monitorsLabel);

		componentIndex = getComponentCount(); // where the monitors start
		selectMonitorList = new ArrayList<ComboBox>();
		for (String monitorID : selectedMonitorIDs) {
			addComponent(addRow(monitorID));
		}

		final Button addButton = new Button("Add");
		addComponent(addButton);
		addButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 0x4C656F6E6172646FL;

			public void buttonClick(ClickEvent event) {
				Component newRow = addRow(null);
				replaceComponent(addButton, newRow);
				addComponent(addButton);
			}
		});

	}

	public void addChartPreview(ChartPreviewLayout chartPreviewLayout) {
		this.chartPreviewLayout = chartPreviewLayout;
	}

	private void refreshMonitors() {

		selectedMonitorIDs.clear();
		for (ComboBox select : selectMonitorList) {
			String monitorID = (String) select.getValue();
			selectedMonitorIDs.add(monitorID);
		}
		chartPreviewLayout.refresh();

	}

	private Component addRow(String monitorID) {
		HorizontalLayout row = new HorizontalLayout();
		ComboBox selectMonitor = new ComboBox();
		for (MonitorRecord availMonitor : availableMonitors.values()) {
			selectMonitor.addItem(availMonitor.getID());
			selectMonitor.setItemCaption(availMonitor.getID(), availMonitor.getName());
		}
		row.addComponent(selectMonitor);
		selectMonitorList.add(selectMonitor);

		selectMonitor.setValue(monitorID);
		selectMonitor.setImmediate(true);
		selectMonitor.addValueChangeListener(monitorSelectListener);

		if (getComponentCount() > componentIndex) {
			Button deleteButton = new Button("X");
			row.addComponent(deleteButton);
			deleteButton.setData(selectMonitor);
			deleteButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 0x4C656F6E6172646FL;

				public void buttonClick(ClickEvent event) {
					// remove combobox from list
					Button button = event.getButton();
					ComboBox select = (ComboBox) button.getData();
					selectMonitorList.remove(select);

					// remove layout from dialog
					int layoutIndex = getComponentIndex(button.getParent());
					HorizontalLayout layout = (HorizontalLayout) getComponent(layoutIndex);
					removeComponent(layout);

					refreshMonitors();
				}
			});
		}

		focus();

		return (row);

	}
}
