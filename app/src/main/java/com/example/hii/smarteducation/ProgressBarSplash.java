package com.example.hii.smarteducation;

import android.os.AsyncTask;
import android.widget.ProgressBar;

/**
 * Created by hii on 1/29/2016.
 */
public class ProgressBarSplash extends AsyncTask<String, Integer, Integer> {

    public interface LoadingTaskFinishedListener {
        void onTaskFinished(); // If you want to pass something back to the listener add a param to this method
    }

    private final ProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingTaskFinishedListener finishedListener;

   public ProgressBarSplash(ProgressBar progressBar, LoadingTaskFinishedListener finishedListener) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
    }

    @Override
    protected Integer doInBackground(String... params) {

        int count = 5;
        for (int i = 0; i < count; i++) {

            try {
                int progress = (int) ((i / (float) count) * 100);
                publishProgress(progress);
                Thread.sleep(500);
            } catch (Exception e) {

            }
        }
        return 1234;
    }
        @Override
        protected void onProgressUpdate (Integer... values){
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
        }

        @Override
        protected void onPostExecute (Integer result){
            super.onPostExecute(result);
            finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
        }
    }

