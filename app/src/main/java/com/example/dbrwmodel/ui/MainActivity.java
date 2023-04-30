package com.example.dbrwmodel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbrwmodel.R;
import com.example.dbrwmodel.data.DatabaseAdapter;
import com.example.dbrwmodel.data.Profile;
import com.example.dbrwmodel.data.ProfileAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.text.Format;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textViewCount;
    DatabaseAdapter databaseAdapter;
    ProfileAdapter profileAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.listItem);
        textViewCount = findViewById(R.id.textCount);

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:{
                    //--------Тут начинаем задавать различные вопросы
                    Profile deletedProfile = databaseAdapter.getSinleProfile((long)viewHolder.itemView.getTag());
                    new AlertDialog.Builder(MainActivity.this)
                            //.setIcon(R.drawable.ic_delete)
                            .setTitle("Удаление пользователя")
                            .setMessage("Удалить пользователя " + deletedProfile.name + "?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    databaseAdapter.delete((long)viewHolder.itemView.getTag());
                                    onResume();
                                    //в качестве бонуса добавив внизу снэкбар для восстановления удаленной записи
                                    Snackbar.make(recyclerView, "Пользователь " + deletedProfile.name + " удален!", Snackbar.LENGTH_LONG)
                                            .setAction("Восстановить?", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    databaseAdapter.insert(deletedProfile);
                                                    onResume();
                                                }
                                            }).show();
                                }
                            })
                            // если передумали, то просто обновляем наш Recycler
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onResume();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    break;
                }
                case ItemTouchHelper.RIGHT:{
                    editProfile(recyclerView, (long)viewHolder.itemView.getTag());
                    break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        databaseAdapter = new DatabaseAdapter(MainActivity.this);
        databaseAdapter.open();
        profileAdapter = new ProfileAdapter(MainActivity.this, databaseAdapter.profiles());
        recyclerView.setAdapter(profileAdapter);
        textViewCount.setText("Найдено элементов: " +  profileAdapter.getItemCount());
        profileAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }

    public void addProfile(View view) {
        Dialog addDialog = new Dialog(this, R.style.Theme_DbRwModel);
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        addDialog.setContentView(R.layout.add_profile_dialog);

        EditText dialogSname = addDialog.findViewById(R.id.personSname);
        EditText dialogName = addDialog.findViewById(R.id.personName);
        EditText dialogAge = addDialog.findViewById(R.id.personAge);
        addDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname = dialogSname.getText().toString();
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());

                databaseAdapter.insert(new Profile(sname, name, age, R.drawable.ic_launcher_background));
                Toast.makeText(MainActivity.this, "Данные добавлены!", Toast.LENGTH_SHORT).show();
                onResume();
                addDialog.hide();
            }
        });
        addDialog.show();
    }
    public void editProfile(View view, long id) {
        Dialog editDialog = new Dialog(this, R.style.Theme_DbRwModel);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        editDialog.setContentView(R.layout.add_profile_dialog);

        EditText dialogSname = editDialog.findViewById(R.id.personSname);
        EditText dialogName = editDialog.findViewById(R.id.personName);
        EditText dialogAge = editDialog.findViewById(R.id.personAge);

        Profile profile = databaseAdapter.getSinleProfile(id);
        dialogSname.setText(profile.sName);
        dialogName.setText(profile.name);
        dialogAge.setText(String.valueOf(profile.age));

        editDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname = dialogSname.getText().toString();
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());

                databaseAdapter.update(new Profile(id, sname, name, age, R.drawable.ic_launcher_background));
                Toast.makeText(MainActivity.this, "Изменения внесены!", Toast.LENGTH_SHORT).show();
                onResume();
                editDialog.hide();
            }
        });
        editDialog.show();
    }
}