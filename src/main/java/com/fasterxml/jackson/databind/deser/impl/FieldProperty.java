package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * This concrete sub-class implements property that is set
 * directly assigning to a Field.
 */
public final class FieldProperty
    extends SettableBeanProperty
{
    protected final AnnotatedField _annotated;

    /**
     * Actual field to set when deserializing this property.
     */
    protected final Field _field;

    public FieldProperty(BeanPropertyDefinition propDef, JavaType type,
            TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field)
    {
        super(propDef, type, typeDeser, contextAnnotations);
        _annotated = field;
        _field = field.getAnnotated();
    }

    protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser) {
        super(src, deser);
        _annotated = src._annotated;
        _field = src._field;
    }

    protected FieldProperty(FieldProperty src, String newName) {
        super(src, newName);
        _annotated = src._annotated;
        _field = src._field;
    }

    @Override
    public FieldProperty withName(String newName) {
        return new FieldProperty(this, newName);
    }
    
    @Override
    public FieldProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new FieldProperty(this, deser);
    }
    
    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return _annotated.getAnnotation(acls);
    }

    @Override public AnnotatedMember getMember() {  return _annotated; }

    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void deserializeAndSet(JsonParser jp,
    		DeserializationContext ctxt, Object instance)
        throws IOException, JsonProcessingException
    {
        set(instance, deserialize(jp, ctxt));
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser jp,
    		DeserializationContext ctxt, Object instance)
        throws IOException, JsonProcessingException
    {
        return setAndReturn(instance, deserialize(jp, ctxt));
    }
    
    @Override
    public final void set(Object instance, Object value)
        throws IOException
    {
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
    }

    @Override
    public Object setAndReturn(Object instance, Object value)
        throws IOException
    {
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(e, value);
        }
        return instance;
    }
}