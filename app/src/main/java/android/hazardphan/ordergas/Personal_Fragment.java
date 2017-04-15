package android.hazardphan.ordergas;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Personal_Fragment extends Fragment implements View.OnClickListener {
    int check = 0;
    CardView cardViewDangNhap;
    TextView txtDangNhap;
    Button btnDangXuat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);
        cardViewDangNhap = (CardView) view.findViewById(R.id.cardFirst);
        txtDangNhap = (TextView) view.findViewById(R.id.btn_DangNhap);
        cardViewDangNhap.setOnClickListener(this);

        btnDangXuat = (Button) view.findViewById(R.id.btnDangXuat);
        btnDangXuat.setOnClickListener(this);
        test();
        return view;

    }

    public void test() {
        SharedPreferences share = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
        if (share.getString("checklogin", "") != "")
            if (share.getString("checklogin", "").equals("1")) {

                String name = share.getString("name", "");
                if (!name.equals("") || !name.equals("")) {

                    txtDangNhap.setText(name);
                    btnDangXuat.setVisibility(View.VISIBLE);
                }

            }
        if (share.getString("checklogin", "") == "" || share.getString("checklogin", "").equals("0")) {
            txtDangNhap.setText("Đăng Nhập");
            btnDangXuat.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardFirst:
                SharedPreferences share = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
                if (share.getString("checklogin", "").equals("0") || share.getString("checklogin", "") == "") {
                    Intent intent = new Intent(getActivity(), Signin_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "0");
                    intent.putExtra("login", bundle);
                    startActivityForResult(intent, 10);
                } else {
                    Intent intent = new Intent(getContext(), DetailInfro_Activity.class);
                    SharedPreferences share1 = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);

                    intent.putExtra("username", share1.getString("tendangnhap", ""));
                    startActivity(intent);
                }
                break;
            case R.id.btnDangXuat:
                SharedPreferences share2 = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
                SharedPreferences.Editor editor = share2.edit();
                editor.putString("checklogin", "0");
                editor.commit();
                txtDangNhap.setText("Đăng Nhập");
                btnDangXuat.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 10) {
                if (resultCode == 1) {
                    test();
                    // SharedPreferences share = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
//                  if(share.getString("checklogin","").equals("1")){
//                      check =1 ;
//                      test();
//                      btnDangXuat.setVisibility(View.VISIBLE);
//                  }
//                  else {
//                      check=0;
//                      btnDangXuat.setVisibility(View.GONE);
//
//                  }
                }
            }

        }
    }


}
