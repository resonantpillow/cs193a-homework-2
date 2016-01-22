/*
 * Michael Mullins-Jensen
 * To-Do List App
 *
 * Description: A to do list that the user can add items to. A Long click removes an item.
 *              The app handles exits and screen rotations by writing the list to internal memory.
 */

package com.example.michael.tdlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private ArrayList<String> toDoItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Connect the ListView to an ArrayList of to-do items
        toDoItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                toDoItems
        );
        ListView list = (ListView) findViewById(R.id.listListView);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(this);

        getListFromFile();
    }

   @Override
    protected void onStop() {
        writeListToFile();
        super.onStop();

    }

    private void writeListToFile() {
        try {
            PrintStream ps = new PrintStream(openFileOutput("list.txt", Context.MODE_PRIVATE));
            for (int i = 0; i < toDoItems.size(); i++) {
                ps.println(toDoItems.get(i));
            }
            ps.close();
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException f) {
        }
    }

    /*
     * Get the the to-do list which has been stored in the list file.
     */
    private void getListFromFile() {
        try {
            Scanner scan = new Scanner(openFileInput("list.txt"));
            while (scan.hasNextLine()) {
                toDoItems.add(scan.nextLine());
            }
            scan.close();
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException f) {

        }
    }

    /*
     * When the button is pressed, add the currently entered word to the to-do list, and update
     * the adapter.
     */
    public void addButtonClick(View view) {
        // Add the new item to the resource file containing the to-do list
        EditText et = (EditText) findViewById(R.id.enterText);
        toDoItems.add(et.getText().toString());
        adapter.notifyDataSetChanged();
        et.setText("");
    }

    /*
     * On a long click, remove the corresponding item from the list.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toDoItems.remove(position);
        adapter.notifyDataSetChanged();

        return true;
    }

}
