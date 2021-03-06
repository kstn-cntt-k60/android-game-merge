// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kstn/game/logic/playing_event/guess/guess_message.proto

package kstn.game.logic.playing_event.guess;

public final class GuessMessage {
  private GuessMessage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GuessResultOrBuilder extends
      // @@protoc_insertion_point(interface_extends:GuessResult)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>string result = 1;</code>
     */
    java.lang.String getResult();
    /**
     * <code>string result = 1;</code>
     */
    com.google.protobuf.ByteString
        getResultBytes();
  }
  /**
   * Protobuf type {@code GuessResult}
   */
  public  static final class GuessResult extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:GuessResult)
      GuessResultOrBuilder {
    // Use GuessResult.newBuilder() to construct.
    private GuessResult(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GuessResult() {
      result_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private GuessResult(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              java.lang.String s = input.readStringRequireUtf8();

              result_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return kstn.game.logic.playing_event.guess.GuessMessage.internal_static_GuessResult_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return kstn.game.logic.playing_event.guess.GuessMessage.internal_static_GuessResult_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.class, kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.Builder.class);
    }

    public static final int RESULT_FIELD_NUMBER = 1;
    private volatile java.lang.Object result_;
    /**
     * <code>string result = 1;</code>
     */
    public java.lang.String getResult() {
      java.lang.Object ref = result_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        result_ = s;
        return s;
      }
    }
    /**
     * <code>string result = 1;</code>
     */
    public com.google.protobuf.ByteString
        getResultBytes() {
      java.lang.Object ref = result_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        result_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getResultBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, result_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getResultBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, result_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof kstn.game.logic.playing_event.guess.GuessMessage.GuessResult)) {
        return super.equals(obj);
      }
      kstn.game.logic.playing_event.guess.GuessMessage.GuessResult other = (kstn.game.logic.playing_event.guess.GuessMessage.GuessResult) obj;

      boolean result = true;
      result = result && getResult()
          .equals(other.getResult());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + RESULT_FIELD_NUMBER;
      hash = (53 * hash) + getResult().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(kstn.game.logic.playing_event.guess.GuessMessage.GuessResult prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code GuessResult}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:GuessResult)
        kstn.game.logic.playing_event.guess.GuessMessage.GuessResultOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return kstn.game.logic.playing_event.guess.GuessMessage.internal_static_GuessResult_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return kstn.game.logic.playing_event.guess.GuessMessage.internal_static_GuessResult_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.class, kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.Builder.class);
      }

      // Construct using kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        result_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return kstn.game.logic.playing_event.guess.GuessMessage.internal_static_GuessResult_descriptor;
      }

      public kstn.game.logic.playing_event.guess.GuessMessage.GuessResult getDefaultInstanceForType() {
        return kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.getDefaultInstance();
      }

      public kstn.game.logic.playing_event.guess.GuessMessage.GuessResult build() {
        kstn.game.logic.playing_event.guess.GuessMessage.GuessResult result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public kstn.game.logic.playing_event.guess.GuessMessage.GuessResult buildPartial() {
        kstn.game.logic.playing_event.guess.GuessMessage.GuessResult result = new kstn.game.logic.playing_event.guess.GuessMessage.GuessResult(this);
        result.result_ = result_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof kstn.game.logic.playing_event.guess.GuessMessage.GuessResult) {
          return mergeFrom((kstn.game.logic.playing_event.guess.GuessMessage.GuessResult)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(kstn.game.logic.playing_event.guess.GuessMessage.GuessResult other) {
        if (other == kstn.game.logic.playing_event.guess.GuessMessage.GuessResult.getDefaultInstance()) return this;
        if (!other.getResult().isEmpty()) {
          result_ = other.result_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        kstn.game.logic.playing_event.guess.GuessMessage.GuessResult parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (kstn.game.logic.playing_event.guess.GuessMessage.GuessResult) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object result_ = "";
      /**
       * <code>string result = 1;</code>
       */
      public java.lang.String getResult() {
        java.lang.Object ref = result_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          result_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string result = 1;</code>
       */
      public com.google.protobuf.ByteString
          getResultBytes() {
        java.lang.Object ref = result_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          result_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string result = 1;</code>
       */
      public Builder setResult(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        result_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string result = 1;</code>
       */
      public Builder clearResult() {
        
        result_ = getDefaultInstance().getResult();
        onChanged();
        return this;
      }
      /**
       * <code>string result = 1;</code>
       */
      public Builder setResultBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        result_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:GuessResult)
    }

    // @@protoc_insertion_point(class_scope:GuessResult)
    private static final kstn.game.logic.playing_event.guess.GuessMessage.GuessResult DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new kstn.game.logic.playing_event.guess.GuessMessage.GuessResult();
    }

    public static kstn.game.logic.playing_event.guess.GuessMessage.GuessResult getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GuessResult>
        PARSER = new com.google.protobuf.AbstractParser<GuessResult>() {
      public GuessResult parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new GuessResult(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<GuessResult> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<GuessResult> getParserForType() {
      return PARSER;
    }

    public kstn.game.logic.playing_event.guess.GuessMessage.GuessResult getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GuessResult_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GuessResult_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n7kstn/game/logic/playing_event/guess/gu" +
      "ess_message.proto\"\035\n\013GuessResult\022\016\n\006resu" +
      "lt\030\001 \001(\tB%\n#kstn.game.logic.playing_even" +
      "t.guessb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_GuessResult_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GuessResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GuessResult_descriptor,
        new java.lang.String[] { "Result", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
