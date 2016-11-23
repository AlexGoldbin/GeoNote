package comgoldbin.vk.geonotes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;




public class ListActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button btndell = (Button) findViewById(R.id.alldel);

        final RepoBDHelper db = new RepoBDHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ArrayList<DBData> datenotes = db.selectAll();

        mAdapter = new RecyclerAdapter(datenotes);
        mRecyclerView.setAdapter(mAdapter);


        btndell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.deleteAll();
                finish();
                showMessagaDELL(v);
            }
        });
    }

    public void showMessagaDELL(View view) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Все записи удалены!",
                Toast.LENGTH_SHORT);
                toast.show();
    }

}


