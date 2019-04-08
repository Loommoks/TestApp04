package su.zencode.testapp04;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import su.zencode.testapp04.CategoryLab.IOfferLab;
import su.zencode.testapp04.CategoryLab.OfferLab;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public class OffersListFragment extends Fragment implements UpdatableOffersFragment{
    private static String TAG = "OffersListFragment";
    private static String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;
    private IOfferLab mOfferLab;
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
        mOfferLab = OfferLab.getInstance(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);

        mOfferRecyclerView = view.findViewById(R.id.offers_recycler_view);
        mOfferRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showProgressBar();
        mOfferLab.getCategory(mCategoryId, this);
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

    private void showProgressBar() {
        ((EaptekaProgressBarableActivity) getActivity()).showProgressBar();
    }

    private void hideProgressBar() {
        ((EaptekaProgressBarableActivity) getActivity()).hideProgressBar();
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
    public void setupCategory(Category category) {
        setActivityBarTitle(category);
    }

    @Override
    public void updateCategoryData(Category category) {
        mOfferList = category.getOfferList();
        updateUI();
    }

    @Override
    public void updateOfferImage(Offer offer) {
        int id = offer.getId();
        //OfferHolder holder = mImageRequests.get(id);
        //Drawable drawable = new BitmapDrawable(getResources(), offer.getIconBitmap());
        //holder.setupImage(drawable);
        mImageRequests.remove(id);
        mAdapter.notifyItemChanged(mOfferList.indexOf(offer));
    }

    private void requestImage(Offer offer, OfferHolder holder) {
        mImageRequests.put(offer.getId(), holder);
        mOfferLab.getOfferImage(offer, this);
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
            //todo image downloading

            if(mOffer.getIconBitmap() == null)
                requestImage(offer, this);
            else {
                Drawable drawable = new BitmapDrawable(getResources(), mOffer.getIconBitmap());
                setupImage(drawable);
            }
        }

        public void setupImage(Drawable drawable) {
            mIconImageView.setImageDrawable(drawable);
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
