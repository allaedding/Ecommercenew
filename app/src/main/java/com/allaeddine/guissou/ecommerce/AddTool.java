package com.allaeddine.guissou.ecommerce;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddTool extends AppCompatActivity {
    int AddImagesPlease=0;
    int RESULT_LOAD_IMAGE=1;
    String TempToolID;
    GridView ls;
    MyCustomAdapter myadapter;
    Spinner spinner ;
    AQuery aq;
    public Bitmap bitmap; // the selected image from gellary
    ArrayList<AddToolItem> fullsongpath =new ArrayList<AddToolItem>();




    @Override
    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        this.setContentView(R.layout.activity_add_tool);



        int i = (new Random()).nextInt(7000000);
        this.TempToolID = SaveSettings.UserID + "000000" + (i + 5000);
        this.fullsongpath.add(new AddToolItem("NewImage", R.drawable.loadimage, (Bitmap)null));
        this.ls = (GridView)this.findViewById(R.id.gridView);
        this.myadapter = new MyCustomAdapter(this.fullsongpath);
        this.ls.setAdapter(this.myadapter);
        this.aq = new AQuery(this);
        this.spinner = (Spinner)this.findViewById(R.id.spinner);
        if(ControlPanel.ToolType.size() == 0) {
            this.finish();
        } else {
            String[] ToolTypeArray = new String[ControlPanel.ToolType.size()];

            for(i = 0; i < ControlPanel.ToolType.size(); ++i) {
                ToolTypeArray[i] = ((ToolType)ControlPanel.ToolType.get(i)).ToolTypeName;
            }

            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ToolTypeArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(spinnerArrayAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_tool_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int var1, KeyEvent var2) {
        if(var1 == 4) {
            this.startActivity(new Intent(this.getApplicationContext(), ControlPanel.class));
        }

        return super.onKeyDown(var1, var2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.gbackmenu) {

            this.startActivity(new Intent(this.getApplicationContext(), ControlPanel.class));
        }

        return super.onOptionsItemSelected(item);
    }


    // when select image from gellary it will loaded here
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            //loading images
            int index = fullsongpath.size() - 1;
            if (index < 0){
                index = 0;
            }
            this.fullsongpath.remove(index);
            this.fullsongpath.add(index, new AddToolItem("Loading", R.drawable.hu,(Bitmap) null));
            this.myadapter.notifyDataSetChanged();

            ImageView IVADDPIC=new ImageView(this);
            Uri selectedImage = data.getData();
            String[] filePathColumn = new String[] {"_data"};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, (String)null, (String[])null, (String)null);
            cursor.moveToFirst();
            String columnIndex = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            IVADDPIC.setImageBitmap(BitmapFactory.decodeFile(columnIndex));
            this.bitmap = Bitmap.createScaledBitmap(((BitmapDrawable)IVADDPIC.getDrawable()).getBitmap(), 500, 500, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] Imagedata = baos.toByteArray();

            String encodedImageData = Base64.encodeToString(Imagedata, 0);

            HashMap var10 = new HashMap();
            var10.put("image", encodedImageData);
            var10.put("TempToolID", this.TempToolID);
            String url = SaveSettings.ServerURL + "UsersWS.asmx";
            (new MyAsyncGetNewNews()).execute(new String[]{url, encodedImageData, this.TempToolID});
        }
    }

    public void buAddTool(View view) {
        // Toast.makeText(getApplicationContext(),String.valueOf(spinner.getSelectedItemPosition()),Toast.LENGTH_LONG).show();
        EditText ToolName=(EditText)findViewById(R.id.EDTToolName);
        EditText ToolDes=(EditText)findViewById(R.id.EDToolDes);
        EditText  ToolPrice=(EditText)findViewById(R.id.EDTToolPrice);
        //validate user info
        if((ToolName.getText().length()>=2 )||(ToolDes.getText().length()>=2 ) ||(ToolPrice.getText().length()>=2 )) {



// check if he added images
        if(AddImagesPlease==0){
            Operations.DisplayMessage(this, getResources().getString(R.string.AddImagesPlease));
        }
        else {
            String url = SaveSettings.ServerURL + "UsersWS.asmx" + "/AddTools?UserID=" + SaveSettings.UserID + " &ToolName=" + ToolName.getText() + "&ToolDes=" + ToolDes.getText() + "&ToolPrice=" + ToolPrice.getText() + "&ToolTypeID=" + String.valueOf(ControlPanel.ToolType.get(spinner.getSelectedItemPosition()).ToolTypeID) + "&TempToolID=" + String.valueOf(TempToolID);

            aq.ajax(url, JSONObject.class, this, "jsonCallback");
        }
        } else {
            Operations.DisplayMessage(this, getResources().getString(R.string.AddAllinfo));
        }

    }
    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if(json != null){

            String msg=json.getString("Message");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            int IsAdded=json.getInt("IsAdded");
            if(IsAdded!=0){ // mean the tool is added go to manage tool later
                this.startActivity(new Intent(this.getApplicationContext(), MyTool.class));

            }
        }
    }
    //number of images that added
    // send image to the webservice


    public class MyAsyncGetNewNews extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String URL = (String) params[0];
            SoapObject var2 = new SoapObject("http://tempuri.org/", "UploadImage");
            var2.addProperty("image", params[1]);
            var2.addProperty("TempToolID", params[2]);
            SoapSerializationEnvelope var5 = new SoapSerializationEnvelope(110);
            var5.dotNet = true;
            var5.setOutputSoapObject(var2);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call("http://tempuri.org/UploadImage", var5);
                Object var8 = var5.bodyIn;
                SoapObject var6 = (SoapObject)var5.getResponse();
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String var1) {
            int var3 = AddTool.this.fullsongpath.size() - 1;
            int var2 = var3;
            if(var3 < 0) {
                var2 = 0;
            }

            AddTool.this.fullsongpath.remove(var2);
            AddTool.this.fullsongpath.add(var2, new AddToolItem("NewImage", 2130837663, (Bitmap)null));
            AddTool.this.fullsongpath.add(var2, new AddToolItem("path", 2130837634, AddTool.this.bitmap));
            AddTool.this.myadapter.notifyDataSetChanged();
            AddTool.this.ls.smoothScrollToPosition(AddTool.this.fullsongpath.size() - 1);
            ++AddTool.this.AddImagesPlease;
        }



        protected void onPreExecute() {
        }

        protected void onProgressUpdate(String... var1) {

        }
    }



    private class MyCustomAdapter extends BaseAdapter {

        public ArrayList listnewsDataAdpater;

        public MyCustomAdapter(ArrayList var2) {
            this.listnewsDataAdpater = var2;
        }

        public int getCount() {
            return this.listnewsDataAdpater.size();
        }

        public String getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return (long)position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater var5 = AddTool.this.getLayoutInflater();
            final AddToolItem var6 = (AddToolItem)this.listnewsDataAdpater.get(position);
            if(var6.ImagePath.equals("Loading")) {
                convertView = var5.inflate(R.layout.add_tool_ticket_loading, (ViewGroup)null);
            } else {
                convertView = var5.inflate(R.layout.add_tool_ticket, (ViewGroup)null);
                ImageView var4 = (ImageView)convertView.findViewById(R.id.IVloadimage);
                if(var6.ImagePath.equals("NewImage")) {
                    var4.setImageResource(var6.Image);
                } else {
                    var4.setImageBitmap(var6.bitmap);
                }

                var4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View var1) {
                        if(var6.ImagePath.equals("NewImage")) {
                            Intent var2 = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            AddTool.this.startActivityForResult(var2, AddTool.this.RESULT_LOAD_IMAGE);
                        }

                    }
                });
            }

            return convertView;
        }
    }


}
