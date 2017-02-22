package com.contentful.java.cma;

import com.contentful.java.cma.lib.TestUtils;
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAUpload;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.contentful.java.cma.ModuleUploads.readAllBytes;
import static org.bouncycastle.util.Arrays.concatenate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnderstandingTests {

  private final static byte[] FIRST_BYTES = new byte[]{0, 1, 2, 3, 4, 5, 6};
  private final static byte[] SECOND_BYTES = new byte[]{10, 11, 12, 13, 14, 15, 16};

  private final Answer<Integer> firstAnswer = new Answer<Integer>() {
    @Override public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
      byte[] bytes = (byte[]) invocationOnMock.getArguments()[0];
      System.arraycopy(FIRST_BYTES, 0, bytes, 0, FIRST_BYTES.length);
      return FIRST_BYTES.length;
    }
  };

  private final Answer<Integer> secondAnswer = new Answer<Integer>() {
    @Override public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
      byte[] bytes = (byte[]) invocationOnMock.getArguments()[0];
      System.arraycopy(SECOND_BYTES, 0, bytes, 0, SECOND_BYTES.length);
      return SECOND_BYTES.length;
    }
  };

  @Mock
  public InputStream mockedStream;

  @Before
  public void setup() throws Exception {
    when(mockedStream.read(any(byte[].class)))
        .thenAnswer(firstAnswer)
        .thenAnswer(secondAnswer)
        .thenReturn(-1)
    ;
  }

  @Test
  public void testCreationOfByteArray() throws Exception {
    byte[] content = readAllBytes(mockedStream);

    assertEquals("correct length", FIRST_BYTES.length + SECOND_BYTES.length, content.length);
    assertArrayEquals("correct elements",
        concatenate(FIRST_BYTES, SECOND_BYTES),
        content);
  }

  @Ignore
  @Test
  public void testE2EUpload() throws Exception {

    final String TOKEN = System.getenv("CMA_TOKEN");
    final String SPACE = System.getenv("CMA_SPACE");
    final String CORE_URL = System.getenv("CMA_URL");
    final String UPLOAD_URL = System.getenv("CMA_UPLOAD_URL");

    final CMAClient client = new CMAClient.Builder()
        .setAccessToken(TOKEN)
        .setCoreEndpoint(CORE_URL)
        .setUploadEndpoint(UPLOAD_URL)
        .build();

    final InputStream file = TestUtils.Companion.fileToInputStream("upload_post_payload.jpg");

    final CMAUpload uploadResponse = client.uploads().create(SPACE, file);
    assertEquals(uploadResponse.getSys().get("type"), "Upload");

    final CMAAsset asset = new CMAAsset();
    final Map<String, Object> map = new HashMap<String, Object>();
    map.put("fileName", "random_cat.jpg");
    map.put("upload", uploadResponse.getResourceId());
    map.put("contentType", "image/jpg");

    asset.setField("title", "my_id", "en-US");
    asset.setField("file", map, "en-US");

    final CMAAsset createdAsset = client.assets().create(SPACE, asset);

    final LinkedHashMap<String, Map<String, Object>> createdFields = createdAsset.getFields();
    final Map<String, Object> createdFile = createdFields.get("file");
    final Map<String, Object> createdLocalizedFile = (Map<String, Object>) createdFile.get("en-US");
    final Object uploadFrom = createdLocalizedFile.get("uploadFrom");
    final Object upload = createdLocalizedFile.get("upload");

    assertEquals(uploadResponse.getResourceId(), upload);
    assertEquals(uploadResponse.getResourceId(), uploadFrom);
  }
}
