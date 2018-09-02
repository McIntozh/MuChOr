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
package de.websplatter.muchor.projection;

import de.websplatter.muchor.persistence.dao.BrandDAO;
import de.websplatter.muchor.persistence.dao.ManufacturerDAO;
import de.websplatter.muchor.persistence.dao.PriStoDelDAO;
import de.websplatter.muchor.persistence.entity.Article;
import de.websplatter.muchor.persistence.entity.Attributed;
import de.websplatter.muchor.persistence.entity.Brand;
import de.websplatter.muchor.persistence.entity.ChannelInstanceSpecifics;
import de.websplatter.muchor.persistence.entity.ChannelSpecifics;
import de.websplatter.muchor.persistence.entity.LanguageSpecifics;
import de.websplatter.muchor.persistence.entity.Manufacturer;
import de.websplatter.muchor.persistence.entity.MediaLinked;
import de.websplatter.muchor.persistence.entity.Named;
import de.websplatter.muchor.persistence.entity.PriStoDel;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * Default implementation of a projection for an article in a specific
 * channel/instance
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@Dependent
public class DefaultArticleProjection {
  
  private String languageCode = null;
  private String channelCode = null;
  private String channelInstanceCode = null;
  
  @Inject
  BrandDAO brandDAO;
  @Inject
  ManufacturerDAO manufacturerDAO;
  @Inject
  PriStoDelDAO priStoDelDAO;
  
  public DefaultArticleProjection forLanguage(String languageCode) {
    this.languageCode = languageCode;
    return this;
  }
  
  public DefaultArticleProjection forChannel(String channelCode) {
    this.channelCode = channelCode;
    return this;
  }
  
  public DefaultArticleProjection forChannelInstance(String channelInstanceCode) {
    this.channelInstanceCode = channelInstanceCode;
    return this;
  }
  
  public DefaultProjectedArticle project(Article article) {
    return this.project(article, null);
  }
  
  public DefaultProjectedArticle project(Article article, PriStoDel priStoDel) {
    DefaultProjectedArticle pa = new DefaultProjectedArticle();
    pa.setLanguageCode(languageCode);
    pa.setChannel(channelCode);
    pa.setChannelInstance(channelInstanceCode);
    
    pa.setSku(article.getSku());
    
    Brand brand = brandDAO.findByKey(article.getBrandKey());
    if (brand != null) {
      DefaultProjectedArticle.Brand pBrand = new DefaultProjectedArticle.Brand();
      pBrand.setKey(brand.getKey());
      pBrand.setName(brand.getName());
      pa.setBrand(pBrand);
      Manufacturer manufacturer = manufacturerDAO.findByKey(brand.getManufacturerKey());
      if (manufacturer != null) {
        DefaultProjectedArticle.Manufacturer pManufacturer = new DefaultProjectedArticle.Manufacturer();
        pManufacturer.setKey(manufacturer.getKey());
        pManufacturer.setName(manufacturer.getName());
        pa.setManufacturer(pManufacturer);
      }
    }
    pa.setGtin(article.getGtin());
    pa.setMpn(article.getMpn());
    
    mapNamed(article, pa);
    mapAttributed(article, pa);
    mapMediaLinked(article, pa);
    
    if (this.languageCode != null) {
      mapLanguageSpecifics(article.getLanguageSpecifics().get(this.languageCode), pa);
    }
    if (this.channelCode != null) {
      mapChannelSpecifics(article.getChannelSpecifics().get(this.channelCode), pa);
    }
    if (this.channelInstanceCode != null) {
      mapChannelInstanceSpecifics(article.getChannelInstanceSpecifics().get(this.channelInstanceCode), pa);
    }
    
    pa.getMedia().values().forEach(Collections::sort);
    
    if (priStoDel == null && channelInstanceCode != null) {
      priStoDel = priStoDelDAO.findBySkuAndChannelInstance(article.getSku(), channelInstanceCode);
    }
    if (priStoDel != null) {
      DefaultProjectedArticle.Price p = new DefaultProjectedArticle.Price();
      p.setGrossPrice(priStoDel.getGrossPrice());
      p.setNetPrice(priStoDel.getNetPrice());
      p.setCurrency(priStoDel.getCurrency());
      p.setVatPercentage(priStoDel.getVatPercentage());
      pa.setPrice(p);
      
      DefaultProjectedArticle.Stock s = new DefaultProjectedArticle.Stock();
      s.setProcessingTimeInDays(priStoDel.getProcessingTimeInDays());
      s.setQuantity(priStoDel.getStockQuantity());
      s.setRestockQuantity(priStoDel.getRestockQuantity());
      s.setRestockTimeInDays(priStoDel.getRestockTimeInDays());
      s.setRestockable(priStoDel.isRestockable());
      s.setShippingTimeInDays(priStoDel.getShippingTimeInDays());
      pa.setStock(s);
    }
    
    return pa;
  }
  
  protected void mapLanguageSpecifics(LanguageSpecifics ls, DefaultProjectedArticle pa) {
    if (ls != null) {
      mapNamed(ls, pa);
      mapAttributed(ls, pa);
      mapMediaLinked(ls, pa);
    }
  }
  
  protected void mapChannelSpecifics(ChannelSpecifics cs, DefaultProjectedArticle pa) {
    if (cs != null) {
      pa.setCatalogId(cs.getCatalogId());
      pa.getCategory().putAll(cs.getCategoryAssignments());
      mapNamed(cs, pa);
      mapAttributed(cs, pa);
      mapMediaLinked(cs, pa);
    }
  }
  
  protected void mapChannelInstanceSpecifics(ChannelInstanceSpecifics cis, DefaultProjectedArticle pa) {
    if (cis != null) {
      pa.getCategory().putAll(cis.getCategoryAssignments());
      mapNamed(cis, pa);
      mapAttributed(cis, pa);
      mapMediaLinked(cis, pa);
    }
  }
  
  protected void mapNamed(Named nd, DefaultProjectedArticle pa) {
    if (nd.getName() != null) {
      pa.setName(nd.getName());
    }
  }
  
  protected void mapAttributed(Attributed ad, DefaultProjectedArticle pa) {
    Map<String, DefaultProjectedArticle.Attribute> existingAttributesByKey = pa.getAttributes();
    
    ad.getAttributes().entrySet().stream().forEach(e -> {
      if (e.getValue() != null) {
        DefaultProjectedArticle.Attribute a = existingAttributesByKey.get(e.getKey());
        
        if (a == null) {
          a = new DefaultProjectedArticle.Attribute();
          a.setKey(e.getKey());
          pa.getAttributes().put(e.getKey(), a);
        }
        
        a.setValue(e.getValue().getValue());
        a.setUnit(e.getValue().getUnit());
      }
    });
    
    pa.getAttributes().values().removeIf(a -> a.getValue() == null);
    
  }
  
  protected void mapMediaLinked(MediaLinked mld, DefaultProjectedArticle pa) {
    
    mld.getMediaLinks().forEach(ml -> {
      DefaultProjectedArticle.Media m = new DefaultProjectedArticle.Media();
      m.setPriority(ml.getPriority());
      m.setType(ml.getType());
      m.setUrl(ml.getUrl());
      
      List<DefaultProjectedArticle.Media> list = pa.getMedia().get(m.getType());
      if (list == null) {
        list = new LinkedList<>();
        pa.getMedia().put(m.getType(), list);
      }
      list.add(m);
    });
  }
}
