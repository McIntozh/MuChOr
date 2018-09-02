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

import com.google.api.services.content.model.Inventory;
import com.google.api.services.content.model.Price;
import de.websplatter.muchor.persistence.entity.PriStoDel;
import java.text.NumberFormat;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class GoogleInventoryMapper {

  private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);

  static {
    NUMBER_FORMAT.setGroupingUsed(false);
    NUMBER_FORMAT.setMinimumFractionDigits(2);
    NUMBER_FORMAT.setMaximumFractionDigits(2);
  }

  public Inventory map(PriStoDel psd) {
    Inventory inv = new Inventory();
    inv.setAvailability(psd.getStockQuantity() > 0 ? "in stock" : psd.isRestockable() ? "preorder" : "out of stock");
    Price p = new Price();
    p.setCurrency(psd.getCurrency());
    synchronized (NUMBER_FORMAT) {
      p.setValue(NUMBER_FORMAT.format(psd.getGrossPrice() / 100.0f));
    }
    inv.setPrice(p);
    inv.setSellOnGoogleQuantity((long) psd.getStockQuantity());
    return inv;
  }
}
