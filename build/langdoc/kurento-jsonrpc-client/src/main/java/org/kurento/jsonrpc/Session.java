/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */

package org.kurento.jsonrpc;

import java.io.IOException;
import java.util.Map;

import org.kurento.jsonrpc.internal.JsonRpcRequestSender;

public interface Session extends JsonRpcRequestSender {

  public String getSessionId();

  public Object getRegisterInfo();

  public boolean isNew();

  public void close() throws IOException;

  void setReconnectionTimeout(long millis);

  public Map<String, Object> getAttributes();

}
