package com.fasterxml.jackson.databind.util;

/**
 * Helper class used for checking whether a property is visible
 * in the active view
 */
public abstract class ViewMatcher
{
    public abstract boolean isVisibleForView(Class<?> activeView);

    public static ViewMatcher construct(Class<?>[] views)
    {
        if (views == null) {
            return Empty.instance;
        }
        switch (views.length) {
        case 0:
            return Empty.instance;
        case 1:
            return new Single(views[0]);
        }
        return new Multi(views);
    } 
    
    /*
    /**********************************************************
    /* Concrete sub-classes
    /**********************************************************
     */

    private final static class Empty extends ViewMatcher {
        final static Empty instance = new Empty();
        @Override
        public boolean isVisibleForView(Class<?> activeView) {
            return false;
        }
    }

    private final static class Single extends ViewMatcher {
        private final Class<?> _view;
        public Single(Class<?> v) { _view = v; }
        @Override
        public boolean isVisibleForView(Class<?> activeView) {
            return (activeView == _view) || _view.isAssignableFrom(activeView);
        }
    }

    private final static class Multi extends ViewMatcher {
        private final Class<?>[] _views;

        public Multi(Class<?>[] v) { _views = v; }

        @Override
        public boolean isVisibleForView(Class<?> activeView)
        {
            for (int i = 0, len = _views.length; i < len; ++i) {
                Class<?> view = _views[i];
                if ((activeView == view) || view.isAssignableFrom(activeView)) {
                    return true;
                }
            }
            return false;
        }
    }
}
