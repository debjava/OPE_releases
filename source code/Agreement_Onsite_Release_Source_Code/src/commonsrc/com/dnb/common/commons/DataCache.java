package com.dnb.common.commons;

/**
 * An interface that allows for data to be cached and refreshed either on a scheduled
 * basis or on demand.
 */
public interface DataCache
{
    /** Refresh the data in the local cache */
    public void refreshCache();
}
