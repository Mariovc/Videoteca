package com.example.videoteca;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * Author: Mario Velasco Casquero
 * Date: 20/05/2016
 */
public class FragmentDetalles extends DetailsFragment {

    private static final int ACTION_WATCH_TRAILER = 1;
    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;
    private DetailsOverviewRowPresenter mDorPresenter;
//    private DetailRowBuilderTask mDetailRowBuilderTask;
    private static final int NUM_COLS = 10;

    private Movie mSelectedMovie;
    private Target mBackgroundTarget;
    private DisplayMetrics mMetrics;
    private static final String MOVIE = "Movie";
    private BackgroundManager mBackgroundManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackground();
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mSelectedMovie = (Movie) getActivity().getIntent()
                    .getSerializableExtra(MOVIE);
        } else {
            int selectedIndex = Integer.parseInt(getActivity().getIntent()
                    .getData().getLastPathSegment());
            int indice = 0;
            for (Movie movie : MovieList.list) {
                indice++;
                if (indice == selectedIndex) {
                    mSelectedMovie = movie;
                }
            }
        }
        updateBackground(mSelectedMovie.getBackgroundImageURI().toString());
        mDorPresenter =new DetailsOverviewRowPresenter(
                new DetailsDescriptionPresenter());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        cargarDetalles(mSelectedMovie);
        mDorPresenter.setSharedElementEnterTransition(getActivity(),
                ActividadDetalles.SHARED_ELEMENT_NAME);
        mDorPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_WATCH_TRAILER) {
                    Intent intent = new Intent(getActivity(),
                            PlaybackOverlayActivity.class);
                    intent.putExtra(getResources().getString(R.string.movie),
                            mSelectedMovie);
                    intent.putExtra(getResources().getString(R.string.should_start),
                            true);
                    startActivity(intent);
                }
            }
        });
    }
    private void initBackground() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }
    protected void updateBackground(String uri) {
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(R.drawable.details_img)
                .into(new SimpleTarget<GlideDrawable>(mMetrics.widthPixels,
                        mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
    }
    private void cargarDetalles(Movie mSelectedMovie){
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
        int width = Utils.convertDpToPixel(getActivity()
                .getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = Utils.convertDpToPixel(getActivity()
                .getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(mSelectedMovie.getCardImageUrl())
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        row.setImageDrawable(resource);
                    }
                });
        Drawable icon = ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_media_play, null);
        row.addAction(new Action(ACTION_WATCH_TRAILER, "VER", "VÍDEO", icon));
        ClassPresenterSelector ps = new ClassPresenterSelector();
        ps.addClassPresenter(DetailsOverviewRow.class, mDorPresenter);
        ps.addClassPresenter(ListRow.class, new ListRowPresenter());
        ArrayObjectAdapter adapter = new ArrayObjectAdapter(ps);
        adapter.add(row);
        setAdapter(adapter);
    }
}
