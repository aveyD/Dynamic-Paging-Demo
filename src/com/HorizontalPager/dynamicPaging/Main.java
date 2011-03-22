package com.HorizontalPager.dynamicPaging;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class Main extends Activity 
{
	HorizontalPager dynamicViewSwitcher;
	ImageView image_prev, image_centre, image_next;
	
	// this could be an arraylist of objects which contains resource ids to the images
	// but for this simple example we will just use the ids.
	ArrayList<Integer> image_res_ids; 
	
	int numPages;
	int currentPage = 0;
	int currPrevCentreNextPage = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        image_prev = (ImageView)findViewById(R.id.image_prev);
        image_centre = (ImageView)findViewById(R.id.image_centre);
        image_next = (ImageView)findViewById(R.id.image_next);
        
        image_res_ids = new ArrayList<Integer>();
        image_res_ids.add(R.drawable.zero);
        image_res_ids.add(R.drawable.one);
        image_res_ids.add(R.drawable.two);
        image_res_ids.add(R.drawable.three);
        image_res_ids.add(R.drawable.four);
        
        numPages = image_res_ids.size();
        
        Log.d("HorizontalPager", "NUM PAGES = "+numPages);
        
        // screens are as follows:
        //    0      1      2
        // [ Prev, Centre, Next ]
        dynamicViewSwitcher = (HorizontalPager)findViewById(R.id.horizontal_pager);

    	// these cases are for if you select a different currentPage from a listView for example
    	if (currentPage == 0)
    	{
    		// user chooses to start on first page (default behaviour in this app)
    		// ie: don't let them scroll left
    		dynamicViewSwitcher.setCurrentScreen(0, false);
    		loadNewContent(1); 
    	}
    	else if (currentPage == numPages)
    	{
    		// user chooses to start on last page
    		// ie: don't let them scroll right
    		dynamicViewSwitcher.setCurrentScreen(2, false);
    		loadNewContent(numPages-1);
    	}
    	else
    	{
    		// user chooses to start somewhere in the middle of the list
    		dynamicViewSwitcher.setCurrentScreen(currentPage, false);
    		loadNewContent(currentPage);
    	}
    	
    	dynamicViewSwitcher.setOnScreenSwitchListener(onScreenSwitchListener);
    }
    
    public void loadNewContent(int newCurrPage)
    {
    	if (newCurrPage > 0)
    		image_prev.setImageResource(image_res_ids.get(newCurrPage-1));
    	
    	if (newCurrPage > 0 && newCurrPage < numPages)
    		image_centre.setImageResource(image_res_ids.get(newCurrPage));
    	
    	if (newCurrPage < numPages-1)
    		image_next.setImageResource(image_res_ids.get(newCurrPage+1));
    }
    
    private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener = new HorizontalPager.OnScreenSwitchListener() 
    {
        public void onScreenSwitched(final int screen) 
        {
            /*
			* this method is executed if a screen has been activated, i.e. the screen is
			* completely visible and the animation has stopped (might be useful for
			* removing / adding new views)
			*/

        	if (screen < currPrevCentreNextPage && currentPage <= 1)
        	{
        		loadNewContent(1);
        		dynamicViewSwitcher.setCurrentScreen(0, false);
        		currPrevCentreNextPage = 0;
        	}
        	else if (screen > currPrevCentreNextPage && currentPage+1 >= numPages-1)
        	{
        		loadNewContent(numPages-2);
        		dynamicViewSwitcher.setCurrentScreen(2, false);
        		currPrevCentreNextPage = 2;
        	}
        	else
        	{
	        	if (screen > currPrevCentreNextPage && currPrevCentreNextPage != 0)
	        	{
	        		currentPage = currentPage+1;
	        		loadNewContent(currentPage);
	        		
	        		dynamicViewSwitcher.setCurrentScreen(1, false);
	        		currPrevCentreNextPage = 1;
	        	}
	        	else if (screen < currPrevCentreNextPage && currPrevCentreNextPage != 2)
	        	{
	        		currentPage = currentPage-1;
	        		loadNewContent(currentPage);
	        		
	        		dynamicViewSwitcher.setCurrentScreen(1, false);
	        		currPrevCentreNextPage = 1;
	        	}
	        	else if (currPrevCentreNextPage == 0 && screen == 1)
	        	{
	        		currentPage = 1;
	        	}
	        	else if (currPrevCentreNextPage == 2 && screen == 1)
	        	{
	        		currentPage = numPages-2;
	        	}
	        	currPrevCentreNextPage = 1;
        	}
        }
	};
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
    {
    	super.onConfigurationChanged(newConfig);
    	dynamicViewSwitcher.orientationSnapToScreen(this);
    }
}