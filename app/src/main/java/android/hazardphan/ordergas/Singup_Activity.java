package android.hazardphan.ordergas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dangha.dev on 04/04/2017.
 */

public class Singup_Activity extends AppCompatActivity implements View.OnClickListener{
    EditText edtHoten, edtEmail, edtMatKhau, edtDiachi, edtSdt;
    Button btnDangky, btnHuy, btnShowPass;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        toolbar = (Toolbar) findViewById(R.id.id_toolbarDangKy);
        toolbar.setTitleTextColor(Color.WHITE);
        edtHoten = (EditText) findViewById(R.id.edt_hoTen);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtMatKhau = (EditText) findViewById(R.id.edt_pass);
        edtDiachi = (EditText) findViewById(R.id.edt_diaChi);
        edtSdt = (EditText) findViewById(R.id.edt_sdt);
        btnDangky = (Button) findViewById(R.id.btn_dangKyCreate);
        btnHuy = (Button) findViewById(R.id.btn_huyCreate);
        btnShowPass = (Button) findViewById(R.id.btn_showPass);
        btnDangky.setOnClickListener(this);
        btnHuy.setOnClickListener(this);
        setSupportActionBar(toolbar);

        btnShowPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        edtMatKhau.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        edtMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    public void CreateAcc(){
        if(edtHoten.getText().toString().isEmpty()){
            edtHoten.setError("Hãy nhập tên");
        }
        else if(edtEmail.getText().toString().isEmpty()){
            edtEmail.setError("Hãy nhập Email");
        }else if(edtMatKhau.getText().toString().isEmpty()){
            edtMatKhau.setError("Hãy nhập mật khẩu");
        }else if(edtDiachi.getText().toString().isEmpty()){
            edtSdt.setError("Hãy nhập địa chỉ");
        }else if(edtSdt.getText().toString().isEmpty()){
            edtSdt.setError("Hãy nhập SĐT");
        }else{
            String url = "http://goigas.96.lt/cuahang/dangky.php";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Post", response);
                    Toast.makeText(getApplicationContext(),"Đăng kí thành công!",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Add new ", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", edtHoten.getText().toString());
                    params.put("pass", edtMatKhau.getText().toString());
                    params.put("address",edtDiachi.getText().toString());
                    params.put("id",edtSdt.getText().toString());
                    params.put("email",edtEmail.getText().toString());
                    return params;
                }
            };

            queue.add(stringRequest); // Add the request to the RequestQueue.
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dangKyCreate:
                if(edtHoten.getText().toString().isEmpty()||edtEmail.getText().toString().isEmpty()
                        ||edtMatKhau.getText().toString().isEmpty()||edtDiachi.getText().toString().isEmpty()||edtSdt.getText().toString().isEmpty()){
                    Toast.makeText(this, "Bạn cần điền đầy đủ thông tin !!! Đăng Ký lỗi ", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Đưa thông tin lên server
                    CreateAcc();
                    // intent dữ liệu trở lại signin activity
                    Intent intent1 = new Intent(this, Signin_Activity.class); // tạo intent
                    Bundle bundle = new Bundle(); // tạo bunble để truyền sang 2 editext
                    String user = edtEmail.getText().toString().trim();
                    String pass = edtMatKhau.getText().toString().trim();
                    bundle.putString("flag", "1"); // put vào bundle
                    bundle.putString("user", user);
                    bundle.putString("pass", pass);
                    intent1.putExtra("login", bundle);
                    startActivity(intent1);

                }
                break;
            case R.id.btn_huyCreate:
                finish();
                break;
        }
    }
}
