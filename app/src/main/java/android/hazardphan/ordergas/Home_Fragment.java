package android.hazardphan.ordergas;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hazardphan.ordergas.themch.MainActivity_CreateCH;
import android.hazardphan.ordergas.themch.SendData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends Fragment implements SendData{
    private String url = "http://goigas.96.lt/cuahang/get_all_cuahang.php";
    private RecyclerView recyclerView;
    private ArrayList<Item_GasHome> ds = new ArrayList<>();
    private  RecyclerViewAdapter adapter;
    ProgressDialog dialog;

    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        SharedPreferences share = getActivity().getSharedPreferences("Home",  MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("home_checkLoad","0");
        editor.commit();
        return  v;
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences share = getActivity().getSharedPreferences("Home", MODE_PRIVATE);
        if (share.getString("home_checkLoad", "") != "")
        {
            if(share.getString("home_checkLoad", "").equals("1"))
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new docJson().execute("http://goigas.96.lt/cuahang/get_all_cuahang.php");
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences share =getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
                if(share.getString("checklogin","").equals("0")||share.getString("checklogin","")=="") {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    // khởi tạo dialog
                    alertDialogBuilder.setMessage("Vui lòng đăng nhập trước khi đăng tin ");
                    // thiết lập nội dung cho dialog
                    alertDialogBuilder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences share = getActivity().getSharedPreferences("MyShare", MODE_PRIVATE);
                            if (share.getString("checklogin", "").equals("0") || share.getString("checklogin", "") == "") {
                                Intent intent = new Intent(getActivity(), Signin_Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("flag", "0");
                                intent.putExtra("login", bundle);
                                startActivityForResult(intent, 10);
                            }
                            // button "Có" thoát khỏi ứng dụng
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // button "no" ẩn dialog đi
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // tạo dialog
                    alertDialog.show();
                }
                else
                {
                    Intent intent = new Intent(getContext(), MainActivity_CreateCH.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }

            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJson().execute("http://goigas.96.lt/cuahang/get_all_cuahang.php");
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void passData(String data) {

    }

    class docJson extends AsyncTask<String ,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog =new ProgressDialog(getContext());
            dialog.setMessage("Đang tải dữ liệu");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            ds.clear();
            super.onPostExecute(s);
            dialog.dismiss();
            ArrayList<String> arrGas = new ArrayList<>();
            try {
                JSONObject mangto = new JSONObject(s);
                JSONArray jsonArray = mangto.getJSONArray("cuahang");
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    String tencuahang = jsonObject.getString("ten");
                    String diachi =jsonObject.getString("diadiem");
                    String giatien =jsonObject.getString("motagia");
                    String sdt =jsonObject.getString("sdt");
                    String anh =jsonObject.getString("link_img");
                    String loaigas=jsonObject.getString("loaigas");
                    String chucuahang =jsonObject.getString("chucuahang");
                    String latlng =jsonObject.getString("latlng");
                    String ch_id =jsonObject.getString("ch_id");
                    ds.add(new Item_GasHome(ch_id,tencuahang,giatien,sdt,diachi,anh,loaigas,chucuahang,latlng));
                }
                adapter=new RecyclerViewAdapter(ds,getContext(), new RecyclerViewAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        //Toast.makeText(getContext(), "saddasd", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),CategoryGas_Activity.class);
                        Item_GasHome gas = (Item_GasHome) adapter.getItem(position);
                        intent.putExtra("detail",gas);

                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {

                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setQuery("Tìm kiếm theo địa chỉ ", true);


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery("Tìm kiếm theo địa chỉ ... ", false); //to set the text
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.filter(searchQuery.toString().trim());
                recyclerView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(mSearchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
    }



}
