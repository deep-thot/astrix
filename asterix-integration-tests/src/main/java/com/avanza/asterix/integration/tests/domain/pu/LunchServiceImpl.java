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
package com.avanza.asterix.integration.tests.domain.pu;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;

import com.avanza.asterix.integration.tests.domain.api.GetLunchRestaurantRequest;
import com.avanza.asterix.integration.tests.domain.api.LunchRestaurant;
import com.avanza.asterix.integration.tests.domain.api.LunchService;
import com.avanza.asterix.integration.tests.domain.apiruntime.feeder.InternalLunchFeeder;
import com.avanza.asterix.provider.component.AsterixServiceComponentNames;
import com.avanza.asterix.provider.core.AsterixServiceComponentName;
import com.avanza.asterix.provider.core.AsterixServiceExport;

@AsterixServiceComponentName(AsterixServiceComponentNames.GS_REMOTING)
@AsterixServiceExport({LunchService.class, InternalLunchFeeder.class})
public class LunchServiceImpl implements LunchService, InternalLunchFeeder {

	private final GigaSpace gigaSpace;
	
	@Autowired
	public LunchServiceImpl(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
	}

	@Override
	public LunchRestaurant suggestRandomLunchRestaurant(String foodType) {
		LunchRestaurant template = new LunchRestaurant();
		template.setFoodType(foodType);
		LunchRestaurant[] candiates = gigaSpace.readMultiple(template);
		return candiates.length > 0 ? candiates[0] : null;
	}

	@Override
	public void addLunchRestaurant(LunchRestaurant restaurant) {
		this.gigaSpace.write(restaurant);
	}

	@Override
	public LunchRestaurant getLunchRestaurant(GetLunchRestaurantRequest r) {
		if ("throwException".equals(r.getName())) {
			throw new IllegalArgumentException("Illegal restaurant: " + r.getName());
		}
		return this.gigaSpace.readById(LunchRestaurant.class, r.getName());
	}

}

