package com.example.shenjiaqi.pictureviewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /** RecyclerView */
    private RecyclerView mRecyclerView;
    /** tag */
    private static final String TAG = "MainActivity";
    /** context */
    private Context mContext;
    /** 位置参数 */
    public static final String POS_ARRAY = "posArray";
    /** 图片index */
    public static final String IMAGE_POS = "imagePos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        PictureAdapter adapter = new PictureAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView 的适配器
     */
    private class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public PictureAdapter(Context context) {
            mContext = context;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                ImageView imageView = ((ViewHolder) viewHolder).imageView;
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.timg1));
            } else {
                ImageView imageView = ((ViewHolder) viewHolder).imageView;
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.timg));
            }
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        @Override
        public int getItemViewType(int position) {
            if (position % 2 == 0) {
                return 0;
            }
            return 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            ViewHolder viewHolder = null;
            if (i % 2 == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.image_item, null);
                viewHolder = new ViewHolder(view);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.image_item2, null);
                viewHolder = new ViewHolder(view);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.theimage);
            }
            return viewHolder;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(onClickListener);
            }
        }

        // 点击事件
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mRecyclerView.getChildAdapterPosition(v);
                Intent intent = new Intent();
                intent.setClass(mContext, Main2Activity.class);
                intent.putIntegerArrayListExtra(POS_ARRAY, getImagePos(v));
                intent.putExtra(IMAGE_POS, pos);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        };
    }

    /**
     * 获取图片的位置信息
     */
    public ArrayList getImagePos(View view) {
        int[] locat = new int[2];
        view.getLocationOnScreen(locat);
        int width = view.getWidth();
        int height = view.getHeight();
        ArrayList<Integer> posArr = new ArrayList<>();
        posArr.add(locat[0]);
        posArr.add(locat[0] + width);
        posArr.add(locat[1]);
        posArr.add(locat[1] + height);
        return posArr;
    }

}

