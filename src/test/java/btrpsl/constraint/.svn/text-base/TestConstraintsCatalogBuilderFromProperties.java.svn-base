/*
 * Copyright (c) Fabien Hermenier
 *
 *        This file is part of Entropy.
 *
 *        Entropy is free software: you can redistribute it and/or modify
 *        it under the terms of the GNU Lesser General Public License as published by
 *        the Free Software Foundation, either version 3 of the License, or
 *        (at your option) any later version.
 *
 *        Entropy is distributed in the hope that it will be useful,
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *        GNU Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public License
 *        along with Entropy.  If not, see <http://www.gnu.org/licenses/>.
 */

package btrpsl.constraint;

import entropy.PropertiesHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Unit tests for ConstraintsCatalogBuilderFromProperties.
 *
 * @author Fabien Hermenier
 */
@Test
public class TestConstraintsCatalogBuilderFromProperties {

    private static final String RESOURCES_ROOT = "src/test/resources/btrpsl/constraint/TestConstraintsCatalogBuilderFromProperties.";

    /**
     * Read a properties file. The test fails in an error occurred.
     *
     * @param file the file to read
     * @return the properties
     */
    public static PropertiesHelper readEntropyProperties(String file) {
        PropertiesHelper p = null;
        try {
            p = new PropertiesHelper(file);
        } catch (IOException e) {
            Assert.fail(e.getMessage(), e);
        }
        return p;
    }

    public void testWithNoConstraints() {
        PropertiesHelper props = readEntropyProperties(RESOURCES_ROOT + "noConstraints.txt");
        ConstraintsCatalogBuilder builder = new ConstraintsCatalogBuilderFromProperties(props);
        try {
            ConstraintsCatalog c = builder.build();
            Assert.assertEquals(c.getAvailableConstraints().size(), 0);
        } catch (ConstraintsCalalogBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    public void testWithAvailableConstraints() {
        PropertiesHelper props = readEntropyProperties(RESOURCES_ROOT + "fine.txt");
        ConstraintsCatalogBuilder builder = new ConstraintsCatalogBuilderFromProperties(props);
        try {
            ConstraintsCatalog c = builder.build();
            Assert.assertEquals(c.getAvailableConstraints().size(), 1);
            Assert.assertTrue(c.getAvailableConstraints().contains("mock"));
        } catch (ConstraintsCalalogBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    }

    @Test(expectedExceptions = {ConstraintsCalalogBuilderException.class})
    public void testWithBadFQCN() throws ConstraintsCalalogBuilderException {
        PropertiesHelper props = readEntropyProperties(RESOURCES_ROOT + "badFQCN.txt");
        ConstraintsCatalogBuilder builder = new ConstraintsCatalogBuilderFromProperties(props);
        builder.build();
    }

    @Test(expectedExceptions = {ConstraintsCalalogBuilderException.class})
    public void testWithMissingLocation() throws ConstraintsCalalogBuilderException {
        PropertiesHelper props = readEntropyProperties(RESOURCES_ROOT + "noAvailable.txt");
        ConstraintsCatalogBuilder builder = new ConstraintsCatalogBuilderFromProperties(props);
        builder.build();
    }

    /*public void testWithDefaultProperties() {
        PropertiesHelper props = readDefaultEntropyProperties();
        ConstraintsCatalogBuilder builder = new ConstraintsCatalogBuilderFromProperties(props);
        try {
            PBConstraintsCatalog c = builder.build();
            Assert.assertEquals(c.getAvailableConstraints().size(), 7);
        } catch (ConstraintsCalalogBuilderException e) {
            Assert.fail(e.getMessage(), e);
        }
    } */

}
