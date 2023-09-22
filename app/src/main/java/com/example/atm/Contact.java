package com.example.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.Contract;

import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class Contact extends AppCompatActivity {

    private static final int READ_CONTACTS = 80;
    private static final String TAG = Contact.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permission == PackageManager.PERMISSION_GRANTED){
            readContacts();
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},READ_CONTACTS);
        }

    }

    private void readContacts() {
        //readcontact
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,null);
        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        List<Contact_data> contacts_datas = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(
                    nameColumnIndex);
            Contact_data contact_data = new Contact_data(id,name);
            int hasPhone = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Log.d(TAG, "readContacts: " + name);
            if(hasPhone == 1){
                Cursor c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        new String[]{String.valueOf(id)},
                        null);
                while (c2.moveToNext()){
                    String phone = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    Log.d(TAG, "readContacts: \t" + phone);
                    contact_data.getPhone().add(phone);
                }
            }
            contacts_datas.add(contact_data);
        }
        ContactAdapter adapter = new ContactAdapter(contacts_datas);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
        List<Contact_data> contact_datas;
        public ContactAdapter(List<Contact_data> contact_datas){
            this.contact_datas = contact_datas;
        }

        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2,parent,false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contact_data contact_data = contact_datas.get(position);
            holder.nameText.setText(contact_data.getName());
            StringBuilder sb = new StringBuilder();
            for (String phone : contact_data.getPhone()) {
                sb.append(phone);
                sb.append(" ");
            }
            holder.phoneText.setText(sb.toString());
        }

        @Override
        public int getItemCount() {
            return contact_datas.size();
        }

        public class ContactHolder extends RecyclerView.ViewHolder{
            TextView nameText;
            TextView phoneText;
            public ContactHolder(@NonNull View itemView) {
                super(itemView);
                nameText = itemView.findViewById(android.R.id.text1);
                phoneText = itemView.findViewById(android.R.id.text2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_CONTACTS)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts();
            }
        }
    }
}