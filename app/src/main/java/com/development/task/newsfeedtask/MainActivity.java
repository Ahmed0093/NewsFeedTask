package com.development.task.newsfeedtask;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.development.task.newsfeedtask.Adapter.ImageAdapter;
import com.development.task.newsfeedtask.Adapter.NewsAdapter;
import com.development.task.newsfeedtask.Adapter.NewsAdapterClickListener;
import com.development.task.newsfeedtask.DB.DataBaseClient;
import com.development.task.newsfeedtask.DB.localdb.TaskLocalDataSource;
import com.development.task.newsfeedtask.DB.localdb.TasksDataSource;
import com.development.task.newsfeedtask.model.Article;
import com.development.task.newsfeedtask.model.NewsFeed;
import com.development.task.newsfeedtask.model.WeatherData;
import com.development.task.newsfeedtask.model.WeatherModel;
import com.development.task.newsfeedtask.model.WeatherResponse;
import com.development.task.newsfeedtask.network.ApiCLient;
import com.development.task.newsfeedtask.network.NewsFeedService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,NewsAdapterClickListener {
    //NewsAdapter newsAdapter;
    ImageAdapter newsAdapter;

    RecyclerView recyclerView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 12;
    ImageView imgView;
    String timeStamp;
    String imageFileName;
    String currentPhotoPath;
    List<Article> articles;
    List<WeatherModel> weatherModelslist = new ArrayList<>();
    TaskLocalDataSource taskLocalDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_news);
        imgView = (ImageView) findViewById(R.id.imageCamera);
        taskLocalDataSource= TaskLocalDataSource.getInstance(
                getApplicationContext()
                ,DataBaseClient.getInstance(getApplication()).getAppDatabase().taskDao());
        taskLocalDataSource.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<WeatherModel> tasks) {
                newsAdapter.UpdateImageList(tasks);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        newsAdapter = new ImageAdapter(getApplicationContext(), weatherModelslist, this, getResources());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Bitmap drawText1(Bitmap originalBitmap) {

        Canvas canvas = new Canvas(originalBitmap.copy(Bitmap.Config.ARGB_8888, true));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // Text Color
        paint.setTextSize(12); // Text Size
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
        // some more settings...

        canvas.drawBitmap(originalBitmap, 0, 0, paint);
        canvas.drawText("Testing123...", 10, 10, paint);
        // NEWLY ADDED CODE ENDS HERE ]
        return originalBitmap;

    }

    private Bitmap ProcessingBitmap(String captionString,Bitmap oldBitmap) {
        Bitmap bm1 = oldBitmap;
        Bitmap newBitmap = null;
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        //            Toast.makeText(MainActivity.this, pickedImage.getPath(), Toast.LENGTH_LONG).show();
//            bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(pickedImage));
        Bitmap.Config config = bm1.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bm1, 0, 0, null);
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLUE);
        paintText.setTextSize(50*scale);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
        Rect textRect = new Rect();
        paintText.getTextBounds(captionString, 0, captionString.length(), textRect);
        if(textRect.width() >= (canvas.getWidth() - 4))
            paintText.setTextSize(20*scale);
        int xPos = (canvas.getWidth() / 2) - 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((paintText.descent() + paintText.ascent()) / 2)) ;
        canvas.drawText(captionString, xPos, yPos, paintText);
        return newBitmap;
    }
    private void storeImage(Bitmap mBitmap, String path) {
        OutputStream fOut = null;
        File file = new File(path);
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap drawTextToBitmap(Context context,int gResId, int textSize, String text1, String text2,Bitmap imageBitmap)  {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
       Bitmap bitmap = imageBitmap;//BitmapFactory.decodeResource(resources, drawable.getRe);

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.rgb(93, 101, 67));
        paint.setColor(Color.BLUE);
        // text size in pixels
        paint.setTextSize((Float)(textSize * scale));
        //custom fonts
//        Typeface fontFace = ResourcesCompat.getFont(context, R.font.acrobat);
//        paint.typeface = Typeface.create(fontFace, Typeface.NORMAL)
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        //draw the first text
        paint.getTextBounds(text1, 0, text1.length(), bounds);
        float x = (bitmap.getWidth() - bounds.width()) / 2f ;//- 470;
        float y = (bitmap.getHeight() + bounds.height()) / 2f ;//- 140;
        canvas.drawText(text1, 100, 100, paint);
        //draw the second text
        paint.getTextBounds(text2, 0, text2.length(), bounds);
        x = (bitmap.getWidth() - bounds.width()) / 2f - 470;
        y = (bitmap.getHeight() + bounds.height()) / 2f + 235;
        canvas.drawText(text2, x, y, paint);

        return bitmap;
    }
    public Bitmap drawText(String text, int textWidth, int color ,Bitmap bitmap) {
        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);         	textPaint.setColor(Color.parseColor("#ff00ff"));         	textPaint.setTextSize(30);
        StaticLayout mTextLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        // Create bitmap and canvas to draw to
        Bitmap b =bitmap;// Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), 		Bitmap.Config.ARGB_4444);
        Bitmap workingBitmap = Bitmap.createBitmap(b);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        // Draw background

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | 			Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        c.drawPaint(paint);
        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();
        return b;
    }
    private void initAdapter(List<Article> articles) {
       this.articles = articles;
        setListImage();
      //  newsAdapter = new ImageAdapter(getApplicationContext(), this.articles, this, getResources());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initNewsFeedService() {
        NewsFeedService newsFeedService = ApiCLient.getClient(this).create(NewsFeedService.class);
        newsFeedService.getCairoWeaterResponse().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observer getObserver() {
        return new DisposableObserver<WeatherResponse>() {
            @Override
            public void onNext(WeatherResponse weatherData) {
                Toast.makeText(MainActivity.this,"on Next call api",Toast.LENGTH_LONG).show();
                initWeatherAdapter(weatherData.getList().get(0).getWeather().get(0).getDescription());

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();
                initWeatherAdapter("error description");

            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this,"Complete call api",Toast.LENGTH_LONG).show();

            }
        };
    }

    private void initWeatherAdapter(String description) {
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        WeatherModel weatherMode= new WeatherModel();
        for(int i =0 ; i<=path.list().length-1;i++) {
            File file = new File(path.getAbsolutePath()+"/"+path.list()[i]);
            Uri uriForFile = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    file);//Uri.fromFile(file);
          Bitmap bitmap;
            try {
                // path.getAbsolutePath() + "/" + path.list()[0]
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriForFile);
//                bitmap = crupAndScale(bitmap, 300); // if you mind scaling
//                imgView.setImageBitmap(bitmap);
                if(bitmap != null) {
                    //articles.get(i).setImgBitmap(drawTextToBitmap(getApplicationContext(),R.drawable.explore,15,"Ahmed123","MOHAMED",bitmap));
                   // storeImage(ProcessingBitmap(description,bitmap),path.getAbsolutePath()+"/"+path.list()[i]);
                    weatherMode.setImgBitmap(ProcessingBitmap(description,bitmap));
                    weatherMode.setImgUri(uriForFile);
                    weatherMode.setImagepath(path.getAbsolutePath()+"/"+path.list()[i]);
                   taskLocalDataSource.saveTask(weatherMode);
                    weatherModelslist.add(weatherMode);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        newsAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            initNewsFeedService();
            // Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) data.getData();
            //imageView.setImageBitmap(imageBitmap);
            //setCurrentImage();

        }
//            imgView.setImageURI(data.getData());

    }

    private void setCurrentImage() {
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(currentPhotoPath);
        Uri uri = Uri.fromFile(file);
        Bitmap bitmap;
        try {
           // path.getAbsolutePath()+"/"+path.list()[0]
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                bitmap = crupAndScale(bitmap, 300); // if you mind scaling
//            articles.set(i);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setListImage() {
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        int maxLoop=0;
        if (path.list().length > articles.size()) {
            maxLoop = articles.size() - 1;
        } else {
            maxLoop = path.list().length-1;
        }
        for(int i =0 ; i<=maxLoop;i++) {
            File file = new File(path.getAbsolutePath()+"/"+path.list()[i]);
            Uri uriForFile = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    file);//Uri.fromFile(file);
            articles.get(i).setImgUri(uriForFile);
            Bitmap bitmap;
            try {
               // path.getAbsolutePath() + "/" + path.list()[0]
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriForFile);
//                bitmap = crupAndScale(bitmap, 300); // if you mind scaling
//                imgView.setImageBitmap(bitmap);
                if(bitmap != null) {
                   //articles.get(i).setImgBitmap(drawTextToBitmap(getApplicationContext(),R.drawable.explore,15,"Ahmed123","MOHAMED",bitmap));
                    storeImage(ProcessingBitmap( articles.get(i).getAuthor(),bitmap),path.getAbsolutePath()+"/"+path.list()[i]);
                    articles.get(i).setImgBitmap(ProcessingBitmap( articles.get(i).getAuthor(),bitmap));
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void sendTweet(Uri imgUri,Bitmap bitmap) {
        String msg = "post";
        Uri uri = imgUri;
       // String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
//        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpg");
//        intent.setPackage("com.twitter.android");
        startActivity(Intent.createChooser(intent, "Share image via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id ==R.id.nav_explore) {
            dispatchTakePictureIntent();
        }
        Toast.makeText(this,item.getTitle(),Toast.LENGTH_LONG).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onArticleItemClicked(WeatherModel article) {
       // startDetailsActivity(article);
        //dispatchTakePictureIntent();
        sendTweet(article.getImgUriConverted(),article.getImgBitmapConverted());

    }
    private File createImageFile() throws IOException {
        // Create an image file name
         timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void dispatchTakePictureIntent1() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void startDetailsActivity(Article article) {
        Intent intent = new Intent(MainActivity.this,ArticleDetailsActivity.class);
        intent.putExtra("article",article);
        startActivity(intent);
        Toast.makeText(this, "titleClicked", Toast.LENGTH_LONG).show();
    }
}
