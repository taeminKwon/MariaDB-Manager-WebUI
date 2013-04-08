package com.skysql.consolev.api;

import com.vaadin.ui.VerticalLayout;

public class ClusterComponent {

	public enum CCType {
		system, node;
	}

	protected String ID;
	protected String name;
	protected VerticalLayout button;
	protected CCType type;
	protected String status;
	protected String health;
	protected String connections;
	protected String packets;

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VerticalLayout getButton() {
		return button;
	}

	public void setButton(VerticalLayout button) {
		this.button = button;
	}

	public CCType getType() {
		return type;
	}

	public void setType(CCType type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getConnections() {
		return connections;
	}

	public void setConnections(String connections) {
		this.connections = connections;
	}

	public String getPackets() {
		return packets;
	}

	public void setPackets(String packets) {
		this.packets = packets;
	}

}