package com.example.rssfeed;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;



public class RSSlist extends ListActivity {
	
	public int dim;
	public RssHandler handler[];   // array di Rss handler
	public String url[];		   // array di stringhe che contengono gli url degli RSS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // codice per eseguire le connessioni sul main thread 
        StrictMode.setThreadPolicy(policy);															// per android 4.0 in su
        this.url=getResources().getStringArray(R.array.array_url); // url è settato passandogli una risorsa array xml
        this.dim = url.length; // si imposta la dimensione dell'array Rss handler in base alla dimensione di url (rapporto 1:1)
        this.set_handler(); // metodo che inizializza ogni elemento dell'array handler[]
        
        try {
        	
        	for(int j=0;j<dim;j++){ // si esegue un ciclo for in modo da settare tutti gli attributi dell'array handler[]
        		
        		SAXParserFactory factory=SAXParserFactory.newInstance();
        		SAXParser parser=factory.newSAXParser();
        		InputStream in = new URL(url[j]).openStream(); //connessione http al browser
        		XMLReader reader = parser.getXMLReader();
        		reader.setContentHandler(handler[j]);
        		reader.parse(new InputSource(in));
        	}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        ArrayList<Map<String, Object>> listaDati = new ArrayList<Map<String, Object>>(); // si inizializza un ArrayList
        listaDati = riempilistaDati(listaDati); // è un metodo per riempire l'ArrayList con i dati desiderati (titolo e data degli RSS)
        
        String[] from = {"titolo", "data"}; // sono stringhe che contengono nomi di riferimento
        
        int[] views = {R.id.text1, R.id.text2}; // sono le textviews in cui dovranno comparire i dati desiderati
        
        SimpleAdapter mioAdapter = new SimpleAdapter (this, listaDati, R.layout.item, from, views); // nel simpleAdapter si passa la classe, la struttura dati, il riferimento al layout dell'item, le stringhe con i nomi di riferimento e le views
        this.setListAdapter(mioAdapter);
        ListView lv = getListView(); // creo un oggetto listView e mi faccio restituire la listview dell activity corrente
        lv.setOnItemClickListener(new OnItemClickListener() {
      
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
            	Intent intent1 = new Intent(RSSlist.this, RSS.class); 	// creo un nuovo intent
            	intent1.putExtra("titolo", handler[position].title);  	// carico nell'intent tutti i dati che voglio passare alla nuova activity
            	intent1.putExtra("data", handler[position].date); 		// mediante l'int position prelevo il dato desiderato
            	intent1.putExtra("descrizione", handler[position].description);
            	Bitmap b = handler[position].getImage();
            	if(b!=null){
            	ByteArrayOutputStream bs = new ByteArrayOutputStream();
            	b.compress(Bitmap.CompressFormat.PNG, 60, bs);
            	intent1.putExtra("byteArray", bs.toByteArray());
            	}
            	startActivity(intent1); //avvio l'activity
            	
            }
        
        });
        
        
    }
    
        protected void set_handler() {
        	
        	this.handler = new RssHandler[dim];
        	for(int i=0;i<dim;i++){
        		handler[i] = new RssHandler();
        	}
		
	}

		private ArrayList<Map<String, Object>> riempilistaDati(ArrayList<Map<String, Object>> listaDati) {
     
        	for(int i=0;i<dim;i++){
        	
        		listaDati.add(creaMappa(handler[i].title, handler[i].date));

        	}
        	
            return listaDati;
        }
     
        private Map<String, Object> creaMappa(String titolo, String data) {
     
        	Map<String, Object> map = new HashMap<String, Object>();
     
        	map.put("titolo", titolo);
        	map.put("data", data);
     
        	return map;
        }   
    
  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_rss_list, menu);
        return true;
    }
}
