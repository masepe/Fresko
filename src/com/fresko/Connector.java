package com.fresko;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Connector {

	public static final String DEFAULT_SERVICE_URL = "http://172.17.37.69:8080/task3/puzzle";
	public static final String ANSWER_SERVICE_URL = "http://172.17.37.69:8080/task3/solution/";
	private static final Object[][] EMPTPY_SERVICE_RESPONCE = new Object[1][1];
	private static final String TEAM_ID = "fresko_team";
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

	public Bitmap[][] connectAsArrayOfImages() throws IOException {
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

		return convertToArrayOfBitmaps(responseArray);
	}

	private Bitmap[][] convertToArrayOfBitmaps(Object[][] responseArray) {
		Bitmap[][] picturesMatrix = new Bitmap[responseArray.length][responseArray[0].length];
		for (int i = 0; i < responseArray.length; i++)
			for (int j = 0; j < responseArray[i].length; j++) {
				picturesMatrix[i][j] = convertStreamToBitmap((byte[]) responseArray[i][j]);
			}
		return picturesMatrix;
	}


	private Bitmap convertStreamToBitmap(byte[] responseArray) {
		Bitmap pic = null;
		byte[] data = responseArray;
		ByteArrayInputStream bytes = new ByteArrayInputStream(data);
		BitmapDrawable bmd = new BitmapDrawable(bytes);
		pic = bmd.getBitmap();
		return pic;
	}

	public String sendAnswer(String secret) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(ANSWER_SERVICE_URL + secret);
		httpget.setHeader("teamId", TEAM_ID);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				
				return convertStreamToString(instream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Answer sending failure!!!");
		
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}

}
