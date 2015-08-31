/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Cajian\\Android\\Encounter4U\\src\\com\\imrub\\shoulder\\module\\im\\IMBridge.aidl
 */
package com.imrub.shoulder.module.im;
public interface IMBridge extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.imrub.shoulder.module.im.IMBridge
{
private static final java.lang.String DESCRIPTOR = "com.imrub.shoulder.module.im.IMBridge";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.imrub.shoulder.module.im.IMBridge interface,
 * generating a proxy if needed.
 */
public static com.imrub.shoulder.module.im.IMBridge asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.imrub.shoulder.module.im.IMBridge))) {
return ((com.imrub.shoulder.module.im.IMBridge)iin);
}
return new com.imrub.shoulder.module.im.IMBridge.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendMsg:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendMsg(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setImCallBack:
{
data.enforceInterface(DESCRIPTOR);
com.imrub.shoulder.module.im.IMCallBack _arg0;
_arg0 = com.imrub.shoulder.module.im.IMCallBack.Stub.asInterface(data.readStrongBinder());
this.setImCallBack(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.login(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_register:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.register(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sendPing:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendPing(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isToFriend:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isToFriend(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.imrub.shoulder.module.im.IMBridge
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void sendMsg(java.lang.String jid, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_sendMsg, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setImCallBack(com.imrub.shoulder.module.im.IMCallBack callBack) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callBack!=null))?(callBack.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setImCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void login(java.lang.String uid, java.lang.String password, java.lang.String server) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(uid);
_data.writeString(password);
_data.writeString(server);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void register(java.lang.String uid, java.lang.String password, java.lang.String server) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(uid);
_data.writeString(password);
_data.writeString(server);
mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendPing(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_sendPing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isToFriend(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_isToFriend, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_sendMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setImCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_sendPing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_isToFriend = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void sendMsg(java.lang.String jid, java.lang.String msg) throws android.os.RemoteException;
public void setImCallBack(com.imrub.shoulder.module.im.IMCallBack callBack) throws android.os.RemoteException;
public void login(java.lang.String uid, java.lang.String password, java.lang.String server) throws android.os.RemoteException;
public void register(java.lang.String uid, java.lang.String password, java.lang.String server) throws android.os.RemoteException;
public void sendPing(java.lang.String jid) throws android.os.RemoteException;
public boolean isToFriend(java.lang.String jid) throws android.os.RemoteException;
}
