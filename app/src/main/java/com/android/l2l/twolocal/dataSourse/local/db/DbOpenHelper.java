package com.android.l2l.twolocal.dataSourse.local.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.android.l2l.twolocal.model.AddressBookDao;
import com.android.l2l.twolocal.model.DaoMaster;
import com.android.l2l.twolocal.model.WalletDao;
import com.android.l2l.twolocal.model.WalletTransactionHistoryDao;
import com.android.l2l.twolocal.utils.constants.AppConstants;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;


@Singleton
public class DbOpenHelper extends DaoMaster.OpenHelper {
    @Inject
    public DbOpenHelper(Context context) {
        super(context, AppConstants.APP_MAIN_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        if (oldVersion == 1) {
            updateOldVersion1(db);
            oldVersion++;
        }

        if (oldVersion == 2) {
            updateOldVersion2(db);
            oldVersion++;
        }

        if (oldVersion == 3) {
            updateOldVersion3(db);
            oldVersion++;
        }

        if (oldVersion == 4) {
            updateOldVersion4(db);
            oldVersion++;
        }

        if (oldVersion == 5) {
            updateOldVersion5(db);
            oldVersion++;
        }

        if (oldVersion == 7) {
            updateOldVersion7(db);
            oldVersion++;
        }
        if (oldVersion == 8) {
            updateOldVersion8(db);
            oldVersion++;
        }
        Timber.i("new version database schema %d", newVersion);

    }

    private void updateOldVersion1(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS transactions");
    }

    private void updateOldVersion2(SQLiteDatabase db) {
//        Cursor mCursor = db.rawQuery("SELECT * FROM " + WalletTransactionHistoryDao.TABLENAME + " LIMIT 0", null);
//        if (mCursor.getColumnIndex("AFFILIATED_STATUS") == -1)
//            db.execSQL("ALTER TABLE " + WalletTransactionHistoryDao.TABLENAME + " ADD COLUMN 'AFFILIATED_STATUS' String;");
    }

    private void updateOldVersion3(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS EtherTransaction");
        String CREATE_CLIENT_TABLE = "CREATE TABLE " + WalletTransactionHistoryDao.TABLENAME + " (" +
                WalletTransactionHistoryDao.Properties.Hash.columnName + " TEXT PRIMARY KEY, " +
                WalletTransactionHistoryDao.Properties.BlockHash.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Confirmations.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.GasUsed.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.CumulativeGasUsed.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.ContractAddress.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Input.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TxreceiptStatus.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.IsError.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.GasPrice.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Gas.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Value.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.To.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.From.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TransactionIndex.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Nonce.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TimeStamp.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.BlockNumber.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TransactionHash.columnName + " TEXT)";
        db.execSQL(CREATE_CLIENT_TABLE);
    }

    private void updateOldVersion4(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + WalletDao.TABLENAME);
        db.execSQL("DROP TABLE IF EXISTS L2_LEXCHANGE_RATE");
//        db.execSQL("ALTER TABLE " + WalletTransactionHistoryDao.TABLENAME + " ADD COLUMN " + WalletTransactionHistoryDao.Properties.Type.columnName + " TEXT;");

        db.execSQL("DROP TABLE IF EXISTS "+WalletTransactionHistoryDao.TABLENAME);
        String CREATE_CLIENT_TABLE2 = "CREATE TABLE " + WalletTransactionHistoryDao.TABLENAME + " (" +
                WalletTransactionHistoryDao.Properties.Hash.columnName + " TEXT PRIMARY KEY, " +
                WalletTransactionHistoryDao.Properties.BlockHash.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Confirmations.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.GasUsed.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.CumulativeGasUsed.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.ContractAddress.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Input.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TxreceiptStatus.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.IsError.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.GasPrice.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Gas.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Value.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.To.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.From.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TransactionIndex.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Nonce.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TimeStamp.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.BlockNumber.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.Type.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TokenName.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TokenSymbol.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TokenDecimal.columnName + " TEXT, " +
                WalletTransactionHistoryDao.Properties.TransactionHash.columnName + " TEXT)";
        db.execSQL(CREATE_CLIENT_TABLE2);

        String CREATE_CLIENT_TABLE = "CREATE TABLE " + WalletDao.TABLENAME + " (" +
                WalletDao.Properties.UniqueID.columnName + " TEXT PRIMARY KEY, " +
                WalletDao.Properties.WalletName.columnName + " TEXT, " +
                WalletDao.Properties.Amount.columnName + " TEXT, " +
                WalletDao.Properties.Type.columnName + " TEXT, " +
                WalletDao.Properties.UserVerifiedMnemonic.columnName + " TEXT, " +
                WalletDao.Properties.FileName.columnName + " TEXT, " +
                WalletDao.Properties.Mnemonic.columnName + " TEXT, " +
                WalletDao.Properties.CreateWalletDate.columnName + " TEXT, " +
                WalletDao.Properties.Address.columnName + " TEXT, " +
                WalletDao.Properties.PrivateKey.columnName + " TEXT, " +
                WalletDao.Properties.ShowingOrder.columnName + " TEXT)";
        db.execSQL(CREATE_CLIENT_TABLE);

        db.execSQL("ALTER TABLE " + AddressBookDao.TABLENAME + " ADD COLUMN " + AddressBookDao.Properties.Type.columnName + " String;");
    }

    private void updateOldVersion5(SQLiteDatabase db) {
//        db.execSQL("ALTER TABLE " + "EtherTransactionHistory" + " RENAME TO " + WalletTransactionHistoryDao.TABLENAME);
        db.execSQL("DROP TABLE IF EXISTS " + "EtherTransactionHistory");
        db.execSQL("DROP TABLE IF EXISTS " + "ProfileInfo");
    }

    private void updateOldVersion7(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + WalletDao.TABLENAME + " ADD COLUMN " + WalletDao.Properties.PrivateKey.columnName + " String;");
    }

    private void updateOldVersion8(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + WalletTransactionHistoryDao.TABLENAME + " ADD COLUMN " + WalletTransactionHistoryDao.Properties.TokenName.columnName + " TEXT;");
        db.execSQL("ALTER TABLE " + WalletTransactionHistoryDao.TABLENAME + " ADD COLUMN " + WalletTransactionHistoryDao.Properties.TokenSymbol.columnName + " TEXT;");
        db.execSQL("ALTER TABLE " + WalletTransactionHistoryDao.TABLENAME + " ADD COLUMN " + WalletTransactionHistoryDao.Properties.TokenDecimal.columnName + " TEXT;");

    }
}