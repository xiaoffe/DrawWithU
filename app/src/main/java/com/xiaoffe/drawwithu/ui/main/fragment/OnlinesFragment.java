package com.xiaoffe.drawwithu.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiaoffe.drawwithu.R;
import com.xiaoffe.drawwithu.app.Constants;
import com.xiaoffe.drawwithu.base.RootFragment;
import com.xiaoffe.drawwithu.base.contract.fragment.OnlinesContract;
import com.xiaoffe.drawwithu.model.bean.OnlinesBean;
import com.xiaoffe.drawwithu.presenter.fragment.OnlinesPresenter;
import com.xiaoffe.drawwithu.ui.main.activity.SyncPadActivity;
import com.xiaoffe.drawwithu.util.TIMUtil;
import com.xiaoffe.drawwithu.widget.CoordinatorTabLayout;
import com.xiaoffe.drawwithu.widget.RefreshItemView;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/1.
 */
//
public class OnlinesFragment extends RootFragment<OnlinesPresenter> implements OnlinesContract.View {
    private final String TAG = "online";
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout mCoordinatorTabLayout;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.view_main)
    RecyclerView recyclerView;
    @Inject TIMUtil timUtil;

    private static final int ITEM_TYPE_NORMAL = 1;//普通的条目
    private static final int ITEM_TYPE_BLANK = 2;//最后一条空白的条目
    private static final int ITEM_TYPE_MORE = 3;//这是加载更多的界面
    private List<String> mDataList = new ArrayList<>();
    private RefreshItemView refreshItemView;
    private AskListAdapter askListAdapter;
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mCoordinatorTabLayout.setTitle("DrawWithU")
                .setBackEnable(true)
                .setContentScrimColorArray(new int[]{android.R.color.holo_blue_light})
                .setLoadHeaderImagesListener(null);

        askListAdapter = new AskListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(askListAdapter);
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }
        });
        mPresenter.getOnlineData();
    }

    @Override
    public void showContent(OnlinesBean info) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        mDataList.clear();
        for(int n=0; n<10; n++){mDataList.add("dummy data");}
        askListAdapter.notifyDataSetChanged();
        if(mDataList.size()>=51){
            refreshItemView.reflashUI(RefreshItemView.LOADMORE_NONE);
        }else{
            refreshItemView.reflashUI(RefreshItemView.LOADMORE_LOADING);
        }
    }

    @Override
    public void showMoreContent(OnlinesBean info) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
            return;
        }
        if(mDataList.size()<51){
            for(int n=0; n<10; n++){
                mDataList.add("dummy data");
            }
        }
        askListAdapter.notifyDataSetChanged();
        if(mDataList.size()>=51){
            refreshItemView.reflashUI(RefreshItemView.LOADMORE_NONE);
        }else{
            refreshItemView.reflashUI(RefreshItemView.LOADMORE_LOADING);
        }
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online;
    }

    @Override
    public void stateError() {
        super.stateError();
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }
    //
    private class AskListAdapter extends RecyclerView.Adapter<TeacherHolder> {
        private Context mContext;
        private LayoutInflater mInflater;
        public AskListAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public TeacherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            switch (viewType) {
                case ITEM_TYPE_NORMAL://这里是普通的类型
                    itemView = mInflater.inflate(R.layout.item_ask_list, parent, false);
                    break;
                case ITEM_TYPE_BLANK://这里是空的类型
                    itemView = mInflater.inflate(R.layout.ask_list_last_holder, parent, false);
                    break;
                case ITEM_TYPE_MORE://这是倒数第二个条目，加载更多:
                    itemView = getLoadMoreView(parent).getRootView();
                    break;
            }
            return new TeacherHolder(itemView, viewType);
        }

        @Override
        public void onBindViewHolder(TeacherHolder holder, int position) {
            if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
                holder.setDatas(mDataList.get(position));
            }
            //当移动到刷新条目的时候，就可以去加载数据了
            if (position == mDataList.size()) {
                //在这里开始加载数据
                mPresenter.getMoreOnlineData();
            }
        }

        @Override
        public int getItemCount() {
            if (mDataList != null) {
                return mDataList.size() + 2;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == mDataList.size()) {
                return ITEM_TYPE_MORE;
            } else if (position == mDataList.size() + 1) {
                return ITEM_TYPE_BLANK;
            } else {
                //否则返回正常的类型
                return ITEM_TYPE_NORMAL;
            }
        }
    }

    private RefreshItemView getLoadMoreView(ViewGroup parent) {
        if (refreshItemView == null) {
            refreshItemView = new RefreshItemView(getActivity(), parent);
        }
        refreshItemView.reflashUI(RefreshItemView.LOADMORE_LOADING);//默认显示加载的状态
        return refreshItemView;
    }

    /**
     * holder
     */
//  extends RecyclerView.ViewHolder
    public class TeacherHolder extends RecyclerView.ViewHolder {
        public ImageView mAvater;
        public TextView mName;
        public TextView mSubject;
        public TextView mGrades;
        private TextView mAskBtn;
        private View mLine;
        private String datas;
        private View itemView;

        public TeacherHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;

            if (viewType == ITEM_TYPE_NORMAL) {

                mAvater = (ImageView) itemView.findViewById(R.id.teacher_icon);
                mName = (TextView) itemView.findViewById(R.id.teacher_name);
                mSubject = (TextView) itemView.findViewById(R.id.teacher_subject);
                mGrades = (TextView) itemView.findViewById(R.id.teacher_grade);
                mLine = itemView.findViewById(R.id.teacher_line);
                //这是问的按钮
                mAskBtn = (TextView) itemView.findViewById(R.id.ask_teacher);
                mAskBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //一会儿直接调用，一会儿又放到Presenter里。写得没风格。。
                        //而且这一句不能放到SyncPadActivity的onCreate里面去。暂且就这么写
                        timUtil.sendCode(Constants.INVITE_DRAW);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),SyncPadActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        public void setDatas(String datas) {
            this.datas = datas;
//            Glide.with(getActivity())
//                    .load(datas.getAvatar()).transform(new GlideCircleTransform(getActivity()))
//                    .override(UIUtils.dip2px(54), UIUtils.dip2px(54)).into(mAvater);
            mName.setText("名字O(∩_∩)O哈哈~");
            mSubject.setText("科目呢~~(>_<)~~");
            mGrades.setText("没写年级♪(^∇^*)");
            //还要设置问的状态
            mAskBtn.setBackgroundResource(R.mipmap.wen_green_online);
        }
    }
}