package org.uli.jmx.annotationmbeanexporter;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.jmx.export.annotation.ManagedAttribute;
//import org.springframework.stereotype.Component;

// This doesn't work at all!
// The bean is exported via jmx, but not a single attribute/operation shows up
@Data
public class NonManagedLombokBean {
    @Getter(onMethod=@_({@ManagedAttribute}))
    @Setter(onMethod=@_({@ManagedAttribute}))
    Integer integer;
    BigDecimal bigDecimal;
    String string;
}
