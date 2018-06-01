package com.event2go.app.features.chat.presentation;

import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.R;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class OneOnOneChatRecyclerFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.include_compose_chat_message;
    }

//    protected FragmentOneOnOneChatBinding mBinding;
//    protected RecyclerFragmentViewModel mFragmentViewModel;
//    protected SwipeRefreshLayout mSwipeRefreshLayout;
//    private OneOnOneChatViewModel mRecyclerViewModel;
//    private BaseRecyclerAdapter<ChatMessage> mAdapter;
//    private boolean mSwipeRefreshEnabled = true;
//
//
//    private OneOnOneChatViewModel mOneOnOneChatViewModel;
//
//    protected int getLayoutId() {
////        return R.layout.fragment_one_on_one_chat;
//        return R.layout.fragment_chat;
//    }
//
//    /**
//     * Code to be executed when user "pulls to refreshData"
//     */
//    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//
//            if (mRecyclerViewModel.getState() == BaseRecyclerViewModel.STATE_NONE) {
//
//                mRecyclerViewModel.setState(BaseRecyclerViewModel.STATE_REFRESH);
//                mBinding.textEmpty.setVisibility(View.GONE);
//            }
//        }
//    };
//
//    @Override
//    public void onResume() {
//        mRecyclerViewModel.onViewResumed();
//        super.onResume();
//    }
//
//    @Override
//    public void onStop() {
//        mRecyclerViewModel.onViewStopped();
//        super.onStop();
//    }
//
//    @NonNull
//    @Override
//    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final View view = inflater.inflate(getLayoutId(), container, false);
//
//        mRecyclerViewModel = getRecyclerViewModel();
//        mRecyclerViewModel.setOnLoadCompleteCallback(new BaseRecyclerViewModel.OnLoadCompleteCallback() {
//            @Override
//            public void onSuccess(List items) {
//
//                mFragmentViewModel.notifyChange();
//                if (!isAdded()) return;
//                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//                mFragmentViewModel.notifyChange();
//
//                if (!isAdded()) return;
//                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//        mAdapter = mRecyclerViewModel.getAdapter();
//        mFragmentViewModel = new RecyclerFragmentViewModel(mRecyclerViewModel);
//
//        mBinding = DataBindingUtil.bind(view);
//        mBinding.setViewModel(mFragmentViewModel);
//
//        final RecyclerView recyclerView = mBinding.recyclerView;
//        mSwipeRefreshLayout = mBinding.swipeContainer;
//
//        setLayoutManager(recyclerView);
//
//        if (hasItemDecoration()) {
//            recyclerView.addItemDecoration(
//                    new DividerItemDecoration(recyclerView.getView(), LinearLayoutManager.VERTICAL)
//            );
//        }
//
//        recyclerView.setAdapter(mAdapter);
//
//        // disabled by default, use {@method setSwipeRefreshEnabled} method to enable
//        mSwipeRefreshLayout.setEnabled(mSwipeRefreshEnabled);
//        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
//
//        // load items only there's no data yet
//        if (mRecyclerViewModel.getAdapter().getItemCount() == 0 && hasToLoadDataWhenCreated()) {
//
//            mRecyclerViewModel.setState(BaseRecyclerViewModel.STATE_FIRST_TIME_LOAD);
//        }
//
//        return view;
//    }
//
//    @NonNull
//    protected OneOnOneChatViewModel getRecyclerViewModel() {
//        mOneOnOneChatViewModel = new OneOnOneChatViewModel();
//        return mOneOnOneChatViewModel;
//    }
//
//
//    public boolean hasToLoadDataWhenCreated() {
//        return true;
//    }
//
//    /**
//     * Sets the {@linkplain RecyclerView.LayoutManager} for the {@linkplain RecyclerView}.
//     * Calling to super is not necessary.
//     *
//     * @param recyclerView The RecyclerView on which to change the layout manager.
//     */
//    protected void setLayoutManager(RecyclerView recyclerView) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getView()));
//    }
//
//    /**
//     * Define usage {@linkplain RecyclerView.ItemDecoration} of the {@linkplain RecyclerView}.
//     */
//    protected boolean hasItemDecoration() {
//        return false;
//    }
//
//    /**
//     * Used if the ability to swipe refreshData is required by your view. This functionality is
//     * disabled by default. If enabled the provided {@linkplain BaseRecyclerViewModel}'s refreshData method
//     * will be invoked. It will be up to the view model to retrieve the data.
//     *
//     * @param enabled set to true if swipe to refreshData is desired.
//     */
//    protected void setSwipeRefreshEnabled(boolean enabled) {
//        mSwipeRefreshEnabled = enabled;
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setEnabled(enabled);
//        }
//    }
//
//    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
//        mRecyclerViewModel.setOnItemClickListener(listener);
//    }
//
//
//    protected ChatMessage getItem(int position) {
//        return mRecyclerViewModel.getItem(position);
//    }
}
