package android.hazardphan.ordergas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dangha.dev on 04/04/2017.
 */

public class Signin_Activity extends AppCompatActivity implements View.OnClickListener{
    Button btnDangKy, btnDangNhap;
    EditText edtuser,edtpass ;
    Intent data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        btnDangKy  = (Button) findViewById(R.id.btn_dangKy);
        btnDangNhap = (Button) findViewById(R.id.btn_dangNhap);
        btnDangKy.setOnClickListener(this);
        btnDangNhap.setOnClickListener(this);

        edtpass = (EditText) findViewById(R.id.edt_passLogin);
        edtuser= (EditText) findViewById(R.id.edt_userLogin);
        // Nhận dữ liệu từ signup Activity về


        data = this.getIntent(); // tao intent
        Bundle bundle = data.getBundleExtra("login"); // lay ra bundle tu intent
        if( bundle.getString("flag").equals("1")) { // nếu bundle trả về trùng thì ....
            String user = bundle.getString("user");
            String pass = bundle.getString("pass");

            edtuser.setText(user);
            edtpass.setText(pass);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dangNhap:
                KiemtraDangNhap();

                break;
            case R.id.btn_dangKy:
                Intent intent = new Intent(Signin_Activity.this,Singup_Activity.class);
                startActivity(intent);
                break;
        }
    }

    public void KiemtraDangNhap(){
        String url = "http://goigas.96.lt/cuahang/dangnhap.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    String check = obj.getString("success");
                    if(check.equals("1")){
                        Toast.makeText(getApplicationContext(),"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                        String name = obj.getString("user_name");


                     sData(name);


                    }else{
                        Toast.makeText(getApplicationContext(),"Đăng nhập lỗi!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                params.put("id", edtuser.getText().toString());
                params.put("pass", edtpass.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest); // Add the request to the RequestQueue.
    }
    public void sData(String name){

        SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("name",name);
        editor.putString("tendangnhap", edtuser.getText().toString());
        editor.putString("checklogin","1");
        editor.commit();

        senToFragment(1,1);
    }
    public void senToFragment(int resultcode ,int i){
        Intent intent =data;
        intent.putExtra("data",i);
        setResult(resultcode,intent);
        finish();
    }
}

