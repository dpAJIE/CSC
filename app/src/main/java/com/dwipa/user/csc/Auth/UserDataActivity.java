package com.dwipa.user.csc.Auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dwipa.user.csc.Activities.MainActivity;
import com.dwipa.user.csc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserDataActivity extends AppCompatActivity {
    private static final String WAJIB = "Wajib diisi" ;
    private EditText inputUsername, inputBirthdate, inputNamaAhass, inputNomorAhass;
    private Button signupBtn, backBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private Spinner spinner;
    private static final String[] paths = {"Pilih Jenis Kelamin","Pria","Wanita"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        inputUsername = (EditText) findViewById(R.id.user_name);
        inputBirthdate = (EditText) findViewById(R.id.birth_date);
        inputNamaAhass = (EditText) findViewById(R.id.nama_ahass);
        inputNomorAhass = (EditText) findViewById(R.id.nomor_ahass);
        spinner = (Spinner) findViewById(R.id.spinner);
        signupBtn = (Button) findViewById(R.id.sign_up_button);
        backBtn = (Button) findViewById(R.id.btn_back);


        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        //Dropdown
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserDataActivity.this, R.layout.spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDataActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        //Date picker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.US);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                inputBirthdate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        inputBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserDataActivity.this,date,myCalendar
                        .get(Calendar.DAY_OF_MONTH),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.YEAR)).show();
            }
        });
    }

    private void submitPost() {
        String nama = inputUsername.getText().toString();
        String alamat = inputNamaAhass.getText().toString();
        String tglLahir = inputBirthdate.getText().toString();
        String noHape = inputNomorAhass.getText().toString();
        String gender = spinner.getSelectedItem().toString();

        //validasi
        if (spinner.getSelectedItemPosition()==0){
            Toast.makeText(UserDataActivity.this,WAJIB,Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(nama)) {
            inputUsername.setError(WAJIB);
            return;
        }
        if (TextUtils.isEmpty(alamat)) {
            inputNamaAhass.setError(WAJIB);
            return;
        }
        if (TextUtils.isEmpty(tglLahir)) {
            inputBirthdate.setError(WAJIB);
            return;
        }
        if (TextUtils.isEmpty(noHape)){
            inputNamaAhass.setError(WAJIB);
            return;
        }

        final String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = mDataBase.child("Users").child(userId);
        myRef.child("nama").setValue(nama);
        myRef.child("nama_ahass").setValue(alamat);
        myRef.child("tglLahir").setValue(tglLahir);
        myRef.child("nomor_ahass").setValue(noHape);
        myRef.child("gender").setValue(gender);

        Intent intent = new Intent(UserDataActivity.this,MainActivity.class);
        Toast.makeText(UserDataActivity.this,"Pendaftaran berhasil",Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();

    }
}
