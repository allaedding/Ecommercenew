package com.allaeddine.guissou.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.allaeddine.guissou.ecommerce.ControlPanel;
import com.allaeddine.guissou.ecommerce.ToolType;
import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    ArrayList fullsongpath = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_categories);
        this.fullsongpath.addAll(ControlPanel.ToolType);
        ListView ls = (ListView)this.findViewById(R.id.listView);
        ls.setAdapter(new MyCustomAdapter());
        ls.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
                Intent intet = new Intent(Categories.this.getApplicationContext(), ControlPanel.class);
                intet.putExtra("ToolTypeID", ((ToolType)Categories.this.fullsongpath.get(var3)).ToolTypeID);
                Categories.this.startActivity(intet);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.add_tool_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem var1) {
        if(var1.getItemId() == R.id.gbackmenu) {
            this.finish();
        }

        return super.onOptionsItemSelected(var1);
    }

    private class MyCustomAdapter extends BaseAdapter {
        public int getCount() {
            return Categories.this.fullsongpath.size();
        }

        public String getItem(int var1) {
            return null;
        }

        public long getItemId(int var1) {
            return (long)var1;
        }

        public View getView(int var1, View var2, ViewGroup var3) {
            LayoutInflater var5 = Categories.this.getLayoutInflater();
            ToolType var4 = (ToolType)Categories.this.fullsongpath.get(var1);
            View var6 = var5.inflate(R.layout.settingitem, (ViewGroup)null);
            ((TextView)var6.findViewById(R.id.textView)).setText(var4.ToolTypeName);
            return var6;
        }
    }
}
