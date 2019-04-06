package su.zencode.testapp04;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import su.zencode.testapp04.EaptekaRepositories.CacheableDatabaseRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;
import su.zencode.testapp04.EaptekaRepositories.IEaptekaCategoryRepository;
import su.zencode.testapp04.TestAppApiClient.EaptekaApiClient;
import su.zencode.testapp04.TestAppApiClient.IEaptekaApiClient;

public class CategoriesListFragment extends Fragment {
    private static final String TAG = "CategoriesListFragment";
    private static final String ARG_CATEGORY_ID = "category_id";

    private int mCategoryId;
    private IEaptekaCategoryRepository mRepository;
    private RecyclerView mCategoryRecyclerView;
    private CategoryAdapter mAdapter;
    private Category mCategory;

    public static CategoriesListFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);

        CategoriesListFragment fragment = new CategoriesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID, 0);
        //mRepository = CacheRepository.getInstance();
        mRepository = CacheableDatabaseRepository.getInstance(getActivity());
        //mRepository = DatabaseRepository.getInstance(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list,container,false);
        mCategoryRecyclerView = view.findViewById(R.id.categories_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        showProgressBar();
        mCategory = mRepository.get(mCategoryId);
        setActivityBarTitle();
        getSubCategoriesList();

        return view;
    }

    private void setActivityBarTitle() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mCategory.getName());
    }

    private void showProgressBar() {
        ((EaptekaProgressBarableActivity) getActivity()).showProgressBar();
    }

    private void hideProgressBar() {
        ((EaptekaProgressBarableActivity) getActivity()).hideProgressBar();
    }

    private void getSubCategoriesList() {
        //todo заменить на обращение @get к репозиторию/Lab
        if(mCategory.getSubCategoriesList() == null) {
            FetchSubCategoriesTask fetchSubCategoriesTask = new FetchSubCategoriesTask();
            fetchSubCategoriesTask.execute(mCategory.getId());
        } else updateCategoriesListUI();
    }

    private void setupSubCategoriesList(ArrayList<Category> subCategoriesList) {
        updateCategoriesListUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressBar();
        updateCategoriesListUI();
    }

    private void updateCategoriesListUI() {
        List<Category> subCategories = mCategory.getSubCategoriesList();
        //todo remove null check after LAb&Repo update
        if(subCategories == null) return;
        setupNewAdapterList(subCategories);
        hideProgressBar();
    }

    private void setupNewAdapterList(List<Category> subCategories) {
        //todo refactor: separate dataSet change & setNew Adapter
        if(mAdapter == null) {
            mAdapter = new CategoryAdapter(subCategories);
        } else {
            mAdapter.setSubCategories(subCategories);
            mAdapter.notifyDataSetChanged();
        }
        mCategoryRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Category mCategory;
        private TextView mIdTextView;
        private TextView mTitleTextView;


        public CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_category,parent,false));
            itemView.setOnClickListener(this);

            mIdTextView = itemView.findViewById(R.id.category_id);
            mTitleTextView = itemView.findViewById(R.id.category_title);
        }

        public void bind(Category category) {
            mCategory = category;
            mIdTextView.setText(Integer.toString(mCategory.getId()));
            mTitleTextView.setText(mCategory.getName());
        }

        @Override
        public void onClick(View v) {
            if(mCategory.hasSubCategories()) {
                startNewCategoryFragment();
            } else {
                startNewOffersListActivity();
            }
        }

        private void startNewOffersListActivity() {
            Intent intent = OffersListActivity.newIntent(getActivity(), mCategory.getId());
            startActivity(intent);
        }

        private void startNewCategoryFragment() {
            Fragment newFragment = newInstance(mCategory.getId());
            getFragmentManager().beginTransaction()
                    .replace(R.id.launch_activity_fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> mSubCategories;

        public CategoryAdapter(List<Category> subCategories){
            mSubCategories = subCategories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CategoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int i) {
            Category category = mSubCategories.get(i);
            holder.bind(category);
        }

        @Override
        public int getItemCount() {
            return mSubCategories.size();
        }

        public void setSubCategories(List<Category> categories) {
            mSubCategories = categories;
        }
    }

    private class FetchSubCategoriesTask extends AsyncTask<Integer, Void, Integer> {
        ArrayList<Category> mSubCategories;

        @Override
        protected Integer doInBackground(Integer... values) {
            IEaptekaApiClient apiClient = new EaptekaApiClient();
            mSubCategories = apiClient.fetchSubCategories(values[0]);

            return values[0];
        }

        @Override
        protected void onPostExecute(Integer id) {
            mCategory.setSubCategoriesList(mSubCategories);
            for (Category subCategory :
                    mSubCategories) {
                mRepository.add(subCategory);
            }
            mRepository.update(mCategory);
            updateCategoriesListUI();
        }
    };

}
