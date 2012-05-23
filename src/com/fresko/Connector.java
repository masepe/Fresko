package com.fresko;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;

public class Connector {

	public static final String DEFAULT_SERVICE_URL = "http://172.17.37.69:8080/task3/puzzle";
	private static final Object[][] EMPTPY_SERVICE_RESPONCE = new Object[1][1];
	private String m_url;

	public Connector(String url) {
		m_url = url;
	}

	protected InputStream connect() throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(m_url);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				return instream;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		throw new RuntimeException(m_url + " connection failure!");
	}

	public Drawable[][] connectAsArrayOfImages() throws IOException {
		Object[][] responseArray = EMPTPY_SERVICE_RESPONCE;
		InputStream ip = connect();

		ObjectInputStream inputStream = null;

		try {

			// Construct the ObjectInputStream object
			inputStream = new ObjectInputStream(ip);

			Object obj = null;

			if ((obj = inputStream.readObject()) != null) {

				responseArray = (Object[][]) obj;
				System.out.println((Integer.toString(responseArray.length))
						+ " elements fetched");

			}

		} catch (EOFException ex) { // This exception will be caught when EOF is
									// reached
			System.out.println("End of file reached.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectInputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return convertToArrayOfDrawables(responseArray);
	}

	private Drawable[][] convertToArrayOfDrawables(Object[][] responseArray) {
		Drawable[][] picturesMatrix = new Drawable[responseArray.length][responseArray[0].length];
		for (int i = 0; i < responseArray.length; i++)
			for (int j = 0; j < responseArray[i].length; j++) {
				picturesMatrix[i][j] = convertStreamToDrawable(new ByteArrayInputStream(
						(byte[]) responseArray[i][j]));
			}
		return picturesMatrix;
	}

	private static Drawable convertStreamToDrawable(InputStream ip) {
		Drawable picture = null;
		String srcName = "image";
		picture = Drawable.createFromStream(ip, srcName);
		return picture;
	}

	public static void main(String... strings) {
		Connector con = new Connector("http://172.17.37.69:8080/task3/puzzle");
		try {
			con.connectAsArrayOfImages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
