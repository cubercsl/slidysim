package cn.cubercsl.slidysim.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.cubercsl.slidysim.results.Result;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "RESULT".
 */
public class ResultDao extends AbstractDao<Result, Long> {

    public static final String TABLENAME = "RESULT";

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RESULT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TIME\" INTEGER NOT NULL ," + // 1: time
                "\"MOVES\" INTEGER NOT NULL ," + // 2: moves
                "\"TIME_STAMP\" INTEGER NOT NULL );"); // 3: timeStamp
    }


    public ResultDao(DaoConfig config) {
        super(config);
    }

    public ResultDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RESULT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Result entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTime());
        stmt.bindLong(3, entity.getMoves());
        stmt.bindLong(4, entity.getTimeStamp());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Result entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTime());
        stmt.bindLong(3, entity.getMoves());
        stmt.bindLong(4, entity.getTimeStamp());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public Result readEntity(Cursor cursor, int offset) {
        Result entity = new Result( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getLong(offset + 1), // time
                cursor.getInt(offset + 2), // moves
                cursor.getLong(offset + 3) // timeStamp
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, Result entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTime(cursor.getLong(offset + 1));
        entity.setMoves(cursor.getInt(offset + 2));
        entity.setTimeStamp(cursor.getLong(offset + 3));
     }
     
    @Override
    public Long getKey(Result entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final Long updateKeyAfterInsert(Result entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * Properties of entity Result.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Time = new Property(1, long.class, "time", false, "TIME");
        public final static Property Moves = new Property(2, int.class, "moves", false, "MOVES");
        public final static Property TimeStamp = new Property(3, long.class, "timeStamp", false, "TIME_STAMP");
    }

    @Override
    public boolean hasKey(Result entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
