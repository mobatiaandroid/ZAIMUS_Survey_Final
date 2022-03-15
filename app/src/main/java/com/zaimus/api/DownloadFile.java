package com.zaimus.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Environment;

public class DownloadFile {
	
	public void DownloadFromUrl(String fileURL, String fileName) {
		try {
			URL url = new URL(fileURL); // you can write here any link
			File sdCard = Environment.getExternalStorageDirectory();
			
			File dir = new File (sdCard.getAbsolutePath() + "/media_data");
			dir.mkdirs();
			File file = new File(dir, fileName);
			//Log.v("Path",dir.toString());
			long startTime = System.currentTimeMillis();

			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();

			/*
			 * Define InputStreams to read from the URLConnection.
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			FileOutputStream fos = new FileOutputStream(file);
			ByteArrayBuffer baf = new ByteArrayBuffer(500);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
				fos.write(baf.toByteArray());
				baf.clear();
			}

			/* Convert the Bytes read to a String. */
			fos.close();


		}
		catch(FileNotFoundException e){
			//Log.e("File not found!",""+e);
		}
		catch (IOException e) {

		}

	}
}
