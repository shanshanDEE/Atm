package com.example.atm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.atm.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //private static final int REQUSEST_LOGIN = 100;
    boolean logon = false;
    //String[] functions = null;
    private ActivityMainBinding binding;
    private List<Function> functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                //Log.d(TAG, "RESULT OK");
                            }
                        }
                    }
            );
            if(!logon) {
                Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                launcher.launch(intent);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
        //Recycler
        setupFunction();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //Adapter
        //FunctionAdapter adapter = new FunctionAdapter(this);
        IconAdapter adapter = new IconAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setupFunction(){
        functions = new ArrayList<>();
        String[] funcs = getResources().getStringArray(R.array.functions);
        functions.add(new Function(funcs[0],R.drawable.func_transaction));
        functions.add(new Function(funcs[1],R.drawable.func_balance));
        functions.add(new Function(funcs[2],R.drawable.func_finance));
        functions.add(new Function(funcs[3],R.drawable.func_contacts));
        functions.add(new Function(funcs[4],R.drawable.func_exit));
    }

    public  class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_icon_xml,parent, false);
            return new IconHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder holder, int position) {
            final Function function = functions.get(position);
            holder.nameText.setText(function.getName());
            holder.iconImage.setImageResource(function.getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked(function);
                }
            });
        }

        @Override
        public int getItemCount() {
            return functions.size();
        }

        public class IconHolder extends RecyclerView.ViewHolder{
            ImageView iconImage;
            TextView nameText;
            public IconHolder(@NonNull View itemView) {
                super(itemView);
                iconImage = itemView.findViewById(R.id.item_icon);
                nameText = itemView.findViewById(R.id.item_name);
            }
        }
    }

    /*private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked:" + function.getName());
        switch (function.getIcon()){
            case R.drawable.func_transaction:
                break;
            case R.drawable.func_balance:
                    break;
            case R.drawable.func_finance:
                break;
            case R.drawable.func_contacts:
                    break;
            case R.drawable.func_exit:
                finish();
                break;
        }
    }*/

    private void itemClicked(Function function) {
        Log.d(TAG, "itemClicked:" + function.getName());

        // 根據圖標執行不同的操作
        int iconResId = function.getIcon();

        if (iconResId == R.drawable.func_transaction) {
            // 執行交易功能
        } else if (iconResId == R.drawable.func_balance) {
            // 執行餘額功能
        } else if (iconResId == R.drawable.func_finance) {
            // 執行財務功能
        } else if (iconResId == R.drawable.func_contacts) {
            Intent contacts = new Intent(this,Contact.class);
            startActivity(contacts);
            // 執行聯繫人功能
        } else if (iconResId == R.drawable.func_exit) {
            finish(); // 結束 Activity
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == REQUSEST_LOGIN) {
            if (resultCode != RESULT_OK) {
                finish();
            }
        //}
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 */
}