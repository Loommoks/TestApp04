package su.zencode.testapp04;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import su.zencode.testapp04.EaptekaRepositories.CategoriesRepository;
import su.zencode.testapp04.EaptekaRepositories.Category;

public class CategoriesListFragment extends Fragment {
    private static final String TAG = "CategoriesListFragment";
    private static final String ARG_CATEGORY_ID = "category_id";
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

        int categoryId = getArguments().getInt(ARG_CATEGORY_ID, 0);

        mCategory = CategoriesRepository.getIstance().getCategory(categoryId);
        if (mCategory == null && categoryId == 0) {
            Category baseCategory = new Category(0, "base", true);
            CategoriesRepository.getIstance().putCategory(baseCategory);
            mCategory = baseCategory;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list,container,false);
        mCategoryRecyclerView = view.findViewById(R.id.categories_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(mCategory.getSubCategoriesList() == null) {
            fetchSubCategoriesTask.execute(mCategory.getId());
        } else updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Category> subCategories = mCategory.getSubCategoriesList();
        if(subCategories == null) return;

        if(mAdapter == null) {
            mAdapter = new CategoryAdapter(subCategories);
            //mCategoryRecyclerView.setAdapter(mAdapter);
        } else {
            //mCategoryRecyclerView.setAdapter(mAdapter);
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
                Fragment newFragment = newInstance(mCategory.getId());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                //todo new Activity & OffersListFragment
                Fragment newFragment = OffersListFragment.newInstance(mCategory.getId());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            //todo обработать клик
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

    AsyncTask<Integer,Void,Integer> fetchSubCategoriesTask = new AsyncTask<Integer, Void, Integer>() {
        @Override
        protected Integer doInBackground(Integer... values) {
            Category category = CategoriesRepository.getIstance().getCategory(values[0]);
            if(category.getSubCategoriesList() == null)
                new EaptekaFetcher().fetchSubCategories(values[0]);

            return values[0];
        }

        @Override
        protected void onPostExecute(Integer id) {
            updateUI();
        }
    };

}
