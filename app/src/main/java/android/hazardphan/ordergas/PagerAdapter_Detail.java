package android.hazardphan.ordergas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by CuongPhan on 4/15/2017.
 */

public class PagerAdapter_Detail extends FragmentPagerAdapter {
    public PagerAdapter_Detail(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position){
                case 0: fragment = new Posted_Fragment();break;
                case 1: fragment = new UserDetail_Fragment();break;
                case 2: fragment = new Like_Fragment();break;
            }
            return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title="TIN ĐÃ ĐĂNG";

                break;
            case 1:
                title="THÔNG TIN";
                break;
            case 2:
                title="TIN YÊU THÍCH";
                break;

        }

        return title;
    }

}
