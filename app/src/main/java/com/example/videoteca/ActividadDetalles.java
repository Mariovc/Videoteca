package com.example.videoteca;

import android.app.Activity;
import android.os.Bundle;

/**
 * Author: Mario Velasco Casquero
 * Date: 20/05/2016
 */
public class ActividadDetalles extends Activity {
    public static final String MOVIE = "Movie";
    public static final String SHARED_ELEMENT_NAME = "hero";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
    }
}