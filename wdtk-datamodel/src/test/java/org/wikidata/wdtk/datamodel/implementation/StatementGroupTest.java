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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.wikidata.wdtk.datamodel.interfaces.Claim;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.Snak;
import org.wikidata.wdtk.datamodel.interfaces.SnakGroup;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.datamodel.interfaces.StatementRank;

public class StatementGroupTest {

	StatementGroup sg1;
	StatementGroup sg2;
	Statement statement1;
	Statement statement2;
	Statement statementEmptyId;
	EntityIdValue subject;
	PropertyIdValue property;

	@Before
	public void setUp() throws Exception {
		subject = new ItemIdValueImpl("Q42",
				"http://wikidata.org/entity/");
		property = new PropertyIdValueImpl("P42",
				"http://wikidata.org/entity/");
		Snak mainSnak = new ValueSnakImpl(property, subject);
		Claim claim = new ClaimImpl(subject, mainSnak,
				Collections.<SnakGroup> emptyList());

		statement1 = new StatementImpl(claim,
				Collections.<Reference> emptyList(), StatementRank.NORMAL,
				"MyId");
		statement2 = new StatementImpl(claim,
				Collections.<Reference> emptyList(), StatementRank.PREFERRED,
				"MyId");
		statementEmptyId = new StatementImpl(claim,
				Collections.<Reference> emptyList(), StatementRank.NORMAL,
				"");

		sg1 = new StatementGroupImpl(
				Collections.singletonList(statement1));
		sg2 = new StatementGroupImpl(
				Collections.singletonList(statement1));
	}

	@Test
	public void statementListIsCorrect() {
		assertEquals(sg1.getStatements(),
				Collections.singletonList(statement1));
	}

	@Test
	public void propertyIsCorrect() {
		assertEquals(sg1.getProperty(), property);
	}

	@Test
	public void subjectIsCorrect() {
		assertEquals(sg1.getSubject(), subject);
	}

	@Test
	public void equalityBasedOnContent() {
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(statement1);
		statements.add(statement2);
		StatementGroup sg3 = new StatementGroupImpl(statements);

		assertEquals(sg1, sg1);
		assertEquals(sg1, sg2);
		assertThat(sg1, not(equalTo(sg3)));
		assertThat(sg1, not(equalTo(null)));
		assertFalse(sg1.equals(this));
	}

	@Test
	public void hashBasedOnContent() {
		assertEquals(sg1.hashCode(), sg2.hashCode());
	}

	@Test(expected = NullPointerException.class)
	public void statementListNotNull() {
		new StatementGroupImpl(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void statementListNotEmpty() {
		new StatementGroupImpl(Collections.<Statement> emptyList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void statementListRequiresSameSubject() {
		List<Statement> statements = new ArrayList<Statement>();

		statements.add(statement1);

		EntityIdValue subject2 = new ItemIdValueImpl("Q23",
				"http://wikidata.org/entity/");
		Snak mainSnak = new NoValueSnakImpl(property);
		Claim claim = new ClaimImpl(subject2, mainSnak,
				Collections.<SnakGroup> emptyList());
		Statement s2 = new StatementImpl(claim,
				Collections.<Reference> emptyList(), StatementRank.NORMAL,
				"MyId");
		statements.add(s2);

		new StatementGroupImpl(statements);
	}

	@Test(expected = IllegalArgumentException.class)
	public void statementListRequiresSameProperty() {
		List<Statement> statements = new ArrayList<Statement>();

		statements.add(statement1);

		PropertyIdValue property2 = new PropertyIdValueImpl("P23", "http://wikidata.org/entity/");
		Snak mainSnak = new NoValueSnakImpl(property2);
		Claim claim = new ClaimImpl(subject, mainSnak,
				Collections.<SnakGroup> emptyList());
		Statement s2 = new StatementImpl(claim,
				Collections.<Reference> emptyList(), StatementRank.NORMAL,
				"MyId");
		statements.add(s2);

		new StatementGroupImpl(statements);
	}
	
	public void addSameStatementToGroup() {
		StatementGroup added = sg1.withStatement(statement1);
		
		assertEquals(sg1, added);
	}
	
	public void addStatementWithMatchingId() {
		StatementGroup added = sg1.withStatement(statement2);
		
		assertEquals(new StatementGroupImpl(Collections.singletonList(statement2)), added);
	}

	public void addStatementEmptyId() {
		StatementGroup initial = new StatementGroupImpl(Collections.singletonList(statementEmptyId));
		StatementGroup added = initial.withStatement(statementEmptyId);
		
		assertEquals(new StatementGroupImpl(Arrays.asList(statementEmptyId, statementEmptyId)),
				added);
	}
}
