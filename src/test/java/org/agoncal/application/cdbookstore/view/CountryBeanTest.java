package org.agoncal.application.cdbookstore.view;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.agoncal.application.cdbookstore.model.Country;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CountryBeanTest
{

   @Inject
   private CountryBean countryBean;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap
               .create(JavaArchive.class)
               .addClass(CountryBean.class)
               .addClass(Country.class)
               .addAsManifestResource("META-INF/persistence-test.xml", "persistence.xml")
               .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(countryBean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Country country = new Country();
      country.setName("Dummy value");
      country.setIsoCode("XY");
      country.setPrintableName("Dummy value");
      country.setIso3("XYZ");
      country.setNumcode("XYZ");

      // Inserts the object into the database
      countryBean.setCountry(country);
      countryBean.create();
      countryBean.update();
      country = countryBean.getCountry();
      assertNotNull(country.getId());

      // Finds the object from the database and checks it's the right one
      country = countryBean.findById(country.getId());
      assertEquals("Dummy value", country.getName());

      // Deletes the object from the database and checks it's not there anymore
      countryBean.setId(country.getId());
      countryBean.create();
      countryBean.delete();
      country = countryBean.findById(country.getId());
      assertNull(country);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Country example = new Country();

      // Paginates through the example
      countryBean.setExample(example);
      countryBean.paginate();
      assertTrue((countryBean.getPageItems().size() == countryBean.getPageSize())
               || (countryBean.getPageItems().size() == countryBean.getCount()));
   }
}
