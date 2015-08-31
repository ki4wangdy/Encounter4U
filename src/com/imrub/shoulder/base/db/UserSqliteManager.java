package com.imrub.shoulder.base.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.table.HotPoint;
import com.imrub.shoulder.base.db.table.ISqliteOnCreateOrUpgrade;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.addr.UserFriend;
import com.imrub.shoulder.base.db.table.cajian.UserDetailInfoData;
import com.imrub.shoulder.base.db.table.cajian.WifiCollection;
import com.imrub.shoulder.base.db.table.cajian.WifiUserData;
import com.imrub.shoulder.base.db.table.chat.ChatInfoData;

public class UserSqliteManager extends SQLiteOpenHelper{

	private final ISqliteOnCreateOrUpgrade mOnstartUp;
	private static UserSqliteManager mInstance;
	
	private UserSqliteManager(final ISqliteOnCreateOrUpgrade onStartUp, String databaseName){
		super(AppContext.getAppContext(), databaseName, null, DBConstant.DATABASE_USER_DB_VERSION);
		this.mOnstartUp = onStartUp;
	}
	
	public static final UserSqliteManager getInstance(){
		if(mInstance == null){
			initilize();
		}
		return mInstance;
	}

	private static void initilize(){
		if(mInstance == null){
			String uid = UserInfo.getInstance().getUid();
			if("".equalsIgnoreCase(uid)){
				return ;
			}
			mInstance = new UserSqliteManager(onStartUp, DBConstant.DATABASE_USER_PREFIX_NAME + uid +
					DBConstant.DATABASE_SUFFIX);
			// just let database create table
			mInstance.getWritableDatabase();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		mOnstartUp.onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mOnstartUp.onUpgrade(db, oldVersion, newVersion);
	}
	
	private static final ISqliteOnCreateOrUpgrade onStartUp = new ISqliteOnCreateOrUpgrade() {
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			HotPoint.onCreateDB(db);
			AddrlistNewData.onCreateDB(db);
			UserDetailInfoData.onCreateDB(db);
			WifiCollection.onCreateDB(db);
			WifiUserData.onCreateDB(db);
			UserFriend.onCreateDB(db);
			ChatInfoData.onCreateDB(db);
		}
	};
	
}
