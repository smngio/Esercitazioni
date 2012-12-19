package com.example.drawreciver;


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class MyView extends View {
	
	private int type;
	private int x;
	private int y;
	private String tmp[];
	private Paint paint;
	private Path path;
	private XMPPConnection connection;

	public MyView(Context context, XMPPConnection con) {
		super(context);
		this.connection = con;
		tmp = new String[3];
		paint = new Paint();
		path = new Path();
		paint.setDither(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(15);
		// TODO Auto-generated constructor stub

        
        connection.addPacketListener(new PacketListener() {
			
			@Override
			public void processPacket(Packet arg0) {
				// TODO Auto-generated method stub
				Message msg = (Message) arg0;
				final String body = msg.getBody();
				int start = 0;
				int cont = 0;
				if(body.trim().equals("clear")){
					
					
					post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							refresh();
						
						}
					});
					
					
				}
				
			
				else{	
				
				for(int i=0; i<body.length(); i++){

					if(body.charAt(i) == ' '){
						tmp[cont] = body.substring(start, i);
						i++;
						start=i;
						cont++;
					}
					tmp[cont] = body.substring(start, body.length());
				}
				type = Integer.parseInt(tmp[0].trim());
				x = Integer.parseInt(tmp[1].trim());
				y = Integer.parseInt(tmp[2].trim());
				
				post(new Runnable() {
					
					@Override
					public void run() {
						
						switch (type) {
						
						case 0:
							
							path.close();
							invalidate();
						
						case 1:
							
							path.moveTo(x, y);
							invalidate();
							break;
								
						case 2:
							
							path.lineTo(x, y);
							path.moveTo(x, y);
							invalidate();
							break;
							
						default:
							
							path.close();
							invalidate();
							break;
						}
						
						
					}
				});
				
			}}
		}, new MessageTypeFilter(Message.Type.normal));
        
	}

	/**
	 * @param args
	 */
	@Override
	protected void onDraw(Canvas c) {
		// TODO Auto-generated method stub
		
		c.drawPath(path, paint);
	}
	
	
	public void refresh(){
		this.path.reset();
        invalidate();
        
	}

}
