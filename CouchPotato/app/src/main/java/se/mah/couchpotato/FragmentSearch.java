package se.mah.couchpotato;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import se.mah.couchpotato.activitytvshow.RecyclerViewSpacing;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class FragmentSearch extends Fragment implements FragmentInterface {

    private RecyclerView recyclerViewShows;
    private RecyclerViewAdapter adapter;
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initializeComponents(rootView, savedInstanceState);
        return rootView;
    }

    private void initializeComponents(View rootView, Bundle savedInstanceState) {
        recyclerViewShows = (RecyclerView) rootView.findViewById(R.id.rv_search);
        recyclerViewShows.addItemDecoration(new RecyclerViewStaggeredSpacing(10));
        recyclerViewShows.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new RecyclerViewAdapter((AppCompatActivity) getActivity());
        recyclerViewShows.setAdapter(adapter);

        etSearch = (EditText) rootView.findViewById(R.id.et_search);
        if (savedInstanceState != null)
            etSearch.setText(savedInstanceState.getString("searchString"));
        etSearch.addTextChangedListener(new Listener());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchString", etSearch.getText().toString());
    }

    @Override
    public Controller getController() {
        return ((ActivityInterface) getActivity()).getController();
    }

    @Override
    public void updateFragmentData(ArrayList<TvShow> shows) {
        adapter.setTvShowArrayList(shows);
    }

    private class Listener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String searchString = charSequence.toString();
            if (searchString.length() > 3) {
                getController().search(searchString);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
