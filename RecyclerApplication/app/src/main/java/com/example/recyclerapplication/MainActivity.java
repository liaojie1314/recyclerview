package com.example.recyclerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.recyclerapplication.adapters.GridViewAdapter;
import com.example.recyclerapplication.adapters.ListViewAdapter;
import com.example.recyclerapplication.adapters.RecyclerViewBaseAdapter;
import com.example.recyclerapplication.adapters.StaggerAdapter;
import com.example.recyclerapplication.beans.Datas;
import com.example.recyclerapplication.beans.ItemBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mList;
    private List<ItemBean> mData;
    private RecyclerViewBaseAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList=(RecyclerView) this.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refresh_layout);
        initData();
        //设置默认显示的样式为list 效果
        showList(true,false);
        //处理下拉刷新
        handlerDownPullUpdate();

    }

    private void handlerDownPullUpdate() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimer,R.color.teal_700,R.color.teal_200);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在这里执行刷新数据的操作这个方法是MainThread,不可以执行耗时操作，一般我们请求数据在开一个新线程获取
                //添加数据
                ItemBean data=new ItemBean();
                data.title="我是新添加的标题...";
                data.icon=R.mipmap.pic_09;
                mData.add(0,data);
                //更新UI
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //1,让刷新停止2,更新列表
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                },3000);

            }
        });
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //这里处理点击事件
                Toast.makeText(MainActivity.this,"你点击的是"+position+"个条目",Toast.LENGTH_SHORT).show();
            }
        });
        //处理上拉加载更多
        if (mAdapter instanceof ListViewAdapter){
            ((ListViewAdapter)mAdapter).setOnRefreshListener(new ListViewAdapter.OnRefreshListener() {
                @Override
                public void onUpPullRefresh( final ListViewAdapter.LoaderMoreHolder loaderMoreHolder) {
                    //这里面去加载数据，同样需要在子线程中完成这里仅作演示
                    //更新UI
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random random=new Random();
                            if (random.nextInt()%2==0){
                                ItemBean data=new ItemBean();
                                data.title="我是新添加的标题...";
                                data.icon=R.mipmap.pic_09;
                                mData.add(data);
                                //1.让刷新停止2.更新列表
                                mAdapter.notifyDataSetChanged();
                                loaderMoreHolder.update(loaderMoreHolder.LOADER_STATE_NORMAL);
                            }else {
                                loaderMoreHolder.update(loaderMoreHolder.LOADER_STATE_RELOAD);
                            }
                        }
                    },3000);
                }
            });
        }
    }

    //初始化模拟数据
    private void initData() {
//        List<DateBea>--->Adapter--->setAdapter--->显示数据
        mData=new ArrayList<>();

        for (int i = 0; i< Datas.icons.length; i++){
            ItemBean data =new ItemBean();
            data.icon=Datas.icons[i];
            data.title="我是第"+i+"个条目";
            mData.add(data);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
//            list部分

            case R.id.list_view_vertical_stander:
                showList(true,false);

                break;
            case R.id.list_view_vertical_reverse:
                showList(true,true);
                break;
            case R.id.list_view_horizontal_stander:
                showList(false,false);
                break;
            case R.id.list_view_horizontal_reverse:
                showList(false,true);
                break;

//             grid 部分
            case R.id.grid_view_vertical_stander:
                showGrid(true,false);
                break;
            case R.id.grid_view_vertical_reverse:
                showGrid(true,true);
                break;
            case R.id.grid_view_horizontal_stander:
                showGrid(false,false);
                break;
            case R.id.grid_view_horizontal_reverse:
                showGrid(false,true);
                break;
//                瀑布流
            case R.id.stagger_view_vertical_stander:
                showStagger(true,false);
                break;
            case R.id.stagger_view_vertical_reverse:
                showStagger(true,true);
                break;
            case R.id.stagger_view_horizontal_stander:
                showStagger(false,false);
                break;
            case R.id.stagger_view_horizontal_reverse:
                showStagger(false,true);
                break;
                //多种条目类型点击
            case R.id.more_type:

                //跳转到一个新的activity
                Intent intent=new Intent(this,MoreTypeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//这个方法实现瀑布流效果
    private void showStagger(boolean isVertical,boolean isReverse) {
//        设置布局管理器
        StaggeredGridLayoutManager layoutManager =new StaggeredGridLayoutManager(2,isVertical?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);
//        设置布局管理器的方向
        layoutManager.setReverseLayout(isReverse);
//        设置布局管理器到Recyclerview里
        mList.setLayoutManager(layoutManager);
//        创建适配器
        mAdapter= new StaggerAdapter(mData);
//        设置适配器
        mList.setAdapter(mAdapter);
        //        初始化事件
        initListener();

    }
    //这个方法用于显示grid一样的效果

    private void showGrid(boolean isVertical,boolean isReverse) {
//        创建布局管理器
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        layoutManager.setOrientation(isVertical ? GridLayoutManager.VERTICAL: GridLayoutManager.HORIZONTAL);
//        设置水平还是垂直
//        设置正向还是反向
        layoutManager.setReverseLayout(isReverse);
//        设置布局管理器
        mList.setLayoutManager(layoutManager);
//        创建适配器
        mAdapter=new GridViewAdapter(mData);
//        设置适配器
        mList.setAdapter(mAdapter);
        //        初始化事件
        initListener();
    }

    //这个方法用于显示list一样的效果
    private void showList(boolean isVertical,boolean isReverse) {
        //        Recyclerview 需要设置样式，设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//设置布局管理器来控制
        layoutManager.setOrientation(isVertical? LinearLayoutManager.VERTICAL: LinearLayoutManager.HORIZONTAL);
//        设置水平还是垂直
//        设置正向还是反向
        layoutManager.setReverseLayout(isReverse);
        mList.setLayoutManager(layoutManager);

//        创建适配器
        mAdapter=new ListViewAdapter(mData);
//        设置到Recyclerview里
        mList.setAdapter(mAdapter);
        //        初始化事件
        initListener();
    }
}