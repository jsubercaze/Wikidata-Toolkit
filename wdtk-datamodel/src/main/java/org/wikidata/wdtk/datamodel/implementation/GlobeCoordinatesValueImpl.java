package org.wikidata.wdtk.datamodel.implementation;

/*
 * #%L
 * Wikidata Toolkit Data Model
 * %%
 * Copyright (C) 2014 Wikidata Toolkit Developers
 * %%
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
 * #L%
 */

import org.wikidata.wdtk.datamodel.helpers.Equality;
import org.wikidata.wdtk.datamodel.helpers.Hash;
import org.wikidata.wdtk.datamodel.helpers.ToString;
import org.wikidata.wdtk.datamodel.implementation.json.JacksonInnerGlobeCoordinates;
import org.wikidata.wdtk.datamodel.interfaces.GlobeCoordinatesValue;
import org.wikidata.wdtk.datamodel.interfaces.ValueVisitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Jackson implementation of {@link GlobeCoordinatesValue}.
 *
 * @author Fredo Erxleben
 * @author Antonin Delpeuch
 * @author Markus Kroetzsch
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = None.class)
public class GlobeCoordinatesValueImpl extends ValueImpl implements
		GlobeCoordinatesValue {

	/**
	 * Inner helper object to store the actual data. Used to get the nested JSON
	 * structure that is required here.
	 */
	private final JacksonInnerGlobeCoordinates value;
	
	/**
	 * Constructor.
	 * 
	 * @param latitude
	 *            the latitude of the coordinates in degrees
	 * @param longitude
	 *            the longitude of the coordinates in degrees
	 * @param precision
	 *            the precision of the coordinates in degrees
	 * @param globe
	 *            IRI specifying the celestial objects of the coordinates
	 */
	public GlobeCoordinatesValueImpl(double latitude, double longitude,
			double precision, String globe) {
		super(JSON_VALUE_TYPE_GLOBE_COORDINATES);
		this.value = new JacksonInnerGlobeCoordinates(latitude, longitude,
				precision, globe);
	}

	
	/**
	 * Constructor for deserialization from JSON via Jackson.
	 */
	@JsonCreator
	protected GlobeCoordinatesValueImpl(
			@JsonProperty("value") JacksonInnerGlobeCoordinates innerCoordinates) {
		super(JSON_VALUE_TYPE_GLOBE_COORDINATES);
		this.value = innerCoordinates;
	}

	/**
	 * Returns the inner value helper object. Only for use by Jackson during
	 * serialization.
	 *
	 * @return the inner globe coordinates value
	 */
	public JacksonInnerGlobeCoordinates getValue() {
		return value;
	}

	@JsonIgnore
	@Override
	public double getLatitude() {
		return this.value.getLatitude();
	}

	@JsonIgnore
	@Override
	public double getLongitude() {
		return this.value.getLongitude();
	}

	@JsonIgnore
	@Override
	public double getPrecision() {
		return this.value.getPrecision();
	}

	@JsonIgnore
	@Override
	public String getGlobe() {
		return this.value.getGlobe();
	}

	@Override
	public <T> T accept(ValueVisitor<T> valueVisitor) {
		return valueVisitor.visit(this);
	}

	@Override
	public int hashCode() {
		return Hash.hashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return Equality.equalsGlobeCoordinatesValue(this, obj);
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
