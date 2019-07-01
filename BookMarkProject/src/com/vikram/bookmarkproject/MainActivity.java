package com.vikram.bookmarkproject;

import android.R.string;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity 
{

	WebView webView;
	Button b1,b2,b3,b4,b5,b6;
	EditText t1;
	String bookmark="";
	String url="";
	String url_array[];
	Context context=this;
	int flag=0;
	MyBrowser myBrowser=new MyBrowser();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences pref=getSharedPreferences("mypref", MODE_PRIVATE);
		String name=pref.getString("username", ""); 
		String pass=pref.getString("password", ""); 
		
		
		
		b1=(Button) findViewById(R.id.button1); 
		b2=(Button) findViewById(R.id.button2); 
		b3=(Button) findViewById(R.id.button3); 
		b4=(Button) findViewById(R.id.button4); 
		b5=(Button) findViewById(R.id.button5); 
		b6=(Button) findViewById(R.id.button6);
		
		if(name!=null)
		{
			t1=(EditText) findViewById(R.id.editText1);
			webView = (WebView) findViewById(R.id.webView1);
			webView.loadUrl("http://www.google.co.in");
			webView.setWebViewClient(myBrowser);
					b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) 
				{
					url=t1.getText().toString();
					webView.getSettings().setBuiltInZoomControls(true);
				    webView.getSettings().setSupportZoom(true); 
				    webView.getSettings().setUseWideViewPort(true);
				    webView.getSettings().setLoadWithOverviewMode(true);
					webView.getSettings().setJavaScriptEnabled(true);
					webView.loadUrl(url);
					webView.setWebViewClient(myBrowser);
				}

				
			});
			
			b2.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View arg0) 
				{
					
					LayoutInflater li = LayoutInflater.from(context);
					View promptsView = li.inflate(R.layout.inputdialog, null);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					
					alertDialogBuilder.setView(promptsView);

					final EditText userInput = (EditText) promptsView
							.findViewById(R.id.editTextDialogUserInput);

					
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("OK",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							
							bookmark=userInput.getText().toString();
							
							
							try
							{
								SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
								String sql="select  *  from bookmark;";
								Cursor c= db.rawQuery(sql, null);
								int bid=c.getCount()+1;
								String pageurl=t1.getText().toString();
								sql="insert into bookmark(pageid,name,pageurl) values("+bid+",'"+bookmark+"','"+pageurl+"')";
								db.execSQL(sql);
								db.close();
								Toast.makeText(MainActivity.this, "Bookmark saved", Toast.LENGTH_LONG).show();
							}
							catch(Exception e)
							{
								Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
							}
							
							
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					
					alertDialog.show();
					
				}
			});
			
			b3.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View arg0) 
				{
					
					LayoutInflater li = LayoutInflater.from(context);
					View promptsView = li.inflate(R.layout.list_of_bookmarks, null);
					final ListView list=(ListView) promptsView.findViewById(R.id.listView1);
					
					
					try
					{
						SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
						String sql="select  *  from bookmark";
						Cursor c= db.rawQuery(sql, null);
						int count=c.getCount();
						
						db.close();
						String items[]=new String[count];
						url_array=new String[count];
						int i=0;
						for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
						{
							items[i]=c.getString(c.getColumnIndex("name"));
							url_array[i]=c.getString(c.getColumnIndex("pageurl"));
							i++;
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1, items);
						list.setAdapter(adapter);
						
						
						list.setOnItemClickListener(new OnItemClickListener() 
				        {
							@Override
				              public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
				              {
				            	  url=url_array[position];
				            	  Toast.makeText(getApplicationContext(),"Position :"+position+"  ListItem : " +url , Toast.LENGTH_LONG).show();
				              }
				         });

						
						
						
					}
					catch(Exception e)
					{
						
					}
					
					
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					
					alertDialogBuilder.setView(promptsView);
							 
					
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("Select",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							
						    	webView.loadUrl(url);
				  				webView.setWebViewClient(myBrowser);
				  				t1.setText(url);
						    	
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					
					alertDialog.show();
				}
			});
			
			b4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) 
				{
					LayoutInflater li = LayoutInflater.from(context);
					View promptsView = li.inflate(R.layout.change_password, null);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

					
					alertDialogBuilder.setView(promptsView);

					final EditText et1 = (EditText) promptsView.findViewById(R.id.editText1);
					final EditText et2 = (EditText) promptsView.findViewById(R.id.editText2);
					final EditText et3 = (EditText) promptsView.findViewById(R.id.editText3);
					
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("Change Password",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) 
						    {
						    	try
						    	{
						    		String password,newpassword,confirm;
									password=et1.getText().toString().trim().trim();
									newpassword=et2.getText().toString().trim();
									confirm=et3.getText().toString().trim();
									
									SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
									String sql="update login set password='"+newpassword+"' where username='vgt' and password='"+password+"'";
									db.execSQL(sql);
									Toast.makeText(MainActivity.this, "Password Changed", Toast.LENGTH_LONG).show();
									db.close();
						    	}
						    	catch(Exception e)
						    	{
						    		Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_LONG).show();
						    	}
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					
					alertDialog.show();
				}
			});
			
			
			b5.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) 
				{
					SharedPreferences sp= getSharedPreferences("mypref", MODE_PRIVATE);
					Editor editor=sp.edit();
					editor.remove("username");
					editor.remove("password");
					editor.commit();

					
					Intent i1=new Intent(MainActivity.this,Login.class);
					startActivity(i1);
				}
			});
		
			b6.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View arg0) 
				{
					
					LayoutInflater li = LayoutInflater.from(context);
					View promptsView = li.inflate(R.layout.list_of_bookmarks, null);
					final ListView list=(ListView) promptsView.findViewById(R.id.listView1);
					
					
					try
					{
						SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
						String sql="select  *  from bookmark";
						Cursor c= db.rawQuery(sql, null);
						int count=c.getCount();
						
						db.close();
						String items[]=new String[count];
						url_array=new String[count];
						int i=0;
						for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
						{
							items[i]=c.getString(c.getColumnIndex("name"));
							url_array[i]=c.getString(c.getColumnIndex("pageurl"));
							i++;
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1, items);
						list.setAdapter(adapter);
						
						
						list.setOnItemClickListener(new OnItemClickListener() 
				        {
							@Override
				              public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
				              {
				            	  url=url_array[position];
				            	  Toast.makeText(getApplicationContext(),"Position :"+position+"  ListItem : " +url , Toast.LENGTH_LONG).show();
				              }
				         });
					}
					catch(Exception e)
					{
						
					}
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

					
					alertDialogBuilder.setView(promptsView);
							 
					
					alertDialogBuilder.setCancelable(false);
					alertDialogBuilder.setPositiveButton("Delete",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) 
						    {
							
						    	SQLiteDatabase db=openOrCreateDatabase("mydatabase",MODE_PRIVATE,null);
								String sql="Delete from bookmark where pageurl='"+url+"'";
								db.execSQL(sql);
								db.close();
								Toast.makeText(MainActivity.this, " Bookmark Deleted Successfully", Toast.LENGTH_LONG).show();
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) 
						    {
						    	dialog.cancel();
						    }
						  });
					
					AlertDialog alertDialog = alertDialogBuilder.create();
					
					alertDialog.show();
				}
			});
			
		}
		else
		{
			AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
			adb.setMessage("YOU ARE LOGOUT");
			adb.setPositiveButton("ok", new DialogInterface.OnClickListener() 
			{
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					Intent i1=new Intent(MainActivity.this,Login.class);
					startActivity(i1);
					
				}
			});
			adb.create();
			adb.show();
			
		}
		
		
	}
	
	
	class MyBrowser extends WebViewClient 
	{
		 MyBrowser()
		 { 
			 super();
		 }
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) 
	      {
	         view.loadUrl(url);
	         return true;
	      }
	   }
}