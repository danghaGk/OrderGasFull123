package android.hazardphan.ordergas;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by PhanCuong on 2/8/2017.
 */

public class Info extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webview = new WebView(this);
        setContentView(webview);
        String summary = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>Untitled Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h4>GỌI GAS</h4>\n" +
                "<p style=\"text-indent:10px\"> Phát triển bởi HCCStudio</p>\n" +
                "<h5> Thành viên Thực hiện :</h5>\n" +
                "<p style=\"text-indent:10px\"> Phan Mạnh Cường (CLC-KTPM-K9)</p>\n" +
                "<p style=\"text-indent:10px\"> Nguyễn Văn Cường (KTPM2-K9) :</p>\n" +
                "<p style=\"text-indent:10px\"> Phạm Đăng Hà (KTPM2-K9) :</p>\n" +
                "<p style=\"text-indent:10px\"> Nguyễn Hải Yến (KHMT1-K9) :</p>\n" +
                "<p style=\"text-indent:10px\">– Chúng tôi rất mong nhận được sự phản hồi của các bạn.Mọi thôn tin các bạn vui lòng liên hệ theo địa chỉ :</p>\n" +
                "<p style=\"color:#F06;text-indent:5px\">Email: <a href=\"#\">hazardphan@gmail.com</a></p>\n" +
                "<p style=\"color:#F06;text-indent:5px\">FB: <a href=\"#\">https://www.facebook.com/hazard9x</a></p>\n" +
                "</body>\n" +
                "</html>";
        webview.loadData(summary, "text/html; charset=utf-8", "UTF-8");
    }
}
