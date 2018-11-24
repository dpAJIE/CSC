package com.dwipa.user.csc.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwipa.user.csc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DaftarSaActivity extends AppCompatActivity {
    private static final String WAJIB = "Wajib diisi" ;
    private EditText namaAhass, nomorAhass, kodeAktivasi;
    private Button btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_sa);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pendaftaran SA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        namaAhass = findViewById(R.id.nama_ahass);
        nomorAhass = findViewById(R.id.nomor_ahass);
        kodeAktivasi = findViewById(R.id.kode_aktivasi);
        btnDaftar = findViewById(R.id.btn_daftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        //Validasi
        final String nama = namaAhass.getText().toString().toUpperCase().trim();
        final String nomor = nomorAhass.getText().toString().trim();
        final String inputCode = kodeAktivasi.getText().toString().toUpperCase().trim();

        if (TextUtils.isEmpty(nama)) {
            namaAhass.setError(WAJIB);
            return;
        }
        if (TextUtils.isEmpty(nomor)) {
            namaAhass.setError(WAJIB);
            return;
        }
        //Firebase database checking
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference uidRef = rootRef.child("sacode").child(uid);
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String saCode = dataSnapshot.child("code").getValue(String.class);
                if (saCode != null && saCode.equals(inputCode)){

                    DatabaseReference myRef = rootRef.child("Users").child(uid);
                    myRef.child("namaAhass").setValue(nama);
                    myRef.child("nomorAhass").setValue(nomor);
                    myRef.child("sacode").setValue(saCode);

                    Intent intent = new Intent(DaftarSaActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(DaftarSaActivity.this,"fitur SA berhasil diaktifkan",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DaftarSaActivity.this,"silahkan hubungi admin untuk mendapatkan kode aktivasi",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        uidRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
