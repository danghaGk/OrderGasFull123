package android.hazardphan.ordergas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DetailInfro_Activity extends AppCompatActivity {
    ViewPager viewPager1 ;
    TabLayout tabLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_infro);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbarD);
        setSupportActionBar(toolbar1);
        toolbar1.setTitleTextColor(Color.WHITE);
        toolbar1.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager1 = (ViewPager) findViewById(R.id.view_pager1);
        tabLayout1= (TabLayout) findViewById(R.id.tab_layout1);

        viewPager1.setAdapter(new PagerAdapter_Detail(this.getSupportFragmentManager()) {
        });
        tabLayout1.setupWithViewPager(viewPager1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
