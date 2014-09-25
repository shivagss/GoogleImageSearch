GoogleImageSearch
=================

An image searching application powered by the Google Image Search API

####Total time spent: 8 Hours

####User Stories:
  * [x] User can enter a search query that will display a grid of image results from the Google Image API.
  * [x] User can click on "settings" which allows selection of advanced search options to filter results.
  * [x] User can configure advanced search filters such as:
    * Size (small, medium, large, extra-large)
    * Color filter (black, blue, brown, gray, green, etc...)
    * Type (faces, photo, clip art, line art)
    * Site (espn.com)
  * [x] Subsequent searches will have any filters applied to the search results
  * [x] User can tap on any image in results to see the image full-screen
  * [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
  * [x] Robust error handling, check if internet is available, handle error cases, network failures
  * [x] Use the ActionBar SearchView or custom layout as the query box instead of an EditText
  * [x] User can share an image to their friends or email it to themselves
  * [x] Replace Filter Settings Activity with a lightweight modal overlay
  * [x] Use the StaggeredGridView to display improve the grid of image results
  * [x] User can zoom or pan images displayed in full-screen detail view
  
Libraries used: android-async-http-1.4.5, picasso-2.3.4, com.etsy.android.grid:library:1.0.5, Mike Ortiz's TouchView and android-support-v4.

####Demo:
![Video Walkthrough](demo.gif)
