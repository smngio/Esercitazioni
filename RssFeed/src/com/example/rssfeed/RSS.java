package com.example.rssfeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class RSS extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss);
		Intent intent2 = getIntent(); //creo un oggetto intent e lo imposto con l'intent che ha creato la nuova activity
		
		String title = intent2.getExtras().getString("titolo"); // prendo tutti i dati inseriti nell'intent
		String data = intent2.getExtras().getString("data");
		String desc = intent2.getExtras().getString("descrizione");
		if(intent2.hasExtra("byteArray")) {
		    ImageView iv = (ImageView) findViewById(R.id.imageView1);
		    Bitmap b = BitmapFactory.decodeByteArray(intent2.getByteArrayExtra("byteArray"),0,intent2.getByteArrayExtra("byteArray").length);        
		    iv.setImageBitmap(b);
		}
		
		TextView tv = (TextView) findViewById(R.id.titolo); // creo i riferimenti alle text view e li imposto con i dati desiderati
		tv.setText(title);
		TextView tv1 = (TextView) findViewById(R.id.textView1);
		tv1.setText(data);
		TextView tv2 = (TextView) findViewById(R.id.textView2);
		tv2.setText(desc);
	}
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_rss_list, menu);
	        return true;
	    }
	

}
