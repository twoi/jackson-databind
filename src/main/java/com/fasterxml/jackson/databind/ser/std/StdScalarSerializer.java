package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.visitors.JsonFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public abstract class StdScalarSerializer<T>
    extends StdSerializer<T>
{
    protected StdScalarSerializer(Class<T> t) {
        super(t);
    }

    /**
     * Alternate constructor that is (alas!) needed to work
     * around kinks of generic type handling
     */
    @SuppressWarnings("unchecked")
    protected StdScalarSerializer(Class<?> t, boolean dummy) {
        super((Class<T>) t);
    }
    
    /**
     * Default implementation will write type prefix, call regular serialization
     * method (since assumption is that value itself does not need JSON
     * Array or Object start/end markers), and then write type suffix.
     * This should work for most cases; some sub-classes may want to
     * change this behavior.
     */
    @Override
    public void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException, JsonGenerationException
    {
        typeSer.writeTypePrefixForScalar(value, jgen);
        serialize(value, jgen, provider);
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitor visitor, JavaType typeHint)
    {
        visitor.anyFormat();
    }
}
