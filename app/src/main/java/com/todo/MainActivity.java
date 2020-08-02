package com.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.todo.components.Recyclerview.Adapters.ToDoListRecyclerViewAdapter;
import com.todo.components.Recyclerview.SwipeListeners.RecyclerViewSwipeListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter todo_adapter;
    private RecyclerView.LayoutManager todo_layoutManager;
    private ArrayList<String> todoRecyclerViewDataset = new ArrayList<>(Arrays.asList(new String[]{"Task 1", "Taks 2", "Task 3", "Task 4"}));

    private float y1, y2;
    private final int MIN_PULL_DOWN_DISTANCE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        createRecyclerView();
    }

    public void createRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.todo_tasks_list);

        recyclerView.hasFixedSize();
        todo_layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(todo_layoutManager);
        todo_adapter = new ToDoListRecyclerViewAdapter(todoRecyclerViewDataset);
        recyclerView.setAdapter(todo_adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback recyclerViewSwipeListenerCallback = new RecyclerViewSwipeListener(0, ItemTouchHelper.LEFT, todoRecyclerViewDataset, todo_adapter, MainActivity.this);
        new ItemTouchHelper(recyclerViewSwipeListenerCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.y1 = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                this.y2 = event.getY();
                if (y2 - y1 > MIN_PULL_DOWN_DISTANCE) {
                    if (checkForActivityChange()) {
                        Intent intent = new Intent(this, addTodoActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.transition_fade_down_in, R.anim.transition_fade_down_out);
                    }
                }
                this.y1 = 0;
                this.y2 = 0;
                break;
        }

        return super.onTouchEvent(event);
    }

    public boolean checkForActivityChange() {
        View view = findViewById(R.id.check_for_activity_change);
        return view != null && view.isShown();
    }
}