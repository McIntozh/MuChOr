/*
 * Copyright 2018 Dennis Schwarz <McIntozh@gmx.net>.
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
package de.websplatter.muchor.channel.google;

import com.google.api.services.content.model.Product;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
public class GoogleProductMapperTest {

  @Test
  public void testReflectionMagic() throws IOException {
    GoogleProductMapper instance = new GoogleProductMapper();
    Product p = new Product();

    instance.reflectionMagic(p, "color", "blue");
    instance.reflectionMagic(p, "unitPricingMeasure.unit", "kg");
    instance.reflectionMagic(p, "unitPricingMeasure.value", "0.5");

    instance.reflectionMagic(p, "unitPricingBaseMeasure.value", "50");

    assertEquals("blue", p.getColor());
    assertEquals("kg", p.getUnitPricingMeasure().getUnit());
    assertEquals(0.5d, p.getUnitPricingMeasure().getValue(), 0d);
    assertEquals(50l, p.getUnitPricingBaseMeasure().getValue(), 0);

    instance.reflectionMagic(p, "additionalImageLinks", "Link1");
    assertEquals(1, p.getAdditionalImageLinks().size(), 0);
    assertEquals("Link1", p.getAdditionalImageLinks().get(0));

    instance.reflectionMagic(p, "additionalImageLinks", Lists.newArrayList("Link1", "Link2"));
    assertEquals(2, p.getAdditionalImageLinks().size(), 0);
    assertEquals("Link1", p.getAdditionalImageLinks().get(0));
    assertEquals("Link2", p.getAdditionalImageLinks().get(1));

    p.setAdditionalImageLinks(null);
    instance.reflectionMagic(p, "additionalImageLinks", new String[]{"Link1", "Link2"});
    System.out.println(p.getAdditionalImageLinks());
    assertEquals(2, p.getAdditionalImageLinks().size(), 0);
    assertEquals("Link1", p.getAdditionalImageLinks().get(0));
    assertEquals("Link2", p.getAdditionalImageLinks().get(1));

  }

}
