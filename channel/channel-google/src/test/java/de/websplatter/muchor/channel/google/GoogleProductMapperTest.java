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
import java.io.IOException;
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

  }

}
