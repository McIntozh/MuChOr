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
package de.websplatter.muchor.persistence.dao;

import de.websplatter.muchor.persistence.entity.ChannelOrder;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public abstract class ChannelOrderDAO extends AbstractDAO<ChannelOrder> {

  public abstract ChannelOrder findByChannelInstanceAndOrderId(String channelInstance, String orderId);

  public abstract List<ChannelOrder> findByChannelInstance(String channelInstance);

  public abstract List<ChannelOrder> findWithNewOrderStatesForChannelInstance(String channelInstance);

}
