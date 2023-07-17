package com.sg.kata.model.common.validation;

import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ValidatorUtils {
    protected static Errors validate(Object object) {
        Errors errors = new Errors();
        try {
            errors.addAll(validateObjectFieldsNotNull(object));
            errors.addAll(validateObjectFiledNotEmpty(object));
            errors.addAll(validateObjectFiled(object));
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException | NoSuchFieldException |
                 NoSuchMethodException | InstantiationException e) {
            throw new CommonException(ErrorCode.EXP999, e.getMessage());
        }

        return errors;

    }

    private static Errors validateObjectFiled(Object object) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InstantiationException {
        List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object, Valid.class);
        Errors errors = new Errors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                Object fieldValue = propertyDescriptor.getReadMethod().invoke(object);
                if (fieldValue == null) {
                    errors.add(ErrorCode.EXP001, propertyDescriptor.getName(), object.getClass().getSimpleName());
                } else {
                    errors.addAll(invokeValidate(object, propertyDescriptor));
                }
            }
        }
        return errors;
    }

    private static Errors validateObjectFieldsNotNull(Object object) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object, NotNull.class);
        Errors errors = new Errors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                Object fieldValue = propertyDescriptor.getReadMethod().invoke(object);
                if (fieldValue == null) {
                    errors.add(ErrorCode.EXP001, propertyDescriptor.getName(), object.getClass().getSimpleName());
                }
            }
        }
        return errors;
    }

    private static Errors validateObjectFiledNotEmpty(Object object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(object, NotEmpty.class);

        Errors errors = new Errors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getReadMethod() != null) {
                Object fieldValue = propertyDescriptor.getReadMethod().invoke(object);
                if (Objects.isNull(fieldValue)) {
                    errors.add(ErrorCode.EXP001, propertyDescriptor.getName(), object.getClass().getSimpleName());
                } else {
                    if (fieldValue.getClass().isArray() && Array.getLength(fieldValue) <= 0) {
                        errors.add(ErrorCode.EXP002, propertyDescriptor.getName(), object.getClass().getSimpleName());
                        continue;
                    }
                    if (fieldValue instanceof Map<?,?> map && map.isEmpty()) {
                        errors.add(ErrorCode.EXP002, propertyDescriptor.getName(), object.getClass().getSimpleName());
                        continue;
                    }
                    if (fieldValue instanceof Collection<?> collection && collection.isEmpty()) {
                        errors.add(ErrorCode.EXP002, propertyDescriptor.getName(), object.getClass().getSimpleName());
                        continue;
                    }
                    if (fieldValue instanceof String string && string.isEmpty()) {
                        errors.add(ErrorCode.EXP002, propertyDescriptor.getName(), object.getClass().getSimpleName());
                    }
                }
            }
        }
        return errors;
    }

    private static List<PropertyDescriptor> getPropertyDescriptors(Object object, Class<? extends Annotation> annotation) throws IntrospectionException {
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                propertyDescriptors.add(new PropertyDescriptor(field.getName(), object.getClass()));
            }
        }
        return propertyDescriptors;
    }

    private static Errors invokeValidate(Object object, PropertyDescriptor propertyDescriptor) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Valid valid = object.getClass().getDeclaredField(propertyDescriptor.getName()).getAnnotation(Valid.class);
        Class<? extends ValidatorHandler> validatorHandlerClass = valid.handler();
        ValidatorHandler validatorHandler = validatorHandlerClass.getConstructor().newInstance();
        return validatorHandler.validate(object);
    }

}
