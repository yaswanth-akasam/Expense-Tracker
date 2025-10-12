package com.expensetracker.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.ExpenseWithCategory;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Expense> __insertionAdapterOfExpense;

  private final EntityDeletionOrUpdateAdapter<Expense> __deletionAdapterOfExpense;

  private final EntityDeletionOrUpdateAdapter<Expense> __updateAdapterOfExpense;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExpense = new EntityInsertionAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `expenses` (`id`,`amount`,`category_id`,`date_time`,`note`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        statement.bindLong(3, entity.categoryId);
        statement.bindLong(4, entity.dateTime);
        if (entity.note == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.note);
        }
      }
    };
    this.__deletionAdapterOfExpense = new EntityDeletionOrUpdateAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfExpense = new EntityDeletionOrUpdateAdapter<Expense>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `expenses` SET `id` = ?,`amount` = ?,`category_id` = ?,`date_time` = ?,`note` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        statement.bindLong(3, entity.categoryId);
        statement.bindLong(4, entity.dateTime);
        if (entity.note == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.note);
        }
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public void insert(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfExpense.insert(expense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfExpense.handle(expense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Expense expense) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfExpense.handle(expense);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<ExpenseWithCategory>> getAllExpensesWithCategory() {
    final String _sql = "SELECT * FROM expenses ORDER BY date_time DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"categories",
        "expenses"}, true, new Callable<List<ExpenseWithCategory>>() {
      @Override
      @Nullable
      public List<ExpenseWithCategory> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
            final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "date_time");
            final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
            final LongSparseArray<Category> _collectionCategory = new LongSparseArray<Category>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomExpensetrackerModelCategory(_collectionCategory);
            final List<ExpenseWithCategory> _result = new ArrayList<ExpenseWithCategory>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ExpenseWithCategory _item;
              final Expense _tmpExpense;
              if (!(_cursor.isNull(_cursorIndexOfId) && _cursor.isNull(_cursorIndexOfAmount) && _cursor.isNull(_cursorIndexOfCategoryId) && _cursor.isNull(_cursorIndexOfDateTime) && _cursor.isNull(_cursorIndexOfNote))) {
                final double _tmpAmount;
                _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
                final int _tmpCategoryId;
                _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
                final long _tmpDateTime;
                _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
                final String _tmpNote;
                if (_cursor.isNull(_cursorIndexOfNote)) {
                  _tmpNote = null;
                } else {
                  _tmpNote = _cursor.getString(_cursorIndexOfNote);
                }
                _tmpExpense = new Expense(_tmpAmount,_tmpCategoryId,_tmpDateTime,_tmpNote);
                _tmpExpense.id = _cursor.getInt(_cursorIndexOfId);
              } else {
                _tmpExpense = null;
              }
              final Category _tmpCategory;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_1 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_1);
              } else {
                _tmpCategory = null;
              }
              _item = new ExpenseWithCategory();
              _item.expense = _tmpExpense;
              _item.category = _tmpCategory;
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Double> getTotalAmountByDateRange(final long startDate, final long endDate) {
    final String _sql = "SELECT SUM(amount) FROM expenses WHERE date_time BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomExpensetrackerModelCategory(
      @NonNull final LongSparseArray<Category> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomExpensetrackerModelCategory(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`color_hex`,`icon_name`,`is_default` FROM `categories` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColorHex = 2;
      final int _cursorIndexOfIconName = 3;
      final int _cursorIndexOfIsDefault = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Category _item_1;
          final String _tmpName;
          if (_cursor.isNull(_cursorIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _cursor.getString(_cursorIndexOfName);
          }
          final String _tmpColorHex;
          if (_cursor.isNull(_cursorIndexOfColorHex)) {
            _tmpColorHex = null;
          } else {
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
          }
          final String _tmpIconName;
          if (_cursor.isNull(_cursorIndexOfIconName)) {
            _tmpIconName = null;
          } else {
            _tmpIconName = _cursor.getString(_cursorIndexOfIconName);
          }
          final boolean _tmpIsDefault;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
          _tmpIsDefault = _tmp != 0;
          _item_1 = new Category(_tmpName,_tmpColorHex,_tmpIconName,_tmpIsDefault);
          _item_1.id = _cursor.getInt(_cursorIndexOfId);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
