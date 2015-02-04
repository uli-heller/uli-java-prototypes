package org.uli.jmx;

import java.math.BigDecimal;

import lombok.Data;

import org.springframework.jmx.export.annotation.ManagedResource;
//import org.springframework.stereotype.Component;

// This doesn't work at all!
// The bean is exported via jmx, but not a single attribute/operation shows up
@ManagedResource
@Data
//@Component
public class MyLombokBean {
    Integer integer;
    BigDecimal bigDecimal;
    String string;
}
