package org.helianto.core.utils;

public interface Mergeable<T> {

    String getId();

    T merge(T command);

}
