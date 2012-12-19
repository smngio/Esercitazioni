package com.example.drawsender;


import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

	private Paint paint;
	private Path path;
	private XMPPConnection connection;

	public MyView(Context context, XMPPConnection con) {
		super(context);
		this.connection = con;
		this.paint = new Paint();
		this.path = new Path();
		paint.setDither(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(15);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * @param args
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawPath(path, paint);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int eventaction = event.getAction();
		int touchx = (int) event.getX();
		int touchy = (int) event.getY();
		String mex[] = new String[3];
		String pack;
		short type = 0;
		Message msg= new Message();
		msg.setTo("fioravanti@ppl.eln.uniroma2.it");
		
		switch (eventaction) {
		
		case MotionEvent.ACTION_DOWN:
			
			type = 1;
			
			path.moveTo(touchx, touchy);
			invalidate();
			
			mex[0] = Short.toString(type);
			mex[1] = Integer.toString(touchx);
			mex[2] = Integer.toString(touchy);
			pack = mex[0].concat(" ").concat(mex[1]).concat(" ").concat(mex[2]);
			msg.setBody(pack);
			connection.sendPacket(msg);
			break;
			
		case MotionEvent.ACTION_MOVE:
			
			type = 2;
			
			path.lineTo(touchx, touchy);
			path.moveTo(touchx, touchy);
			invalidate();
			
			mex[0] = Short.toString(type);
			mex[1] = Integer.toString(touchx);
			mex[2] = Integer.toString(touchy);
			pack = mex[0].concat(" ").concat(mex[1]).concat(" ").concat(mex[2]);
			msg.setBody(pack);
			connection.sendPacket(msg);
			break;
			
		case MotionEvent.ACTION_UP:
			
			type = 0;
			path.close();
			invalidate();
			mex[0] = Short.toString(type);
			pack = mex[0];
			msg.setBody(pack);
			connection.sendPacket(msg);
			break;
			
		default:
			
			break;
		}
		return true;
	}
	
	public void refresh(){
		this.path.reset();
        invalidate();
        Message msg= new Message();
        msg.setTo("fioravanti@ppl.eln.uniroma2.it");
        msg.setBody("clear");
        connection.sendPacket(msg);
        
	}

}
