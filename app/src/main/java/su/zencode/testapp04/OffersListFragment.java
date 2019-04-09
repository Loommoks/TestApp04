package su.zencode.testapp04;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import su.zencode.testapp04.AsyncServices.CategoryAsyncService;
import su.zencode.testapp04.AsyncServices.ICategoryAcceptor;
import su.zencode.testapp04.AsyncServices.ImageAsyncService;
import su.zencode.testapp04.EaptekaRepositories.Entities.Category;
import su.zencode.testapp04.EaptekaRepositories.Entities.Offer;

public class OffersListFragment extends Fragment implements ICategoryAcceptor {
    private static String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;
    private CategoryAsyncService mCategoryService;
    private ImageAsyncService<OfferHolder> mImageService;
    private ArrayList<Offer> mOfferList;
    private RecyclerView mOfferRecyclerView;
    private OfferAdapter mAdapter;
    private HashMap<Integer, OfferHolder> mImageRequests;

    public static OffersListFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);

        OffersListFragment fragment = new OffersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mImageRequests = new HashMap<>();
        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID, 0);
        mCategoryService = CategoryAsyncService.getInstance(getActivity().getApplicationContext());
        Handler responseHandler = new Handler();
        mImageService = new ImageAsyncService<>(responseHandler);
        mImageService.setImageAcceptor(
                new ImageAsyncService.IImageAcceptor<OfferHolder>() {
                    @Override
                    public void onImageDownloaded(OfferHolder target, Bitmap bitmap) {
                        target.bindImage(bitmap);
                    }
                });
        mImageService.start();
        mImageService.getLooper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);

        mOfferRecyclerView = view.findViewById(R.id.offers_recycler_view);
        mOfferRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showProgressBar();
        mCategoryService.getCategory(mCategoryId, this);
        return view;
    }

    private void setActivityBarTitle(Category category) {
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(category.getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressBar();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageService.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageService.quit();
    }

    private void showProgressBar() {
        ((IProgressBarableActivity) getActivity()).showProgressBar();
    }

    private void hideProgressBar() {
        ((IProgressBarableActivity) getActivity()).hideProgressBar();
    }

    private void updateUI() {

        if(mOfferList == null) return;

        if(mAdapter == null) {
            mAdapter = new OfferAdapter(mOfferList);
        } else {
            mAdapter.setOfferList(mOfferList);
            mAdapter.notifyDataSetChanged();
        }
        mOfferRecyclerView.setAdapter(mAdapter);
        hideProgressBar();
    }

    @Override
    public void initializeCategory(Category category) {
        if(!activityBinded()) return;
        setActivityBarTitle(category);
    }

    @Override
    public void updateCategoryData(Category category) {
        mOfferList = category.getOfferList();
        if(!activityBinded()) return;
        updateUI();
    }

    private boolean activityBinded() {
        return (getActivity() != null);
    }

    private class OfferHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Offer mOffer;
        private TextView mIdTextView;
        private TextView mTitleTextView;
        private ImageView mIconImageView;

        public OfferHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_offer, parent, false));
            itemView.setOnClickListener(this);

            mIconImageView = itemView.findViewById(R.id.offer_icon);
            mIdTextView = itemView.findViewById(R.id.offer_id);
            mTitleTextView = itemView.findViewById(R.id.offer_title);
        }

        public void bind(Offer offer) {
            mOffer = offer;
            mIdTextView.setText(Integer.toString(mOffer.getId()));
            mTitleTextView.setText(mOffer.getName());
            mIconImageView.setImageDrawable(
                    getResources().getDrawable(R.drawable.placeholder_round)
            );

            Bitmap bitmap = mOffer.getIconBitmap();
            if (bitmap == null) mImageService.queueImage(this, mOffer.getIconUrl());
            else bindImage(bitmap);
        }

        public void bindImage(Bitmap bitmap) {
            mOffer.setIconBitmap(bitmap);
            mIconImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mOffer.getId() + " offer selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public class OfferAdapter extends RecyclerView.Adapter<OfferHolder> {
        private List<Offer> mOfferList;

        public OfferAdapter(List<Offer> offers) {
            mOfferList = offers;
        }

        @NonNull
        @Override
        public OfferHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new OfferHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull OfferHolder offerHolder, int i) {
            Offer offer = mOfferList.get(i);
            offerHolder.bind(offer);
        }

        @Override
        public int getItemCount() {
            return mOfferList.size();
        }

        public void setOfferList(List<Offer> offerList) {
            mOfferList = offerList;
        }
    }

}
