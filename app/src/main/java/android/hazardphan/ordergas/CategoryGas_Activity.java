package android.hazardphan.ordergas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryGas_Activity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, OnMapReadyCallback {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    GoogleMap map;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private TextView textviewTitle, txtTenCuaHangCategory, txtSDTCategory;
    private TextView tv_tench, tv_diachi, tv_loaigas, tv_giatien, tv_chucuahang, tv_sdt;
    private CommentAdapter commentAdapter;
    Item_GasHome item;
    ImageView btn_send;
    RecyclerView recyclerView_comment;
    String url_id = "http://goigas.96.lt/cuahang/get_comment_by_id.php?id=";
    EditText ed_name, ed_text;
    String id_ch;
    LinearLayout lnCall;

    /**
     * Find the Views in the layout
     * Auto-created on 2016-03-03 11:32:38 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar1);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        coverImage = (ImageView) findViewById(R.id.imageview_placeholder);
        framelayoutTitle = (FrameLayout) findViewById(R.id.framelayout_title);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.linearlayout_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        textviewTitle = (TextView) findViewById(R.id.textview_title);
        recyclerView_comment = (RecyclerView) findViewById(R.id.recyclerView_cmt);
        ed_name = (EditText) findViewById(R.id.ed_cmt_name);
        ed_text = (EditText) findViewById(R.id.ed_cmt_text);
        btn_send = (ImageView) findViewById(R.id.btn_cmt_send);
        tv_tench = (TextView) findViewById(R.id.tv_tench);
        tv_diachi = (TextView) findViewById(R.id.tv_diachi);
        tv_sdt = (TextView) findViewById(R.id.tv_sodt);
        tv_giatien = (TextView) findViewById(R.id.tv_giatien);
        tv_chucuahang = (TextView) findViewById(R.id.tv_chucuahang);
        tv_loaigas = (TextView) findViewById(R.id.tv_loaigas);
        lnCall = (LinearLayout) findViewById(R.id.lnCall);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_categorygas);
        findViews();

        toolbar.setTitle("Oder Gas");

        appbar.addOnOffsetChangedListener(this);
        textviewTitle.setText("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Oder Gas");
        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);


        //set avatar and cover
        //   avatar.setImageURI(imageUri);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        item = (Item_GasHome) getIntent().getExtras().getSerializable("detail");
        getSupportActionBar().setTitle(item.getTencuahang());
        getSupportActionBar().setSubtitle(item.getSodienthoai());
        //  Toast.makeText(CategoryGas_Activity.this, item.getSodienthoai(), Toast.LENGTH_SHORT).show();
//        txtTenCuaHangCategory.setText(item.getTencuahang());

        tv_tench.setText(item.getTencuahang());
        tv_diachi.setText(item.getDiadiem());
        tv_loaigas.setText(item.getLoaigas());
        tv_giatien.setText(item.getMotagia());
        tv_chucuahang.setText(item.getTenchucuahang());
        tv_sdt.setText(item.getSodienthoai());
        tv_loaigas.setText(item.getLoaigas());
//
        Glide.with(this)
                .load(item.getAnh())
                .into(coverImage);
        id_ch = item.getCh_id();
        getJsonCmt(url_id + item.getCh_id());
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendComment();


            }
        });

        lnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tv_sdt.getText().toString()));

                if (ActivityCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                v.getContext().startActivity(callIntent);
            }
        });
        //map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        String CurrentString = item.getLatlng();
        if(item.getLatlng().equals("Erro0")==false) {
            String[] separated = CurrentString.split(",");
            Double a = Double.parseDouble(separated[0].trim());
            Double b = Double.parseDouble(separated[1].trim());


            LatLng latLng = new LatLng(a, b);
            map.getUiSettings().setCompassEnabled(true);
            MarkerOptions vitri = new MarkerOptions();
            vitri.draggable(true);
            vitri.position(latLng);
            vitri.title(item.getDiadiem());
            vitri.snippet(item.getTencuahang());
            Marker marker = map.addMarker(vitri);
            marker.showInfoWindow();

//        CameraUpdate move = CameraUpdateFactory.newLatLng(latLng);
//        map. moveCamera(move);
//       CameraUpdate zoom = CameraUpdateFactory.zoomBy(20);
//       map.animateCamera(zoom,3000,null);

//        CameraUpdate move = CameraUpdateFactory.newLatLngZoom(latLng,10);
//        map.animateCamera(move,3000,null);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to Mountain View
                    .zoom(20)                   // Sets the zoom
                    .bearing(4.3f)                // Sets the orientation of the camera to east
                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            statusCheck();
        }
        else{
            Toast.makeText(getApplicationContext(),"Lỗi vị trí",Toast.LENGTH_SHORT).show();
        }
    }

    public void getJsonCmt(String url_id) {

        try {
            final RequestQueue queue = Volley.newRequestQueue(this);
//                Log.i("url_city", urlSearch(lat_lon));
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  Log.i("result", response);
                    readJson(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("erro 2", error.getMessage() + "loi");
                }
            });
            queue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readJson(String response) {
        ArrayList<Item_comment> item_cmt = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("comment");
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject item = arr.getJSONObject(i);
                String name = item.getString("cmt_name");
                String text = item.getString("cmt_text");
                //
                item_cmt.add(new Item_comment(name, text));
            }
            commentAdapter = new CommentAdapter(this, item_cmt);
            commentAdapter.notifyDataSetChanged();
            LinearLayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView_comment.setLayoutManager(mlayoutManager);
            recyclerView_comment.setItemAnimator(new DefaultItemAnimator());
            recyclerView_comment.setAdapter(commentAdapter);
        } catch (Exception e) {
            Log.e("loi", e.getMessage());
        }
    }

    public void sendComment() {
        if (ed_name.getText().toString().trim().equals(""))
            ed_name.setError("Not null");
        else if (ed_text.getText().toString().trim().equals(""))
            ed_text.setError("Not null");
        else {
            String url = "http://goigas.96.lt/cuahang/create_comment.php";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Post", response);
                    //phan tich json
                    try {
                        JSONObject obj = new JSONObject(response);

                        String name = obj.getString("success");
                        if (name.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Gui thanh cong", Toast.LENGTH_SHORT).show();
                            getJsonCmt(url_id + item.getCh_id());
                            ed_name.setText("");
                            ed_text.setText("");
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.layout_category), "Send failed", Snackbar.LENGTH_SHORT)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sendComment();
                                        }
                                    });
                            snackbar.show();
                        }
                    } catch (Exception e) {
                        Log.e("loi", e.getMessage());
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
                    params.put("cuahang_id", id_ch);
                    params.put("name", ed_name.getText().toString());
                    params.put("text", ed_text.getText().toString());
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case 16908332:

                finish();

            default:
                return super.onContextItemSelected(item);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }
}
