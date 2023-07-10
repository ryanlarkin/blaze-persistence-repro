/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.tutorial.em;

import java.util.Date;
import java.util.List;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import junit.framework.TestCase;

/**
 * Illustrates basic use of Hibernate as a Jakarta Persistence provider.
 *
 * @author Steve Ebersole
 */
public class EntityManagerIllustrationTest extends TestCase {
	private EntityManagerFactory entityManagerFactory;

	@Override
	protected void setUp() throws Exception {
		// like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
		// 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
		entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.tutorial.jpa" );
	}

	@Override
	protected void tearDown() throws Exception {
		entityManagerFactory.close();
	}

	public void testBasicUsage() {
		// create a couple of events...
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( new Event( "Our very first event!", new Date() ) );
		entityManager.persist( new Event( "A follow up event", new Date() ) );
		Property<Integer> p1 = new IntegerProperty( "ABC", 123 );
		entityManager.persist( p1 );
		Property<String> p2 = new StringProperty( "DEF", "HIJ" );
		entityManager.persist( p2 );
		entityManager.persist( new PropertyHolder( p1 ) );
		entityManager.persist( new PropertyHolder( p2 ) );
		entityManager.getTransaction().commit();
		entityManager.close();

		// now lets pull events from the database and list them
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<Event> result = entityManager.createQuery( "from Event", Event.class ).getResultList();
		for ( Event event : result ) {
			System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
		}
		List<PropertyHolder> propertyResults = entityManager.createQuery(
				"from PropertyHolder ", PropertyHolder.class ).getResultList();
		for ( PropertyHolder propertyHolder : propertyResults ) {
			System.out.println( "Property ("
					+ propertyHolder.getProperty().getName() + ") : "
					+ propertyHolder );
		}
		entityManager.getTransaction().commit();
		entityManager.close();

		CriteriaBuilderConfiguration config = Criteria.getDefault().setProperty("com.blazebit.persistence.inline_count_query", "false");
		config.createCriteriaBuilderFactory(entityManagerFactory);
	}
}
