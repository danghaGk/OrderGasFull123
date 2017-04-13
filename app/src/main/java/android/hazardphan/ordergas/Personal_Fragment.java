package android.hazardphan.ordergas;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Personal_Fragment extends Fragment implements View.OnClickListener {
    int check=0;
    Signin_Activity signin_activity;
    CardView cardViewDangNhap;
    TextView txtDangNhap;

    public interface OnRefreshSelected {
        public void refreshFragment(boolean flag);
    }

    private OnRefreshSelected onRefreshSelected;
    public Personal_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.personal_fragment, container, false);
        cardViewDangNhap = (CardView) view.findViewById(R.id.cardFirst);
        txtDangNhap  = (TextView) view.findViewById(R.id.btn_DangNhap);
        cardViewDangNhap.setOnClickListener(this);
        SharedPreferences cache = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(cache.getString("checklogin",null).equals("1")){
            check =1 ;
            test();
        }
        else {
            check=0;
        }
        return view;

    }
public void test(){
    if(check == 1) {
        signin_activity = new Signin_Activity();
        String name = signin_activity.LayCacheDangNhap();
        if (!name.equals("") || !name.equals(null)) {
            
            txtDangNhap.setText(name);
        }
    }
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardFirst:
                Intent intent = new Intent(getActivity(),Signin_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag","0");
                intent.putExtra("login",bundle);
                startActivity(intent);
                break;
        }
    }

}
