package com.purecode.househunt.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.purecode.househunt.R;
import com.purecode.househunt.model.Callback;
import com.purecode.househunt.model.House;
import com.purecode.househunt.modules.ServiceLoader;

import java.util.List;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class HouseListFragment extends Fragment {

    private View mRootView;
    private boolean isOk;
    private final int PAGE_SIZE = 10;
    private int currentPage = 1;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_hunt_list_fragment_layout, container, false);
        this.mRootView = view;
        isOk = true;
        loading();
        ServiceLoader.getWebServices().fetchHouses(currentPage, PAGE_SIZE, callback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOk = false;
    }

    Callback<List<House>> callback = new Callback<List<House>>() {
        @Override
        public void onSuccess(List<House> houses) {
            loading = true;
            if (houses.size() > 0) {
                currentPage ++;
                loadingDone(houses);
            } else {
                if (isOk && ! isEmpty()) {
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mRootView.findViewById(R.id.coordinatorLayout);
                    Snackbar.make(coordinatorLayout, "You have reached the end.", Snackbar.LENGTH_SHORT).show();
                }
                empty();
            }
        }

        @Override
        public void onFailure(Exception ex) {
            loading = true;
            error();
        }
    };

    private boolean ok() {
        return isOk && mRootView != null;
    }

    //Ensure mRootView is valid when this method isCalled
    private View noContentContainer() {
        View noContentContainer = mRootView.findViewById(R.id.no_content_container);
        return noContentContainer;
    }

    private RecyclerView housesRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.houses_rv);
        return recyclerView;
    }

    private View progressIndicator() {
        View progressIndicator = mRootView.findViewById(R.id.progressIndicator);
        return progressIndicator;
    }

    private View pageLoader() {
        View pageLoader = mRootView.findViewById(R.id.pageLoader);
        return pageLoader;
    }

    private boolean isEmpty() {
        if (ok()) {
            RecyclerView recyclerView = housesRecyclerView();
            if (recyclerView != null) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter != null) {
                    return adapter.getItemCount() == 0;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else return true;
    }


    private void loading() {
        if (ok() && isEmpty()) {
            housesRecyclerView().setVisibility(View.GONE);
            progressIndicator().setVisibility(View.VISIBLE);
            pageLoader().setVisibility(View.GONE);
            noContentContainer().setVisibility(View.GONE);
        } else {
            housesRecyclerView().setVisibility(View.VISIBLE);
            progressIndicator().setVisibility(View.GONE);
            pageLoader().setVisibility(View.VISIBLE);
            noContentContainer().setVisibility(View.GONE);
        }
    }

    private void error() {
        if (ok() && isEmpty()) {
            housesRecyclerView().setVisibility(View.GONE);
            progressIndicator().setVisibility(View.GONE);
            pageLoader().setVisibility(View.GONE);
            View nocontent = noContentContainer();
            nocontent.setVisibility(View.VISIBLE);
            TextView heading = (TextView) nocontent.findViewById(R.id.heading);
            Button retryBtn = (Button) nocontent.findViewById(R.id.retry);
            heading.setText("Error occurred");
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading();
                    ServiceLoader.getWebServices().fetchHouses(currentPage, PAGE_SIZE, callback);
                }
            });
        }
    }

    private void loadingDone(List<House> houses) {
        if (ok()) {
            RecyclerView recyclerView = housesRecyclerView();
            recyclerView.setVisibility(View.VISIBLE);
            progressIndicator().setVisibility(View.GONE);
            pageLoader().setVisibility(View.GONE);
            noContentContainer().setVisibility(View.GONE);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                if (adapter instanceof HouseListAdapter) {
                    HouseListAdapter houseListAdapter = (HouseListAdapter) adapter;
                    houseListAdapter.addAll(houses);
                }
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > 0) {
                            LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            visibleItemCount = mLayoutManager.getChildCount();
                            totalItemCount = mLayoutManager.getItemCount();
                            pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    loading();
                                    ServiceLoader.getWebServices().fetchHouses(currentPage, PAGE_SIZE, callback);
                                }
                            }
                        }
                    }
                });
                recyclerView.setAdapter(new HouseListAdapter(houses));
            }
        }
    }

    private void empty() {
        if (ok() && isEmpty()) {
            housesRecyclerView().setVisibility(View.GONE);
            progressIndicator().setVisibility(View.GONE);
            pageLoader().setVisibility(View.GONE);
            View nocontent = noContentContainer();
            nocontent.setVisibility(View.VISIBLE);
            TextView heading = (TextView) nocontent.findViewById(R.id.heading);
            Button retryBtn = (Button) nocontent.findViewById(R.id.retry);
            heading.setText("No Houses found.");
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading();
                    ServiceLoader.getWebServices().fetchHouses(currentPage, PAGE_SIZE, callback);
                }
            });
        }
    }

}
