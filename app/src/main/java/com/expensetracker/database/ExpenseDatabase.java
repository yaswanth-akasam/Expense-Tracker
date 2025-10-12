package com.expensetracker.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Category.class}, version = 1, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();

    private static volatile ExpenseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExpenseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ExpenseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "expense_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                try {
                    CategoryDao dao = INSTANCE.categoryDao();
                    dao.insert(new Category("Food", "#FF5722", "restaurant", true));
                    dao.insert(new Category("Travel", "#9C27B0", "directions_car", true));
                    dao.insert(new Category("Bills", "#607D8B", "receipt", true));
                    dao.insert(new Category("Shopping", "#E91E63", "shopping_cart", true));
                    dao.insert(new Category("Health", "#4CAF50", "local_hospital", true));
                    dao.insert(new Category("Entertainment", "#FF9800", "movie", true));
                    dao.insert(new Category("Other", "#795548", "category", true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };
}