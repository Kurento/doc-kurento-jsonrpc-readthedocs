/*
 * (C) Copyright 2013 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.kurento.jsonrpc.internal;

import static org.kurento.jsonrpc.JsonUtils.INJECT_SESSION_ID;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.kurento.jsonrpc.JsonRpcErrorException;
import org.kurento.jsonrpc.client.Continuation;
import org.kurento.jsonrpc.message.Request;
import org.kurento.jsonrpc.message.Response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class JsonRpcRequestSenderHelper implements JsonRpcRequestSender {

  protected AtomicInteger id = new AtomicInteger();
  protected String sessionId;

  public JsonRpcRequestSenderHelper() {
  }

  public JsonRpcRequestSenderHelper(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  @Override
  public <R> R sendRequest(String method, Class<R> resultClass) throws IOException {
    return sendRequest(method, null, resultClass);
  }

  @Override
  public <R> R sendRequest(String method, Object params, Class<R> resultClass) throws IOException {

    Request<Object> request = new Request<>(null, method, params);

    setIdIfNecessary(request);

    if (INJECT_SESSION_ID) {
      request.setSessionId(sessionId);
    }

    return sendRequest(request, resultClass);
  }

  public <P, R> R sendRequest(Request<P> request, Class<R> resultClass)
      throws JsonRpcErrorException, IOException {

    Response<R> response = internalSendRequest(request, resultClass);

    if (response == null) {
      return null;
    }

    if (response.getSessionId() != null) {
      sessionId = response.getSessionId();
    }

    if (response.getError() != null) {
      throw new JsonRpcErrorException(response.getError());
    }

    return response.getResult();
  }

  @Override
  public JsonElement sendRequest(String method) throws IOException {
    return sendRequest(method, JsonElement.class);
  }

  @Override
  public JsonElement sendRequest(String method, Object params) throws IOException {
    return sendRequest(method, params, JsonElement.class);
  }

  @Override
  public void sendRequest(String method, JsonObject params,
      final Continuation<JsonElement> continuation) {

    Request<Object> request =
        new Request<Object>(Integer.valueOf(id.incrementAndGet()), method, params);

    if (INJECT_SESSION_ID) {
      request.setSessionId(sessionId);
    }

    internalSendRequest(request, JsonElement.class, new Continuation<Response<JsonElement>>() {

      @Override
      public void onSuccess(Response<JsonElement> response) {

        if (response == null) {
          continuation.onSuccess(null);
          return;
        }

        if (response.getSessionId() != null) {
          sessionId = response.getSessionId();
        }

        if (response.getError() != null) {
          continuation.onError(new JsonRpcErrorException(response.getError()));
        } else {
          continuation.onSuccess(response.getResult());
        }
      }

      @Override
      public void onError(Throwable cause) {
        continuation.onError(cause);
      }
    });

  }

  @Override
  public void sendNotification(String method) throws IOException {
    sendNotification(method, null);
  }

  @Override
  public void sendNotification(String method, Object params) throws IOException {

    Request<Object> request = new Request<>(null, method, params);

    if (INJECT_SESSION_ID) {
      request.setSessionId(sessionId);
    }

    sendRequest(request, Void.class);
  }

  @Override
  public void sendNotification(String method, Object params,
      final Continuation<JsonElement> continuation) throws IOException {

    throw new UnsupportedOperationException();
  }

  @Override
  public Response<JsonElement> sendRequest(Request<JsonObject> request) throws IOException {

    setIdIfNecessary(request);
    return internalSendRequest(request, JsonElement.class);
  }

  private void setIdIfNecessary(Request<? extends Object> request) {
    if (request.getId() == null) {
      request.setId(Integer.valueOf(id.incrementAndGet()));
    }
  }

  @Override
  public void sendRequest(Request<JsonObject> request,
      Continuation<Response<JsonElement>> continuation) {

    setIdIfNecessary(request);
    internalSendRequest(request, JsonElement.class, continuation);
  }

  @Override
  public Response<JsonElement> sendRequestHonorId(Request<JsonObject> request) throws IOException {
    return internalSendRequest(request, JsonElement.class);
  }

  @Override
  public void sendRequestHonorId(Request<JsonObject> request,
      Continuation<Response<JsonElement>> continuation) throws IOException {
    internalSendRequest(request, JsonElement.class, continuation);
  }

  protected abstract <P, R> Response<R> internalSendRequest(Request<P> request,
      Class<R> resultClass) throws IOException;

  protected abstract void internalSendRequest(Request<? extends Object> request,
      Class<JsonElement> class1, Continuation<Response<JsonElement>> continuation);
}
