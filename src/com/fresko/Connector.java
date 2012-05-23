package com.fresko;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;

public class Connector {
	
	private static final String EMPTY_STRING = "";
	private static final Drawable[][] EMPTY_ARRAY = new Drawable[1][1];
	private String m_url;

	public Connector(String url) {
		m_url = url;
	}

	protected  InputStream connect() throws IOException {
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

	private String connectAsString(String url) throws IOException {
	    String result = EMPTY_STRING;
		InputStream ip = connect();
        result = convertStreamToString(ip);
        ip.close();
	    return result;
	}

	public Drawable[][] connectAsArrayOfImages() throws IOException {
		Drawable[][] result = EMPTY_ARRAY;
		Vector row = new Vector();
        InputStream ip = connect();
        
        String nameop="";

            ObjectInputStream inputStream = null;
            
            try {
                
                //Construct the ObjectInputStream object
                inputStream = new ObjectInputStream(ip);
                
                Object obj = null;
                
                while ((obj = inputStream.readObject()) != null) {
                    
                     row.add(obj);
                     System.out.println((obj).toString());
                    
                }
                
             
            } catch (EOFException ex) { //This exception will be caught when EOF is reached
                System.out.println("End of file reached.");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                //Close the ObjectInputStream
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        
	    return result;
	}


	private static Drawable convertStreamToDrawable(InputStream ip) {
		Drawable picture = null;
		String srcName = "image";
		picture = Drawable.createFromStream(ip, srcName);
		return picture;
	}

	private static String convertStreamToString(InputStream input) {
	    /*
	     * To convert the InputStream to String we use the
	     * BufferedReader.readLine() method. We iterate until the BufferedReader
	     * return null which means there's no more data to read. Each line will
	     * appended to a StringBuilder and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    StringBuilder buffer = new StringBuilder();
	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            input.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return buffer.toString();
	}

	
	public static void main(String...strings ) {
		Connector con = new Connector("http://172.17.37.69:8080/task3/puzzle");
		try {
			con.connectAsArrayOfImages();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
