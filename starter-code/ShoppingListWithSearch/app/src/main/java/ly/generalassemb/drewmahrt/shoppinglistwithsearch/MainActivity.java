package ly.generalassemb.drewmahrt.shoppinglistwithsearch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView mShoppingListView;
    private CursorAdapter mCursorAdapter;
    ShoppingSQLiteOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShoppingListView = (ListView)findViewById(R.id.shopping_list_view);

        Cursor cursor = ShoppingSQLiteOpenHelper.getInstance(MainActivity.this).getShoppingList();

        mCursorAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,new String[]{ShoppingSQLiteOpenHelper.COL_ITEM_NAME},new int[]{android.R.id.text1},0);
        mShoppingListView.setAdapter(mCursorAdapter);

        mHelper = ShoppingSQLiteOpenHelper.getInstance(MainActivity.this);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            // Do The actual database search

            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(MainActivity.this, "You are searched for " + query, Toast.LENGTH_SHORT).show();

            Cursor newCursor = ShoppingSQLiteOpenHelper.getInstance(MainActivity.this).searchList(query);
            mCursorAdapter.swapCursor(newCursor);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());

        searchView.setSearchableInfo(info);

        return super.onCreateOptionsMenu(menu);
    }
}
