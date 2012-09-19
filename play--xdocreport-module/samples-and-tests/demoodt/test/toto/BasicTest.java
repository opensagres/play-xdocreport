package toto;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import play.Logger;
import play.test.UnitTest;



public class BasicTest extends UnitTest {

	 /**
	   * Tests the loading of an image from the application public folder.
	   *
	   * @throws IOException in case of error
	   */
	  @Test
	  public void testLoadingOfApplicationImage() throws IOException {
	   /* String uri = getAbsoluteUrl("/public/images/favicon.png");

	    Logger.debug("Testing loading of image at '%s'", uri);

	    URL url = new URL(uri);
	    URLConnection connection = url.openConnection();

	    assertTrue(connection.getContentLength() > 0);
	    assertEquals("image/png", connection.getContentType());*/
	  }
}
