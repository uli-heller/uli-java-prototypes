package org.uli.richfaces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class ToggleBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private boolean fActive;

    public void setActive(boolean fActive) {
	this.fActive = fActive;
    }

    public boolean getActive() {
	return this.fActive;
    }

    public void toggleActive() {
	this.fActive = !this.fActive;
    }
}
