package com.vikram.bookmarkproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity 
{
	Button b1;
	EditText et1,et2;
	TextView tv3;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		createDatabaseAtFirstTime();
		
		et1=(EditText) findViewById(R.id.editText1);
		et2=(EditText) findViewById(R.id.editText2);
		tv3=(TextView) findViewById(R.id.textView3);
		tv3.setVisibility(View.INVISIBLE);
		b1=(Button) findViewById(R.id.button1);
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				try
				{
					String username,password;
					username=et1.getText().toString().trim();
					password=et2.getText().toString().trim();
					
					SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
					String sql="select  *  from login where username='"+username+"' and password='"+password+"'";
					Cursor c= db.rawQuery(sql, null);
					int count=c.getCount();
					
					
					if(count>0)
					{
						SharedPreferences sp= getSharedPreferences("mypref", MODE_PRIVATE);
						Editor editor=sp.edit();
						editor.clear();
						editor.putString("username", "+username+");
						editor.putString("password", "+password+");
						editor.commit();
						Intent i1=new Intent(Login.this,MainActivity.class);
						startActivity(i1);
						
					}
					else
					{
						tv3.setVisibility(View.VISIBLE);
						tv3.setText("Either username or password is incorrect");
					}
					db.close();
				}
				catch(Exception e) 
				{
					Toast.makeText(Login.this, ""+e, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	private void createDatabaseAtFirstTime() 
	{
		
		try
		{
			SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
			db.execSQL("CREATE TABLE IF NOT EXISTS login(username varchar(100),password varchar(100));");
			db.execSQL("CREATE TABLE IF NOT EXISTS bookmark(pageid integer,name varchar(250),pageurl varchar(250),add_date date);");
			Cursor c=db.rawQuery("select * from login", null);
			
			
			if(c.getCount()>0)
			{
				String str="";
				for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
				{
					str=str+c.getString(c.getColumnIndex("username"))+"\n";
					str=str+c.getString(c.getColumnIndex("password"))+"\n";
				}
				
			}
			else
			{
				db.execSQL("insert into login values('admin','admin');");
				
			}
			
			db.close();
		}
		catch(Exception e)
		{
			Toast.makeText(Login.this, ""+e, Toast.LENGTH_LONG).show();
		}
	}
}
