package com.development.task.newsfeedtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.development.task.newsfeedtask.Adapter.NewsAdapter;
import com.development.task.newsfeedtask.Adapter.NewsAdapterClickListener;
import com.development.task.newsfeedtask.model.Article;
import com.development.task.newsfeedtask.model.NewsFeed;
import com.development.task.newsfeedtask.network.ApiCLient;
import com.development.task.newsfeedtask.network.NewsFeedService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,NewsAdapterClickListener {
    NewsAdapter newsAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         recyclerView = findViewById(R.id.recycler_news);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initNewsFeedService();
    }

    private void initAdapter(List<Article> articles) {
        newsAdapter = new NewsAdapter(getApplicationContext(), articles, this, getResources());
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
        newsFeedService.getNewsFeed().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observer getObserver() {
        return new DisposableObserver<NewsFeed>() {
            @Override
            public void onNext(NewsFeed newsFeed) {
                Toast.makeText(MainActivity.this,"on Next call api",Toast.LENGTH_LONG).show();
                initAdapter(newsFeed.getArticles());
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this,"Complete call api",Toast.LENGTH_LONG).show();

            }
        };
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast.makeText(this,item.getTitle(),Toast.LENGTH_LONG).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onArticleItemClicked(Article article) {
        Intent intent = new Intent(MainActivity.this,ArticleDetailsActivity.class);
        intent.putExtra("article",article);
        startActivity(intent);
        Toast.makeText(this, "titleClicked", Toast.LENGTH_LONG).show();
    }
}
