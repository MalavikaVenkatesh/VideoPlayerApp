package com.example.lenovo.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int REQUEST_PERMISSIONS = 1234;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_COUNT = 2;

    @SuppressLint("NewApi")
    private boolean arePermissionsDenied() {
        for (int i = 0; i < PERMISSIONS_COUNT; i++) {
            if (checkSelfPermission(PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;

    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionsDenied()) {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))). clearApplicationUserData();

                recreate();
            } else {
                onResume();
            }
        }
    }



    private boolean isVideoPlayerInitialized;

    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS,REQUEST_PERMISSIONS);
            return;
        }
        if(isVideoPlayerInitialized){
            final ListView listView=findViewById(R.id.listView);
            final TextAdapter textAdapter = new TextAdapter();
            final List<String> videoList = new ArrayList<>();
            for (int i=0;i<100;i++){
                videoList.add(String.valueOf(i));
            }

            textAdapter.setData(videoList);
            isVideoPlayerInitialized=true;
        }
    }
    class TextAdapter extends BaseAdapter{
        private List<String> data = new ArrayList<>();
       

        public void setData(List<String> mData){
            data.clear();
            data.addAll(mData);
            notifyDataSetChanged();
        }
        @Override
        public int getCount(){
            return 0;
        }
        @Override
        public Object getItem(int position){
            return null;
        }
        @Override
        public long getItemId(int position){
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null){
                convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent);
                convertView.setTag(new ViewHolder((TextView) convertView.findViewById(R.id.myItem)));

            }
            ViewHolder holder=(ViewHolder) convertView.getTag();
            final String item = data.get(position);

            holder.info.setText(item.substring(item.lastIndexOf( '/') +1));
            return convertView;
        }
        class ViewHolder{
            TextView info;
            ViewHolder(TextView mInfo){
                info = mInfo;
            }
        }
    }
}





