package com.skysql.consolev.ui;

import com.skysql.consolev.api.UserObject;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TopPanel extends HorizontalLayout {

	public TopPanel() {
		setSpacing(true);
		addStyleName("titleLayout");
		setWidth("100%");

		Embedded image = new Embedded(null, new ThemeResource("img/SkySQL.png"));
		addComponent(image);

		// LINKS AREA (TOP-RIGHT)
		HorizontalLayout userSettingsLayout = new HorizontalLayout();
		userSettingsLayout.setSizeUndefined();
		userSettingsLayout.setSpacing(true);
		addComponent(userSettingsLayout);
		setComponentAlignment(userSettingsLayout, Alignment.MIDDLE_RIGHT);

		// User icon and name
		VerticalLayout userLayout = new VerticalLayout();
		userSettingsLayout.addComponent(userLayout);

		UserObject userObject = VaadinSession.getCurrent().getAttribute(UserObject.class);
		Embedded userIcon = new Embedded(null, new ThemeResource("img/user.png"));
		userLayout.addComponent(userIcon);
		final Label userName = new Label(userObject.getName());
		userLayout.addComponent(userName);

		// buttons
		VerticalLayout buttonsLayout = new VerticalLayout();
		buttonsLayout.setSizeUndefined();
		buttonsLayout.setSpacing(true);
		userSettingsLayout.addComponent(buttonsLayout);
		userSettingsLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);

		// Settings button
		SettingsDialog settingsDialog = new SettingsDialog("Settings");
		Button settingsButton = settingsDialog.getButton();
		buttonsLayout.addComponent(settingsButton);
		buttonsLayout.setComponentAlignment(settingsButton, Alignment.MIDDLE_CENTER);

		// Logout
		Button logoutButton = new Button("Logout");
		logoutButton.setSizeUndefined();
		buttonsLayout.addComponent(logoutButton);
		buttonsLayout.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);
		logoutButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 0x4C656F6E6172646FL;

			public void buttonClick(ClickEvent event) {
				VaadinSession.getCurrent().setAttribute(UserObject.class, null);
				getSession().close();
				UI.getCurrent().getPage().setLocation("");
			}
		});

	}

}