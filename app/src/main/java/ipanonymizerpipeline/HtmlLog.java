// Generated by Cap'n Proto compiler, DO NOT EDIT
// source: http_log.capnp

package ipanonymizerpipeline;

public final class HtmlLog {
  public static class HttpLogRecord {
    public static final org.capnproto.StructSize STRUCT_SIZE = new org.capnproto.StructSize((short)5,(short)4);
    public static final class Factory extends org.capnproto.StructFactory<Builder, Reader> {
      public Factory() {
      }
      public final Reader constructReader(org.capnproto.SegmentReader segment, int data,int pointers, int dataSize, short pointerCount, int nestingLimit) {
        return new Reader(segment,data,pointers,dataSize,pointerCount,nestingLimit);
      }
      public final Builder constructBuilder(org.capnproto.SegmentBuilder segment, int data,int pointers, int dataSize, short pointerCount) {
        return new Builder(segment, data, pointers, dataSize, pointerCount);
      }
      public final org.capnproto.StructSize structSize() {
        return HttpLogRecord.STRUCT_SIZE;
      }
      public final Reader asReader(Builder builder) {
        return builder.asReader();
      }
    }
    public static final Factory factory = new Factory();
    public static final org.capnproto.StructList.Factory<Builder,Reader> listFactory =
      new org.capnproto.StructList.Factory<Builder, Reader>(factory);
    public static final class Builder extends org.capnproto.StructBuilder {
      Builder(org.capnproto.SegmentBuilder segment, int data, int pointers,int dataSize, short pointerCount){
        super(segment, data, pointers, dataSize, pointerCount);
      }
      public final Reader asReader() {
        return new Reader(segment, data, pointers, dataSize, pointerCount, 0x7fffffff);
      }
      public final long getTimestampEpochMilli() {
        return _getLongField(0);
      }
      public final void setTimestampEpochMilli(long value) {
        _setLongField(0, value);
      }

      public final long getResourceId() {
        return _getLongField(1);
      }
      public final void setResourceId(long value) {
        _setLongField(1, value);
      }

      public final long getBytesSent() {
        return _getLongField(2);
      }
      public final void setBytesSent(long value) {
        _setLongField(2, value);
      }

      public final long getRequestTimeMilli() {
        return _getLongField(3);
      }
      public final void setRequestTimeMilli(long value) {
        _setLongField(3, value);
      }

      public final short getResponseStatus() {
        return _getShortField(16);
      }
      public final void setResponseStatus(short value) {
        _setShortField(16, value);
      }

      public final boolean hasCacheStatus() {
        return !_pointerFieldIsNull(0);
      }
      public final org.capnproto.Text.Builder getCacheStatus() {
        return _getPointerField(org.capnproto.Text.factory, 0, null, 0, 0);
      }
      public final void setCacheStatus(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 0, value);
      }
      public final void setCacheStatus(String value) {
        _setPointerField(org.capnproto.Text.factory, 0, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initCacheStatus(int size) {
        return _initPointerField(org.capnproto.Text.factory, 0, size);
      }
      public final boolean hasMethod() {
        return !_pointerFieldIsNull(1);
      }
      public final org.capnproto.Text.Builder getMethod() {
        return _getPointerField(org.capnproto.Text.factory, 1, null, 0, 0);
      }
      public final void setMethod(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 1, value);
      }
      public final void setMethod(String value) {
        _setPointerField(org.capnproto.Text.factory, 1, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initMethod(int size) {
        return _initPointerField(org.capnproto.Text.factory, 1, size);
      }
      public final boolean hasRemoteAddr() {
        return !_pointerFieldIsNull(2);
      }
      public final org.capnproto.Text.Builder getRemoteAddr() {
        return _getPointerField(org.capnproto.Text.factory, 2, null, 0, 0);
      }
      public final void setRemoteAddr(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 2, value);
      }
      public final void setRemoteAddr(String value) {
        _setPointerField(org.capnproto.Text.factory, 2, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initRemoteAddr(int size) {
        return _initPointerField(org.capnproto.Text.factory, 2, size);
      }
      public final boolean hasUrl() {
        return !_pointerFieldIsNull(3);
      }
      public final org.capnproto.Text.Builder getUrl() {
        return _getPointerField(org.capnproto.Text.factory, 3, null, 0, 0);
      }
      public final void setUrl(org.capnproto.Text.Reader value) {
        _setPointerField(org.capnproto.Text.factory, 3, value);
      }
      public final void setUrl(String value) {
        _setPointerField(org.capnproto.Text.factory, 3, new org.capnproto.Text.Reader(value));
      }
      public final org.capnproto.Text.Builder initUrl(int size) {
        return _initPointerField(org.capnproto.Text.factory, 3, size);
      }
    }

    public static final class Reader extends org.capnproto.StructReader {
      Reader(org.capnproto.SegmentReader segment, int data, int pointers,int dataSize, short pointerCount, int nestingLimit){
        super(segment, data, pointers, dataSize, pointerCount, nestingLimit);
      }

      public final long getTimestampEpochMilli() {
        return _getLongField(0);
      }

      public final long getResourceId() {
        return _getLongField(1);
      }

      public final long getBytesSent() {
        return _getLongField(2);
      }

      public final long getRequestTimeMilli() {
        return _getLongField(3);
      }

      public final short getResponseStatus() {
        return _getShortField(16);
      }

      public boolean hasCacheStatus() {
        return !_pointerFieldIsNull(0);
      }
      public org.capnproto.Text.Reader getCacheStatus() {
        return _getPointerField(org.capnproto.Text.factory, 0, null, 0, 0);
      }

      public boolean hasMethod() {
        return !_pointerFieldIsNull(1);
      }
      public org.capnproto.Text.Reader getMethod() {
        return _getPointerField(org.capnproto.Text.factory, 1, null, 0, 0);
      }

      public boolean hasRemoteAddr() {
        return !_pointerFieldIsNull(2);
      }
      public org.capnproto.Text.Reader getRemoteAddr() {
        return _getPointerField(org.capnproto.Text.factory, 2, null, 0, 0);
      }

      public boolean hasUrl() {
        return !_pointerFieldIsNull(3);
      }
      public org.capnproto.Text.Reader getUrl() {
        return _getPointerField(org.capnproto.Text.factory, 3, null, 0, 0);
      }

    }

  }



public static final class Schemas {
public static final org.capnproto.SegmentReader b_c5c66f22213ada4b =
   org.capnproto.GeneratedClassSupport.decodeRawBytes(
   "\u0000\u0000\u0000\u0000\u0005\u0000\u0006\u0000" +
   "\u004b\u00da\u003a\u0021\"\u006f\u00c6\u00c5" +
   "\u003c\u0000\u0000\u0000\u0001\u0000\u0005\u0000" +
   "\u00ca\u000e\u0052\u00ff\u0042\u00d3\u002c\u00f4" +
   "\u0004\u0000\u0007\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0000\u0000\u0000\u0052\u0002\u0000\u0000" +
   "\u0039\u0000\u0000\u0000\u0007\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0035\u0000\u0000\u0000\u00ff\u0001\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0061\u0070\u0070\u002f\u0073\u0072\u0063\u002f" +
   "\u006d\u0061\u0069\u006e\u002f\u0072\u0065\u0073" +
   "\u006f\u0075\u0072\u0063\u0065\u0073\u002f\u0061" +
   "\u006e\u006f\u006e\u0079\u006d\u0069\u007a\u0065" +
   "\u0072\u002f\u0061\u006e\u006f\u006e\u0079\u006d" +
   "\u0069\u007a\u0065\u0072\u002f\u0068\u0074\u0074" +
   "\u0070\u005f\u006c\u006f\u0067\u002e\u0063\u0061" +
   "\u0070\u006e\u0070\u003a\u0048\u0074\u0074\u0070" +
   "\u004c\u006f\u0067\u0052\u0065\u0063\u006f\u0072" +
   "\u0064\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0001\u0000\u0001\u0000" +
   "\u0024\u0000\u0000\u0000\u0003\u0000\u0004\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00ed\u0000\u0000\u0000\u00a2\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00f0\u0000\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u00fc\u0000\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0001\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00f9\u0000\u0000\u0000\u005a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u00f8\u0000\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0004\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0002\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0001\u0001\u0000\u0000\u0052\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u000c\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0003\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0001\u0000\u0000\u008a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0018\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0004\u0000\u0000\u0000\u0010\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0004\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0015\u0001\u0000\u0000\u007a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0014\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0020\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0005\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0005\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u001d\u0001\u0000\u0000\u0062\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u001c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0028\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0006\u0000\u0000\u0000\u0001\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0006\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0025\u0001\u0000\u0000\u003a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0020\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u002c\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0007\u0000\u0000\u0000\u0002\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0007\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0029\u0001\u0000\u0000\u005a\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0028\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0034\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0008\u0000\u0000\u0000\u0003\u0000\u0000\u0000" +
   "\u0000\u0000\u0001\u0000\u0008\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0031\u0001\u0000\u0000\"\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u002c\u0001\u0000\u0000\u0003\u0000\u0001\u0000" +
   "\u0038\u0001\u0000\u0000\u0002\u0000\u0001\u0000" +
   "\u0074\u0069\u006d\u0065\u0073\u0074\u0061\u006d" +
   "\u0070\u0045\u0070\u006f\u0063\u0068\u004d\u0069" +
   "\u006c\u006c\u0069\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0072\u0065\u0073\u006f\u0075\u0072\u0063\u0065" +
   "\u0049\u0064\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0062\u0079\u0074\u0065\u0073\u0053\u0065\u006e" +
   "\u0074\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0072\u0065\u0071\u0075\u0065\u0073\u0074\u0054" +
   "\u0069\u006d\u0065\u004d\u0069\u006c\u006c\u0069" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0009\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0072\u0065\u0073\u0070\u006f\u006e\u0073\u0065" +
   "\u0053\u0074\u0061\u0074\u0075\u0073\u0000\u0000" +
   "\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0007\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0063\u0061\u0063\u0068\u0065\u0053\u0074\u0061" +
   "\u0074\u0075\u0073\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u006d\u0065\u0074\u0068\u006f\u0064\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0072\u0065\u006d\u006f\u0074\u0065\u0041\u0064" +
   "\u0064\u0072\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0075\u0072\u006c\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u000c\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
   "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + "");
}
}

