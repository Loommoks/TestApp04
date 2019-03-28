package su.zencode.testapp04;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import su.zencode.testapp04.EaptekaRepositories.CategoriesRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.Offer;

public class OffersListFragment extends Fragment {
    private static String TAG = "OffersListFragment";
    private static String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;
    private EaptekaCategoryRepository mCategoryRepository;
    private ArrayList<Offer> mOfferList;
    private RecyclerView mOfferRecyclerView;
    private OfferAdapter mAdapter;

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

        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID, 0);
        mCategoryRepository = CategoriesRepository.getInstance();

        mOfferList = mCategoryRepository.get(mCategoryId).getOfferList();
        if (mOfferList == null) {
            //todo solve null List
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);

        mOfferRecyclerView = view.findViewById(R.id.offers_recycler_view);
        mOfferRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(mOfferList == null) {
            FetchOffersTask fetchOffersTask = new FetchOffersTask();
            fetchOffersTask.execute(mCategoryId);
        } else updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mOfferList = mCategoryRepository.get(mCategoryId).getOfferList();
        if(mOfferList == null) return;

        if(mAdapter == null) {
            mAdapter = new OfferAdapter(mOfferList);
            //mOfferRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setOfferList(mOfferList);
            mAdapter.notifyDataSetChanged();
        }
        mOfferRecyclerView.setAdapter(mAdapter);
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
            //todo image downloading
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

    private class FetchOffersTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            Category category = CategoriesRepository.getInstance().get(integers[0]);
            if(category.getOfferList() == null)
                new EaptekaFetcher().fetchOffers(integers[0]);

            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            updateUI();
        }
    }
}
