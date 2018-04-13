/*
 * Copyright (C) 2018 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMANotWithEnvironmentsException;
import com.contentful.java.cma.model.CMAUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.contentful.java.cma.Constants.OCTET_STREAM_CONTENT_TYPE;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static okhttp3.MediaType.parse;

/**
 * Upload Module.
 * <p>
 * This module will take care of all `upload.contentful.com` related issues, as in directly
 * uploading a file to Contentful, receiving it's id.
 */
public final class ModuleUploads extends AbsModule<ServiceUploads> {
  final Async async;

  public ModuleUploads(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  /**
   * Tries to read all content from a give stream.
   *
   * @param stream stream to be read.
   * @return bytes of the stream
   * @throws IOException if the stream is not accessible
   * @throws IOException if the stream did not contain data.
   */
  static byte[] readAllBytes(InputStream stream) throws IOException {
    int bytesRead = 0;
    byte[] currentChunk = new byte[255];
    final List<byte[]> chunks = new ArrayList<byte[]>();
    while ((bytesRead = stream.read(currentChunk)) != -1) {
      chunks.add(copyOf(currentChunk, bytesRead));
    }

    if (chunks.size() <= 0) {
      throw new IOException("Stream did not contain any data. Please provide data to upload.");
    }

    int size = 0;
    for (byte[] chunk : chunks) {
      size += chunk.length;
    }

    final byte[] content = new byte[size];
    int position = 0;
    for (byte[] chunk : chunks) {
      arraycopy(chunk, 0, content, position, chunk.length);
      position += chunk.length;
    }
    return content;
  }

  @Override protected ServiceUploads createService(Retrofit retrofit) {
    return retrofit.create(ServiceUploads.class);
  }

  /**
   * Create a new upload on the configured space.
   * <p>
   * Once an upload is created, you can use it in {@link ModuleAssets} through
   * {@link CMAClient#assets()} for creating an asset based on a local file.
   *
   * @param stream the actual binary representation of the upload. Cannot be null.
   * @return the upload created, containing the id to be used further on.
   * @throws IllegalArgumentException        if spaceId is null.
   * @throws IllegalArgumentException        if stream is null.
   * @throws java.io.IOException             if the stream could not be read.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAUpload create(InputStream stream) throws IOException {
    throwIfEnvironmentIdIsSet();

    return create(spaceId, stream);
  }

  /**
   * Create a new upload.
   * <p>
   * Once an upload is created, you can use it in {@link ModuleAssets} through
   * {@link CMAClient#assets()} for creating an asset based on a local file.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId a nonnull id representing the space to add the upload to.
   * @param stream  the actual binary representation of the upload. Cannot be null.
   * @return the upload created, containing the id to be used further on.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if stream is null.
   * @throws java.io.IOException      if the stream could not be read.
   */
  public CMAUpload create(String spaceId, InputStream stream) throws IOException {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(stream, "stream");

    final byte[] content = readAllBytes(stream);

    final RequestBody payload = RequestBody.create(parse(OCTET_STREAM_CONTENT_TYPE), content);
    return service.create(spaceId, payload).blockingFirst();
  }

  /**
   * Get information about a given upload on the configured space.
   *
   * @param uploadId what id does the upload have?
   * @return an CMAUpload based on this id and space combination.
   * @throws IllegalArgumentException        if configured spaceId is null.
   * @throws IllegalArgumentException        if uploadId is null.
   * @throws CMANotWithEnvironmentsException if environmentId was set using
   *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
   * @see CMAClient.Builder#setSpaceId(String)
   */
  public CMAUpload fetchOne(String uploadId) {
    throwIfEnvironmentIdIsSet();

    return fetchOne(spaceId, uploadId);
  }

  /**
   * Get information about the given upload.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId  which space is this upload hosted under?
   * @param uploadId what id does the upload have?
   * @return an CMAUpload based on this id and space combination.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if uploadId is null.
   */
  public CMAUpload fetchOne(String spaceId, String uploadId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(uploadId, "uploadId");

    return service.fetchOne(spaceId, uploadId).blockingFirst();
  }

  /**
   * Delete a given upload again.
   *
   * @param upload upload
   * @return response code, 204 on success.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if uploadId is null.
   */
  public int delete(CMAUpload upload) {
    final String uploadId = getResourceIdOrThrow(upload, "upload");
    final String spaceId = getSpaceIdOrThrow(upload, "upload");

    final Response<Void> response = service.delete(spaceId, uploadId).blockingFirst();
    return response.code();
  }

  /**
   * @return a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  public final class Async {
    /**
     * Create a new upload, asynchronously.
     * <p>
     * Once an upload is created, you can use it in CMAClient#assets for creating an asset
     * based on a local file.
     *
     * @param stream   the actual binary representation of the upload. Cannot be null.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if file is null.
     * @throws IllegalStateException           if something in the transmittal went wrong.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAUpload> create(
        final InputStream stream,
        CMACallback<CMAUpload> callback) {
      return defer(new DefFunc<CMAUpload>() {
        @Override CMAUpload method() {
          try {
            return ModuleUploads.this.create(stream);
          } catch (IOException e) {
            throw new IllegalStateException("IO exception while creating asset.", e);
          }
        }
      }, callback);
    }

    /**
     * Create a new upload, asynchronously.
     * <p>
     * Once an upload is created, you can use it in CMAClient#assets for creating an asset
     * based on a local file.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  a nonnull id representing the space to add the upload to.
     * @param stream   the actual binary representation of the upload. Cannot be null.
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if file is null.
     * @throws IllegalStateException    if something in the transmittal went wrong.
     */
    public CMACallback<CMAUpload> create(
        final String spaceId,
        final InputStream stream,
        CMACallback<CMAUpload> callback) {
      return defer(new DefFunc<CMAUpload>() {
        @Override CMAUpload method() {
          try {
            return ModuleUploads.this.create(spaceId, stream);
          } catch (IOException e) {
            throw new IllegalStateException("IO exception while creating asset.", e);
          }
        }
      }, callback);
    }

    /**
     * Get information about the given upload, asynchronously.
     *
     * @param uploadId what id does the upload have?
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException        if configured spaceId is null.
     * @throws IllegalArgumentException        if uploadId is null.
     * @throws CMANotWithEnvironmentsException if environmentId was set using
     *                                         {@link CMAClient.Builder#setEnvironmentId(String)}.
     * @see CMAClient.Builder#setSpaceId(String)
     */
    public CMACallback<CMAUpload> fetchOne(
        final String uploadId,
        CMACallback<CMAUpload> callback) {
      return defer(new DefFunc<CMAUpload>() {
        @Override CMAUpload method() {
          return ModuleUploads.this.fetchOne(uploadId);
        }
      }, callback);
    }

    /**
     * Get information about the given upload, asynchronously.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and will ignore
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId  which space is this upload hosted under?
     * @param uploadId what id does the upload have?
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if uploadId is null.
     */
    public CMACallback<CMAUpload> fetchOne(
        final String spaceId,
        final String uploadId,
        CMACallback<CMAUpload> callback) {
      return defer(new DefFunc<CMAUpload>() {
        @Override CMAUpload method() {
          return ModuleUploads.this.fetchOne(spaceId, uploadId);
        }
      }, callback);
    }

    /**
     * Delete a given upload again, asynchronously.
     *
     * @param upload   upload
     * @param callback the callback to be informed about success or failure.
     * @return the callback passed in.
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if uploadId is null.
     */
    public CMACallback<Integer> delete(
        final CMAUpload upload,
        CMACallback<Integer> callback) {
      return defer(new DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleUploads.this.delete(upload);
        }
      }, callback);
    }
  }
}
