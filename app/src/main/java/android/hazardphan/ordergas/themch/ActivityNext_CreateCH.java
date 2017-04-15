package android.hazardphan.ordergas.themch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hazardphan.ordergas.R;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by VanCuong on 04/04/2017.
 */

public class ActivityNext_CreateCH extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate {
    EditText ed_tinh, ed_quan, ed_dc;
    Button btn_send, btn_out;
    ImageView img_bg;
    Items items = new Items();
    private int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://goigas.96.lt/cuahang/upload.php";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    String link_image;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    ProgressDialog dialog;
    //Uri to store the image uri
    private Uri filePath;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_createch);
        ed_tinh = (EditText) findViewById(R.id.ed_tinh);
        ed_quan = (EditText) findViewById(R.id.ed_quan);
        btn_send = (Button) findViewById(R.id.btn_send);
        ed_dc = (EditText) findViewById(R.id.ed_dc);
        img_bg = (ImageView) findViewById(R.id.img_bg);
        btn_out = (Button) findViewById(R.id.btn_out_next);
        if (Build.VERSION.SDK_INT >= 23)
            requestStoragePermission();
        final Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("data");

        items = (Items) bundle.getSerializable("item");
        img_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(11, intent);
                SharedPreferences share = getSharedPreferences("Home", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString("home_checkLoad", "0");
                editor.commit();
                finish();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    uploadMultipart();
                } else
                    Toast.makeText(getApplicationContext(), "Bạn cần chọn một file ảnh", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void callAddVolley(Items items) {


        final String ten = items.getTen();
        final String user_id = items.getUser_id();
        final String loaigas = items.getLoaigas();
        final String motagia = items.getMotagia();
        final String sdt = items.getSdt();
        final String chucuahang = items.getChucuahang();
        final String diadiem = ed_dc.getText().toString().trim() + "," + ed_quan.getText().toString().trim() + ","
                + ed_tinh.getText().toString().trim();
        final String latlng = items.getLatlng() + "0";
        String url = "http://goigas.96.lt/cuahang/create_cuahang.php";
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
                        Toast.makeText(getApplicationContext(), "Thêm thành cong", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        setResult(11, intent);
                        finish();
                        if (dialog.isShowing())
                            dialog.dismiss();
                        SharedPreferences share = getSharedPreferences("Home", MODE_PRIVATE);
                        SharedPreferences.Editor editor = share.edit();
                        editor.putString("home_checkLoad", "1");
                        editor.commit();

                    } else
                        Toast.makeText(getApplicationContext(), "Gui loi", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("loi", e.getMessage() + "0");
                }
//                Snackbar snackbar = Snackbar
//                        .make(findViewById(R.id.activity_next), "Thêm thành công!", Snackbar.LENGTH_SHORT);
//                snackbar.show();
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
                params.put("ten", ten);
                params.put("loaigas", loaigas);
                params.put("motagia", motagia);
                params.put("sdt", sdt);
                params.put("chucuahang", chucuahang);
                params.put("latlng", latlng);
                params.put("diadiem", diadiem);
                params.put("user_id", user_id);
                params.put("link_img", getLink_image());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void uploadMultipart() {
        //getting name for the image
        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang thêm dữ liệu");
        dialog.show();
        uploadReceiver.register(this);
        //getting the actual path of the image
        final String path = getPath1(getApplicationContext(), filePath);
        final String name = path.substring(path.lastIndexOf("/") + 1);
        //Uploading code

        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            UploadNotificationConfig ntf = new UploadNotificationConfig();
            ntf.setTitle("Upload ảnh");

            ntf.setCompletedMessage("Tải lên ảnh thành công");
            ntf.setErrorMessage("Tải lên ảnh thất bại, vui lòng kiểm tra lại mạng");
            //Creating a multi part request
            String ul = new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(ntf)
                    .setMaxRetries(0)
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    //method to show file chooser
    private void showFileChooser() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //  handling the image chooser activity result
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                String name = getPath1(getApplicationContext(), filePath).substring(getPath1(getApplicationContext(), filePath).lastIndexOf("/") + 1);
                //  String name1 = getRealPathFromURI(filePath).substring(getPath(filePath).lastIndexOf("/") + 1);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_bg.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    //
//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

//
//    //method to get the file path from uri
//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();

//        cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        String path=null;
//        try {
//        cursor.moveToFirst();
//         path= cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//        }
//        catch(Exception e) {
//            Log.e("Path Error", e.toString());
//        }
//        return path;
//        String result = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        cursor = getContentResolver().query(uri, proj, null, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
//                result = cursor.getString(column_index);
//            }
//            cursor.close();
//        }
//        if (result == null) {
//            result = "Not found";
//        }
//        return result;
//    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {

        try {

            JSONObject response = new JSONObject(new String(serverResponseBody, "UTF-8"));
            String link = response.getString("url");
            setLink_image(link);
            callAddVolley(items);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    @Override
    public void onCancelled() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath1(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
