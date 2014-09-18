/*
 * Copyright 2014-2015 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.avanzabank.asterix.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * @author Elias Lindholm (elilin)
 *
 */
public class AsterixSettings {
	
	public static final String BEAN_REBIND_ATTEMP_INTERVAL = "StatefulAsterixBean.beanRebindAttemptInterval";
	
	private final Map<String, Object> settings = new ConcurrentHashMap<>();
	
	public long getLong(String settingsName, long deafualtValue) {
		Object value = settings.get(settingsName);
		if (value == null) {
			return deafualtValue;
		}
		if (value instanceof String) {
			return Long.parseLong((String)value);
		}
		return Long.class.cast(value).longValue();
	}

	public void set(String settingName, long value) {
		this.settings.put(settingName, Long.valueOf(value));
	}
	
	public void set(String settingName, String value) {
		this.settings.put(settingName, value);
	}

	public static AsterixSettings from(Map<String, String> settings) {
		AsterixSettings result = new AsterixSettings();
		for (Map.Entry<String, String> setting : settings.entrySet()) {
			settings.put(setting.getKey(), setting.getValue());
		}
		return result;
	}

	public void setAll(Map<String, String> settings) {
		for (Map.Entry<String, String> setting : settings.entrySet()) {
			this.settings.put(setting.getKey(), setting.getValue());
		}
	}
	

}