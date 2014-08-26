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
package se.avanzabank.service.suite.integration.tests.domain.api;

import java.util.List;

import se.avanzabank.service.suite.core.AstrixRemoteResult;
import se.avanzabank.service.suite.core.AstrixRemoteResultReducer;

public class LunchSuggestionReducer implements AstrixRemoteResultReducer<LunchRestaurant, LunchRestaurant> {

	@Override
	public LunchRestaurant reduce(List<AstrixRemoteResult<LunchRestaurant>> result) {
		for (AstrixRemoteResult<LunchRestaurant> l : result) {
			if (l.getResult() != null) {
				return l.getResult();
			}
		}
		return null;
	}

}