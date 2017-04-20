package android.hazardphan.ordergas;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Posted_Fragment extends Fragment {
    String url;
    ProgressDialog pDialog;
    ArrayList<Item_GasHome> listGas;
    RecyclerView_Adapter_Post AdapterPost;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posted, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_post);
        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("username");
        url = "http://goigas.96.lt/cuahang/get_cuahang_byid.php?id=" + id;
        loadDl();

        return v;

    }

    public void loadDl(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Vui lòng đợi...");
        pDialog.setCancelable(false);
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest strReq = new StringRequest(Request.Method.GET, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DocJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());

            }
        });
        requestQueue.add(strReq);
    }

    void DocJson(String  JsonObj){
        listGas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JsonObj);
            JSONArray jsArray = jsonObject.getJSONArray("cuahang");
            for(int i=0;i<jsArray.length();i++){
                JSONObject JsonObjectCH = jsArray.getJSONObject(i);
                String tencuahang = JsonObjectCH.getString("ten");
                String diachi =JsonObjectCH.getString("diadiem");
                String giatien =JsonObjectCH.getString("motagia");
                String sdt =JsonObjectCH.getString("sdt");
                String anh =JsonObjectCH.getString("link_img");
                String loaigas=JsonObjectCH.getString("loaigas");
                String chucuahang =JsonObjectCH.getString("chucuahang");
                String latlng =JsonObjectCH.getString("latlng");
                String ch_id =JsonObjectCH.getString("ch_id");
                listGas.add(new Item_GasHome(ch_id,tencuahang,giatien,sdt,diachi,anh,loaigas,chucuahang,latlng));
            }

            AdapterPost = new RecyclerView_Adapter_Post(listGas, getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(AdapterPost);
            pDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
