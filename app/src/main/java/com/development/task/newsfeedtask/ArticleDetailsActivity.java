package com.development.task.newsfeedtask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.development.task.newsfeedtask.helper.DateHelper;
import com.development.task.newsfeedtask.model.Article;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArticleDetailsActivity extends AppCompatActivity {

    private Article article;
    private ImageView articleimageView;
    private TextView titleText , articleDescription , articleAuthor,articledate;
    private Button gotoWebVIewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To retrieve object in second Activity
        setContentView(R.layout.activity_details_article);
        article = (Article) getIntent().getSerializableExtra("article");
        articleimageView = findViewById(R.id.article_image);
        titleText = findViewById(R.id.title);
        articleDescription = findViewById(R.id.description);
        articleAuthor = findViewById(R.id.author);
        articledate = findViewById(R.id.publish_date);
        gotoWebVIewButton = findViewById(R.id.button_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        initUi();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    private void initUi() {
        titleText.setText(article.getTitle());
        articleDescription.setText(article.getDescription());
        articleAuthor.setText(article.getAuthor());
        articledate.setText(DateHelper.convertToSpecificFormat(article.getPublishedAt()));
        Glide.with(this).load(article.getUrlToImage()).fitCenter().placeholder(R.drawable.placeholder).into(articleimageView);
        gotoWebVIewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = article.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }

}
