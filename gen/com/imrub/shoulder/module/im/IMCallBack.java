/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\Cajian\\Android\\Encounter4U\\src\\com\\imrub\\shoulder\\module\\im\\IMCallBack.aidl
 */
package com.imrub.shoulder.module.im;
public interface IMCallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.imrub.shoulder.module.im.IMCallBack
{
private static final java.lang.String DESCRIPTOR = "com.imrub.shoulder.module.im.IMCallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.imrub.shoulder.module.im.IMCallBack interface,
 * generating a proxy if needed.
 */
public static com.imrub.shoulder.module.im.IMCallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.imrub.shoulder.module.im.IMCallBack))) {
return ((com.imrub.shoulder.module.im.IMCallBack)iin);
}
return new com.imrub.shoulder.module.im.IMCallBack.Stub.Proxy(obj);
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
case TRANSACTION_onDelayReceivedMsg:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.onDelayReceivedMsg(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_onReceivedMsg:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.onReceivedMsg(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.imrub.shoulder.module.im.IMCallBack
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
@Override public void onDelayReceivedMsg(int msgRoomType, java.lang.String stmp, java.lang.String jid, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(msgRoomType);
_data.writeString(stmp);
_data.writeString(jid);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_onDelayReceivedMsg, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onReceivedMsg(int msgRoomType, java.lang.String jid, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(msgRoomType);
_data.writeString(jid);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_onReceivedMsg, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onDelayReceivedMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onReceivedMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void onDelayReceivedMsg(int msgRoomType, java.lang.String stmp, java.lang.String jid, java.lang.String msg) throws android.os.RemoteException;
public void onReceivedMsg(int msgRoomType, java.lang.String jid, java.lang.String msg) throws android.os.RemoteException;
}
