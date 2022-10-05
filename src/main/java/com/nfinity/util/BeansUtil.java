package com.nfinity.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BeansUtil {
    public static String[] getNullFields(Object object){
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Set<String> nullFieldSet = new HashSet<>();
        if(propertyDescriptors.length > 0){
            for (PropertyDescriptor pd : propertyDescriptors){
                String name = pd.getName();
                Object value = beanWrapper.getPropertyValue(name);
                if(Objects.isNull(value)){
                    nullFieldSet.add(name);
                }
            }
        }
        String[] nullFields = new String[nullFieldSet.size()];
        return nullFieldSet.toArray(nullFields);
    }
}
