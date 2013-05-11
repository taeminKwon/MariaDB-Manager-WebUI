/*
 * This file is distributed as part of the SkySQL Cloud Data Suite.  It is free
 * software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * version 2.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Copyright 2012-2013 SkySQL Ab
 */

package com.skysql.consolev.api;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.vaadin.ui.Notification;

public class UserInfo {

	LinkedHashMap<String, UserObject> usersList;

	public LinkedHashMap<String, UserObject> getUsersList() {
		return usersList;
	}

	public UserInfo() {

	}

	public UserInfo(String userID) {

		APIrestful api = new APIrestful();
		if (api.get("user")) {
			UserInfo userInfo = AppData.getGson().fromJson(api.getResult(), UserInfo.class);
			if (userInfo != null) {
				this.usersList = userInfo.usersList;
			}
		} else {
			Notification.show(api.getErrors());
		}

	}

	public UserObject findRecordByID(String id) {
		return usersList.get(id);
	}

	public String findNameByID(String id) {
		UserObject userObject = usersList.get(id);
		if (userObject == null) {
			return null;
		}
		return userObject.getName();
	}

	public String completeNamesByID(String id) {
		UserObject userObject = usersList.get(id);
		if (userObject == null) {
			return null;
		}
		String name = userObject.getName();
		return id + ((name == null || name.isEmpty()) ? "" : " (" + name + ")");
	}

	public String findIDByName(String name) {
		for (UserObject user : usersList.values()) {
			if (name.equals(user.getName())) {
				return user.getUserID();
			}
		}
		return null;
	}

	public boolean setUser(String userID, String name, String password) {

		boolean success = false;
		try {
			APIrestful api = new APIrestful();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("name", name);
			jsonParam.put("password", password);
			success = api.put("user/" + userID, jsonParam.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not get response from API");
		}

		// TODO: check this code!  Shouldn't it be like Monitors? 
		// if we added a user, versus modified it
		if (!usersList.containsKey(userID)) {
			UserObject userObject = new UserObject(userID, name);
			usersList.put(userID, userObject);
		} else {
			UserObject userObject = usersList.get(userID);
			userObject.setName(name);
		}

		return true;
	}

	public boolean deleteUser(String userID) {

		APIrestful api = new APIrestful();
		if (api.delete("user/" + userID)) {
			usersList.remove(userID);
			return true;
		}

		return false;
	}

	protected void setUsersList(LinkedHashMap<String, UserObject> usersList) {
		this.usersList = usersList;
	}

}

class UserInfoDeserializer implements JsonDeserializer<UserInfo> {
	public UserInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		UserInfo userInfo = new UserInfo();

		JsonElement jsonElement = json.getAsJsonObject().get("users");
		if (jsonElement == null || jsonElement.isJsonNull()) {
			userInfo.setUsersList(new LinkedHashMap<String, UserObject>());
		} else {
			JsonArray array = jsonElement.getAsJsonArray();
			int length = array.size();

			LinkedHashMap<String, UserObject> usersList = new LinkedHashMap<String, UserObject>(length);
			for (int i = 0; i < length; i++) {
				JsonObject backupJson = array.get(i).getAsJsonObject();
				JsonElement element;
				String username = (element = backupJson.get("username")).isJsonNull() ? null : element.getAsString();
				String name = (element = backupJson.get("name")).isJsonNull() ? null : element.getAsString();
				UserObject userObject = new UserObject(username, name);
				usersList.put(username, userObject);
			}
			userInfo.setUsersList(usersList);
		}

		return userInfo;
	}

}
