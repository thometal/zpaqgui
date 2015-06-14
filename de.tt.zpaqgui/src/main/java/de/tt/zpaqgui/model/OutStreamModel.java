package de.tt.zpaqgui.model;

import org.eclipse.e4.core.di.annotations.Creatable;

import javax.inject.Singleton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Creatable
@Singleton
public class OutStreamModel {

    private PropertyChangeListener listener;

    private String s;

    public String getString() {
        return s;
    }

    public void setString(String s) {
        if (listener != null) {
            final PropertyChangeEvent evt = new PropertyChangeEvent(this, "output", this.s, this.s = s);

            listener.propertyChange(evt);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    public void removePropertyChangeListener() {
        this.listener = null;
    }
}
